package com.kamalrider.pod.scan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.kamalrider.pod.R;
import com.kamalrider.pod.core.model.Callback;
import com.kamalrider.pod.scan.Adapter.ScanCNAdapter;
import com.kamalrider.pod.scan.ViewModel.ScanCNViewModel;
import com.kamalrider.pod.scan.model.ScanCN;

import java.util.ArrayList;
import java.util.List;

public class ScanCNActivity extends AppCompatActivity {

    private static final String TAG = ScanCNActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAMERA = 0;
    private static final int DELAY = 2000;


    private DecoratedBarcodeView barcodeScanner;
    private BeepManager beepManager;

    private String manifest;

    private TextView txtScan;
    private int totalScan = 0;

    private ScanCNViewModel scanCNViewModel;
    private List<ScanCN> cnList = new ArrayList<>();

    private ScanCNAdapter adapter;
    private LinearLayoutManager mLayoutmanager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_c_n);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        barcodeScanner = findViewById(R.id.barcodeScanner);
        barcodeScanner.setStatusText("SCAN the manifest");
        beepManager = new BeepManager(ScanCNActivity.this);
        beepManager.setVibrateEnabled(true);
        txtScan = findViewById(R.id.txtTotalScan);

        scanCNViewModel = ViewModelProviders.of(ScanCNActivity.this).get(ScanCNViewModel.class);

        scanCNViewModel.getScanCNlist().observe(this, new Observer<List<ScanCN>>() {
            @Override
            public void onChanged(List<ScanCN> scanCNS) {

                totalScan = scanCNS.size();

                txtScan.setText(String.valueOf(totalScan));

                cnList.clear();
                cnList.addAll(scanCNS);
                recyclerView.scrollToPosition(scanCNS.size() - 1);
                adapter.notifyDataSetChanged();

            }
        });

        mLayoutmanager = new LinearLayoutManager(this);
        mLayoutmanager.setReverseLayout(true);
        mLayoutmanager.setStackFromEnd(true);

        recyclerView = findViewById(R.id.recyclerviewrunsheets);
        recyclerView.setLayoutManager(mLayoutmanager);
        recyclerView.setHasFixedSize(true);

        adapter = new ScanCNAdapter(this, cnList);
        adapter.setOnItemClickListener(new ScanCNAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {

                final ScanCN q = cnList.get(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(ScanCNActivity.this);
                alert.setMessage("Delete this record?");
                alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        scanCNViewModel.deleteScanCN(q);
                    }
                });
                alert.setNegativeButton("CANCEL", null);
                alert.show();

            }

            @Override
            public void onItemClick(int position, View v) {

//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(cnList.get(position).getConno()));
//                startActivity(i);

                Intent intent = new Intent(ScanCNActivity.this, UpdateWorkActivity.class);
                intent.putExtra(UpdateWorkActivity.CN, cnList.get(position).getConno());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);


        //camera permission
        if (ActivityCompat.checkSelfPermission(ScanCNActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanCNActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAMERA);
        } else {
            barcodeScanner.decodeContinuous(callback);
        }

    }

    //scanner function
    private BarcodeCallback callback = new BarcodeCallback() {
        private long lastTimestamp = 0;

        @Override
        public void barcodeResult(BarcodeResult result) {
            if (System.currentTimeMillis() - lastTimestamp < DELAY) {
                // Too soon after the last barcode - ignore.
                return;
            }

            Log.e(TAG, "onScanned: " + result.getText());
            beepManager.playBeepSoundAndVibrate();

            manifest = result.getText();

            insertCN(manifest);
            System.out.println(manifest);

            lastTimestamp = System.currentTimeMillis();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    barcodeScanner.decodeContinuous(callback);
                } else {
                    Toast.makeText(ScanCNActivity.this, "Camera access denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(ScanCNActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            barcodeScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(ScanCNActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            barcodeScanner.pause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void insertCN(final String manifest) {

        ScanCN scanCN = new ScanCN();
        scanCN.setConno(manifest);
        scanCNViewModel.inserScanCN(scanCN, new Callback<Long>() {
            @Override
            public void onSuccess(Long result) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ScanCNActivity.this, manifest, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ScanCNActivity.this, "Duplicate CN", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}