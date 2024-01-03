package org.falldetectives.falldetector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class DisplayHistoryActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private List<FallData> fallDataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        ListView listView = findViewById(R.id.fall_list);

        // Load fall data from the database
        loadFallDataFromDatabase();

        // Display data in ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getDataAsStrings());
        listView.setAdapter(adapter);
    }

    private void loadFallDataFromDatabase() {
        Cursor cursor = database.query(DatabaseHelper.TABLE_FALL_DATA, null, null, null, null, null, null);

        int timestampIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP);
        int isFalseAlarmIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_FALSE_ALARM);

        while (cursor.moveToNext()) {
            if (timestampIndex != -1 && isFalseAlarmIndex != -1) {
                long timestamp = cursor.getLong(timestampIndex);
                boolean isFalseAlarm = cursor.getInt(isFalseAlarmIndex) == 1;
                fallDataList.add(new FallData(timestamp, isFalseAlarm));
            }
        }

        cursor.close();
    }

    private List<String> getDataAsStrings() {
        List<String> dataStrings = new ArrayList<>();

        for (FallData fallData : fallDataList) {
            String dataString = "Timestamp: " + fallData.getFormattedTimestamp() + ", IsFalseAlarm: " + fallData.isFalseAlarm();
            dataStrings.add(dataString);
        }

        return dataStrings;
    }
}
