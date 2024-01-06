package org.falldetectives.falldetector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FallHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FallDataAdapter adapter;
    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall_history);

        ImageView returnArrow = findViewById(R.id.iconRightArrow_settings);
        Toast.makeText(getApplicationContext(), "Profile Page", Toast.LENGTH_SHORT).show();
        returnArrow.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.fallHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        userDatabase = new UserDatabase(this);
        int userId = getCurrentUserId();
        List<FallData> userFallHistory = userDatabase.getFallHistory(userId);

        adapter = new FallDataAdapter(userFallHistory);
        recyclerView.setAdapter(adapter);
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_NAME", MODE_PRIVATE);
        // Assuming "UserID" is the key used to store the user's ID upon login
        return sharedPreferences.getInt("ID", -1); // Returns -1 if no ID is found
    }
}



