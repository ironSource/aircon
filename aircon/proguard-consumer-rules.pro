# Rules that need to be applied on the main app
-keep class com.ironsource.aura.airconkt.** { *; }

-keepclassmembernames class * implements com.ironsource.aura.airconkt.config.FeatureRemoteConfig { *; }
