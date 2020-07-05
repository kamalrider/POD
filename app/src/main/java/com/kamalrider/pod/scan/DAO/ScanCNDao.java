package com.kamalrider.pod.scan.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kamalrider.pod.scan.model.ScanCN;

import java.util.List;

@Dao
public interface ScanCNDao {

    @Query("SELECT * FROM ScanCN")
    LiveData<List<ScanCN>> getAll();

    @Query("SELECT * FROM ScanCN WHERE conno = 'test'")
    LiveData<ScanCN> getImage();

    @Insert
    long insert(ScanCN scanCN);

    @Update
    void update(ScanCN scanCN);

    @Delete
    void delete(ScanCN scanCN);

}
