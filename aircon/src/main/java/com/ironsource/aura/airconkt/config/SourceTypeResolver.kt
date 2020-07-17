package com.ironsource.aura.airconkt.config

data class SourceTypeResolver<T>(val configSourceResolver: ConfigSourceResolver<T>,
                                 val resourcesResolver: ResourcesResolver<T>) {
    companion object {
        fun int(resourcesResolver: ResourcesResolver<Int> = ResourcesResolver.Int) =
                SourceTypeResolver(ConfigSourceResolver.Int, resourcesResolver)

        fun long(resourcesResolver: ResourcesResolver<Long> = ResourcesResolver.Long) =
                SourceTypeResolver(ConfigSourceResolver.Long, resourcesResolver)

        fun float(resourcesResolver: ResourcesResolver<Float> = ResourcesResolver.Float) =
                SourceTypeResolver(ConfigSourceResolver.Float, resourcesResolver)

        fun string(resourcesResolver: ResourcesResolver<String> = ResourcesResolver.String) =
                SourceTypeResolver(ConfigSourceResolver.String, resourcesResolver)

        fun boolean(resourcesResolver: ResourcesResolver<Boolean> = ResourcesResolver.Boolean) =
                SourceTypeResolver(ConfigSourceResolver.Boolean, resourcesResolver)
    }
}

