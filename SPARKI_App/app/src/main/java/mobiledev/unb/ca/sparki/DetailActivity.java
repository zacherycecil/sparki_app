package mobiledev.unb.ca.sparki;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    private Set<BluetoothDevice> pairedDevices;

    private BluetoothGatt bluetoothGatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        final Intent intent = getIntent();
        String profileName = intent.getStringExtra("name");
        String profileMode = intent.getStringExtra("mode");

        TextView modeTextView = findViewById(R.id.mode_textview);

        TextView e1TextView = findViewById(R.id.e1);
        TextView e2TextView = findViewById(R.id.e2);
        TextView e3TextView = findViewById(R.id.e3);
        TextView e4TextView = findViewById(R.id.e4);

        TextView macroView = findViewById(R.id.macroText);

        if (profileMode.equals("Therapeutic")) {
            // ELECTRODE 1
            final TextView e1paramsTextView = findViewById(R.id.e1params);
            e1paramsTextView.setVisibility(View.GONE);
            e1paramsTextView.setText(intent.getStringExtra("e1"));

            // ELECTRODE 2
            final TextView e2paramsTextView = findViewById(R.id.e2params);
            e2paramsTextView.setVisibility(View.GONE);
            e2paramsTextView.setText(intent.getStringExtra("e2"));

            // ELECTRODE 3
            final TextView e3paramsTextView = findViewById(R.id.e3params);
            e3paramsTextView.setVisibility(View.GONE);
            e3paramsTextView.setText(intent.getStringExtra("e3"));

            // ELECTRODE 4
            final TextView e4paramsTextView = findViewById(R.id.e4params);
            e4paramsTextView.setVisibility(View.GONE);
            e4paramsTextView.setText(intent.getStringExtra("e4"));

            e1TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(e1paramsTextView.getVisibility() == View.GONE) {
                        e1paramsTextView.setVisibility(View.VISIBLE);
                        e2paramsTextView.setVisibility(View.GONE);
                        e3paramsTextView.setVisibility(View.GONE);
                        e4paramsTextView.setVisibility(View.GONE);
                    }
                    else
                        e1paramsTextView.setVisibility(View.GONE);
                }
            });

            e2TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(e2paramsTextView.getVisibility() == View.GONE) {
                        e2paramsTextView.setVisibility(View.VISIBLE);
                        e1paramsTextView.setVisibility(View.GONE);
                        e3paramsTextView.setVisibility(View.GONE);
                        e4paramsTextView.setVisibility(View.GONE);
                    }
                    else
                        e2paramsTextView.setVisibility(View.GONE);
                }
            });

            e3TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(e3paramsTextView.getVisibility() == View.GONE) {
                        e3paramsTextView.setVisibility(View.VISIBLE);
                        e1paramsTextView.setVisibility(View.GONE);
                        e2paramsTextView.setVisibility(View.GONE);
                        e4paramsTextView.setVisibility(View.GONE);
                    }
                    else
                        e3paramsTextView.setVisibility(View.GONE);
                }
            });

            e4TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(e4paramsTextView.getVisibility() == View.GONE) {
                        e4paramsTextView.setVisibility(View.VISIBLE);
                        e1paramsTextView.setVisibility(View.GONE);
                        e2paramsTextView.setVisibility(View.GONE);
                        e3paramsTextView.setVisibility(View.GONE);
                    }
                    else
                        e4paramsTextView.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            e1TextView.setVisibility(View.GONE);
            e2TextView.setVisibility(View.GONE);
            e3TextView.setVisibility(View.GONE);
            e4TextView.setVisibility(View.GONE);
            macroView.setText(intent.getStringExtra("macro"));
        }

        modeTextView.setText(profileMode);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(profileName);

        // BLUETOOTH
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView syncButton = findViewById(R.id.sync_button);

        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("h", action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Discovery has found a device. Get the BluetoothDevice
                //object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device != null)
                    Log.v("------", device.getAddress() + " " + device.getName());
            }



            Log.d("API123",""+intent.getAction());

            if(intent.getAction().equals("com.journaldev.broadcastreceiver.SOME_ACTION"))
                Toast.makeText(context, "SOME_ACTION is received", Toast.LENGTH_LONG).show();

            else {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    try {
                        Toast.makeText(context, "Network is connected", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Network is changed or reconnected", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    //get callbacks when something changes
    private final BluetoothGattCallback gattCallback=new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState== BluetoothProfile.STATE_CONNECTED){
                Log.v("h", "CONNECTED ----------------------------");
            }
            Log.v("h", "STATE CHANGE ----------------------------" + newState + "  " + status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status==BluetoothGatt.GATT_SUCCESS){
                //all Services have been discovered
                Log.v("h", "GATT SUCCESS ALL SERVICES DISCOVERED ----------------------------");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //we are still connected to the service
            if (status==BluetoothGatt.GATT_SUCCESS){
                Log.v("read", characteristic.toString());
                //send the characteristic to broadcastupdate
                //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //send the characteristic to broadcastupdate
            //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            Log.v("changed", characteristic.toString());
        }
    };


    public String writeCharacteristic() {

        String statusString = "";

        statusString += bluetoothAdapter.getState();

        statusString += "\n" + bluetoothAdapter.isEnabled();
        statusString += "\n" + bluetoothAdapter.getName();

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("08:66:98:E9:4A:11");

        statusString += "\n" + device.toString() +  "\t" + device.getAddress() +"\t"+ device.getName();



        Set<BluetoothDevice> dev = bluetoothAdapter.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : dev) list.add(bt.getName());
            statusString += "\n" + list.size();


        if(device==null)
        {
            statusString += "\nCould not find device.";
        }
        else {
            statusString += "\nFOUND DEVICE";
            BluetoothGatt bluetoothGatt = device.connectGatt(this, false, gattCallback);

            BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"));
            if(service == null){
                statusString += "\nSERVICE PROBLEM";
            }
            else {
                statusString += "\nSERVICE NOT NULL";
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E"));
                characteristic.setValue("FF");
                bluetoothGatt.writeCharacteristic(characteristic);
            }
        }
        return statusString;
    }
}
