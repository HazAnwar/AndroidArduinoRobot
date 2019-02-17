package com.hazanwar.wifirobot;

import android.app.Dialog;
import android.net.wifi.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.github.niqdev.mjpeg.*;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    ArduinoConnection arduinoConnection;
    SeekBar seekbarLowerArm, seekbarUpperArm, seekbarClawRotate, seekbarClawOpening, seekbarArmRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // extra check that is still connected then establishes TCP connection with ROBOT
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSSID().equals("\"" + "ROBOT" + "\"")){
            arduinoConnection = new ArduinoConnection();
        }

        // define the joystick and sets the click listener for it
        JoystickView controlJoystick = findViewById(R.id.controlJoystick);
        controlJoystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if (strength <= 10) {
                    arduinoConnection.new SendCommand((byte) 0x00, "").execute();
                } else {
                    if ((angle > 0 && angle < 23) || (angle > 338 && angle <= 360)){
                        // move the robot right
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x0A, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x0B, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x0C, "").execute();
                        }
                    } else if (angle > 23 && angle < 68){
                        // move the robot forward right
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x07, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x08, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x09, "").execute();
                        }
                    } else if (angle > 68 && angle < 113){
                        // move the robot forward
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x01, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x02, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x03, "").execute();
                        }
                    } else if (angle > 113 && angle < 158){
                        // move the robot forward left
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x10, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x11, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x12, "").execute();
                        }
                    } else if (angle > 158 && angle < 203) {
                        // move the robot left
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x13, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x14, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x15, "").execute();
                        }
                    } else if (angle > 203 && angle < 248){
                        // move the robot backwards left
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x16, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x17, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x18, "").execute();
                        }
                    } else if (angle > 248 && angle < 293) {
                        // move the robot backwards
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x04, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x05, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x06, "").execute();
                        }
                    } else if (angle > 293 && angle < 338) {
                        // move the robot backwards right
                        if (strength > 10 && strength <=40){
                            arduinoConnection.new SendCommand((byte) 0x0D, "").execute();
                        } else if (strength > 40 && strength <=70){
                            arduinoConnection.new SendCommand((byte) 0x0E, "").execute();
                        } else if (strength > 70 && strength <=100){
                            arduinoConnection.new SendCommand((byte) 0x0F, "").execute();
                        }
                    }

                }
            }
        });

        // get the height and width of the screen and sets the MJPEGView to fit correctly
        DisplayMetrics screenProperties = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screenProperties);
        final int screenWidth = screenProperties.widthPixels;
        final int cameraHeight = ((screenWidth / 4) * 3);
        MjpegSurfaceView cameraSurfaceView = findViewById(R.id.cameraStream);
        android.widget.LinearLayout.LayoutParams surfaceViewLayout = new android.widget.LinearLayout.LayoutParams(screenWidth, cameraHeight);
        cameraSurfaceView.setLayoutParams(surfaceViewLayout);

        // sets up the camera stream and displays it on the camera view
        final MjpegView cameraStream = findViewById(R.id.cameraStream);
        Mjpeg.newInstance().open("http://192.168.1.1:8080/?action=stream")
                .subscribe(new Action1<MjpegInputStream>() {
                               @Override
                               public void call(MjpegInputStream cameraInputStream) {
                                   cameraStream.setSource(cameraInputStream);
                                   cameraStream.setDisplayMode(DisplayMode.BEST_FIT);
                                   cameraStream.showFps(false);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   // do nothing
                               }
                           }
                );

        //defines the sliding panel and sets up how it works
        SlidingUpPanelLayout slidingPanel = findViewById(R.id.slidingPanel);
        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            TextView textSlider = findViewById(R.id.textSlider);

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // do something
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    textSlider.setText("SWIPE DOWN FOR WHEEL CONTROL");
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    textSlider.setText("SWIPE UP FOR ARM CONTROL");
                }
            }
        });

        // define the seekbars to get the values from to push to the robot
        seekbarLowerArm = findViewById(R.id.seekbarLowerArm);
        seekbarLowerArm.setMax(180);
        seekbarLowerArm.setProgress(100);
        seekbarLowerArm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arduinoConnection.new SendCommand((byte) 0x1E, String.valueOf(progress)).execute();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // define the seekbars to get the values from to push to the robot
        seekbarUpperArm = findViewById(R.id.seekbarUpperArm);
        seekbarUpperArm.setMax(180);
        seekbarUpperArm.setProgress(110);
        seekbarUpperArm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arduinoConnection.new SendCommand((byte) 0x1F, String.valueOf(progress)).execute();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // define the seekbars to get the values from to push to the robot
        seekbarClawRotate = findViewById(R.id.seekbarClawRotate);
        seekbarClawRotate.setMax(180);
        seekbarClawRotate.setProgress(180);
        seekbarClawRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arduinoConnection.new SendCommand((byte) 0x20, String.valueOf(progress)).execute();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // define the seekbars to get the values from to push to the robot
        seekbarClawOpening = findViewById(R.id.seekbarClawOpening);
        seekbarClawOpening.setMax(90);
        seekbarClawOpening.setProgress(0);
        seekbarClawOpening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arduinoConnection.new SendCommand((byte) 0x21, String.valueOf(progress + 90)).execute();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // define the seekbars to get the values from to push to the robot
        seekbarArmRotate = findViewById(R.id.seekbarArmRotation);
        seekbarArmRotate.setMax(180);
        seekbarArmRotate.setProgress(90);
        seekbarArmRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arduinoConnection.new SendCommand((byte) 0x22, String.valueOf(progress)).execute();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    // this adds the logo on to the action bar for help and about pages
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
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

            case R.id.menuArmReset:
                seekbarLowerArm.setProgress(100);
                arduinoConnection.new SendCommand((byte) 0x1E, String.valueOf(100)).execute();
                seekbarUpperArm.setProgress(110);
                arduinoConnection.new SendCommand((byte) 0x1F, String.valueOf(110)).execute();
                seekbarClawRotate.setProgress(180);
                arduinoConnection.new SendCommand((byte) 0x20, String.valueOf(180)).execute();
                seekbarArmRotate.setProgress(90);
                arduinoConnection.new SendCommand((byte) 0x22, String.valueOf(90)).execute();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}