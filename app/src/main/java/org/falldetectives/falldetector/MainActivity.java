package org.falldetectives.falldetector;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.content.Intent;

import android.Manifest;
import android.content.Intent;
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
    private static final int COUNTDOWN_REQUEST_CODE = 2;
    private static final int REQUEST_OK = 3;
    private UserModel selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBioLibDeveloper = findViewById(R.id.biolib_developer);
        Button buttonBioLibUser = findViewById(R.id.biolib_user);

        personEmergencyContact = findViewById(R.id.editTextPhone);
        Button buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(this::sendMessage);
        personName = findViewById(R.id.textView13);

        buttonBioLibDeveloper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BioLibTestActivity.class);
                String phoneNumber = personEmergencyContact.getText().toString();
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                startActivity(intent);
            }
        });

        Button editProfileButton = findViewById(R.id.button4);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to start EditProfileActivity
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);

                // Pass the selected user information to EditProfileActivity
                if (selectedUser != null) {
                    intent.putExtra("SELECTED_USER", selectedUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBioLibUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intentBioLibUser = new Intent(MainActivity.this, BioLibUserActivity.class);
                String phoneNumber = personEmergencyContact.getText().toString();
                intentBioLibUser.putExtra("PHONE_NUMBER", phoneNumber);
                startActivity(intentBioLibUser);
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
            selectedUser = (UserModel) intent.getSerializableExtra("SELECTED_USER");

            personEmergencyContact.setText(String.valueOf(selectedUser.getEmergency_contact()));
            String welcomeMessage = "Welcome, " + selectedUser.getName() + "!";
            personName.setText(welcomeMessage);
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
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }
}
