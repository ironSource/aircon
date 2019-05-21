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
import com.ironsource.aura.aircon.sample.BuildConfig;
import com.ironsource.aura.aircon.sample.R;
import com.ironsource.aura.aircon.sample.config.model.ImageLocation;
import com.ironsource.aura.aircon.sample.config.model.Label;
import com.ironsource.aura.aircon.sample.config.model.RemoteObject;
import com.ironsource.aura.aircon.sample.config.model.TextLocation;
import com.ironsource.aura.aircon.source.FireBaseConfigSource;

/**
 * Created on 10/20/2018.
 */
@SuppressLint("MissingAnnotation")
public interface RemoteConfigs {

	@FeatureRemoteConfig(createGroup = true)
	interface SomeCoolFeature {

		@Source(FireBaseConfigSource.class)
		@DefaultRes(R.bool.some_bool)
		@BooleanConfig
		String ENABLED = "enabled";

		@Source(FireBaseConfigSource.class)
		@DefaultConfig(ENABLED)
		@BooleanConfig
		String SOME_FLAG = "someFlag";

		@Source(FireBaseConfigSource.class)
		@Mutable
		@DefaultRes(R.string.app_name)
		@TextConfig(enforceNonEmpty = true)
		String LABEL = "label";

		@Source(FireBaseConfigSource.class)
		@DefaultRes(R.integer.some_int)
		@IntConfig(minValue = 5)
		String SOME_INT = "someInt";

		@Source(FireBaseConfigSource.class)
		@DefaultRes(R.integer.some_int)
		@LongConfig(minValue = 0, minValueFallbackPolicy = RangeFallbackPolicy.RANGE_VALUE)
		String SOME_LONG = "someLong";

		@Source(FireBaseConfigSource.class)
		@FloatConfig(defaultValue = 1.5f, maxValue = 2.5f, maxValueFallbackPolicy = RangeFallbackPolicy.RANGE_VALUE)
		String SOME_FLOAT = "someFloat";

		@Source(FireBaseConfigSource.class)
		@TimeConfig(defaultValue = 0, minValue = 36000, maxValue = 360000000, minValueFallbackPolicy = RangeFallbackPolicy.DEFAULT, maxValueFallbackPolicy = RangeFallbackPolicy.RANGE_VALUE)
		String SOME_DURATION = "someDuration";

		@Source(FireBaseConfigSource.class)
		@StringConfig(defaultValue = "test")
		String SOME_STRING = "someString";

		@Source(FireBaseConfigSource.class)
		@Mutable
		@IntEnumConfig(defaultValue = 2, randomizerValue = 3, enumClass = TextLocation.class)
		String TEXT_LOCATION = "textLocation";

		@Source(FireBaseConfigSource.class)
		@Mutable
		@StringEnumConfig(defaultValue = "top", enumClass = ImageLocation.class)
		String IMAGE_LOCATION = "imageLocation";

		@Source(FireBaseConfigSource.class)
		@Mutable
		@JsonConfig(defaultValue = "{\"int\":2, \"str\":\"test\"}", type = RemoteObject.class)
		String SOME_JSON = "someJson";

		@Source(FireBaseConfigSource.class)
		@Mutable
		@DefaultRes(R.color.colorAccent)
		@ColorConfig
		String SOME_COLOR = "someColor";

		@Source(FireBaseConfigSource.class)
		@Mutable
		@UrlConfig(defaultValue = "")
		String SOME_URL = "someUrl";

		@Source(FireBaseConfigSource.class)
		@StringSetConfig(defaultValue = {"1", "2"})
		String SOME_STRING_SET = "someStringSet";

		@ConfigGroup(value = {SOME_INT, SOME_STRING}) String COOL_GROUP = "coolGroup";

		class Validators {

			@ConfigValidator(SOME_URL)
			static boolean isSomeUrlValid(String val) {
				return val.startsWith("https");
			}
		}

		class DefaultsProvider {

			@ConfigDefaultValueProvider(SOME_URL)
			static String getSomeUrlDefault() {
				return BuildConfig.DEBUG ? "https://debug-url.com" : "https://release-url.com";
			}
		}

		class Adapters {

			@ConfigAdapter(LABEL)
			static Label processLabel(String val) {
				return Label.from(val);
			}
		}
	}
}