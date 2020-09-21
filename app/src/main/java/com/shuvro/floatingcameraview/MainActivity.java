package com.shuvro.floatingcameraview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_DRAW_OVERLAY_PERMISSION = 5;
    private CameraFloatingWindow cameraFloatingWindow;
    private String[] permissions = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DRAW_OVERLAY_PERMISSION) {

            if (Settings.canDrawOverlays(this)) {
                cameraFloatingWindow.show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission not granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startManageDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btn_click(View view) {
        if(hasNoPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        else {
            cameraFloatingWindow = new CameraFloatingWindow(this);
            if (Settings.canDrawOverlays(getApplicationContext())) {
                cameraFloatingWindow.show();
            } else {
                startManageDrawOverlaysPermission();
            }
        }
    }

    private boolean hasNoPermissions(){
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED;
    }
}