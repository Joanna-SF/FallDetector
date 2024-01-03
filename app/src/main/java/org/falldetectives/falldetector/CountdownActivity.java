
package org.falldetectives.falldetector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CountdownActivity extends AppCompatActivity {

    static final int RESULT_SEND_FALL_ALERT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        TextView textCountdown = findViewById(R.id.textCountdown);
        Button buttonImOk = findViewById(R.id.buttonImOk);
        Button buttonSendFallAlert = findViewById(R.id.buttonSendFallAlert);

        buttonImOk.setOnClickListener(this::onImOkClicked);
        buttonSendFallAlert.setOnClickListener(this::onSendFallAlertClicked);

        // Implement your countdown logic here
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
        // The user clicked "I'm Ok" during countdown
        //RESULT_OK=-1
        setResult(RESULT_OK);
        finish();
    }

    private void onSendFallAlertClicked(View view) {
        // The user clicked "Send Fall Alert" during countdown
        setResult(RESULT_SEND_FALL_ALERT);
        finish();
    }

    private void sendFallAlert() {
        setResult(RESULT_SEND_FALL_ALERT);
        finish();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CountdownActivity.class);
    }

}
