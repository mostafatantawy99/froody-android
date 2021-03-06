package io.github.froodyapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.github.froodyapp.R;
import io.github.froodyapp.ui.CustomDialogs;
import io.github.froodyapp.util.Helpers;

/**
 * Splash Screen
 */
public class SplashActivity extends AppCompatActivity {
    //########################
    //## Static
    //########################
    private static final int REQUEST_PERM__STORAGE_AND_LOCATION = 7;

    //########################
    //## Members
    //########################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash__activity);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        requestStoragePermission();
    }

    /**
     * Android M - Request needed Storage & Location permissions
     */
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(ButterKnife.findById(this, android.R.id.content), R.string.error_bad_permissions, Snackbar.LENGTH_LONG).show();
            }

            // If we already learned why this is needed, request the perm from user
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERM__STORAGE_AND_LOCATION);
            return;
        }

        // Older device API, or already granted
        startApp(false);
    }

    /**
     * A request permission result
     *
     * @param req          The requested permission
     * @param perm         Permissions
     * @param grantResults The resuls
     */
    @Override
    public void onRequestPermissionsResult(int req, @NonNull String[] perm, @NonNull int[] grantResults) {
        boolean somethingGranted = grantResults.length > 0;
        switch (req) {
            case REQUEST_PERM__STORAGE_AND_LOCATION:
                if (somethingGranted && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startApp(true);
                    return;
                }
                break;
        }

        CustomDialogs.showErrorLocationPermDeniedDialog(this);
    }

    /**
     * Start the app
     *
     * @param skipDelay Do not add a delay when starting
     */
    private void startApp(boolean skipDelay) {
        int delay = getResources().getInteger(R.integer.splash_delay);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Helpers.animateToActivity(SplashActivity.this, MainActivity.class, true);
            }
        }, skipDelay ? 0 : delay);
    }
}