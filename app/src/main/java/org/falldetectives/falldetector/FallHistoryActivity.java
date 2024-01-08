package org.falldetectives.falldetector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class FallHistoryActivity extends AppCompatActivity {
    private LinearLayout linearLayoutFallHistory;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall_history);

        linearLayoutFallHistory = findViewById(R.id.linear_layout_fall_history);
        userDatabase = new UserDatabase(this);

        String currentUserName = getCurrentUserName();
        displayFallHistory(currentUserName);
    }

    private void displayFallHistory(String userName) {
        List<FallData> fallHistory = userDatabase.getFallHistory(userName);
        if (fallHistory.isEmpty()) {
            // Handle empty fall history
            Log.d("FallHistoryActivity", "No fall history for user: " + userName);
            return;
        }

        for (FallData fallData : fallHistory) {
            TextView textView = new TextView(this);
            // ... [Set text and other properties]
            linearLayoutFallHistory.addView(textView);
        }
    }

    private String getCurrentUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        return sharedPreferences.getString("UserName", "Unknown User");
    }
}