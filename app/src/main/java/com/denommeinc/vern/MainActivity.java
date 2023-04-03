package com.denommeinc.vern;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.denommeinc.vern.adapter.PairedListAdapter;
import com.denommeinc.vern.dialog.PairedDevicesDialog;
import com.denommeinc.vern.elm327.BluetoothIOGateway;
import com.denommeinc.vern.elm327.DeviceBroadcastReceiver;
import com.denommeinc.vern.gauges.GaugeViewFragment;
import com.denommeinc.vern.utility.MyLog;
import com.denommeinc.vern.utility.SendOBD2CMD;
import com.denommeinc.vern.utility.UnicodeFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.script.ScriptException;

import de.greenrobot.event.EventBus;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements
        PairedDevicesDialog.PairedDeviceDialogListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_DIALOG = "dialog";
    private static final String NO_BLUETOOTH = "Oops, your device doesn't support bluetooth";
    public static final String RESPONSE = "RESPONSE: \t";

    public static String vin = " ";
    public static String currentProtocol = " ";

    public static boolean isCONNECTED() {
        return CONNECTED;
    }

    public static void setCONNECTED(boolean CONNECTED) {
        MainActivity.CONNECTED = CONNECTED;
    }

    public static boolean CONNECTED = false;
    public static boolean AT_SENT = false;
    public static boolean INITIAL_SENT = false;
    public static boolean ALL_SENT = false;
    public static boolean USER_SENT = false;

    // Responses
    public static int rpm;
    public static int maxRPM = 0;
    public static int lastRPM = 0;
    public static int engineCoolantTemp = 0;
    public static int lastCoolantTemp = 0;
    public static int maxCoolantTemp = 0;
    public static int ambientTemp = 0;
    public static int oilTemp = 0;
    public static int speed = 0;
    public static int lastSpeed = 0;
    public static int maxSpeed = 0;
    public static int ignType = 0;
    public static int fuelType = 0;
    public static int consumptionRate = 0;
    public static int fuelRailPressure = 0;
    public static int barometricPressure = 0;
    public static int airFuelRatio = 0;
    public static int intakeManifoldPressure = 0;
    public static int engineLoad = 0;
    public static int throttlePosition = 0;
    public static int engineRuntime = 0;
    public static int massAirFlow = 0;
    public static int timeSinceCodesCleared = 0;
    public static int timingAdvance = 0;
    public static int commandedAirStatus = 0;
    public static int timeMILon = 0;
    public static int fuelLevel = 0;
    public static int supportedPID = 0;
    public static int supportedPID_21_40 = 0;
    public static int supportedPID_41_60 = 0;
    public static int supportedPID_61_80 = 0;
    public String obdStandards = null;
    public static int intakeTemp = 0;

    public static double distance = 0.0,
            voltage = 0.0,
            ctrlModuleVoltage = 0.0,
            odometer = 0.0;

    public static int getOilTemp() {
        return oilTemp;
    }
    public static void setOilTemp(int oilTemp) {
        MainActivity.oilTemp = oilTemp;
    }

    public static String getVin() {
        return vin;
    }
    public static void setVin(String vin) {
        MainActivity.vin = vin;
    }

    public static boolean isAtSent() {
        return AT_SENT;
    }
    public static void setAtSent(boolean atSent) {
        AT_SENT = atSent;
    }

    public static boolean isInitialSent() {
        return INITIAL_SENT;
    }
    public static void setInitialSent(boolean initialSent) {
        INITIAL_SENT = initialSent;
    }

    public static boolean isAllSent() {
        return ALL_SENT;
    }
    public static void setAllSent(boolean allSent) {
        ALL_SENT = allSent;
    }

    public static boolean isUserSent() {
        return USER_SENT;
    }
    public static void setUserSent(boolean userSent) {
        USER_SENT = userSent;
    }

    public static int getIntakeTemp() {
        return intakeTemp;
    }
    public static void setIntakeTemp(int intakeTemp) {
        MainActivity.intakeTemp = intakeTemp;
    }

    public static int getRpm() {
        return rpm;
    }
    public static void setRpm(int rpm) {
        MainActivity.rpm = rpm;
    }

    public static int getEngineCoolantTemp() {
        return engineCoolantTemp;
    }
    public static void setEngineCoolantTemp(int engineCoolantTemp) {
        MainActivity.engineCoolantTemp = engineCoolantTemp;
    }

    public static int getSpeed() {
        return speed;
    }
    public static void setSpeed(int speed) {
        MainActivity.speed = speed;
    }

    public static int getEngineLoad() {
        return engineLoad;
    }
    public static void setEngineLoad(int engineLoad) {
        MainActivity.engineLoad = engineLoad;
    }

    public static int getThrottlePosition() {
        return throttlePosition;
    }
    public static void setThrottlePosition(int throttlePosition) {
        MainActivity.throttlePosition = throttlePosition;
    }

    public static int getEngineRuntime() {
        return engineRuntime;
    }
    public static void setEngineRuntime(int engineRuntime) {
        MainActivity.engineRuntime = engineRuntime;
    }

    public static int getTimeSinceCodesCleared() {
        return timeSinceCodesCleared;
    }
    public static void setTimeSinceCodesCleared(int timeSinceCodesCleared) {
        MainActivity.timeSinceCodesCleared = timeSinceCodesCleared;
    }

    public static int getTimeMILon() {
        return timeMILon;
    }
    public static void setTimeMILon(int timeMILon) {
        MainActivity.timeMILon = timeMILon;
    }

    public static int getFuelLevel() {
        return fuelLevel;
    }
    public static void setFuelLevel(int fuelLevel) {
        MainActivity.fuelLevel = fuelLevel;
    }

    public String getObdStandards() {
        return obdStandards;
    }
    public void setObdStandards(String obdStandards) {
        this.obdStandards = obdStandards;
    }

    public static String getCurrentProtocol() {
        return currentProtocol;
    }
    public static void setCurrentProtocol(String currentProtocol) {
        MainActivity.currentProtocol = currentProtocol;
    }

    public static double getDistance() {
        return distance;
    }
    public static void setDistance(double distance) {
        MainActivity.distance = distance;
    }

    public static double getCtrlModuleVoltage() {
        return ctrlModuleVoltage;
    }
    public static void setCtrlModuleVoltage(double ctrlModuleVoltage) {
        MainActivity.ctrlModuleVoltage = ctrlModuleVoltage;
    }

    public static double getOdometer() {
        return odometer;
    }
    public static void setOdometer(double odometer) {
        MainActivity.odometer = odometer;
    }

    public static double getVoltage() {
        return voltage;
    }
    public static void setVoltage(double voltage) {
        MainActivity.voltage = voltage;
    }

    public static int getLastRPM() {
        return lastRPM;
    }
    public static void setLastRPM(int lastRPM) {
        MainActivity.lastRPM = lastRPM;
    }

    public static int getMaxRPM() {
        return maxRPM;
    }
    public static void setMaxRPM(int maxRPM) {
        MainActivity.maxRPM = maxRPM;
    }

    public static int getLastCoolantTemp() {
        return lastCoolantTemp;
    }
    public static void setLastCoolantTemp(int lastCoolantTemp) {
        MainActivity.lastCoolantTemp = lastCoolantTemp;
    }

    public static int getMaxCoolantTemp() {
        return maxCoolantTemp;
    }
    public static void setMaxCoolantTemp(int maxCoolantTemp) {
        MainActivity.maxCoolantTemp = maxCoolantTemp;
    }

    public static int getLastSpeed() {
        return lastSpeed;
    }
    public static void setLastSpeed(int lastSpeed) {
        MainActivity.lastSpeed = lastSpeed;
    }

    public static int getMaxSpeed() {
        return maxSpeed;
    }
    public static void setMaxSpeed(int maxSpeed) {
        MainActivity.maxSpeed = maxSpeed;
    }

    // All Repeated Commands
    private static final String[] ALL_COMMANDS = {
            //"AT Z", // case 0 // Reset
            //"AT SP 0", // case 1 NOTE: SP="Specify Protocol" 0="Automatically Search/Select Protocol"
            "0105", // Engine Coolant Temp // case 2
            "010C", // RPM // case 3
            "010D", // Speed // case 4
            "0131", // Distance since codes cleared// case 5
            "012F", // Fuel Level // case 6
            "01A6", // Odometer // case 7
            "AT RV", // Voltage // case 8
            "0902", // VIN // case 9
            "B3",
            "0142",
            "015C",
            "010F",
            "0146",
            "0151",
            "015E",
            "0122",
            "0133",
            "0144",
            "014F",
            "0400",
            "0104",
            "0111",
            "011F",
            "0166",
            "0300",
            "0700",
            "0A00",
            "0121",
            "014E",
            "010E",
            "0112",
            "0100",
            "0101",
            "0120",
            "014D",
            "0140",
            "0160",
            "011C"
    };

    // Single Use Commands
    private static final String[] AT_COMMANDS = {
            "AT Z", // case 0 // Reset
            "AT SP 0", // case 1 Automatically Search/Select Protocol
            //"AT H1", // Enable Header Bytes
            "AT DP", // Display Current Protocol
            "AT DP N" // Display Current Protocol by Number
    };

    //Initial Default Commands
    private static final String[] INITIAL_COMMANDS = {
            //"AT SP0", // case 1 Automatically Search/Select Protocol
            //"AT Z", // Reset
            "0105", // Engine Coolant Temp // case 2
            "010C", // RPM // case 3
            "010D", // Speed // case 4
            "AT RV" // Voltage // case 8
    };

    public static final String[] KLINE_COMMANDS = {
            "AT SP0", // case 0 Automatically Search/Select Protocol
            "AT RV", // Voltage // case 1
            "0105", // Engine Coolant Temp // case 2
            "010C",// RPM // case 3
            "010D",// Speed // case 4
            "011C", // Display Standards
            "AT DP" //Display Protocol
    };

    //User Selected Commands
    public static String[] USER_COMMANDS = {
            PIDConstants.PROTOCOL,
            PIDConstants.OBD_STANDARDS
    };

    ArrayList<String> SELECTED_COMMAND = new ArrayList<>();
    ArrayList<String> responseArrayList = new ArrayList<String>();

    public static int allCMDPointer = -1;
    public static int userCMDPointer = -1;
    public static int initialCMDPointer = -1;
    public static int atCMDPointer = -1;
    public static int btnCMDPointer = -1;
    public static int kLineCMDPointer = -1;

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 101;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 102;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 103;

    // Message types accessed from the BluetoothIOGateway Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names accesses from the BluetoothIOGateway Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast_message";

    // Bluetooth
    private BluetoothIOGateway mIOGateway;
    private static BluetoothAdapter mBluetoothAdapter;
    private DeviceBroadcastReceiver mReceiver;
    private PairedDevicesDialog dialog;
    private List<BluetoothDevice> mDeviceList;
    BluetoothDevice device;
    SendOBD2CMD sendOBD2CMD;

    // Files
    FileWriter fileWriter;

    // Fragment
    Fragment gaugeFragment;

    // Widgets
    private TextView mConnectionStatus;
    TextView deviceStatus_txt;
    TextView protocol_txt;
    TextView standards_txt;
    TextView kLine_speed;
    TextView kLine_rpm;

    Spinner cmdSelection;
    ImageButton gauges_btn;
    ImageButton hideLayout_btn;
    ToggleButton kLineCMD_tb;

    Button sendCMD_btn;
    // Views
    FragmentContainerView fcv;
    LinearLayout kLineLayout;

    public String getOUTPUT() {
        return OUTPUT;
    }

    public void setOUTPUT(String OUTPUT) {
        this.OUTPUT = OUTPUT;
    }

    public String OUTPUT = null;

    // Variable def
    public static boolean inSimulatorMode = false;
    public static StringBuilder mSbCmdResp;
    private static StringBuilder mPartialResponse;
    private String mConnectedDeviceName;

        Handler mMsgHandler = new Handler(Looper.getMainLooper()) {

            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BluetoothIOGateway.STATE_CONNECTING:
                                mConnectionStatus.setText(getString(R.string.BT_connecting));
                                mConnectionStatus.setBackgroundColor(Color.rgb(197, 145, 19));
                                setCONNECTED(false);
                                break;

                            case BluetoothIOGateway.STATE_CONNECTED:
                                mConnectionStatus.setText(getString(R.string.BT_status_connected_to) + " " + mConnectedDeviceName);
                                mConnectionStatus.setBackgroundColor(Color.rgb(65, 131, 19));
                                sendInitialCommands(getApplicationContext());
                                INITIAL_SENT = true;
                                setCONNECTED(true);
                                break;

                            case BluetoothIOGateway.STATE_LISTEN:
                                setCONNECTED(false);

                            case BluetoothIOGateway.STATE_NONE:
                                mConnectionStatus.setText(getString(R.string.BT_status_not_connected));
                                mConnectionStatus.setBackgroundColor(Color.rgb(190, 19, 15));
                                setCONNECTED(false);
                                break;

                            default:
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;

                        // construct a string from the valid bytes in the buffer
                        String readMessage = new String(readBuf, 0, msg.arg1);

                        readMessage = readMessage.trim();
                        readMessage = readMessage.toUpperCase();
                        char lastChar = ' ';
                        if (!inSimulatorMode) {
                            try {
                                lastChar = readMessage.charAt(readMessage.length() - 1);
                            } catch (Exception e) {
                                displayLog(e.getMessage());
                            }
                            if (lastChar == '>' & mPartialResponse.toString().length() > 0) {
                                try {
                                   parseResponse(mPartialResponse.toString() + readMessage);
                                } catch (ScriptException e) {
                                    displayLog(e.getMessage());
                                }
                                mPartialResponse.setLength(0);
                            } else {
                                mPartialResponse.append(cleanResponse(readMessage));
                            }
                        } else {
                            mSbCmdResp.append("R>>");
                            mSbCmdResp.append(readMessage);
                            mSbCmdResp.append("\n");
                        }
                        break;

                    case MESSAGE_WRITE:
                        byte[] writeBuf = (byte[]) msg.obj;

                        // construct a string from the buffer
                        String writeMessage = new String(writeBuf);

                        mSbCmdResp.append("W>>");
                        mSbCmdResp.append(writeMessage);
                        mSbCmdResp.append("\n");

                        break;

                    case MESSAGE_TOAST:
                        displayMessage(msg.getData().getString(TOAST));
                        break;

                    case MESSAGE_DEVICE_NAME:
                        // save the connected device's name
                        mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                        deviceStatus_txt.setText("Connected : " + mConnectedDeviceName);
                        deviceStatus_txt.setTextColor(getColor(R.color.green));
                        break;
                }
            }
        };

    private void showGaugeViewFragment() {
        fcv.setVisibility(View.VISIBLE);
        gaugeFragment = new GaugeViewFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                )
                .replace(R.id.fragment_view, gaugeFragment)
                .addToBackStack("gaugeFragment")
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Fragment for Gauge
        fcv = findViewById(R.id.fragment_view);

        // File Handler for CSVFile
        File root = Environment.getExternalStorageDirectory();
        File saveFile = new File(root, "save.csv");
        try {
            fileWriter = new FileWriter(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        kLineCMD_tb = findViewById(R.id.kLineCMD_tb);
        kLineCMD_tb.setEnabled(true);
        kLineCMD_tb.setChecked(true);
        sendCMD_btn = findViewById(R.id.sendCMD_btn);
        hideLayout_btn = findViewById(R.id.hideLayout_btn);
        kLineLayout = findViewById(R.id.kLineLayout);

        mConnectionStatus = findViewById(R.id.tvConnectionStatus);
        deviceStatus_txt = findViewById(R.id.deviceStatus_txt);
        protocol_txt = findViewById(R.id.output_txt);
        standards_txt = findViewById(R.id.standards_txt);

        // make sure user has Bluetooth hardware
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            displayMessage(NO_BLUETOOTH);
            MainActivity.this.finish();
        }
        // Init variables
        mSbCmdResp = new StringBuilder();
        mPartialResponse = new StringBuilder();
        mIOGateway = new BluetoothIOGateway(this, mMsgHandler);

        sendCMD_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserCommands(getApplicationContext());
                sendKlineCommands(getApplicationContext());
            }
        });

        kLineCMD_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendKlineCommands(getApplicationContext());
                }
            }
        });

        hideLayout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    kLineLayout.setVisibility(View.GONE);
            }
        });
    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        // make sure Bluetooth is enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            queryPairedDevices();
            setupMonitor();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//         Register EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

