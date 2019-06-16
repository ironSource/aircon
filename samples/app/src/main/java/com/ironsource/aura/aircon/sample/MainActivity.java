package com.ironsource.aura.aircon.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.ironsource.aura.aircon.injection.AirConAppCompatActivity;
import com.ironsource.aura.aircon.sample.config.CoolGroup;
import com.ironsource.aura.aircon.sample.config.SomeCoolFeatureConfig;
import com.ironsource.aura.aircon.sample.config.SomeCoolFeatureConfigProvider;
import com.ironsource.aura.aircon.sample.config.model.ImageLocation;
import com.ironsource.aura.aircon.sample.config.model.Label;
import com.ironsource.aura.aircon.sample.config.model.RemoteObject;
import com.ironsource.aura.aircon.sample.config.model.TextLocation;
import com.ironsource.aura.aircon.utils.ColorInt;

import java.util.Set;

public class MainActivity
		extends AirConAppCompatActivity {

	private static final String TAG = "MainActivity";

	private TextView mTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loadFireBaseConfig();
	}

	private void loadFireBaseConfig() {
		FirebaseRemoteConfig.getInstance()
		                    .fetch(0)
		                    .addOnSuccessListener(new OnSuccessListener<Void>() {
			                    @Override
			                    public void onSuccess(final Void aVoid) {
				                    FirebaseRemoteConfig.getInstance()
				                                        .activate();
				                    Log.i(App.TAG, "Firebase config loaded");
				                    onFireBaseConfigLoaded();
			                    }
		                    })
		                    .addOnFailureListener(new OnFailureListener() {
			                    @Override
			                    public void onFailure(@NonNull final Exception e) {
				                    Log.e(App.TAG, "Failed to load firebase config: " + e);
				                    onFireBaseConfigLoaded();
			                    }
		                    });
	}

	private void onFireBaseConfigLoaded() {
		Toast.makeText(this, "FireBase remote config loaded", Toast.LENGTH_SHORT)
		     .show();
		if (SomeCoolFeatureConfigProvider.isEnabled()) {
			initSomeCoolFeature();
		}
	}

	private void initSomeCoolFeature() {
		final boolean someFlag = SomeCoolFeatureConfigProvider.isSomeFlag();
		if (someFlag) {
			// Do something
		}

		final Label label = SomeCoolFeatureConfigProvider.getLabel();
		final int someInt = SomeCoolFeatureConfigProvider.getSomeInt();
		final float someFloat = SomeCoolFeatureConfigProvider.getSomeFloat();
		final long someLong = SomeCoolFeatureConfigProvider.getSomeLong();
		final ColorInt someColor = SomeCoolFeatureConfigProvider.getSomeColor();
		final ImageLocation imageLocation = SomeCoolFeatureConfigProvider.getImageLocation();
		final TextLocation textLocation = SomeCoolFeatureConfigProvider.getTextLocation();
		final RemoteObject someJson = SomeCoolFeatureConfigProvider.getSomeJson();
		final String someUrl = SomeCoolFeatureConfigProvider.getSomeUrl();
		final long someDuration = SomeCoolFeatureConfigProvider.getSomeDuration();
		final Set<String> someStringSet = SomeCoolFeatureConfigProvider.getSomeStringSet();
		final Label customLabel = SomeCoolFeatureConfigProvider.getSomeCustomLabel();

		final CoolGroup coolGroup = SomeCoolFeatureConfigProvider.getCoolGroup();
		final int coolGroupInt = coolGroup.getSomeInt();

		final SomeCoolFeatureConfig someCoolFeature = SomeCoolFeatureConfigProvider.getAll();
		doExtraConfigWork(someCoolFeature);
	}

	private void doExtraConfigWork(SomeCoolFeatureConfig coolFeatureConfig) {
		// Do something with the config itself
	}
}
