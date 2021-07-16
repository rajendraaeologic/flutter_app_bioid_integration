package com.bioid.authenticator.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import com.bioid.authenticator.BuildConfig;
import androidx.annotation.NonNull;
import com.bioid.authenticator.base.network.bioid.webservice.token.BwsTokenProvider;
import com.bioid.authenticator.facialrecognition.enrollment.EnrollmentActivity;
import com.bioid.authenticator.facialrecognition.verification.VerificationActivity;
import io.flutter.embedding.android.FlutterActivity;

public class MainActivity extends FlutterActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_VERIFY = 0;
    private static final int REQUEST_CODE_ENROLL = 1;
    private static final String CHANNEL = "flutter.native/bioid";
    private static BwsTokenProvider tokenProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //setSupportActionBar(binding.toolbar);

        // In a real world scenario you would determine the BCID based on the user which should be verified or enrolled.
         tokenProvider = new BwsTokenProvider(BuildConfig.BIOID_BCID);

        /*binding.verificationNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
            intent.putExtra(VerificationActivity.EXTRA_TOKEN_PROVIDER, tokenProvider);
            startActivityForResult(intent, REQUEST_CODE_VERIFY);
        });

        binding.enrollmentNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EnrollmentActivity.class);
            intent.putExtra(EnrollmentActivity.EXTRA_TOKEN_PROVIDER, tokenProvider);
            startActivityForResult(intent, REQUEST_CODE_ENROLL);
        });*/
    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("verify")) {
                Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
                intent.putExtra(VerificationActivity.EXTRA_TOKEN_PROVIDER, tokenProvider);
                startActivityForResult(intent, REQUEST_CODE_VERIFY);
            } else if (call.method.equals("enroll")) {
                Intent intent = new Intent(MainActivity.this, EnrollmentActivity.class);
                intent.putExtra(EnrollmentActivity.EXTRA_TOKEN_PROVIDER, tokenProvider);
                startActivityForResult(intent, REQUEST_CODE_ENROLL);
            } else {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_VERIFY:
                Log.i(TAG, "Verification successful: " + (resultCode == Activity.RESULT_OK));
                break;
            case REQUEST_CODE_ENROLL:
                Log.i(TAG, "Enrollment successful: " + (resultCode == Activity.RESULT_OK));
                break;
        }
    }
}
