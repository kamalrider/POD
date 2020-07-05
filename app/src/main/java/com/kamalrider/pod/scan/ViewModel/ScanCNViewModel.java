package com.kamalrider.pod.scan.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kamalrider.pod.core.model.Callback;
import com.kamalrider.pod.scan.Repository.ScanCNRepository;
import com.kamalrider.pod.scan.model.ScanCN;

import java.util.List;

public class ScanCNViewModel extends AndroidViewModel {

    private LiveData<List<ScanCN>> scanCNlist;

    private ScanCNRepository scanCNRepository;

    public ScanCNViewModel(@NonNull Application application) {
        super(application);
        scanCNRepository = ScanCNRepository.getInstance(application);
    }

    public LiveData<List<ScanCN>> getScanCNlist() {
        return scanCNRepository.getAllCN();
    }

    public LiveData<ScanCN> getImage(){
        return scanCNRepository.getImage();
    }

    public void inserScanCN(ScanCN scanCN, Callback<Long> callback) {
        scanCNRepository.insertScanCN(scanCN, callback);
    }

    public void deleteScanCN(ScanCN scanCN) {
        scanCNRepository.deleteScanCN(scanCN);
    }


}