//         Unregister EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Un register receiver
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        // Stop scanning if is in progress
        cancelScanning();

        // Stop mIOGateway
        if (mIOGateway != null) {
            mIOGateway.stop();
        }

        // Clear StringBuilder
        if (mSbCmdResp.length() > 0) {
            mSbCmdResp.setLength(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                queryPairedDevices();
                setupMonitor();
                return true;

            case R.id.menu_send_cmd:
                allCMDPointer = -1;
                sendAllCommands(getApplicationContext());
                ALL_SENT = true;
                return true;

            case R.id.menu_clr_scr:
                mSbCmdResp.setLength(0);
                return true;

            case R.id.menu_toggle_obd_mode:
                inSimulatorMode = !inSimulatorMode;
                if (inSimulatorMode) {
                    displayMessage("Simulator mode enabled.");
                } else {
                    displayMessage("Simulator mode disabled.");
                }
                return true;

            case R.id.menu_clear_code:
               // sendOBD2CMD("04");
                sendOBD2CMD = new SendOBD2CMD(this, "04", mIOGateway);
                return true;

            case R.id.menu_view_kline:
                if (!kLineLayout.isInLayout()) {
                    kLineLayout.setVisibility(View.VISIBLE);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // nothing at the moment
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_CANCELED) {
                displayMessage("Bluetooth not enabled :(");
                return;
            }

            if (resultCode == RESULT_OK) {
                queryPairedDevices();
                setupMonitor();
            }
        }
    }

    private void setupMonitor() {
        // Start mIOGateway
        if (mIOGateway == null) {
            mIOGateway = new BluetoothIOGateway(this, mMsgHandler);
        }

        // Only if the state is STATE_NONE, do we know that we haven't started already
        if (mIOGateway.getState() == BluetoothIOGateway.STATE_NONE) {
            // Start the Bluetooth chat services
            mIOGateway.start();
        }

        // clear string builder if contains data
        if (mSbCmdResp.length() > 0) {
            mSbCmdResp.setLength(0);
        }

    }

    @SuppressLint("MissingPermission")
    private void queryPairedDevices() {
      //  displayLog("Try to query paired devices...");

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            PairedDevicesDialog dialog = new PairedDevicesDialog();
            dialog.setAdapter(new PairedListAdapter(this, pairedDevices), false);
            showChooserDialog(dialog);
        } else {
            //displayLog("No paired device found");
            scanAroundDevices();
        }
    }

    private void showChooserDialog(PairedDevicesDialog dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, "dialog");
    }
    @SuppressLint("MissingPermission")
    private void scanAroundDevices() {
        if (mReceiver == null) {
            // Register the BroadcastReceiver
            mReceiver = new DeviceBroadcastReceiver();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
        }
        // Start scanning
        mBluetoothAdapter.startDiscovery();
    }

    @SuppressLint("MissingPermission")
    private void cancelScanning() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * Callback method for once a new device detected.
     *
     * @param device BluetoothDevice
     */
    public void onEvent(BluetoothDevice device) {
        if (mDeviceList == null) {
            mDeviceList = new ArrayList<>(10);
        }
        mDeviceList.add(device);
        // create dialog
        final Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (fragment instanceof PairedDevicesDialog) {
            PairedListAdapter adapter = dialog.getAdapter();
            adapter.notifyDataSetChanged();
        } else {
            dialog = new PairedDevicesDialog();
            dialog.setAdapter(new PairedListAdapter(this, new HashSet<>(mDeviceList)), true);
            showChooserDialog(dialog);
        }
    }

    public void displayMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public static void displayLog(String msg) {
        Log.d(TAG, msg);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDeviceSelected(BluetoothDevice device) {
        cancelScanning();

        displayLog("Selected device: " + device.getName() + " (" + device.getAddress() + ")");
        displayMessage("Selected device: " + device.getName() + " (" + device.getAddress() + ")");
        // Attempt to connect to the device
        mIOGateway.connect(device, true);
        this.device = device;
    }


    @Override
    public void onSearchAroundDevicesRequested() {
        scanAroundDevices();
        deviceStatus_txt.setTextColor(getColor(R.color.teal_200));
        deviceStatus_txt.setText(R.string.scanning);
    }

    @Override
    public void onCancelScanningRequested() {
        cancelScanning();
        deviceStatus_txt.setText(R.string.scan_canceled);
        deviceStatus_txt.setTextColor(getColor(R.color.halo_red));
    }

    private void sendUserCommands(Context context) {
        USER_SENT = true;
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (userCMDPointer < 0) {
            userCMDPointer = -1;
        }
        if (userCMDPointer >= USER_COMMANDS.length - 1) {
            userCMDPointer = 1;
        }
        userCMDPointer++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendOBD2CMD = new SendOBD2CMD(context, USER_COMMANDS[userCMDPointer], mIOGateway);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendAllCommands(Context context) {
        ALL_SENT = true;
        //displayLog("SENDING --ALL-- COMMANDS...");
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (allCMDPointer < 0) {
            allCMDPointer = -1;
        }

        if (allCMDPointer >= ALL_COMMANDS.length - 1) {
            allCMDPointer = 1;
        }
        allCMDPointer++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(context, ALL_COMMANDS[allCMDPointer], mIOGateway);
            }
        });
    }

    private void sendATCommands(Context context) {
        AT_SENT = true;
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (atCMDPointer < 0) {
            atCMDPointer = -1;
        }

        if (atCMDPointer >= AT_COMMANDS.length - 1) {
            atCMDPointer = 1;
        }
        atCMDPointer++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(context, AT_COMMANDS[atCMDPointer], mIOGateway);
            }
        });
    }

    private void sendKlineCommands(Context context) {
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (kLineCMDPointer < 0) {
            kLineCMDPointer = -1;
        }

        if (kLineCMDPointer >= KLINE_COMMANDS.length - 1) {
            kLineCMDPointer = 1;
        }
        kLineCMDPointer++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(context, KLINE_COMMANDS[kLineCMDPointer], mIOGateway);
            }
        });
        Log.i("KLINE", String.valueOf(kLineCMDPointer));
    }

    private void sendInitialCommands(Context context) {
        INITIAL_SENT = true;
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (initialCMDPointer < 0) {
            initialCMDPointer = -1;
        }

        if (initialCMDPointer >= INITIAL_COMMANDS.length - 1) {
            initialCMDPointer = 1;
        }
        initialCMDPointer++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(context, INITIAL_COMMANDS[initialCMDPointer], mIOGateway);
            }
        });
    }

    private void parseResponse(String buffer) throws ScriptException {
        displayLog("RAW BUFFER Response : " + buffer);
        if (buffer.contains("STOPPED")) {
           // Log.i("MA", String.valueOf(device) + "STOPPED");
            deviceStatus_txt.setText(R.string.stopped);
            deviceStatus_txt.setTextColor(getColor(R.color.halo_red));
        }
        switch (kLineCMDPointer) {
            case 0:
                if (buffer.contains("AT SP0")) {
                    displayLog("Protocol : \t" + buffer);
                }

            case 1:
                if (buffer.contains("ATRV")) {
                    try {
                        setVoltage(showVoltage(buffer));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case 2:
                if (buffer.contains("0105")) {
                    try {
                        setEngineCoolantTemp(showEngineCoolantTemperature(buffer));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case 3:
                if (buffer.contains("010C")) {
                    try {
                        setRpm(showEngineRPM(buffer));
                        kLine_rpm.setText(getRpm());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case 4:
                if (buffer.contains("010D")) {
                    try {
                        setSpeed(showVehicleSpeed(buffer));
                        kLine_speed.setText(getSpeed());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case 5:
                if (buffer.contains("011C")) {
                    try {
                        setObdStandards(showObdStandardsSupported(buffer));
                        showObdStandardsSupported(buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            case 6:
                if (buffer.contains("ATDP")) {
                    try {
                        protocol_txt.setText(buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            default:

        }
        if (kLineCMD_tb.isChecked()) {
            sendKlineCommands(getApplicationContext());
        }

        switch (atCMDPointer) {
            case 0: //AT Z,no parse needed (resets the ELM327)
                if (buffer.contains("AT Z")) {
                    //displayLog("AT Z : Resetting");
                }
            case 1: // AT SP 0 no parse needed (Specify/Select Protocol)
                if (buffer.contains("AT SP")) {
                    //displayLog("AT SP : Protocol 0 Requested");
                }
            case 2: // AT H1 no parse needed Enable Header Bytes
                if (buffer.contains("AT H1")) {
                   // displayLog("AT H1 : Headers Enabled");
                }
            case 3: // AT DP Display Current Protocol
                if (buffer.contains("AT DP")) {
                    String protocol = showCurrentProtocol(buffer);
                    displayLog("AT DP : Protocol : " + protocol);
                }
            case 4: // AT DP N Display Current Protocol Number
                if (buffer.contains("AT DPN")) {
                    String protocolNumber = showCurrentProtocol(buffer);
                    displayLog(protocolNumber);
                }
            default:
                mSbCmdResp.append(buffer);
        }

        switch (userCMDPointer) {
            case 0: // CMD: AT Z, no parse needed (resets the ELM327)

            case 1: // CMD: AT SP 0, no parse needed (Specify/Select Protocol)

            case 2: // CMD: 0105, Engine coolant temperature
                engineCoolantTemp = showEngineCoolantTemperature(buffer);

            case 3: // CMD: 010C, EngineRPM
                rpm = showEngineRPM(buffer);

            case 4: // CMD: 010D, Vehicle Speed
                speed = showVehicleSpeed(buffer);

            case 5: // CMD: 0131
                distance = showDistanceTraveled(buffer);

            case 6: // CMD: 012F Fuel Level
                fuelLevel = showFuelLevel(buffer);

            case 7: // CMD: 01A6 Odometer
                odometer = showOdometer(buffer);

            case 8: // CMD: ATRV Voltage
                voltage = showVoltage(buffer);

            case 9: // Vin 0902
                byte[] vinByte = buffer.getBytes();
                vin = showVIN(buffer, vinByte);

            case 10:// CMD: B3

            case 11:// CMD: 0142

            case 12:// CMD: 015C
                oilTemp = showOilTemp(buffer);

            case 13:// CMD: 010F
                int intakeTemp = showIntakeTemp(buffer);

            case 14:// CMD: 0146
                int ambientTemp = showAmbientTemp(buffer);

            case 15:// CMD: 0151
                int fuelType = 0;

            case 16:// CMD: 015E
                int consumptionRate = 0;

            case 17:// CMD: 0122
                int fuelRailPressure = 0;

            case 18:// CMD: 0133
                int barometricPressure = 0;

            case 19:// CMD: 0144
                int airFuelRatio = 0;

            case 20:// CMD: 014F
                int intakeManifoldPressure = 0;

            case 21:// CMD: MODE 04
                int clearDTC = showClearDTC(buffer);

            case 22:// CMD: engineLoad


            case 23:// CMD:0111


            case 24:// CMD:011F


            case 25:// CMD:0110 or 0166?


            case 26:// CMD: MODE 0300
                int dtcStoredValues = showDTCStoredValues(buffer);

            case 27:// CMD: MODE 0700
                int pendingDTC = showPendingDTC(buffer);

            case 28:// CMD: MODE 0A00 // Displays previously cleared codes
                int permanentTroubleCodes = showPermanentTroubleCodes(buffer);

            case 29:// CMD: 0121


            case 30:// CMD: 014E


            case 31:// CMD:
                int timingAdvance = 0;

            case 32:// CMD: 0112
                int commandedAirStatus = 0;

            case 33:// CMD: 0100


            case 34:// CMD: 0101


            case 35:// CMD: 20


            case 36:// CMD: 4D


            case 37:// CMD: 40


            case 38: // CMD: 60


            case 39: // CMD: 011C
                String obdStandards = showObdStandardsSupported(buffer);

            case 40: //CMD: ATDP
                String currentProtocol = showCurrentProtocol(buffer);

            default:
                mSbCmdResp.append(buffer);
        }

        switch (allCMDPointer) {
//            case 0: // CMD: AT Z, no parse needed (resets the ELM327)
//                mSbCmdResp.append(buffer);
//            case "": // CMD: AT SP 0, no parse needed (Specify/Select Protocol)


            case 2: // CMD: 0105, Engine coolant temperature
                int ect = showEngineCoolantTemperature(buffer);

            case 3: // CMD: 010C, EngineRPM
                int eRPM = showEngineRPM(buffer);

            case 4: // CMD: 010D, Vehicle Speed
                int vs = showVehicleSpeed(buffer);

            case 5: // CMD: 0131
                double dt = showDistanceTraveled(buffer);

            case 6: // CMD: 012F Fuel Level
                int fuel = showFuelLevel(buffer);

            case 7: // CMD: 01A6 Odometer
                int odo = showOdometer(buffer);

            case 8: // CMD: ATRV Voltage
                double volt = showVoltage(buffer);

            case 9: // Vin 0902
                byte[] vinByte = buffer.getBytes();
                String vin = showVIN(buffer, vinByte);

            case 10:// CMD: B3

            case 11:// CMD: 0142

            case 12:// CMD: 015C
                int oilTemp = showOilTemp(buffer);

            case 13:// CMD: 010F
                int intakeTemp = showIntakeTemp(buffer);

            case 14:// CMD: 0146
                int ambientTemp = showAmbientTemp(buffer);

            case 15:// CMD: 0151
                int fuelType = 0;

            case 16:// CMD: 015E
                int consumptionRate = 0;

            case 17:// CMD: 0122
                int fuelRailPressure = 0;

            case 18:// CMD: 0133
                int barometricPressure = 0;

            case 19:// CMD: 0144
                int airFuelRatio = 0;

            case 20:// CMD: 014F
                int intakeManifoldPressure = 0;

            case 21:// CMD: MODE 04
                int clearDTC = showClearDTC(buffer);

            case 22:// CMD:
                int engineLoad = showEngineLoad(buffer);

            case 23:// CMD:0111

            case 24:// CMD:011F

            case 25:// CMD:0110 or 0166?

            case 26:// CMD: MODE 0300
                int dtcStoredValues = showDTCStoredValues(buffer);
                displayLog("Case 26 : show DTC stored values: " + dtcStoredValues);

            case 27:// CMD: MODE 0700
                int pendingDTC = showPendingDTC(buffer);
                // displayLog("Case 27 : show pending DTC: " + pendingDTC);

            case 28:// CMD: MODE 0A00 // Displays previously cleared codes
                int permanentTroubleCodes = showPermanentTroubleCodes(buffer);
                //displayLog("Case 28 : show previously cleared codes: " + permanentTroubleCodes);

            case 29:// CMD: 0121


            case 30:// CMD: 014E


            case 31:// CMD:


            case 32:// CMD: 0112

            case 33:// CMD: 0100

            case 34:// CMD: 0101

            case 35:// CMD: 20

            case 36:// CMD: 4D

            case 37:// CMD: 40

            case 38: // CMD: 60

            case 39: // CMD: 011C
                String obdStandards = showObdStandardsSupported(buffer);
                protocol_txt.setText(getOUTPUT() + obdStandards);

            case 40: //CMD: ATDP
                try {
                    String currentProtocol = showCurrentProtocol(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            default:
                mSbCmdResp.append(buffer);
        }

    }

    private String decodeResponse(String response) {
        if (response.length() > 8) {
            try {
                return response.substring(8, response.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private String cleanResponse(String text) {
        text = text.trim();
        text = text.replaceAll("\\s+", "");
        text = text.replaceAll(">"  , "");

        return text;
    }

    private int getASCIIValue(byte[] bytes) {
        String byteArray = Arrays.toString(bytes);
        int sum = 0;
        for (int i = 0; i < byteArray.length(); i++) {
            try {
                int asciiValue = byteArray.charAt(i);
                sum = sum+ asciiValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    private int convertToDecimal(String hexValue) {
        int len = hexValue.length();
        int base = 1;
        int decVal = 0;

        for (int i = len - 1; i >= 0; i--) {
            try {
                if (hexValue.charAt(i) >= '0' && hexValue.charAt(i) <= '9') {
                    decVal += (hexValue.charAt(i) - 48) * base;
                    base = base * 16;
                }
                else if (hexValue.charAt(i) >= 'A' && hexValue.charAt(i) <= 'F') {
                    decVal += (hexValue.charAt(i) - 55) * base;
                    base = base * 16;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return decVal;
    }
    private int convertToFahrenheit(int celsius) {
        return (celsius * 9/5) + 32;
    }

    public void printBytes(byte[] byteArray, String name) {
        for (int k = 0; k < byteArray.length; k++) {
            try {
                String byteToHexString = UnicodeFormatter.byteToHex(byteArray[k]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String showCurrentProtocol(String buffer) {
        if (buffer.contains("ATDP")) {
            protocol_txt.setText(buffer);
        }
        return currentProtocol;
    }

    // "011C"
    public String showObdStandardsSupported(String buffer) {
        String standardsSupported = null;
        if (buffer.contains("011C")) {
            standardsSupported = String.valueOf(buffer.hashCode());
            standards_txt.setText(buffer);
        }
        return standardsSupported;
    }

    public int showPermanentTroubleCodes(String buffer) {
        return -1;
    }

    public int showPendingDTC(String buffer) {
        return -1;
    }

    public int showDTCStoredValues(String buffer) {
        return -1;
    }

    public int showEngineLoad(String buffer) {
        return -1;
    }

    public int showClearDTC(String buffer) {
        return -1;
    }

    //0146
    public int showAmbientTemp(String buffer) {
        String buf = buffer;
        int ambient = 0;
        buf = cleanResponse(buf);
        if (buf.contains("4146")) {
            buf = decodeResponse(buf);
            try {
                int celsius = convertToDecimal(buf);
                ambient = convertToFahrenheit(celsius);
                return ambient;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return ambient;
    }
    //010F
    public int showIntakeTemp(String buffer) {
        String buf = buffer;
        intakeTemp = 0;
        buf = cleanResponse(buf);
        if (buf.contains("410F")) {
            buf = decodeResponse(buf);
            try {
                //displayLog("Intake Temp RAW : " + buffer);
                int celsius = convertToDecimal(buf);
                intakeTemp = convertToFahrenheit(celsius);

                return intakeTemp;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return intakeTemp;
    }
    //015C
    public int showOilTemp(String buffer) {
        String buf = buffer;
        int oilTemp = 0;
        buf = cleanResponse(buf);
        if (buf.contains("415C")) {
            buf = decodeResponse(buf);
            try {
                //displayLog("Intake Temp RAW : " + buffer);
                int celsius = convertToDecimal(buf);
                oilTemp = convertToFahrenheit(celsius);

                return oilTemp;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return oilTemp;
    }

    //012F
    public int showFuelLevel(String buffer) {
        String buf = buffer;
        fuelLevel = 0;
        buf = cleanResponse(buf);

        if (buf.contains("412F")) {
            //TODO Fuel Level Not Reading Correctly
            try {
                String fuel = buf.substring(0, 2);
                int A = Integer.valueOf(fuel, 16);
                int fuelLevelInput = (A*100)/255;
                if (fuelLevelInput > 0) {
                    try {
                        //displayLog("Fuel : " + fuelLevelInput);
                    } catch (Exception e) {
                        displayLog(e.getMessage());
                    }
                }
                fuelLevel = (A*100)/255;
                setFuelLevel(fuelLevel);
                return (A*100)/255;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return fuelLevel;
    }

    public int showEngineCoolantTemperature(String buffer) {
        String buf = buffer;
        engineCoolantTemp = 0;
        buf = cleanResponse(buf);
        if (buf.contains("4105")) {
            buf = decodeResponse(buf);
            try {
                //displayLog("Coolant Temp RAW : " + buffer);
                int celsius = convertToDecimal(buf);
                int fahrenheit = convertToFahrenheit(celsius);
                setLastCoolantTemp(fahrenheit);
                engineCoolantTemp = fahrenheit;
                return fahrenheit;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return engineCoolantTemp;
    }

    public int showEngineRPM(String buffer) {
        String buf = buffer;
        lastRPM = 0;
        maxRPM = 0;
        buf = cleanResponse(buf);
        if (buf.contains("010C")) {
            buf = decodeResponse(buf);
            try {
                String MSB = buf.substring(0, 2);
                String LSB = buf.substring(2, 4);
                int A = Integer.valueOf(MSB, 16);
                int B = Integer.valueOf(LSB, 16);

                lastRPM = ((A * 256) + B) / 4;
                setRpm(lastRPM);

                if (lastRPM > maxRPM) {
                    maxRPM = lastRPM;
                }
                rpm = lastRPM;
                return lastRPM;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return rpm;
    }

    public int showVehicleSpeed(String buffer) {
        String buf = buffer;
        buf = cleanResponse(buf);
        if (buf.contains("010D")) {

            try {
                buf = buf.substring(buf.indexOf("410D"));
                String temp = buf.substring(4, 6);
                speed = Integer.valueOf(temp, 16);
                // Convert to MPH
                maxSpeed = ((int) (speed * 0.621371));
                lastSpeed = ((int) (speed * 0.621371));
                if (lastSpeed > maxSpeed) {
                    maxSpeed = lastSpeed;
                }
                setSpeed(lastSpeed);
                speed = lastSpeed;
                return lastSpeed;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return speed;
    }

    public double showDistanceTraveled(String buffer) {
        String buf = buffer;

        buf = cleanResponse(buf);
        if (buf.contains("0131")) {
            try {
                buf = buf.substring(buf.indexOf("4131"));

                String MSB = buf.substring(4, 6);
                String LSB = buf.substring(6, 8);
                int A = Integer.valueOf(MSB, 16);
                int B = Integer.valueOf(LSB, 16);
                distance = (A * 256) + B;
                try {
                    if (distance >0) {
                        String showDistance = "Distance : " + distance;
                    }
                } catch (Exception e) {
                    displayLog(e.getMessage());
                }
                return (A * 256) + B;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                MyLog.e(TAG, e.getMessage());
            }
        }
        return distance;
    }
    // Odometer "A6"
    public int showOdometer(String buffer) {
        int odom = 0;
        if (buffer.contains("01A6")) {

            String odo = buffer.substring(4);
            //displayLog("Odometer: \t" + odo);
            odom = Integer.decode(odo);
            return odom;
        }
        return odom;
    }

    public double showVoltage(String buffer) {
        if (buffer.contains("ATRV")) {
            String volt = buffer.substring(4, 8);
            try {
                setVoltage(Double.parseDouble(volt));
                voltage = Double.parseDouble(volt);
            } catch (Exception e) {
                displayLog(e.getMessage());
            }
        }
        return voltage;
    }
    // VIN "09 02"
    public String showVIN(String buffer, byte[] vinByte) throws ScriptException {
        vin = buffer;
        return vin;
    }

    private void supportedTroubleCodes() {
        String supportedTroubleCodes = "0100";
        SELECTED_COMMAND.add(supportedTroubleCodes);
        try {
            USER_COMMANDS = new String[]{"0100"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            displayLog(e.getMessage());
        }
    }

    private void clearDTC_CMD() {
        String clearDTC_codes = "04";
        SELECTED_COMMAND.add("04");
        try {
            USER_COMMANDS = new String[]{"04"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // resets the ELM327
    private void resetCMD() {
        String reset_cmd = "ATZ";
        try {
            USER_COMMANDS = new String[]{"ATZ"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void speedCMD() {
        String speed = "010D";
        try {
            USER_COMMANDS = new String[]{"010D"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void rpmCMD() {
        String rpm = "010C";
        try {
            USER_COMMANDS = new String[]{"010C"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void odometerCMD() {
        String odometer = "01A6";
        try {
            USER_COMMANDS = new String[]{"01A6"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void vinCMD() {
        String vin = "0902";
        try {
            USER_COMMANDS = new String[]{"0902"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void voltageCMD() {
        String voltage = "ATRV";
        try {
            USER_COMMANDS = new String[]{"ATRV"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void coolantTempCMD() {
        String engineCoolantTemp = "0105";
        try {
            USER_COMMANDS = new String[]{"0105"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void oilTempCMD() {
        String oilTemp = "015C";
        try {
            USER_COMMANDS = new String[]{"015C"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void fuelLevelCMD() {
        String fuelLevel = "012F";
        try {
            USER_COMMANDS = new String[]{"012F"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void engineLoadCMD() {
        String engineLoad = "0104";
        try {
            USER_COMMANDS = new String[]{"0104"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void pendingTroubleCodesCMD() {
        String pending_dtc = "07";
        try {
            USER_COMMANDS = new String[]{"07"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void permanentTroubleCodesCMD() {
        String perm_dtc = "0A";
        try {
            USER_COMMANDS = new String[]{"0A"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void diagnosticTroubleCodesCMD() {
        String all_dtc = "03";
        try {
            USER_COMMANDS = new String[]{"03"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}