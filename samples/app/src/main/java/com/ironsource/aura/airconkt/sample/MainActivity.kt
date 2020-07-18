package com.ironsource.aura.airconkt.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFireBaseConfig()
    }

    private fun loadFireBaseConfig() {
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

    private fun onFireBaseConfigLoaded() {
        Toast.makeText(this, "FireBase remote config loaded", Toast.LENGTH_SHORT)
                .show()
    }
}