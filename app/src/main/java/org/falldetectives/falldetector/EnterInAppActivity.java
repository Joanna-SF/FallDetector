package org.falldetectives.falldetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EnterInAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_in_app);

        // Find the "New User" and "Registered User" buttons
        Button newUserButton = findViewById(R.id.button);
        Button registeredUserButton = findViewById(R.id.button2);

        // Set up click listener for the "New User" button
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NewUserActivity
                Intent intent = new Intent(EnterInAppActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        // Set up click listener for the "Registered User" button
        registeredUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the RegisteredUserActivity
                Intent intent = new Intent(EnterInAppActivity.this, RegisteredUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
