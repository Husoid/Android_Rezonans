package com.rezonans.Logon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.rezonans.R;

import java.io.IOException;

public class BarCodeReaderActivity extends AppCompatActivity implements Detector.Processor {

    private TextView textView;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    private Button button;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_REFRESH_TOKEN = "refreshToken";
    SharedPreferences mSettings;
    String qrData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        button = this.findViewById(R.id.button2);
        textView = (TextView) this.findViewById(R.id.textView);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);

        startCam();
    }

    public void startCam() {
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        //barcodeDetector.setProcessor(this);

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException ie) {
                    Log.e("Camera start problem", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(this);
    }

    @Override
    public void release() {}

    @Override
    public void receiveDetections(Detector.Detections detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

        if (barcodes.size() != 0) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < barcodes.size(); ++i) {
                sb.append(barcodes.valueAt(i).rawValue).append("\n");
            }

            qrData = sb.toString();

            button.post(new Runnable() {
                @Override
                public void run() {
                    // textView.setText(sb.toString());
                    button.setVisibility(View.VISIBLE);
                    cameraSource.stop();//.release();
                    barcodeDetector.release();
                }
            });

        }
    }

    public void logon(View view) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_REFRESH_TOKEN, qrData);
        editor.apply();
        button.setVisibility(View.GONE);
        Intent intent = new Intent(BarCodeReaderActivity.this, LogonActivity.class);
        startActivity(intent);
        finish();
    }
}
