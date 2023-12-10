package org.falldetectives.falldetector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

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

        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel selectedUser = (UserModel) parent.getItemAtPosition(position);

                // Start MainActivity and pass the selected user

                Intent intent = new Intent(RegisteredUserActivity.this, MainActivity.class);
                intent.putExtra("SELECTED_USER", selectedUser);
                startActivity(intent);
            }
        });
    }
}
