package org.falldetectives.falldetector;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RegisteredUserActivity extends AppCompatActivity {
    ListView user_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_user);

        user_list = findViewById(R.id.user_list);

        // Retrieve and display the user list
        UserDatabase userDatabase = new UserDatabase(RegisteredUserActivity.this);
        List<UserModel> everyone = userDatabase.getEveryone();
        ArrayAdapter<UserModel> userArrayAdapter = new ArrayAdapter<>(RegisteredUserActivity.this, android.R.layout.simple_list_item_1, everyone);
        user_list.setAdapter(userArrayAdapter);
    }
}
