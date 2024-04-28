package com.example.ultrahang;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class ReserveAppointmentActivity extends AppCompatActivity {
    private static final String LOG_TAG = AppointmentActivity.class.getName();
    private ReserveAppointmentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private ArrayList<Appointment> mAppointmentsData;
    private CollectionReference mItems;
    ItemClickListener itemClickListener;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_appointment);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            finish();
        }

        mFirestore = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, 1));
        itemClickListener = new ItemClickListener() {
            @Override public void onClick(String s)
            {
                mRecyclerView.post(new Runnable() {
                    @Override public void run()
                    {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mAppointmentsData = new ArrayList<>();
        mAdapter = new ReserveAppointmentAdapter(mAppointmentsData,this, itemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mItems = mFirestore.collection("Appointments");
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            date = extras.getString("date");
        }
        queryData();
    }

    private void queryData() {
        mAppointmentsData.clear();
        mItems.whereEqualTo("userUID", "")
                .whereEqualTo("date", date)
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

}
