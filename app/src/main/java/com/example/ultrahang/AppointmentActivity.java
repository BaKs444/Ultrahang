package com.example.ultrahang;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {
    private static final String LOG_TAG = AppointmentActivity.class.getName();
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private RecyclerView mRecyclerView;
    private ArrayList<Appointment> mAppointmentsData;
    private AppointmentAdapter mAdapter;
    private CollectionReference mItems;
    private Integer itemLimit = 5;
    private FirebaseFirestore db;
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
        db = FirebaseFirestore.getInstance();
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

    public void deleteAppointment(Appointment currentItem) {
        DocumentReference ref = mItems.document(currentItem._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Időpont sikeresen törölve");
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Időpont törlése sikertelen", Toast.LENGTH_LONG).show();
                });

        queryData();
    }

    public void updateAppointment(Appointment currentItem) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                    update(selectedDate, currentItem);
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void update(String selectedDate, Appointment currentItem) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (selectedHour >= 12) {
                    selectedHour = 12;
                } else if (selectedHour <= 8) {
                    selectedHour = 8;
                }

                if (0 <= selectedMinute && selectedMinute <= 29) {
                    selectedMinute = 0;
                } else if (30 <= selectedMinute && selectedMinute <= 59) {
                    selectedMinute = 30;
                }

        String date = selectedDate + " " +  (selectedHour < 10 ? "0" + String.valueOf(selectedHour) : String.valueOf(selectedHour))  + ":" + (selectedMinute < 10 ? "0" + String.valueOf(selectedMinute) : String.valueOf(selectedMinute)) + ":00";
        DocumentReference appointmentRef = db.collection("Appointments").document(currentItem._getId());
        appointmentRef.update("date", date)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AppointmentActivity.this, "Módosítás sikeres!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AppointmentActivity.this, "Módosítás sikertelen!", Toast.LENGTH_SHORT).show();
                });
        queryData();

            }
        }, hour, minute, true);
        mTimePicker.setTitle("Válassz!");
        mTimePicker.show();
    }

}