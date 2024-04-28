package com.example.ultrahang;

import android.annotation.SuppressLint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {

    private String id;
    private Date date;
    private String userUID;
    private Date created_at;
    private Date updated_at;

    public Appointment() {}
    public Appointment(String id, Date date, Date created_at, Date updated_at) {
        this.id = id;
        this.date = date;
        this.created_at = created_at;
        this.updated_at = updated_at;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.userUID = user.getUid();
        } else {
            this.userUID = "";
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public String getUserUID() {
        return userUID;
    }

    public String getCreated_at() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        return dateFormat.format(created_at);
    }

    public String getUpdated_at() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        return dateFormat.format(updated_at);
    }
}
