package com.ironsource.aura.aircon.sample.config;

import android.annotation.SuppressLint;

import com.ironsource.aura.aircon.common.RangeFallbackPolicy;
import com.ironsource.aura.aircon.common.annotations.ConfigAdapter;
import com.ironsource.aura.aircon.common.annotations.ConfigDefaultValueProvider;
import com.ironsource.aura.aircon.common.annotations.ConfigValidator;
import com.ironsource.aura.aircon.common.annotations.DefaultConfig;
import com.ironsource.aura.aircon.common.annotations.DefaultRes;
import com.ironsource.aura.aircon.common.annotations.FeatureRemoteConfig;
import com.ironsource.aura.aircon.common.annotations.Mutable;
import com.ironsource.aura.aircon.common.annotations.Source;
import com.ironsource.aura.aircon.common.annotations.config.BooleanConfig;
import com.ironsource.aura.aircon.common.annotations.config.ColorConfig;
import com.ironsource.aura.aircon.common.annotations.config.ConfigGroup;
import com.ironsource.aura.aircon.common.annotations.config.FloatConfig;
import com.ironsource.aura.aircon.common.annotations.config.IntConfig;
import com.ironsource.aura.aircon.common.annotations.config.IntEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.JsonConfig;
import com.ironsource.aura.aircon.common.annotations.config.LongConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringEnumConfig;
import com.ironsource.aura.aircon.common.annotations.config.StringSetConfig;
import com.ironsource.aura.aircon.common.annotations.config.TextConfig;
import com.ironsource.aura.aircon.common.annotations.config.TimeConfig;
import com.ironsource.aura.aircon.common.annotations.config.UrlConfig;
import com.ironsource.aura.aircon.common.utils.Consts;
import com.ironsource.aura.aircon.sample.BuildConfig;
import com.ironsource.aura.aircon.sample.LabelConfig;
import com.ironsource.aura.aircon.sample.R;
import com.ironsource.aura.aircon.sample.StringListConfig;
import com.ironsource.aura.aircon.sample.config.model.ImageLocation;
import com.ironsource.aura.aircon.sample.config.model.Label;
import com.ironsource.aura.aircon.sample.config.model.RemoteObject;
import com.ironsource.aura.aircon.sample.config.model.TextLocation;
import com.ironsource.aura.aircon.source.FireBaseConfigSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 10/20/2018.
 */
@SuppressLint("MissingAnnotation")
public interface RemoteConfigs {

    @Source(FireBaseConfigSource.class)
    @FeatureRemoteConfig
    interface SomeCoolFeature {

        @DefaultRes(R.bool.some_bool)
        @BooleanConfig
        String ENABLED = "enabled";

        @DefaultConfig(ENABLED)
        @BooleanConfig
        String SOME_FLAG = "someFlag";

        @Mutable
        @DefaultRes(R.string.app_name)
        @TextConfig(enforceNonEmpty = true)
        String LABEL = "label";

        @DefaultRes(R.integer.some_int)
        @IntConfig(minValue = 5)
        String SOME_INT = "someInt";

        @DefaultRes(R.integer.some_int)
        @LongConfig(minValue = 0, minValueFallbackPolicy = RangeFallbackPolicy.RANGE_VALUE)
        String SOME_LONG = "someLong";

        @FloatConfig(defaultValue = 1.5f, maxValue = 2.5f, maxValueFallbackPolicy = RangeFallbackPolicy.RANGE_VALUE)
        String SOME_FLOAT = "someFloat";

        @TimeConfig(defaultValue = 0, minValue = 36000, maxValue = 360000000, minValueFallbackPolicy = RangeFallbackPolicy.DEFAULT, maxValueFallbackPolicy = RangeFallbackPolicy.RANGE_VALUE)
        String SOME_DURATION = "someDuration";

        @StringConfig(defaultValue = "test")
        String SOME_STRING = "someString";

        @Mutable
        @IntEnumConfig(defaultValue = 2, randomizerValue = 3, enumClass = TextLocation.class)
        String TEXT_LOCATION = "textLocation";

        @Mutable
        @StringEnumConfig(defaultValue = "top", enumClass = ImageLocation.class)
        String IMAGE_LOCATION = "imageLocation";

        @Mutable
        @JsonConfig(defaultValue = "{\"int\":2, \"str\":\"test\"}", type = RemoteObject.class)
        String SOME_JSON = "someJson";

		@Mutable
		@DefaultRes(R.color.colorAccent)
		@ColorConfig
		String SOME_COLOR = "someColor";

		@Mutable
		@UrlConfig(defaultValue = "")
		String SOME_URL = "someUrl";

        @StringSetConfig(defaultValue = {"1", "2"})
        String SOME_STRING_SET = "someStringSet";

        @JsonConfig(defaultValue = "", type = Map.class, genericTypes = {String.class, Integer.class})
        String SOME_GENERIC_JSON = "someGenericJson";

        @ConfigGroup(value = {SOME_INT, SOME_STRING})
        String COOL_GROUP = "coolGroup";

        @DefaultRes(R.string.app_name)
        @LabelConfig(invalidValues = {"invalid1", "invalid2"})
        String SOME_CUSTOM_LABEL = "someCustomLabel";

        @StringListConfig(defaultValue = Consts.NULL_STRING)
        String SOME_STRING_LIST = "someStringList";

        class Validators {

            @ConfigValidator(SOME_URL)
            static boolean isSomeUrlValid(String val) {
                return val.startsWith("https");
            }

            @ConfigValidator(SOME_STRING_LIST)
            static boolean isSomeStringListValid(Object list) {
                return true;
            }
        }

        class DefaultsProvider {

            @ConfigDefaultValueProvider(SOME_URL)
            static String getSomeUrlDefault() {
                return BuildConfig.DEBUG ? "https://debug-url.com" : "https://release-url.com";
            }

            @ConfigDefaultValueProvider(SOME_STRING_LIST)
            static List<String> getSomeStringListDefault() {
                return Arrays.asList();
            }
        }

        class Adapters {

            @ConfigAdapter(LABEL)
            static Label processLabel(String val) {
                return Label.from(val);
            }

            @ConfigAdapter(SOME_STRING_LIST)
            static List<String> processSomeList(List<String> list) {
                return list;
            }
        }
    }
}