package com.ironsource.aura.dslint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.util.PropertyUtilBase
import org.jetbrains.uast.*
import org.jetbrains.uast.util.isAssignment
import javax.annotation.Nonnull

class DslMandatoryDetector : Detector(),
        UastScanner {

    companion object {
        val ISSUE = Issue.create("DslMandatory",
                "Mandatory property missing",
                "Mandatory property missing",
                Category.CORRECTNESS, 6,
                Severity.ERROR,
                Implementation(DslMandatoryDetector::class.java, Scope.JAVA_FILE_SCOPE))
    }

    override fun getApplicableUastTypes(): List<Class<out UElement?>> {
        return listOf<Class<out UElement?>>(ULambdaExpression::class.java)
    }

    override fun createUastHandler(@Nonnull context: JavaContext): UElementHandler? {
        val psi = context.uastFile?.sourcePsi
        psi ?: return null
        if (isJava(psi)) {
            return null
        }

        return object : UElementHandler() {
            override fun visitLambdaExpression(node: ULambdaExpression) {
                try {
                    visitLambda(context, node)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun visitLambda(context: JavaContext,
                            node: ULambdaExpression) {
        val dslClass = getDslClass(node)
        dslClass ?: return

        val dslPropertiesDefs = getMandatoryDslProperties(dslClass)
        val dslPropertiesCalls = getDslPropertyCalls(dslPropertiesDefs, node.body)

//        println("Defs = $dslPropertiesDefs")
//        println("Calls = $dslPropertiesCalls")

        dslPropertiesDefs.forEach { (group, groupProperties) ->
            val propertiesNames = groupProperties.map { it.name }

            val groupCallCount = dslPropertiesCalls
                    .filter { propertiesNames.contains(it.key) }
                    .map { it.value }
                    .sum()

            if (groupCallCount == 0) {
                println("Reporting Group = $group, count = $groupCallCount")

                context.report(ISSUE, node, context.getLocation(node as UElement),
                        "\"$group\" property must be defined")
            }
        }
    }

    private fun getDslPropertyCalls(dslProperties: Map<String, List<DslMandatoryProperty>>,
                                    body: UExpression): Map<String?, Int> {
        val propertiesNames = dslProperties.flatMap { it.value }.map { it.name }
        return (body as UBlockExpression).expressions
                .filterIsInstance<UBinaryExpression>()
                .filter { it.isAssignment() }
                .map {
                    (((it.leftOperand as UReferenceExpression).referenceNameElement)
                            as UIdentifier).name
                }
                .filter { propertiesNames.contains(it) }
                .groupingBy { it }
                .eachCount()
    }

    private fun getMandatoryDslProperties(clazz: PsiClass): Map<String, List<DslMandatoryProperty>> {
        return PropertyUtilBase.getAllProperties(clazz, true, false, true).values
                .filter {
                    it.hasAnnotation("com.ironsource.aura.dslint.annotations.DslMandatory")
                }
                .map {
                    val annotation = it.getAnnotation(
                            "com.ironsource.aura.dslint.annotations.DslMandatory")
                    val group = getAttributeValue<String>(annotation, "group")
                    DslMandatoryProperty(getPropertyName(it.name), group)
                }
                .groupBy {
                    it.group ?: it.name
                }
    }

    private fun getPropertyName(setterName: String) = setterName.substring(3).decapitalize()

    private fun getDslClass(node: ULambdaExpression): PsiClass? {
        val callExpression = node.uastParent
        if (callExpression !is UCallExpression) {
            return null
        }

        val parameter = callExpression.getParameterForArgument(node)?.type

        val receiverType = (parameter as PsiClassReferenceType).parameters.getOrNull(0)
        if (receiverType !is PsiWildcardType) {
            return null
        }

        val dslClass = (receiverType.bound as PsiClassType).resolve()

        return if (dslClass != null && dslClass.hasAnnotation(
                        "com.ironsource.aura.dslint.annotations.DSLint"))
            dslClass else null
    }
}

fun <T> getAttributeValue(annotation: PsiAnnotation?,
                          attribute: String): T? {
    val attributeValue = annotation?.findAttributeValue(attribute)
    attributeValue ?: return null
    val evaluationHelper = JavaPsiFacade.getInstance(
            attributeValue.project)
            .constantEvaluationHelper
    return evaluationHelper.computeConstantExpression(attributeValue) as T
}

data class DslMandatoryProperty(val name: String,
                                val group: String?)