// EditProfileActivity.java
package org.falldetectives.falldetector;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextBloodType;
    private EditText editTextEmergencyContact;
    private Button buttonSubmit;

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

        // Pre-fill the EditText fields with user information
        if (selectedUser != null) {
            editTextName.setText(selectedUser.getName());
            editTextAge.setText(String.valueOf(selectedUser.getAge()));
            editTextBloodType.setText(selectedUser.getBlood_type());
            editTextEmergencyContact.setText(selectedUser.getEmergency_contact());
        }
    }

    private void submitEdit() {
        // Retrieve the edited values from EditText fields
        String editedName = editTextName.getText().toString();
        // Retrieve other edited values

        // You can now update the user information in your database or wherever it is stored.
        // For demonstration purposes, let's display a toast message.
        Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
    }
}
