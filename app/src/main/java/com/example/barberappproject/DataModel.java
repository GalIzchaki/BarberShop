package com.example.barberappproject;

import java.util.Date;

public class DataModel {
    Date startDate;
    long duration;
    String userEmail;
    String docId; // Id in firebase
    int id_; // index in RecycleView

    public DataModel(Date startDate, long duration, String userEmail, String docId, int id_) {
        this.startDate = startDate;
        this.duration = duration;
        this.userEmail = userEmail;
        this.docId = docId;
        this.id_ = id_;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getDuration() {
        return duration;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDocId() { return docId; }

    public int getId_() {
        return id_;
    }

}
