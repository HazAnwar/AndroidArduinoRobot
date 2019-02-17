package com.hazanwar.wifirobot;

import android.os.*;
import android.util.*;
import java.io.*;
import java.net.*;

public class ArduinoConnection {

    Socket arduinoConnection;
    OutputStream arduinoOutput;
    InputStream arduinoInput;

    public ArduinoConnection (){
        new InitiateConnection().execute();
    }

    public class InitiateConnection extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("CONNECTION", "Attempting to connect to robot...");
                InetAddress hostURL = InetAddress.getByName("192.168.1.1");
                arduinoConnection = new Socket(hostURL, 2001);
                Log.d("CONNECTION", "Connected successfully!");
                arduinoInput = arduinoConnection.getInputStream();
                arduinoOutput = arduinoConnection.getOutputStream();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.d("CONNECTION", "Connection has been unsuccessful, please check network and try again.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("CONNECTION", "Connection has been unsuccessful, please check network and try again.");
            }
            return null;
        }
    }

    public class SendCommand extends AsyncTask<Void, Void, Void> {
        byte fullCommand;
        String fullAngle;

        public SendCommand (byte command, String angle){
            fullCommand = command;
            fullAngle = angle;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("OUTPUT", "Sending command: " + fullCommand);
                arduinoOutput.write(fullCommand);
                if (fullAngle != ""){
                    Log.d("OUTPUT", "Sending angle: " + fullAngle);
                    arduinoOutput.write(Integer.valueOf(fullAngle));
                }
                arduinoOutput.flush();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("OUTPUT", "Error sending command to robot!");
            }
            return null;
        }
    }

}
