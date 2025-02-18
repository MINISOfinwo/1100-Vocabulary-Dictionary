package com.coolmanshaft.Vocabulary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NavSearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchBar;
    private Button searchButton;
    private ListView historyListView;
    private ArrayList<String> searchHistory;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_search);

        // Initialize views
        searchBar = findViewById(R.id.history);
        searchButton = findViewById(R.id.btnh);
        historyListView = findViewById(R.id.history_list_view);

        // Load search history
        loadSearchHistory();

        // Set up adapter for ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchHistory);
        historyListView.setAdapter(adapter);

        // Handle list item clicks
        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedWord = searchHistory.get(position);
            searchBar.setText(selectedWord);
            fetchMeaning(selectedWord);
        });

        // Handle search button click
        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                addToSearchHistory(query);
                fetchMeaning(query);
            } else {
                Toast.makeText(this, "Please enter a word to search.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.search_nav);

        // Highlight the correct menu item
        int selectedItem = getIntent().getIntExtra("selected_nav_item", R.id.nav_search);
        bottomNavigationView.setSelectedItemId(selectedItem);

        // Handle navigation
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            // Navigate to MainActivity
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.putExtra("selected_nav_item", R.id.nav_home);
            startActivity(homeIntent);
            return true;
        } else if (itemId == R.id.nav_search) {
            // Stay on the current activity
            return true;
        } else if (itemId == R.id.nav_profile) {
            Intent homeIntent = new Intent(this,NavProfileActivity.class);
            startActivity(homeIntent);
            return true;
        }
        return false;
    }

    private void loadSearchHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        Set<String> historySet = sharedPreferences.getStringSet("history", new HashSet<>());
        searchHistory = new ArrayList<>(historySet);
    }

    private void addToSearchHistory(String query) {
        if (!searchHistory.contains(query)) {
            searchHistory.add(query);
            adapter.notifyDataSetChanged();

            SharedPreferences sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Set<String> historySet = new HashSet<>(searchHistory);
            editor.putStringSet("history", historySet);
            editor.apply();
        }
    }

    private void fetchMeaning(String query) {
        // Placeholder logic for fetching the meaning
        Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }
}
