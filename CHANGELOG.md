Change Log
==========

Version 1.4.6
----------------------------
   * Bug fixes.

Version 1.4.5
----------------------------
   * Bug fixes.

Version 1.4.4
----------------------------
   * Added quick fixes for lint checks.
   * Bug fixes.

Version 1.4.3
----------------------------
   * Added support for generic types in @JsonConfig.
   * Added support for custom config types annotations.

Version 1.4.2
----------------------------
   * bug fixes.

Version 1.4.1
----------------------------
   * Added support for groups with multiple identifiable sources
   * bug fixes.

Version 1.4.0
----------------------------
   * First open source version.

Version 1.3.0
----------------------------

   * All non getters/setters methods are generated in an inner class called Aux.
   * Added missing javadoc.
   * Support for randomizer value for enum configs.

Version 1.2.3
----------------------------

   * Updated annotation processing lib version.

Version 1.2.2
----------------------------

   * Added raw getter for all config types.

Version 1.2.0
----------------------------

   * Support for mock values.

Version 1.1.1
----------------------------

   * Support for min and max values for number configs.

Version 1.1.0
----------------------------

   * String support to xml injection.
   * @ConfigAdapter - for changing the value and/or type of the returned config value.
   * @ConfigValidator - providing custom validation for config
   * Getter for raw config value.

Version 1.0.0
----------------------------

   * Hello world!
   * XML injection.
   * Supported configs:
        * @BooleanConfig
        * @ColorConfig
        * @FloatConfig
        * @IntConfig
        * @IntEnumConfig
        * @JsonConfig
        * @LongConfig
        * @StringConfig
        * @StringEnumConfig
        * @StringSetConfig
        * @TextConfig
        * @TimeConfig
        * @UrlConfig
   * @DefaultRes - for defining a resource as default value
   * @DefaultConfig - for defining another config as a default value
   * @ConfigGroup - for grouping configs in a generated POJO