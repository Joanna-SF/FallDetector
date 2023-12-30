package org.falldetectives.falldetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ImageView returnArrow = findViewById(R.id.iconRightArrow_settings);
        AppCompatButton editProfileButton = findViewById(R.id.editProfileButton);
        RelativeLayout goToBluetoothSettings = findViewById(R.id.goToBluetoothSettings);
        RelativeLayout goToFallHistory = findViewById(R.id.goToFallHistory);
        RelativeLayout goToFAQs = findViewById(R.id.goToFAQs);
        LinearLayout goToLogOut = findViewById(R.id.goToLogOut);


        returnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        goToBluetoothSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, BluetoothSettingsActivity.class);
                startActivity(intent);
            }
        });

        goToFAQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "FAQs", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(SettingsActivity.this, FallHistoryActivity.class);
                //startActivity(intent);
            }
        });

        goToFallHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Fall History", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, FallHistoryActivity.class);
                startActivity(intent);
            }
        });

        goToLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, EnterInAppActivity.class);
                startActivity(intent);
            }
        });


        }
    }

