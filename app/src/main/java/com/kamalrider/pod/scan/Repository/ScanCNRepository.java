package com.kamalrider.pod.scan.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.kamalrider.pod.core.database.AppDatabase;
import com.kamalrider.pod.core.model.Callback;
import com.kamalrider.pod.scan.DAO.ScanCNDao;
import com.kamalrider.pod.scan.model.ScanCN;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ScanCNRepository {

    private static final String TAG = ScanCNRepository.class.getSimpleName();

    private static volatile ScanCNRepository INSTANCE;

    private ScanCNDao scanCNDao;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private ScanCNRepository(Application application) {
        scanCNDao = AppDatabase.getDatabase(application).scanCNDao();
    }

    public static ScanCNRepository getInstance(Application application) {
        if (INSTANCE == null)
            synchronized (ScanCNRepository.class) {
                if (INSTANCE == null) INSTANCE = new ScanCNRepository(application);
            }

        return INSTANCE;
    }


    public LiveData<List<ScanCN>> getAllCN() {
        return scanCNDao.getAll();
    }

    public LiveData<ScanCN> getImage(){
        return scanCNDao.getImage();
    }

    public void insertScanCN(final ScanCN scanCN, final Callback<Long> callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long id = scanCNDao.insert(scanCN);
                if (id> 0)
                    callback.onSuccess(id);
                else
                    callback.onFailure("Duplicate entry. Record not saved.");
                //deliveredScanDao.insert(deliveredScan);
            }
        });
    }

    public void updateScanCN(final ScanCN scanCN) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scanCNDao.update(scanCN);
            }
        });
    }

    public void deleteScanCN(final ScanCN scanCN) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                scanCNDao.delete(scanCN);
            }
        });
    }

}
