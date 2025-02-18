package com.coolmanshaft.Vocabulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolmanshaft.Vocabulary.api.DictionaryApi;
import com.coolmanshaft.Vocabulary.model.WordResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Add your API Key
    private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    private TextView EnterWords;
    private AutoCompleteTextView compile;
    private Button button1;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_main);
        initialize();
        initializeLogic();

        // Set up toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // BottomNavigationView setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_search) {
                Intent homeIntent = new Intent(this, NavSearchActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent homeIntent = new Intent(this, NavProfileActivity.class);
                startActivity(homeIntent);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        EnterWords = findViewById(R.id.EnterWords);
        compile = findViewById(R.id.compile);
        button1 = findViewById(R.id.button1);
        resultTextView = findViewById(R.id.text1);

        button1.setOnClickListener(_view -> {
            String query = compile.getText().toString().trim();
            if (!query.isEmpty()) {
                saveSearchQuery(query);
                fetchMeaning(query);
            } else {
                showMessage("Please enter a word.");
            }
        });
    }

    private void saveSearchQuery(String query) {
        SharedPreferences sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> historySet = sharedPreferences.getStringSet("history", new HashSet<>());
        historySet.add(query);
        editor.putStringSet("history", historySet);
        editor.apply();
    }

    private void initializeLogic() {
        // Add any additional initialization logic here
    }

    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void fetchMeaning(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DictionaryApi api = retrofit.create(DictionaryApi.class);

        api.getWordDefinition(query).enqueue(new Callback<List<WordResponse>>() {
            @Override
            public void onResponse(Call<List<WordResponse>> call, Response<List<WordResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    StringBuilder definitions = new StringBuilder();
                    for (WordResponse wordResponse : response.body()) {
                        definitions.append("Word: ").append(wordResponse.getWord()).append("\n");
                        if (wordResponse.getMeanings() != null) {
                            for (WordResponse.Meaning meaning : wordResponse.getMeanings()) {
                                definitions.append("Part of Speech: ").append(meaning.getPartOfSpeech()).append("\n");
                                for (WordResponse.Definition definition : meaning.getDefinitions()) {
                                    definitions.append("Definition: ").append(definition.getDefinition()).append("\n");
                                    if (definition.getExample() != null) {
                                        definitions.append("Example: ").append(definition.getExample()).append("\n");
                                    }
                                }
                            }
                        }
                    }
                    resultTextView.setText(definitions.toString());
                } else {
                    Log.e(TAG, "No definitions available.");
                    resultTextView.setText("Word not found or no definitions available.");
                }
            }

            @Override
            public void onFailure(Call<List<WordResponse>> call, Throwable t) {
                Log.e(TAG, "API Failure: " + t.getMessage(), t);
                resultTextView.setText("Error fetching word: " + t.getMessage());
            }
        });
    }
}
