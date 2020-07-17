package com.ironsource.aura.airconkt.config

data class TypeResolver<T>(val configSourceResolver: ConfigSourceResolver<T>,
                           val resourcesResolver: ResourcesResolver<T>) {
    companion object {
        fun int(resourcesResolver: ResourcesResolver<Int> = ResourcesResolver.Int) =
                TypeResolver(ConfigSourceResolver.Int, resourcesResolver)

        fun long(resourcesResolver: ResourcesResolver<Long> = ResourcesResolver.Long) =
                TypeResolver(ConfigSourceResolver.Long, resourcesResolver)

        fun float(resourcesResolver: ResourcesResolver<Float> = ResourcesResolver.Float) =
                TypeResolver(ConfigSourceResolver.Float, resourcesResolver)

        fun string(resourcesResolver: ResourcesResolver<String> = ResourcesResolver.String) =
                TypeResolver(ConfigSourceResolver.String, resourcesResolver)

        fun boolean(resourcesResolver: ResourcesResolver<Boolean> = ResourcesResolver.Boolean) =
                TypeResolver(ConfigSourceResolver.Boolean, resourcesResolver)
    }
}

