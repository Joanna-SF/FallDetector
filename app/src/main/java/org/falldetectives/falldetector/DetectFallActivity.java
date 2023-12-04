package org.falldetectives.falldetector;

import android.app.Activity;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import Bio.Library.namespace.BioLib;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DetectFallActivity extends Activity {
    private BioLib.DataACC dataACC = null;
    private String accConf = "";
    private TextView textACC;
    //Constants
    private static final int WINDOW_SIZE = 10;  // Window size for smoothing data
    private static final double FALL_THRESHOLD = 2.0;  // Threshold for fall detection
    private static final int FALL_CONFIRMATION_COUNT = 3;  // Number of consecutive readings to confirm a fall

    // Data structures for storing accelerometer readings
    private LinkedList<Double> xValues = new LinkedList<>();
    private LinkedList<Double> yValues = new LinkedList<>();
    private LinkedList<Double> zValues = new LinkedList<>();

    // Counter for consecutive readings indicating a fall
    private int fallCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acc_data);

        textACC= findViewById(R.id.acc_data);

        // Example usage: Simulate real-time accelerometer data
        DetectFallActivity fallDetectionAlgorithm = new DetectFallActivity();

        // Simulate accelerometer readings (replace with actual real-time data)
        for (int i = 0; i < 20; i++) {
            double x = Math.random() * 5;  // Simulate accelerometer reading on the x-axis
            double y = Math.random() * 5;  // Simulate accelerometer reading on the y-axis
            double z = Math.random() * 5;  // Simulate accelerometer reading on the z-axis

            /* if (msg.what == BioLib.MESSAGE_ACC_UPDATED) {
                dataACC = (BioLib.DataACC) msg.obj;
                if (accConf == "")
                { textACC.setText("ACC:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z); }
                else
                {  textACC.setText("ACC [" + accConf + "]:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z);}

                double x = dataACC.X;
                double y = dataACC.Y;
                double z = dataACC.Z;
            }*/

            textACC.setText("ACC:  X: "+x+" Y: "+y+"  Z: "+z);

            // Process the simulated accelerometer data
            fallDetectionAlgorithm.processAccelerometerData(x, y, z);

            // Sleep to simulate real-time intervals between readings
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // Method to process real-time accelerometer data
    public void processAccelerometerData(double x, double y, double z) {
        // Smooth the accelerometer data using a moving window
        double smoothedX = smoothData(x, xValues);
        double smoothedY = smoothData(y, yValues);
        double smoothedZ = smoothData(z, zValues);

        // Calculate the magnitude of the acceleration vector
        double accelerationMagnitude = calculateAccelerationMagnitude(smoothedX, smoothedY, smoothedZ);

        // Check if the magnitude indicates a fall
        if (accelerationMagnitude > FALL_THRESHOLD) {
            fallCounter++;
            if (fallCounter >= FALL_CONFIRMATION_COUNT) {
                // Fall detected
                System.out.println("Fall detected!");
                // Additional actions can be taken here (e.g., alerting emergency services)
            }
        } else {
            // Reset the fall counter if the magnitude is below the threshold
            fallCounter = 0;
        }
    }

    // Method to smooth data using a moving window
    private double smoothData(double newValue, LinkedList<Double> values) {
        values.addLast(newValue);
        if (values.size() > WINDOW_SIZE) {
            values.removeFirst();
        }

        // Calculate the average of the values in the window
        double sum = 0;
        for (double value : values) {
            sum += value;
        }

        return sum / values.size();
    }

    // Method to calculate the magnitude of the acceleration vector
    private double calculateAccelerationMagnitude(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public static void main(String[] args,Message msg, BioLib.DataACC dataACC, String accConf, TextView textACC) {
        // Example usage: Simulate real-time accelerometer data
        DetectFallActivity fallDetectionAlgorithm = new DetectFallActivity();

        // Simulate accelerometer readings (replace with actual real-time data)
        for (int i = 0; i < 20; i++) {
            double x = Math.random() * 5;  // Simulate accelerometer reading on the x-axis
            double y = Math.random() * 5;  // Simulate accelerometer reading on the y-axis
            double z = Math.random() * 5;  // Simulate accelerometer reading on the z-axis

            /* if (msg.what == BioLib.MESSAGE_ACC_UPDATED) {
                dataACC = (BioLib.DataACC) msg.obj;
                if (accConf == "")
                { textACC.setText("ACC:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z); }
                else
                {  textACC.setText("ACC [" + accConf + "]:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z);}

                double x = dataACC.X;
                double y = dataACC.Y;
                double z = dataACC.Z;
            }*/

            textACC.setText("ACC:  X: "+x+" Y: "+y+"  Z: "+z);

            // Process the simulated accelerometer data
            fallDetectionAlgorithm.processAccelerometerData(x, y, z);

            // Sleep to simulate real-time intervals between readings
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}