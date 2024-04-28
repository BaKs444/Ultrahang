package com.example.ultrahang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {
    private static final String LOG_TAG = AppointmentActivity.class.getName();
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private RecyclerView mRecyclerView;
    private ArrayList<Appointment> mAppointmentsData;
    private AppointmentAdapter mAdapter;
    private CollectionReference mItems;
    private Integer itemLimit = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            finish();
        }

        mFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, 1));
        mAppointmentsData = new ArrayList<>();
        mAdapter = new AppointmentAdapter(mAppointmentsData,this);
        mRecyclerView.setAdapter(mAdapter);
        mItems = mFirestore.collection("Appointments");
        queryData();
    }

    private void queryData() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mAppointmentsData.clear();
        mItems.limit(itemLimit)
                .whereEqualTo("userUID", currentUserUid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Appointment item = document.toObject(Appointment.class);
                        item.setId(document.getId());
                        mAppointmentsData.add(item);
                    }

                    if (mAppointmentsData.isEmpty()) {
                        queryData();
                    }

                    mAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "Hiba a lekérdezés során: ", e);
                });
    }

    public void createAppointment(View view) {
        Intent intent = new Intent(this, CreateAppointmentActivity.class);
        startActivity(intent);
    }

}