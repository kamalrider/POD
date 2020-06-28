package com.kamalrider.pod.scan.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;

@Entity
public class ScanCN {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String conno;
    private String date;
    private String status;
    private double lat;
    private double lon;

//    private File photoFilePath;
//    private File pdfFilePath;
    private String user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConno() {
        return conno;
    }

    public void setConno(String conno) {
        this.conno = conno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

//    public File getPhotoFilePath() {
//        return photoFilePath;
//    }

//    public void setPhotoFilePath(File photoFilePath) {
//        this.photoFilePath = photoFilePath;
//    }

//    public File getPdfFilePath() {
//        return pdfFilePath;
//    }

//    public void setPdfFilePath(File pdfFilePath) {
//        this.pdfFilePath = pdfFilePath;
//    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
