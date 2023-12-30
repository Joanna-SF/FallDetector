package org.falldetectives.falldetector;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FallHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall_history);

        ImageView returnArrow = findViewById(R.id.iconRightArrow_settings);

        Toast.makeText(getApplicationContext(), "Profile Page", Toast.LENGTH_SHORT).show();

        returnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}

