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
        String userName = getCurrentUserName(); // Fetch the current user's name
        List<FallData> userFallHistory = userDatabase.getFallHistory(userName);

        adapter = new FallDataAdapter(userFallHistory);
        recyclerView.setAdapter(adapter);
    }

    private String getCurrentUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_NAME", MODE_PRIVATE);
        return sharedPreferences.getString("UserName", null); // Returns null if no name is found
    }
}




