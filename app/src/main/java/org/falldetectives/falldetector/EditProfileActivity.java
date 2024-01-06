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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextBloodType;
    private EditText editTextEmergencyContact;
    private Button buttonSubmit;

    private void submitEdit() {
        // Retrieve the edited values from EditText fields
        String editedName = editTextName.getText().toString();
        // Retrieve other edited values

        // You can now update the user information in your database or wherever it is stored.
        // For demonstration purposes, let's display a toast message.
        Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
    }

    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextBloodType = findViewById(R.id.editTextBloodType);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);

        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(v -> submitEdit());

        // Retrieve user information from the intent
        UserModel selectedUser = (UserModel) getIntent().getSerializableExtra("SELECTED_USER");

        // Log the received user information
        Log.d("EditProfileActivity", "Received user: " + selectedUser);

        // Pre-fill the EditText fields with user information
        if (selectedUser != null) {
            Log.d("EditProfileActivity", "Selected user not null");

            editTextName.setText(selectedUser.getName());
            editTextAge.setText(String.valueOf(selectedUser.getAge()));
            editTextBloodType.setText(selectedUser.getBlood_type());
            editTextEmergencyContact.setText(selectedUser.getEmergency_contact());
        } else {
            Log.d("EditProfileActivity", "Selected user is null");
        }
    }
}