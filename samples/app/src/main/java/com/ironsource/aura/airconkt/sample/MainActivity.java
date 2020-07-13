package com.ironsource.aura.airconkt.sample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.ironsource.aura.airconkt.injection.AirConAppCompatActivity;

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
    }
}
