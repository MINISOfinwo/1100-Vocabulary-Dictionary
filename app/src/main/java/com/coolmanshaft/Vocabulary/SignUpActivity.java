package com.coolmanshaft.Vocabulary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.etSignUpEmail);
        passwordEditText = findViewById(R.id.etSignUpPassword);
        nameEditText = findViewById(R.id.etSignUpName);
        signUpButton = findViewById(R.id.btnSignUp);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                saveUserData(email, password, name);
                sendVerificationEmail(email);
                Toast.makeText(SignUpActivity.this, "Account Created! Verification email sent.", Toast.LENGTH_SHORT).show();
                finish(); // Close the SignUpActivity
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(String email, String password, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();
    }

    private void sendVerificationEmail(String email) {
        String subject = "Welcome to Vocabulary App!";
        String message = "Hi, \n\nThank you for signing up! Please verify your email to complete the registration process.\n\nBest regards,\nVocabulary Team";

        EmailSender emailSender = new EmailSender(email, subject, message);
        emailSender.execute(); // Start sending email in background
    }
}
