![Logo](images/logo.png)
[ ![Download](https://api.bintray.com/packages/ironsource-aura/AirCon/aircon/images/download.svg) ](https://bintray.com/ironsource-aura/AirCon/aircon/_latestVersion)

AirCon
============

Control over the air
---
Remote config management Android library powered by annotation processing and code generation.
https://medium.com/@hanan_rofe_haim/remote-config-in-android-one-release-to-rule-them-all-5ffa7750dec9

 * Manage your app's remote configs using simple annotations, use generated providers classes to obtain config values.
 * Supports adding any config source (FireBase support included in library).
 * Supports custom validation and adaptation of config values.
 * Inject remotely configured colors/Strings directly to XML layouts.
 
Usage
---
```java
@Source(FireBaseConfigSource.class) // Can use any custom source instead of Firebase
@FeatureRemoteConfig
interface CoolFeature {
    	
    @BooleanConfig(defaultValue = false)
    String ENABLED = "enabled";
    	
    @Mutable
    @IntConfig(defaultValue = 1, maxValue = 3)
    String MAX_TIMES_TO_SHOW = "maxTimeToShow";
    	
    @StringConfig(defaultValue = "Hello world")
    String MESSAGE = "msg";
}
```

```java
final boolean enabled = CoolFeatureConfigProvider.isEnabled();
final int maxTimesToShow = CoolFeatureConfigProvider.getMaxTimesToShow();
final String message = CoolFeatureConfigProvider.getMessage();
```

Initializing the SDK
---
The AirCon SDK should be initialized in the Application.onCreate() method using a `AirConConfiguration` object. 
```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AirCon.get().init(new AirConConfiguration.Builder(this).setLoggingEnabled(BuildConfig.DEBUG)
		                                               .setJsonConverter(new GsonConverter())
		                                               .addConfigSource(new FireBaseConfigSource(this, FirebaseRemoteConfig.getInstance())
		                                               .build());
    }
}
```

Supported config types
--------
#### Primitives
* `@BooleanConfig`
* `@IntConfig`
* `@LongConfig`
* `@FloatConfig`
* `@StringConfig`

#### Collection
* `@StringSetConfig`

#### Json
* `@JsonConfig`

#### Enums
* `@StringEnumConfig`
* `@IntEnumConfig`

#### Special
* `@ColorConfig`
* `@TimeConfig`
* `@TextConfig`
* `@UrlConfig`

Mutable configs
--------
All configs are read-only by default.
If the custom source supports overriding local values (e.g the built-in Firebase config source) use the `@Mutable` annotation
on a config field to generate a setter for that config.

Remote config sources
--------
AirCon supports any source of remote config.
For the common case of using FireBase as a remote config source, the library is packaged with a `FireBaseConfigSource`.
Config sources can either be added in the SDK init phase:
```java
AirCon.get().init(new AirConConfiguration.Builder(this).addConfigSource(new FireBaseConfigSource(this, FirebaseRemoteConfig.getInstance())
```
Or at a later stage:
```java
AirCon.get().getConfigSourceRepository().addSource(new FireBaseConfigSource(this, FirebaseRemoteConfig.getInstance());
```

Multiple remote config sources for the same app are supported.

Each config is a assigned a source using the `@Source` annotation.
Either define a source for the entire feature:
```java	
@Source(FireBaseConfigSource.class)
@FeatureRemoteConfig
interface CoolFeature {
    	
    @BooleanConfig(defaultValue = false)
    String ENABLED = "enabled";
```

Or define a specific source for each config:
```java
@Source(FireBaseConfigSource.class)
@StringConfig(defaultValue = "Hello world")
String MESSAGE = "msg";
```

If a source is defined on both the feature interface and the config field, the config field source will be used.

Default value
--------
A default value for a config can be defined in several ways (only one way can be used for the same config):

* Define the `defaultValue` attribute of the config annotation:
   ```java
    @BooleanConfig(defaultValue = false)
    String ENABLED = "enabled";
	```
* Define a resource as the default value:
   ```java
	@DefaultRes(R.string.title)
    @StringConfig
    String TITLE = "title";
	```	
* Define another config value as a default value:
  ```java
    @BooleanConfig(defaultValue = false)
    String ENABLED = "enabled";
	
	@DefaultConfig(ENABLED) // If "flag" is not configured, the provider will fallback to the configured value of "enabled".
    @BooleanConfig
    String ANOTHER_FLAG = "flag";
	```
* Define a custom default value provider method:
   ```java
   @BooleanConfig
   String ENABLED = "enabled";
	
   @ConfigDefaultValueProvider(ENABLED)
   public static boolean getEnabledDefault() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}
    ```
    
Custom value validation
--------
Some config types provide inherit validation.
For example, the `@UrlConfig` verifies that the configured URL is a valid URL, otherwise the default value is returned. 
If custom validation is needed for a config the then a validation predicate method can be defined:
```java
@ConfigValidator(TITLE)
public static boolean isTitleValid(String title) {
   return title.length <= 20; // If a title longer than 20 chars is configured, fallback to the default value.
}
```

Custom adapters
--------
Some config types provide implementation for common adaptation needs.
For example, the `@JsonConfig` converts a remotely configured json string to an object.
In some cases extra processing is needed on the returned config value, which can be achieved be defining an adapter method:
```java
@ConfigAdapter(ENABLED)
public static boolean processEnabled(boolean enabled) {
   return enabled && BuildConfig.DEBUG; // Prevent feature from being enabled in production builds.
}
```

An adapter method can return a different type than the original config type:
```java
@ConfigAdapter(TITLE)
public static Label processTitle(String title) {
   return new Label(title);
}
```

Enum configs
--------
A common use case is converting a remotely configured int/String to an Enum:
```java
public enum TextLocation {
   @RemoteIntValue(0) BOTTOM,
   @RemoteIntValue(1) CENTER,
   @RemoteIntValue(2) TOP
}
```
```java
@IntEnumConfig(defaultValue = 2, enumClass = TextLocation.class)
String TEXT_LOCATION = "textLocation";
```

Usage:
```java
TextLocation textLocation = CoolFeatureConfigProvider.getTextLocation();
```

`@StringEnumConfig` is also supported for converting strings to enum consts.

Config groups
--------
Multiple configs can be groups together.
A POJO class containing all the config values will be generated and can be passed around to methods.

Groups can be used in 2 ways:
1. For every feature annotated with `@FeatureRemoteConfig` a group containing all configs is generated.
Usage:
```java
final CoolFeatureConfig config = CoolFeatureConfigProvider.getAll();
```

2. Creating a custom group and defining the contained configs.
The value of the field defines the group name.
```java
@ConfigGroup({MAX_TIMES_TO_SHOW, MESSAGE}) 
String MY_GROUP = "myGroup";
```
Usage:
```java
MyGroupConfig myGroupConfig = CoolFeatureConfigProvider.getMyGroup();
```

Mock values
--------
Mocking remotely configured values is useful for testing purposes.
To mock a config value a mock method should be defined:
```java
@ConfigMock(RemoteConfigs.CoolFeature.ENABLED)
public static boolean mockEnabled() {
   return true;
}
```

A good practice for preventing mocks in release builds is defining all the config mock methods in the same class and ignore it in git.
In addition, a custom lint check will throw an error if a config mock is defined in a release build.

Custom config types
--------
AirCon supports defining custom config type annotations in cases where the built-in types are not sufficient
or when common validation and processing is used across multiple config keys.

To define a custom config type:
1. Define a config type resolver
```java
public class LabelConfigResolver implements ConfigTypeResolver<LabelConfig, String, Label> {

   @Override
   public Class<LabelConfig> getAnnotationClass() {
      return LabelConfig.class;
   }

   @Override
   public boolean isValid(final LabelConfig annotation, final String value) {
      final String[] invalidValues = annotation.invalidValues();
      return !Arrays.asList(invalidValues).contains(value);
   }

   @Override
   public Label process(final LabelConfig annotation, final String value) {
      return Label.from(value);
   }
}
```
The resolver defines:
- The custom config annotation (defined in step 2) to which it defines the processing logic.
- How a configured value is processed for the custom type.
- The primitive type of the config (i.e the type that will be used remotely).
- The processed type of the config, which can be whatever you like.

2. Define a config type annotation and annotate it with `@ConfigType` and bind it to the resolver you've created via 
   the parameter to the annotation
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ConfigType(LabelConfigResolver.class)
public @interface LabelConfig {
	
   String[] invalidValues() default {};
	
   String defaultValue() default "";
}
```
The annotation can contain any number of attributes but must contain a `defaultValue` attribute.

Note that the annotation must be retained in runtime and be targeted only for fields.

3. Register the custom config type when initializing the SDK
```java
new AirConConfiguration.Builder(this).registerConfigType(new LabelConfigResolver())
```

4. Use the custom config
```java
@DefaultRes(R.string.app_name)
@LabelConfig(defaultValue = "", invalidValues = {"invalid1", "invalid2"})
String SOME_CUSTOM_LABEL = "someCustomLabel";
```

The new config type acts exactly the same as the build-in config annotations and therefore
can be used in conjunction with all other AirCon features such as mocks, custom adapters, custom validators and so on.

XML injection
--------
Aircon is also equipped with XML injection capabilities.
Injection for attributes of type String, color and color state list are supported.
To use XML injection:
1. Enable XML injection by providing the app R.attr class and a config source:
```java
AirCon.get().init(new AirConConfiguration.Builder(context).enableXmlInjection(R.attr.class, fireBaseConfigSource);
```
Or provide a custom attribute resolver:
```java
AirCon.get().init(new AirConConfiguration.Builder(context).enableXmlInjection(R.attr.class, new AttributeResolver() {
			@Override
			public ColorStateList getColorStateList(final String attrName) {
				// return the remotely configured color state list for the `attrName` key
			}

			@Override
			public Integer getColor(final String attrName) {
				// return the remotely configured color for the `attrName` key
			}

			@Override
			public String getString(final String attrName) {
				// return the remotely configured string for the `attrName` key
			}
		})
```

2. Define attributes corresponding to config keys:
```xml
<resources>
    <attr name="aTitle" />
    <attr name="aTitleTextColor" />
</resources>
```

3. Define default values in the app theme:
```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
	<item name="aTitle">Hey there!</item>
        <item name="aTitleTextColor">@color/white</item>
</style>
```

4. Use attributes in XML:
```xml
<TextView
            android:id="@+id/title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="?attr/aTitle"
            android:textColor="?attr/aTitleTextColor" />
```

5. Extend either `AirConAppCompatActivity` or `AirConFragmentActivity`.
6. Behold the magic.


Lint
--------
AirCon library is bundled with various lint checks for verifying correct usage of the library.
No extra integration is needed from the app side.


Download
--------
```groovy
dependencies {
    implementation 'com.ironsource.aura.aircon:aircon:1.4.0'
    annotationProcessor 'com.ironsource.aura.aircon:aircon-compiler:1.4.0'
}
```

For using Firebase config source:
```groovy
dependencies {
    implementation 'com.ironsource.aura.aircon:firebase-source:1.4.0'	
}
```

For using `Gson` to parse `@JsonConfig`:
```groovy
dependencies {
    implementation 'com.ironsource.aura.aircon:gson-converter:1.4.0'	
}
```

If you are using Kotlin, replace `annotationProcessor` with `kapt`.

License
-------

Copyright (c) 2019 Hanan Rofe Haim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
