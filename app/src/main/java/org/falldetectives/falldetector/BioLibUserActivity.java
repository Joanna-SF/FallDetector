package org.falldetectives.falldetector;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Bio.Library.namespace.BioLib;


public class BioLibUserActivity extends Activity {
    // creating a variable
    // for our graph view.
    private BioLib lib = null;
    private String address = "";
    private String macaddress = "";
    private String mConnectedDeviceName = "";
    private BluetoothDevice deviceToConnect;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private TextView text;
    private TextView textACC;
    private TextView textDataReceived;
    private TextView textBAT;
    private TextView textTimeSpan;
    private TextView textPULSE;
    private TextView textDeviceId;


    private Button buttonConnect;
    private Button buttonDisconnect;
    private Button buttonSearch;
    private Button buttonGetDeviceId;
    private Button buttonGetAcc;

    private int BATTERY_LEVEL = 0;
    private int PULSE = 0;
    private Date DATETIME_PUSH_BUTTON = null;
    private Date DATETIME_RTC = null;
    private Date DATETIME_TIMESPAN = null;
    private int SDCARD_STATE = 0;
    private int numOfPushButton = 0;
    private BioLib.DataACC dataACC = null;
    private String deviceId = "";
    private String firmwareVersion = "";
    private byte accSensibility = 1;    // NOTE: 2G= 0, 4G= 1
    private byte typeRadioEvent = 0;
    private byte[] infoRadioEvent = null;
    private short countEvent = 0;
    private boolean isConn = false;
    private byte[][] ecg = null;
    private int nBytes = 0;
    private String accConf = "";
    public static TextView textACCFall;

    private static final int WINDOW_SIZE = 20;  // Window size for smoothing data
    private static final double FALL_THRESHOLD_1 = 55.0;  // Threshold for fall detection
    private static final double FALL_THRESHOLD_2 = 200.0;  // Threshold for fall detection

    private static final int FALL_CONFIRMATION_COUNT = 2;  // Number of consecutive readings to confirm a fall
    private int COUNTDOWN_BUSY=0;
    private static final int COUNTDOWN_REQUEST_CODE = 2;
    private static final int REQUEST_OK = 3;
    private double accelerationMagnitude;

    private LinkedList<Double> xValues = new LinkedList<>();
    private LinkedList<Double> yValues = new LinkedList<>();
    private LinkedList<Double> zValues = new LinkedList<>();

    private List<DataPoint> dataPointsX = new ArrayList<>();
    private List<DataPoint> dataPointsY = new ArrayList<>();
    private List<DataPoint> dataPointsZ = new ArrayList<>();
    private List<DataPoint> dataPointsMag = new ArrayList<>();

    String phoneNumber;


    // Counter for consecutive readings indicating a fall
    private int fallCounter = 0;
    private Handler handler = new Handler();

