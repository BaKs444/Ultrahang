package com.example.ultrahang;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {
    private static final String PREFERENCE_KEY = RegistrationActivity.class.getPackage().toString();
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    EditText fullNameEditText;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_registration);
        } else {
            setContentView(R.layout.activity_registration_land);
        }

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key != 2102) {
            finish();
        }

        emailEditText = findViewById(R.id.editTextEmailRegistration);
        passwordEditText = findViewById(R.id.editTextPassword);
        passwordAgainEditText = findViewById(R.id.editTextPasswordAgain);
        fullNameEditText = findViewById(R.id.editTextFullName);

        preferences = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        emailEditText.setText(email);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void registration(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if (!password.equals(passwordAgain)) {
            Toast.makeText(this, "A jelszavak nem egyeznek.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(RegistrationActivity.this, "Sikeres regisztráció.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegistrationActivity.this, "Sikertelen regisztráció: ", Toast.LENGTH_LONG).show();
            }
        });
    }

}