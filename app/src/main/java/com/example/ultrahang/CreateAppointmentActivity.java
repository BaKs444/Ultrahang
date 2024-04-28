package com.example.ultrahang;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CreateAppointmentActivity extends AppCompatActivity implements
        View.OnClickListener {
    Button datePickerButton;
    EditText dateEditText;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        datePickerButton=(Button)findViewById(R.id.appointmentDateSelector);
        dateEditText=(EditText)findViewById(R.id.AppointmentDate);
        datePickerButton.setOnClickListener(this);
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
                dateEditText.setText(year + "-" + (monthOfYear + 1) +  "-" + dayOfMonth);
                }
            }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showAppointments(View view) {
            Intent intent = new Intent(this, ReserveAppointmentActivity.class);
            intent.putExtra("date", dateEditText.getText().toString());
            startActivity(intent);
        }
}
