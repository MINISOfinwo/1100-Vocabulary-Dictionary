package com.coolmanshaft.Vocabulary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in); // Ensure this matches your XML file name

        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        signInButton = findViewById(R.id.btn_sign_in);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (validateUser(email, password)) {
                Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // Navigate to the next activity
                Intent intent = new Intent(SignInActivity.this, EmailSender.class); // Replace with your target activity
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignInActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateUser(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        return email.equals(savedEmail) && password.equals(savedPassword);
    }
}
