package com.coolmanshaft.Vocabulary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    private static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/MINISOfinwo/my-app-updates/refs/heads/main/update.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setContentView(R.layout.activity_settings);

        // Button to check for updates
        Button checkUpdateButton = findViewById(R.id.updater);
        checkUpdateButton.setOnClickListener(v -> checkForUpdates());

        // Button to open a Google Drive link
        Button openDriveButton = findViewById(R.id.Get);
        openDriveButton.setOnClickListener(v -> openDriveLink());
    }

    private void checkForUpdates() {
        new CheckForUpdateTask().execute(UPDATE_JSON_URL);
    }

    private void openDriveLink() {
        String driveUrl = "https://drive.google.com/drive/folders/1GUG1zGLC1VdNXvZ2ZKfwoQUIZmkjZFH3?usp=sharing";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(driveUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No browser found to open the link", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentAppVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.5"; // Default version in case of error
        }
    }

    private class CheckForUpdateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();
                return jsonBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(SettingsActivity.this, "Failed to check for updates", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject json = new JSONObject(result);
                String latestVersion = json.getString("version");
                String apkUrl = json.getString("apk_url");

                String currentVersion = getCurrentAppVersion();
                if (compareVersions(currentVersion, latestVersion) < 0) {
                    showUpdateDialog(apkUrl);
                } else {
                    Toast.makeText(SettingsActivity.this, "You are already on the latest version", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SettingsActivity.this, "Error parsing update information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showUpdateDialog(String apkUrl) {
        new AlertDialog.Builder(this)
                .setTitle("Update Available")
                .setMessage("A newer version is available. Do you want to update?")
                .setPositiveButton("Update", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apkUrl));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private int compareVersions(String version1, String version2) {
        String[] versionParts1 = version1.split("\\.");
        String[] versionParts2 = version2.split("\\.");
        int length = Math.max(versionParts1.length, versionParts2.length);

        for (int i = 0; i < length; i++) {
            int v1 = i < versionParts1.length ? Integer.parseInt(versionParts1[i]) : 0;
            int v2 = i < versionParts2.length ? Integer.parseInt(versionParts2[i]) : 0;
            if (v1 > v2) return 1;
            if (v1 < v2) return -1;
        }
        return 0;
    }
}
