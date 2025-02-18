package com.coolmanshaft.Vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NavProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nav_profile);

        // Adjust for system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnBoostCoins = findViewById(R.id.btnBoostCoins);

        // Set click listeners
        btnSignIn.setOnClickListener(v -> navigateToSignIn());
        btnSignUp.setOnClickListener(v -> navigateToSignUp());
        btnBoostCoins.setOnClickListener(v -> boostCoins());
    }

    private void navigateToSignIn() {
        // Replace with your Sign In activity or fragment navigation logic
        Toast.makeText(this, "Navigating to Sign In", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void navigateToSignUp() {
        // Replace with your Sign Up activity or fragment navigation logic
        Toast.makeText(this, "Navigating to Sign Up", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void boostCoins() {
        // Add logic for boosting coins, e.g., opening a payment dialog or activity
        Toast.makeText(this, "Boost Coins feature coming soon!", Toast.LENGTH_SHORT).show();
        // You can implement a dialog or new activity for payment here
    }
}
