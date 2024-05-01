package com.example.ultrahang;

import android.annotation.SuppressLint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {

    private String id;
    private String date;

    private String userUID;
    private String created_at;
    private String updated_at;

    public Appointment() {}
    public Appointment(String date) {
        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        this.date = date;
        this.created_at = dateFormat.format(currentTime);
        this.updated_at = dateFormat.format(currentTime);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.userUID = user.getUid();
        } else {
            this.userUID = "";
        }
    }

    public String getDate() {
        return date;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String _getId() {
        return this.id;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
