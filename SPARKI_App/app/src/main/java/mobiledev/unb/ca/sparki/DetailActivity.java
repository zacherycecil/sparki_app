package mobiledev.unb.ca.sparki;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    private Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final Intent intent = getIntent();
        String profileName = intent.getStringExtra("name");
        String profileMode = intent.getStringExtra("mode");

        final TextView statusTextView = findViewById(R.id.status_textview);
        TextView modeTextView = findViewById(R.id.mode_textview);

        // ELECTRODE 1
        TextView e1TextView = findViewById(R.id.e1);
        final TextView e1paramsTextView = findViewById(R.id.e1params);
        e1paramsTextView.setVisibility(View.GONE);
        e1paramsTextView.setText(intent.getStringExtra("e1"));

        // ELECTRODE 2
        TextView e2TextView = findViewById(R.id.e2);
        final TextView e2paramsTextView = findViewById(R.id.e2params);
        e2paramsTextView.setVisibility(View.GONE);
        e2paramsTextView.setText(intent.getStringExtra("e2"));

        // ELECTRODE 3
        TextView e3TextView = findViewById(R.id.e3);
        final TextView e3paramsTextView = findViewById(R.id.e3params);
        e3paramsTextView.setVisibility(View.GONE);
        e3paramsTextView.setText(intent.getStringExtra("e3"));

        // ELECTRODE 4
        TextView e4TextView = findViewById(R.id.e4);
        final TextView e4paramsTextView = findViewById(R.id.e4params);
        e4paramsTextView.setVisibility(View.GONE);
        e4paramsTextView.setText(intent.getStringExtra("e4"));

        modeTextView.setText(profileMode);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(profileName);

        // BLUETOOTH
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        TextView syncButton = findViewById(R.id.sync_button);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                }

                String status = statusTextView.getText() + writeCharacteristic();
                statusTextView.setText(status);

                Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
            }
        });

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

    public String writeCharacteristic() {

        String statusString = "";

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("08:66:98:E9:4A:11");
        BluetoothGattCallback gattCallback = null;

        if(device==null)
        {
            statusString += "\nCould not find device.";
        }
        else {
            statusString += "\nFOUND DEVICE";
            BluetoothGatt bluetoothGatt = device.connectGatt(this, true, gattCallback);

            BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"));
            if(service == null){
                statusString += "\nSERVICE PROBLEM";
            }
            else {
                statusString += "\nSERVICE NOT NULL";
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E"));
                characteristic.setValue("FF");
                bluetoothGatt.writeCharacteristic(characteristic);
            }
        }
        return statusString;
    }
}
