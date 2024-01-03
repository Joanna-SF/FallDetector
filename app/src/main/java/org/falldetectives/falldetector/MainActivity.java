package org.falldetectives.falldetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    TextView personEmergencyContact;
    TextView personName;
    //public String phoneNumber;
    private static final int COUNTDOWN_REQUEST_CODE = 2;
    private static final int REQUEST_OK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBioLibDeveloper = findViewById(R.id.biolib_developer);
        Button buttonBioLibUser = findViewById(R.id.biolib_user);
        Button buttonFallHistory = findViewById(R.id.fall_history);

        personEmergencyContact = findViewById(R.id.editTextPhone);
        //phoneNumber= personEmergencyContact.getText().toString();

        //button that simulates the fall
        Button buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(this::sendMessage);
        personName=findViewById(R.id.textView13);

        buttonBioLibDeveloper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, BioLibTestActivity.class);
                String phoneNumber=personEmergencyContact.getText().toString();
                //Toast.makeText(getApplicationContext(), "phoneNumber" +phoneNumber, Toast.LENGTH_SHORT).show();
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                startActivity(intent);
            }
        });

        buttonBioLibUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intentBioLibUser = new Intent(MainActivity.this, BioLibUserActivity.class);
                String phoneNumber=personEmergencyContact.getText().toString();
                //Toast.makeText(getApplicationContext(), "phoneNumber" +phoneNumber, Toast.LENGTH_SHORT).show();
                intentBioLibUser.putExtra("PHONE_NUMBER", phoneNumber);
                startActivity(intentBioLibUser);
            }
        });

        buttonFallHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intentFallHistory = new Intent(MainActivity.this, DisplayHistoryActivity.class);
                startActivity(intentFallHistory);
            }
        });


        // Check for SMS permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SELECTED_USER")) {
            UserModel selectedUser = (UserModel) intent.getSerializableExtra("SELECTED_USER");

            personEmergencyContact.setText(String.valueOf(selectedUser.getEmergency_contact()));
            String welcomeMessage = "Welcome, " + selectedUser.getName() + "!";
            personName.setText(welcomeMessage);

            //personName.setText(String.valueOf((selectedUser.getName())));


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
                Toast.makeText(getApplicationContext(), "User is Ok", Toast.LENGTH_SHORT).show();

            } else if (resultCode == CountdownActivity.RESULT_SEND_FALL_ALERT) {

                sendFallAlert();
            } else {

                sendFallAlert();
            }

        }
    }
    public void sendFallAlert() {

        EditText editTextPhoneNumber = findViewById(R.id.editTextPhone);
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String message = getResources().getString(R.string.fall_message);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Fall alert sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Fall alert failed to send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            // Handle the case where SMS permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }


};
