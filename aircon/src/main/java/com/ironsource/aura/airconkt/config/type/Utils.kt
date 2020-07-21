package com.ironsource.aura.airconkt.config.type

import com.ironsource.aura.airconkt.config.annotations.RemoteIntValue
import com.ironsource.aura.airconkt.config.annotations.RemoteStringValue
import kotlin.reflect.KClass

object Utils {

    fun <T : Enum<T>> getIntEnumConst(enumClass: KClass<T>, remoteValue: Int) =
            getEnumConst(enumClass, RemoteIntValue::class.java, remoteValue) {
                value
            }

    fun <T : Enum<T>> getStringEnumConst(enumClass: KClass<T>, remoteValue: String) =
            getEnumConst(enumClass, RemoteStringValue::class.java, remoteValue.toLowerCase()) {
                value.toLowerCase()
            }

    fun <T : Enum<T>> getIntEnumRemoteValue(enumClass: KClass<T>, enumConstant: T) =
            getEnumRemoteValue(enumClass, RemoteIntValue::class.java, enumConstant) {
                value
            }

    fun <T : Enum<T>> getStringEnumRemoteValue(enumClass: KClass<T>, enumConstant: T) =
            getEnumRemoteValue(enumClass, RemoteStringValue::class.java, enumConstant) {
                value
            }

    @Suppress("UNCHECKED_CAST")
    private fun <A : Annotation, T : Enum<T>, S> getEnumRemoteValue(enumClass: KClass<T>,
                                                                    annotationClass: Class<A>,
                                                                    enumConstant: T,
                                                                    remoteValueResolver: A.() -> S) =
            enumClass.java.declaredFields
                    .filter { it.isEnumConstant }
                    .associateBy({ it.get(enumClass) as T },
                            { it.getAnnotation(annotationClass)?.remoteValueResolver() }
                    ).get(enumConstant)

    @Suppress("UNCHECKED_CAST")
    private fun <A : Annotation, T : Enum<T>, S> getEnumConst(enumClass: KClass<T>,
                                                              annotationClass: Class<A>,
                                                              remoteValue: S,
                                                              resolver: A.() -> S) =
            enumClass.java.declaredFields
                    .filter { it.isEnumConstant }
                    .associateBy({ it.getAnnotation(annotationClass)?.resolver() },
                            { it.get(enumClass) as T }).get(remoteValue)
}