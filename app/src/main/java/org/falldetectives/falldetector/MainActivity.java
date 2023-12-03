package org.falldetectives.falldetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.content.Intent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int COUNTDOWN_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(this::sendMessage);

        // Check for SMS permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    public void sendMessage(View v) {
        startActivityForResult(CountdownActivity.newIntent(this), COUNTDOWN_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUNTDOWN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // The user clicked "I'm Ok" in CountdownActivity
                Toast.makeText(getApplicationContext(), "User is Ok", Toast.LENGTH_SHORT).show();
            } else if (resultCode == CountdownActivity.RESULT_SEND_FALL_ALERT) {
                // The user clicked "Send Fall Alert" in CountdownActivity
                sendFallAlert();
            } else {
                // The countdown elapsed without any action
                sendFallAlert(); // You can adjust this behavior based on your requirements
            }
        }
    }

    private void sendFallAlert() {
        // Implement the logic to send the fall alert message
        EditText editTextPhoneNumber = findViewById(R.id.editTextPhone);
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String message = getResources().getString(R.string.fall_message);

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Fall alert sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fall alert failed to send", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}