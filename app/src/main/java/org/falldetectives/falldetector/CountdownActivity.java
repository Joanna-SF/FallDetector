
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

    static final int RESULT_SEND_FALL_ALERT = 2; // Change private to package-private or public

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
                // The countdown has finished, you may want to handle it here
                sendFallAlert();
            }
        };

        countDownTimer.start();
    }

    private void onImOkClicked(View view) {
        // The user clicked "I'm Ok" during countdown
        setResult(RESULT_OK);
        finish();
    }

    private void onSendFallAlertClicked(View view) {
        // The user clicked "Send Fall Alert" during countdown
        setResult(RESULT_SEND_FALL_ALERT);
        finish();
    }

    private void sendFallAlert() {
        // Implement any additional logic related to sending the fall alert message
        // This is where you can put the code from your MainActivity's sendFallAlert method
    }
    // In CountdownActivity.java
    public static Intent newIntent(Context context) {
        return new Intent(context, CountdownActivity.class);
    }

}
