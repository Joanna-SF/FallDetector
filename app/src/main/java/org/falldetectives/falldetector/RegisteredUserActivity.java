package org.falldetectives.falldetector;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RegisteredUserActivity extends AppCompatActivity {
    private ListView user_list;
    private UserModel lastClickedUser;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_INTERVAL = 500; // Time interval in milliseconds for a double click

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

                long clickTime = System.currentTimeMillis();

                // Check for double click
                if (selectedUser.equals(lastClickedUser) && (clickTime - lastClickTime) < DOUBLE_CLICK_INTERVAL) {
                    // Double click detected, delete the user
                    Intent intent1 = new Intent(RegisteredUserActivity.this, RegisteredUserActivity.class);
                    startActivity(intent1);
                    deleteUser(selectedUser);
                } else {
                    // Single click, update the last clicked user and time
                    lastClickedUser = selectedUser;
                    lastClickTime = clickTime;

                    // Start MainActivity and pass the selected user
                    Intent intent = new Intent(RegisteredUserActivity.this, MainActivity.class);
                    intent.putExtra("SELECTED_USER", selectedUser);
                    startActivity(intent);
                }
            }
        });
    }

    private void deleteUser(UserModel user) {
        // Placeholder: Implement the logic to delete the user from the list and database
        ArrayAdapter<UserModel> adapter = (ArrayAdapter<UserModel>) user_list.getAdapter();
        if (adapter != null) {
            adapter.remove(user);
            adapter.notifyDataSetChanged();
        }

        UserDatabase userDatabase = new UserDatabase(RegisteredUserActivity.this);
        userDatabase.deleteUser(user);

        Toast.makeText(RegisteredUserActivity.this, "User deleted", Toast.LENGTH_SHORT).show();

    }
}

