package com.ironsource.aura.airconkt.sample;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
//        FirebaseRemotegetInstance()
//                .fetch(0)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(final Void aVoid) {
//                        FirebaseRemotegetInstance()
//                                .activate();
//                        Log.i(App.TAG, "Firebase config loaded");
//                        onFireBaseConfigLoaded();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull final Exception e) {
//                        Log.e(App.TAG, "Failed to load firebase config: " + e);
//                        onFireBaseConfigLoaded();
//                    }
//                });
    }

    private void onFireBaseConfigLoaded() {
        Toast.makeText(this, "FireBase remote config loaded", Toast.LENGTH_SHORT)
                .show();
    }
}
