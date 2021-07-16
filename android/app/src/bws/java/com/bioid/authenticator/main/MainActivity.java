package com.bioid.authenticator.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.bioid.authenticator.base.network.bioid.webservice.token.BwsTokenProvider;
import com.bioid.authenticator.facialrecognition.enrollment.EnrollmentActivity;
import com.bioid.authenticator.facialrecognition.verification.VerificationActivity;
import com.example.flutter_app_bioid_integration.BuildConfig;
import com.example.flutter_app_bioid_integration.R;
import com.example.flutter_app_bioid_integration.databinding.ActivityMainBinding;

/**
 * Activity demonstrating how to start the verification and enrollment process.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_VERIFY = 0;
    private static final int REQUEST_CODE_ENROLL = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        // In a real world scenario you would determine the BCID based on the user which should be verified or enrolled.
        final BwsTokenProvider tokenProvider = new BwsTokenProvider(BuildConfig.BIOID_BCID);

        binding.verificationNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
            intent.putExtra(VerificationActivity.EXTRA_TOKEN_PROVIDER, tokenProvider);
            startActivityForResult(intent, REQUEST_CODE_VERIFY);
        });

        binding.enrollmentNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EnrollmentActivity.class);
            intent.putExtra(EnrollmentActivity.EXTRA_TOKEN_PROVIDER, tokenProvider);
            startActivityForResult(intent, REQUEST_CODE_ENROLL);
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