    private int currentIndex = 0;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_settings);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // ###################################################
        // MACADDRESS:
        address = "00:23:FE:00:0B:59";
        // ###################################################
        text = (TextView) findViewById(R.id.lblStatus);
        textBAT = (TextView) findViewById(R.id.lblBAT);
        text.setText("");
        textACCFall = findViewById(R.id.ACC_fall);
        textACC = (TextView) findViewById(R.id.lblACC);


        try {
            lib = new BioLib(this, mHandler);
            text.append("Init BioLib \n");
        } catch (Exception e) {
            text.append("Error to init BioLib \n");
            e.printStackTrace();
        }

        // Retrieve the phone number from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PHONE_NUMBER")) {
            phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        }

        buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Connect();
            }

            /*** Connect to device.*/
            private void Connect() {
                try {



                    address = "00:23:FE:00:0B:59";
                    deviceToConnect = lib.mBluetoothAdapter.getRemoteDevice(address);
                    Reset();
                    text.setText("");
                    lib.Connect(address, 5);

                } catch (Exception e) {
                    text.setText("Error to connect device: " + address);
                    e.printStackTrace();
                }
            }
        });

        /*** Disconnect to device.*/
        buttonDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Disconnect();
            }
        });


        /*buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Search(view);
            }

            *//*
             * Search for bluetooth devices.
             *//*
            private void Search(View view) {
                try {
                    Intent myIntent = new Intent(view.getContext(), SearchDeviceActivity.class);
                    startActivityForResult(myIntent, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });*/

        buttonConnect.setEnabled(false);
        buttonDisconnect.setEnabled(false);
    }

    public void OnDestroy() {
        if (isConn) {
            Disconnect();
        }
    }

    protected void onDestroy() {
        super.onDestroy();

        if (lib.mBluetoothAdapter != null) {
            lib.mBluetoothAdapter.cancelDiscovery();
        }

        lib = null;
    }


    /***
     * Disconnect from device.
     */
    private void Disconnect() {
        try {
            lib.Disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Reset();
        }
    }

    /***
     * Reset variables and UI.
     */
    private void Reset() {
        try {
            textBAT.setText("BAT: - - %");
            //textDataReceived.setText("RECEIVED: - - - ");
            textACC.setText("ACC:  X: - -  Y: - -  Z: - -");
            //textDeviceId.setText("Device Id: - - - - - - - - - -");


            SDCARD_STATE = 0;
            BATTERY_LEVEL = 0;
            PULSE = 0;
            DATETIME_PUSH_BUTTON = null;
            DATETIME_RTC = null;
            DATETIME_TIMESPAN = null;
            numOfPushButton = 0;
            countEvent = 0;
            accConf = "";
            firmwareVersion = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The Handler that gets information back from the BioLib
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BioLib.MESSAGE_READ:
                    textDataReceived.setText("RECEIVED: " + msg.arg1);
                    break;

                case BioLib.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    text.append("Connected to " + mConnectedDeviceName + " \n");
                    break;

                case BioLib.MESSAGE_BLUETOOTH_NOT_SUPPORTED:
                    Toast.makeText(getApplicationContext(), "Bluetooth NOT supported. Aborting! ", Toast.LENGTH_SHORT).show();
                    text.append("Bluetooth NOT supported. Aborting! \n");
                    isConn = false;
                    break;

                case BioLib.MESSAGE_BLUETOOTH_ENABLED:
                    Toast.makeText(getApplicationContext(), "Bluetooth is now enabled! ", Toast.LENGTH_SHORT).show();
                    text.append("Bluetooth is now enabled \n");
                    text.append("Macaddress selected: " + address + " \n");
                    buttonConnect.setEnabled(true);
                    break;

                case BioLib.MESSAGE_BLUETOOTH_NOT_ENABLED:
                    Toast.makeText(getApplicationContext(), "Bluetooth not enabled! ", Toast.LENGTH_SHORT).show();
                    text.append("Bluetooth not enabled \n");
                    isConn = false;
                    break;

                case BioLib.REQUEST_ENABLE_BT:
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, BioLib.REQUEST_ENABLE_BT);
                    text.append("Request bluetooth enable \n");
                    break;

                case BioLib.STATE_CONNECTING:
                    text.append("   Connecting to device ... \n");
                    break;

                case BioLib.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(), "Connected to " + deviceToConnect.getName(), Toast.LENGTH_SHORT).show();
                    text.append("   Connect to " + deviceToConnect.getName() + " \n");
                    isConn = true;

                    buttonConnect.setEnabled(false);
                    buttonDisconnect.setEnabled(true);

                    break;

                case BioLib.UNABLE_TO_CONNECT_DEVICE:
                    Toast.makeText(getApplicationContext(), "Unable to connect device! ", Toast.LENGTH_SHORT).show();
                    text.append("   Unable to connect device \n");
                    isConn = false;

                    buttonConnect.setEnabled(true);
                    buttonDisconnect.setEnabled(false);

                    break;

                case BioLib.MESSAGE_DISCONNECT_TO_DEVICE:
                    Toast.makeText(getApplicationContext(), "Device connection was lost", Toast.LENGTH_SHORT).show();
                    text.append("   Disconnected from " + deviceToConnect.getName() + " \n");
                    isConn = false;

                    buttonConnect.setEnabled(true);
                    buttonDisconnect.setEnabled(false);

                    break;

                case BioLib.MESSAGE_TIMESPAN:
                    DATETIME_TIMESPAN = (Date) msg.obj;

                    break;

                case BioLib.MESSAGE_DATA_UPDATED:
                    BioLib.Output out = (BioLib.Output) msg.obj;
                    BATTERY_LEVEL = out.battery;

                    if (BATTERY_LEVEL < 20 && BATTERY_LEVEL>0 ) {
                        Toast.makeText(getApplicationContext(), "Warning: Battery level low", Toast.LENGTH_SHORT).show();
                    }

                    textBAT.setText("BAT: " + BATTERY_LEVEL + " %");
                    PULSE = out.pulse;
                    break;

                case BioLib.MESSAGE_FIRMWARE_VERSION:
                    // Show firmware version in device VitalJacket ...
                    firmwareVersion = (String) msg.obj;
                    break;

                case BioLib.MESSAGE_DEVICE_ID:
                    deviceId = (String) msg.obj;
                    //textDeviceId.setText("Device Id: " + deviceId);
                    break;

                case BioLib.MESSAGE_ACC_SENSIBILITY:
                    accSensibility = (byte) msg.arg1;
                    accConf = "4G";
                    switch (accSensibility) {
                        case 0:
                            accConf = "2G";
                            break;

                        case 1:
                            accConf = "4G";
                            break;
                    }

                    textACC.setText("ACC [" + accConf + "]:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z);
                    break;

                case BioLib.MESSAGE_ACC_UPDATED:
                    dataACC = (BioLib.DataACC) msg.obj;

                    if (COUNTDOWN_BUSY==0) {

                        double x = dataACC.X;
                        double y = dataACC.Y;
                        double z = dataACC.Z;
                        double c = System.currentTimeMillis();

                        if (accConf == "")
                            textACC.setText("ACC:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z);
                        else
                            textACC.setText("ACC [" + accConf + "]:  X: " + dataACC.X + "  Y: " + dataACC.Y + "  Z: " + dataACC.Z);

                        accelerationMagnitude = processAccelerometerData(x, y, z);
                        // Check if the magnitude indicates a fall
                        textACCFall.setText("ACC Magnitude: " + accelerationMagnitude + " FallCounter: " + fallCounter);

                        //possible fall detected
                        if (accelerationMagnitude > FALL_THRESHOLD_1) {
                            fallCounter++;

                            if (fallCounter >= FALL_CONFIRMATION_COUNT) {
                                // Fall detected
                                fallDetected();

                            }
                        } else if (accelerationMagnitude > FALL_THRESHOLD_2) {
                            // Fall detected
                            fallDetected();

                        }

                        //not a fall
                        else {
                            // Reset the fall counter if the magnitude is below the threshold
                            fallCounter = 0;
                            //Log.d("YourTag", "Fall not detected");
                            textACCFall.setText("Magnitude: " + accelerationMagnitude);
                        }

                        dataPointsMag.add(new DataPoint(c, accelerationMagnitude));

                        // Limit the number of data points to display (e.g., keep the last N points)
                        int maxDataPoints = 200; // Adjust as needed

                        if (dataPointsX.size() > maxDataPoints) {
                            dataPointsMag.remove(0);
                        }
                    }
                    else {
                        //Toast.makeText(getApplicationContext(), "Countdown occupied:"+COUNTDOWN_BUSY, Toast.LENGTH_SHORT).show();

                    }
                    break;

                case BioLib.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    // Method to process real-time accelerometer data
    public static double processAccelerometerData(double x, double y, double z) {
        //textACCFall.setText("processAccelerometerData");
        Log.d("YourTag", "Fall detected");
        LinkedList<Double> xValues = new LinkedList<>();
        LinkedList<Double> yValues = new LinkedList<>();
        LinkedList<Double> zValues = new LinkedList<>();
        int fallCounter = 0;
        int fall = 0;

        // Smooth the accelerometer data using a moving window
        double smoothedX = smoothData(x, xValues);
        double smoothedY = smoothData(y, yValues);
        double smoothedZ = smoothData(z, zValues);

        // Calculate the magnitude of the acceleration vector
        double accelerationMagnitude = calculateAccelerationMagnitude(smoothedX, smoothedY, smoothedZ);
        if (Double.isNaN(accelerationMagnitude)) {
            accelerationMagnitude = 0;
        }
        return accelerationMagnitude;

    }

    // Method to smooth data using a moving window
    private static double smoothData(double newValue, LinkedList<Double> values) {
        //textACCFall.setText("smoothData");
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
    private static double calculateAccelerationMagnitude(double x, double y, double z) {

        return Math.sqrt(x * x + y * y + z * z - 32 * 32);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BioLib.REQUEST_ENABLE_BT:
                //Handle Bluetooth enable request result
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth is now enabled! ", Toast.LENGTH_SHORT).show();
                    text.append("Bluetooth is now enabled \n");

                    buttonConnect.setEnabled(true);
                    buttonDisconnect.setEnabled(false);
                    buttonGetDeviceId.setEnabled(false);

                    text.append("Macaddress selected: " + address + " \n");
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth not enabled! ", Toast.LENGTH_SHORT).show();
                    text.append("Bluetooth not enabled \n");
                    isConn = false;

                    buttonConnect.setEnabled(false);
                    buttonDisconnect.setEnabled(false);
                    buttonGetDeviceId.setEnabled(false);
                }
                break;

            case 0:
                // Handle result from SearchDeviceActivity
                switch (resultCode) {
                    case SearchDeviceActivity.CHANGE_MACADDRESS:
                        try {
                            text.append("\nSelect new macaddress: ");
                            macaddress = data.getExtras().getString(SearchDeviceActivity.SELECT_DEVICE_ADDRESS);
                            Toast.makeText(getApplicationContext(), macaddress, Toast.LENGTH_SHORT).show();

                            text.append(macaddress);

                            address = macaddress;
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "ERROR: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;

            case COUNTDOWN_REQUEST_CODE:
                COUNTDOWN_BUSY=0;
                if (resultCode == CountdownActivity.RESULT_OK) {

                    //Toast.makeText(getApplicationContext(), "User is Ok", Toast.LENGTH_SHORT).show();
                } else if (resultCode == CountdownActivity.RESULT_SEND_FALL_ALERT) {
                    //Toast.makeText(getApplicationContext(), "Fall Alert", Toast.LENGTH_SHORT).show();
                    sendFallAlertBioLib();

                } else {
                    //Toast.makeText(getApplicationContext(), "Fall Alert", Toast.LENGTH_SHORT).show();
                    sendFallAlertBioLib();


                }


        }

    }

    private void sendFallAlertBioLib() {

        String message = getResources().getString(R.string.fall_message);

        if (ContextCompat.checkSelfPermission(BioLibUserActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                //Toast.makeText(getApplicationContext(), "Fall alert sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Fall alert failed to send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else {

            // Handle the case where SMS permission is not granted
            ActivityCompat.requestPermissions(BioLibUserActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 1);
        }

    }

    private void fallDetected() {
        textACCFall.setText("ACC:  Fall was detected! Magnitude: " + accelerationMagnitude + " FallCounter: " + fallCounter);
        //Toast.makeText(getApplicationContext(), "ACC:  Fall was detected! Magnitude: " + accelerationMagnitude + " FallCounter: " + fallCounter, Toast.LENGTH_SHORT).show();

        COUNTDOWN_BUSY=1;
        Intent intent = new Intent(BioLibUserActivity.this, CountdownActivity.class);
        startActivityForResult(intent, COUNTDOWN_REQUEST_CODE);
    }
}





