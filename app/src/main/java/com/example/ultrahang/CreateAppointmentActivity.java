package com.example.ultrahang;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateAppointmentActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button datePickerButton;
    EditText dateEditText;
    private int mYear, mMonth, mDay;
    private String date;
    Spinner spinner;
    private FirebaseFirestore db;
    private ArrayList<Appointment> mAppointmentsData;
    private CollectionReference mItems;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        datePickerButton=(Button)findViewById(R.id.appointmentDateSelector);
        dateEditText=(EditText)findViewById(R.id.AppointmentDate);
        datePickerButton.setOnClickListener(this);
        spinner = findViewById(R.id.timeSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        mAppointmentsData = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Appointments");
    }

    private void queryData() {
        Appointment appointment = new Appointment(date + " " + spinner.getSelectedItem().toString());
        mAppointmentsData.clear();
        mItems.whereEqualTo("date", appointment.getDate())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Appointment item = document.toObject(Appointment.class);
                        item.setId(document.getId());
                        mAppointmentsData.add(item);
                    }
                    if (this.mAppointmentsData.isEmpty()) {

                        db.collection("Appointments")
                                .add(appointment)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(CreateAppointmentActivity.this, "Időpontfoglalás sikeres!", Toast.LENGTH_LONG).show();
                                    appointments();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(CreateAppointmentActivity.this, "Időpontfoglalás sikertelen!", Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(CreateAppointmentActivity.this, "Az időpont már foglalt!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateEditText.setText(year + "-" + (monthOfYear < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) +  "-" + ( dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
                date = dateEditText.getText().toString();
                }
            }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createAppointment(View view) {
        queryData();
        }

    private void appointments() {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    private void back() {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

}
