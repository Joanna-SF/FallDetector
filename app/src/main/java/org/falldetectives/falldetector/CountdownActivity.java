package org.falldetectives.falldetector;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;

public class CountdownActivity extends AppCompatActivity {

    private List<FallData> fallDataList = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    static final int RESULT_SEND_FALL_ALERT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        TextView textCountdown = findViewById(R.id.textCountdown);
        Button buttonImOk = findViewById(R.id.buttonImOk);
        Button buttonSendFallAlert = findViewById(R.id.buttonSendFallAlert);

        buttonImOk.setOnClickListener(this::onImOkClicked);
        buttonSendFallAlert.setOnClickListener(this::onSendFallAlertClicked);

        loadFallDataFromDatabase();

        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textCountdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                sendFallAlert();
            }
        };

        countDownTimer.start();
    }

    private void onImOkClicked(View view) {
        addFallData(true);
        setResult(RESULT_OK);
        finish();
    }

    private void onSendFallAlertClicked(View view) {
        addFallData(false);
        setResult(RESULT_SEND_FALL_ALERT);
        finish();
    }

    private void sendFallAlert() {
        addFallData(false);
        setResult(RESULT_SEND_FALL_ALERT);
        finish();
    }

    private void addFallData(boolean isFalseAlarm) {
        long timestamp = System.currentTimeMillis();
        FallData fallData = new FallData(timestamp, isFalseAlarm);
        fallDataList.add(fallData);
        saveFallDataToDatabase(isFalseAlarm);
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

    private void saveFallDataToDatabase(boolean isFalseAlarm) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());
        values.put(DatabaseHelper.COLUMN_IS_FALSE_ALARM, isFalseAlarm ? 1 : 0);

        database.insert(DatabaseHelper.TABLE_FALL_DATA, null, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CountdownActivity.class);
    }
}
