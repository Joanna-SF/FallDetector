package org.falldetectives.falldetector;

import java.util.ArrayList;
import java.util.Set;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SearchDeviceActivity extends Activity {
    public static String SELECT_DEVICE_ADDRESS = "device_address";
    public static final int CHANGE_MACADDRESS = 100;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter;
    private String selectedValue = ""; //selected Bluetooth device's address
    private BluetoothAdapter mBluetoothAdapter = null;
    private Button buttonOK;

    public String GetMacAddress()
    {
        return selectedValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null)
            //if null, Bluetooth is not supported on the device or there is an issue with Bluetooth initialization.
        {
            if (mBluetoothAdapter.isEnabled())
            //checks whether Bluetooth is currently enabled on the device
            {
                selectedValue="00:23:FE:00:0B:23";
                Intent intent = new Intent();
                intent.putExtra(SELECT_DEVICE_ADDRESS, selectedValue);

                // Set result and finish this Activity
                setResult(CHANGE_MACADDRESS, intent);
                finish();
            }
        }









        }

    }

