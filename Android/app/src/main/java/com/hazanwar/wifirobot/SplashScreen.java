package com.hazanwar.wifirobot;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.*;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;

// splash screen connects the app to the ROBOT WiFi without the need for any user interaction
public class SplashScreen extends AppCompatActivity {

    // initialise the WiFi manager and configuration
    WifiManager wifiManager;
    WifiConfiguration wifiConfig;
    WifiInfo wifiInfo;
    String wifiName;
    int robotNetwork = 0;
    int runNumber = 0;

    // initialise textView to update the status of the WiFi connection on UI
    TextView textProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // define where the textView are on the UI
        textProgress = findViewById(R.id.textProgress);
        textProgress.setText("Please wait, attempting to connect...");

        // define the settings for the ROBOT's network
        wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", "ROBOT");
        wifiConfig.preSharedKey = String.format("\"%s\"", "abcd1234");

        // method to check if the device has the desired permissions
        checkPermissions();

        // runs method to check if the WiFi is on, if not then it switches it on and connects to the ROBOT SSID
        checkWiFi();
    }

    public void checkWiFi() {
        // define the WiFi manager to get the details of the current WiFi networks
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        runNumber++;

        // checks if the WiFi is currently switched on or not
        if (wifiManager.isWifiEnabled()) {
            Log.d("WIFI", "WiFi is switched on...");
            wifiName = wifiInfo.getSSID();
            Log.d("WIFI", "Currently connected to: " + wifiName);

            if (wifiName.equals("\"" + "ROBOT" + "\"")) {
                Log.d("WIFI", "Connected successfully, opening robot control screen...");
                textProgress.setText("Connected successfully, opening robot control screen...");
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            } else {
                if (wifiName.equals("<unknown ssid>")){
                    Log.d("WIFI", "WiFi is switched on but not connected to anything...");
                    textProgress.setText("WiFi is switched on but not connected to anything...");
                } else {
                    wifiManager.disableNetwork(wifiInfo.getNetworkId());
                    wifiManager.disconnect();
                    Log.d("WIFI", "Disabling WiFi network: " + wifiName);
                    textProgress.setText("Currently connected to " + wifiName + " WiFi network, attempting to disconnect...");
                }

                // checks to see if the ROBOT network has already been saved in the user's phone
                int networkName = 0;
                for (WifiConfiguration getName: wifiManager.getConfiguredNetworks()) {
                    if (getName.SSID.equals("\"" + "ROBOT" + "\"")) {
                        networkName++;
                    }
                }

                // if the user doesn't already have ROBOT network saved, then it adds it
                if (networkName == 0){
                    robotNetwork = wifiManager.addNetwork(wifiConfig);
                    Log.d("WIFI", "Added ROBOT to saved network list...");
                    textProgress.setText("Saving ROBOT network details to device...");
                } else {
                    Log.d("WIFI", "ROBOT network saved already...");
                    textProgress.setText("ROBOT network is already saved...");
                }

                // scans WiFi networks to see if it can find ROBOT network
                int scanNames = 0;
                for (ScanResult getNames: wifiManager.getScanResults()) {
                    if (getNames.SSID.equals("\"" + "ROBOT" + "\"")) {
                        Log.d("WIFI", "Found the ROBOT network within range.");
                        textProgress.setText("Found the ROBOT network within range, attempting to connect....");
                        scanNames++;
                        if (scanNames > 0){
                            wifiManager.enableNetwork(robotNetwork, true);
                            wifiManager.reconnect();
                            Log.d("WIFI", "Trying to connect to ROBOT");
                        }
                    } else {
                        if ((runNumber % 5) == 0){
                            textProgress.setText("If you are having issues connecting to the robot, please see FAQ.");
                        } else {
                            textProgress.setText("Attempting to connect to robot, please wait...");
                        }
                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("WIFI", "10 second delay, rerunning checkWiFi()");
                        checkWiFi();
                    }
                }, 10000);
            }
        } else {
            wifiManager.setWifiEnabled(true);
            Log.d("WIFI", "WiFi is currently off, switching it on.");
            textProgress.setText("Turning on WiFi...");
            checkWiFi();
        }
    }

    public void checkPermissions() {
        // checks what Android version is being used to check location permission for WiFi scan
        if (android.os.Build.VERSION.SDK_INT > 22) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionString[], int[] permissionResults) {
        switch (requestCode) {
            case 1: {
                if (permissionResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                } else {
                    checkPermissions();
                    textProgress.setText("Please enable location permission to connect to robot...");
                }
                return;
            }
        }
    }

    @Override
    // this adds the logo on to the action bar for help and about pages
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.menuArmReset).setVisible(false);
        return true;
    }

    @Override
    // this method opens a dialog page for either the about or help screen if clicked on in the action bar
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menuAbout:
                Dialog dialogAbout = new Dialog(this);
                dialogAbout.setTitle("About");
                dialogAbout.setContentView(R.layout.dialog_about);
                dialogAbout.setCanceledOnTouchOutside(true);
                dialogAbout.show();
                return true;

            case R.id.menuHelp:
                Dialog dialogHelp = new Dialog(this);
                dialogHelp.setTitle("Frequently Asked Questions");
                dialogHelp.setContentView(R.layout.dialog_help);
                dialogHelp.setCanceledOnTouchOutside(true);
                dialogHelp.show();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}