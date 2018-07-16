package com.lonelydeveloper97.proxychanger;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.WifiProxyChanger;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.exceptions.ApiNotSupportedException;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.exceptions.NullWifiConfigurationException;

import java.lang.reflect.InvocationTargetException;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_HOST = "KEY_HOST";
    public static final String KEY_PORT = "KEY_PORT";

    private EditText etHost;
    private EditText etPort;
    private Button btnApply;
    private Button btnClear;
    private SharedPreferences prefs;

    private String currentHost;
    private int currentPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadLastData(prefs);

        etHost = (EditText) findViewById(R.id.etCurrentHost);
        etPort = (EditText) findViewById(R.id.etCurrentPort);
        btnApply = (Button) findViewById(R.id.btnApply);
        btnClear = (Button) findViewById(R.id.btnClear);

        if (!currentHost.isEmpty()) etHost.setText(currentHost);
        if (currentPort > 0) etPort.setText(String.valueOf(currentPort));

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String host = etHost.getText().toString();
                final int port = Integer.valueOf(etPort.getText().toString());
                save(host, port);
                changeProxySettings(host, port);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    }

    private void save(String host, int port) {
        prefs.edit().putString(KEY_HOST, host).apply();
        prefs.edit().putInt(KEY_PORT, port).apply();
    }

    private void loadLastData(SharedPreferences prefs) {
        currentHost = prefs.getString(KEY_HOST, "");
        currentPort = prefs.getInt(KEY_PORT, 0);
    }

    private void changeProxySettings(String host, int port) {
        try {
            WifiProxyChanger.changeWifiStaticProxySettings(host, port, this);
            Toast.makeText(this, "Применено", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | NoSuchFieldException | IllegalAccessException | NullWifiConfigurationException | ApiNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        try {
            WifiProxyChanger.clearProxySettings(this);
            Toast.makeText(this, "Чисто", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | NoSuchFieldException | IllegalAccessException | NullWifiConfigurationException | ApiNotSupportedException e) {
            e.printStackTrace();
        }
    }
}

