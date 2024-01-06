package org.falldetectives.falldetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.content.Context;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    TextView personEmergencyContact;
    TextView personName;
    //public String phoneNumber;
    private static final int COUNTDOWN_REQUEST_CODE = 2;
    private static final int REQUEST_OK = 3;
    private UserModel selectedUser;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 4;

    private FusedLocationProviderClient fusedLocationClient;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button buttonSettings = findViewById(R.id.btSettings);
        Button buttonBluetoothSettings = findViewById(R.id.btBluetoothSettings);

        personEmergencyContact = findViewById(R.id.editTextPhone);

        //button that simulates the fall
        Button buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(this::sendMessage);
        personName=findViewById(R.id.textView13);

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                String phoneNumber=personEmergencyContact.getText().toString();
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

        buttonBluetoothSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intentBioLibUser = new Intent(MainActivity.this, BluetoothSettingsActivity.class);
                String phoneNumber=personEmergencyContact.getText().toString();
                //Toast.makeText(getApplicationContext(), "phoneNumber" +phoneNumber, Toast.LENGTH_SHORT).show();
                intentBioLibUser.putExtra("PHONE_NUMBER", phoneNumber);
                startActivity(intentBioLibUser);
            }
        });

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SELECTED_USER")) {
            selectedUser = (UserModel) intent.getSerializableExtra("SELECTED_USER");

            personEmergencyContact.setText(String.valueOf(selectedUser.getEmergency_contact()));
            String welcomeMessage = "Welcome, " + selectedUser.getName() + "!";
            personName.setText(welcomeMessage);

            //personName.setText(String.valueOf((selectedUser.getName())));


        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
        final String[] fallMessage = {getResources().getString(R.string.fall_message)};

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            getLastKnownLocation(new LocationCallback() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Append location information to the fall message
                        fallMessage[0] += "\n\nLocation: " + location.getLatitude() + ", " +
                                location.getLongitude();

                        try {
                            // Send the SMS with location information
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, fallMessage[0], null, null);
                            Toast.makeText(getApplicationContext(), "Fall alert sent successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Fall alert failed to send", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        // Location is not available
                        // Proceed to send SMS without location information
                        sendSMSWithoutLocation(phoneNumber, fallMessage[0]);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Handle location retrieval failure
                    // Proceed to send SMS without location information
                    sendSMSWithoutLocation(phoneNumber, fallMessage[0]);
                }
            });

        } else {
            // Handle the case where SMS permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    private void sendSMSWithoutLocation(String phoneNumber, String message) {
        try {
            // Send the SMS without location information
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Fall alert sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Fall alert failed to send", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Callback interface for location retrieval
    private interface LocationCallback {
        void onSuccess(Location location);

        void onFailure(String errorMessage);
    }

    private void getLastKnownLocation(LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Location found or not available
                        callback.onSuccess(location);
                    });

        } else {
            // Handle the case where location permission is not granted
            callback.onFailure("Location permission not granted");
        }
    }



};
