package com.denommeinc.vern;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.denommeinc.vern.adapter.PairedListAdapter;
import com.denommeinc.vern.gui.GaugeDialog;
import com.denommeinc.vern.gui.PairedDevicesDialog;
import com.denommeinc.vern.elm327.BluetoothIOGateway;
import com.denommeinc.vern.elm327.DeviceBroadcastReceiver;
import com.denommeinc.vern.gui.RawDataDialog;
import com.denommeinc.vern.gui.ToggleDialog;
import com.denommeinc.vern.utility.SendInitializationOBD2CMD;
import com.denommeinc.vern.utility.SendOBD2CMD;
import com.denommeinc.vern.utility.UnicodeFormatter;
import com.denommeinc.vern.live_data.LiveDataViewModel;
import com.github.anastr.speedviewlib.AwesomeSpeedometer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import javax.script.ScriptException;

import de.greenrobot.event.EventBus;

@SuppressLint({"NewApi", "MissingPermission"})
public class MainActivity extends AppCompatActivity implements
        PairedDevicesDialog.PairedDeviceDialogListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_DIALOG = "K-LINE";
    private static final String TAG_DIALOG_RAW = "RAW-DATA";
    private static final String TAG_GAUGE = "GAUGE";
    private static final String TAG_TOGGLE = "TOGGLE";
    private static final String TAG_ERROR = "K-Line Error";
    private static final String NO_BLUETOOTH = "Oops, your device doesn't support bluetooth";

    /*
    * optional baud rates
    * */
    public static final String IB10 = "IB10";
    public static final String IB12 = "IB12";
    public static final String IB15 = "IB15";
    public static final String IB48 = "IB48";
    public static final String IB96 = "IB96";

    public static String vin = " ";
    public static String currentProtocol = " ";
    public String selection = null;
    public static StringBuffer READ_BUFFER = null;
    public static StringBuffer WRITE_BUFFER = null;

    public static void setCONNECTED(boolean CONNECTED) {
        MainActivity.CONNECTED = CONNECTED;
    }

    public static boolean CONNECTED = false;
    public static boolean AT_SENT = false;
    public static boolean INITIAL_SENT = false;
    public static boolean ALL_SENT = false;
    public static boolean USER_SENT = false;
    public static boolean REPEAT_CMD = false;

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
    public static int intakeTemp = 0;
    public static int currentKeywordsStatus = 0;
    public static int currentActiveStatus = 0;
    public static int currentMonitorAllStatus = 0;
    public static int currentSetDefaultsStatus = 0;
    public static int currentSilentModeStatus = 0;
    public static int currentARStatus = 0;
    public static int currentResetStatus = 0;
    public static int currentBaudRate = 0;
    public int counter = 0;
    public int counter2 = 0;

    public static String obdStandards = null;
    public static String currentSetProtocol9141 = null;
    public static String currentDisplayProtocol = null;
    public static String currentIdentify = null;
    public static String currentStandards = null;
    public static String currentCANStatus = null;
    public static String currentPPSummary = null;


    public static double distance = 0.0,
            voltage = 0.0,
            ctrlModuleVoltage = 0.0,
            odometer = 0.0;

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
           // "AT Z", // case 0 // Reset
           // "AT SP 0", // case 1 Automatically Search/Select Protocol
            //"AT H1", // Enable Header Bytes
            "AT DP", // Display Current Protocol
            "AT DP N", // Display Current Protocol by Number
            "AT RV" // Voltage
    };

    private static final String[] INITIAL_CMDS = {
            "AT SP0"
    };

    //Basic Commands
    private static final String[] BASIC_COMMANDS = {
            //"AT SP3", // case 1 Automatically Search/Select Protocol
            //"AT Z", // Reset
            //"AT TP3",
            "0105", // Engine Coolant Temp // case 2
            "010C", // RPM // case 3
            "010D", // Speed // case 4
            "AT RV", // Voltage // case 8
            "011C", // Display Standards
            //"AT DP", //Display Protocol
            "0104",  // load
           // "012F" // fuel
            "0100" // PID Supported
    };

    // "AT PPS" = Display Summary of Supported PP
    // "AT MA" = Monitor All
    // "AT TP5" = try protocol 5 ISO 14230-4 fast

    public static final String[] KLINE_COMMANDS = {
 //           "AT TP5", // case 0 Automatically Try Protocol 5 (14230-4 KWP Fast)
 //           "AT TP3", // Try Protocol 3 (9141)
            "AT RV", // Voltage // case 1
            "0105", // Engine Coolant Temp // case 2
            "010C",// RPM // case 3
            "010D",// Speed // case 4
            "011C", // Display Standards
            "AT DP" //Display Protocol
    };

    //User Selected Commands
    public static String[] USER_COMMANDS = {
            PIDConstants.RESET,
            PIDConstants.PROTOCOL,
            PIDConstants.OBD_STANDARDS
    };

    public static ArrayList<String> INITIALIZATION_COMMANDS = new ArrayList<>();

    ArrayList<String> SELECTED_COMMAND = new ArrayList<>();
    ArrayList<String> responseArrayList = new ArrayList<String>();

    public static int allCMDPointer = -1;
    public static int userCMDPointer = -1;
    public static int initialCMDPointer = -1;
    public static int atCMDPointer = -1;
    public static int btnCMDPointer = -1;
    public static int kLineCMDPointer = -1;
    public static int kLineCMD = -1;

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
    private RawDataDialog rawDataDialog;
    private GaugeDialog gaugeDialog;
    private List<BluetoothDevice> mDeviceList;
    BluetoothDevice device;
    SendOBD2CMD sendOBD2CMD, sendOBD2CMD_2;
    public LiveDataViewModel model;
    Fragment rawDataFrag;
    Fragment gaugeFrag;
    Fragment toggleDialogFrag;
    Thread initializationThread;

    // Files
    FileWriter fileWriter;

    // Widgets
    private TextView mConnectionStatus;
    TextView deviceStatus_txt;
    TextView protocol_txt;
    TextView standards_txt;
    TextView kLine_speed;
    TextView kLine_rpm;
    TextView voltage_txt;
    TextView coolant_txt;
    TextView fuelLevel_txt;
    TextView load_txt;
    TextView read_txt;
    TextView write_txt;
    TextView appendedResponse_txt;

    EditText enterCMD_edt;
    ImageButton submit_btn;
    ImageButton gauges_btn;
    ImageButton hideLayout_btn;
    ToggleButton kLineCMD_tb;
    ToggleButton atCMD_tb;
    ToggleButton basicCMD_tb;
    ToggleButton allCMD_tb;
    Button sendCMD_btn;
    Button showStatus_btn;
    Button rawData_btn;
    Button viewHUD_btn;
    Button resetCMD_btn;
    Button showToggleLayout_btn;

    // Views
    FragmentContainerView fcv;
    LinearLayout kLineLayout;
    AwesomeSpeedometer tempSpeedometer;

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
    public Thread repeatingSpeedThread;
    public Handler repeatingSpeedHandler;
    public TimerTask repeatingTimerTask;

    public boolean isCONNECTED() {
        if (mIOGateway.getState() == BluetoothIOGateway.STATE_CONNECTED) {
            CONNECTED = true;
            return true;
        } else {
            return false;
        }
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

        allCMD_tb = findViewById(R.id.allCMD_tb);
        basicCMD_tb = findViewById(R.id.basicCMD_tb);
       // basicCMD_tb.setChecked(true);

        sendCMD_btn = findViewById(R.id.sendCMD_btn);
        showStatus_btn = findViewById(R.id.set_btn);
        hideLayout_btn = findViewById(R.id.hideLayout_btn);
        rawData_btn = findViewById(R.id.viewData_btn);
        viewHUD_btn = findViewById(R.id.viewHUD_btn);
        submit_btn = findViewById(R.id.submit_btn);
        resetCMD_btn = findViewById(R.id.resetCMD_btn);
        showToggleLayout_btn = findViewById(R.id.showToggleLayout_btn);

        kLineLayout = findViewById(R.id.kLineLayout);
        kLine_speed = findViewById(R.id.kLine_speed);
        kLine_rpm = findViewById(R.id.kLine_rpm);
        voltage_txt = findViewById(R.id.voltage_txt);
        coolant_txt = findViewById(R.id.coolant_txt);
        fuelLevel_txt = findViewById(R.id.fuel_txt);
        load_txt = findViewById(R.id.load_txt);
        read_txt = findViewById(R.id.realtime_txt);
        write_txt = findViewById(R.id.write_txt);
        enterCMD_edt = findViewById(R.id.enterCMD_edt);
        appendedResponse_txt = findViewById(R.id.appendedResponse_txt);

        mConnectionStatus = findViewById(R.id.tvConnectionStatus);
        deviceStatus_txt = findViewById(R.id.deviceStatus_txt);
        protocol_txt = findViewById(R.id.output_txt);
        standards_txt = findViewById(R.id.standards_txt);
        tempSpeedometer = findViewById(R.id.tempSpeedometer);
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
        model = new ViewModelProvider(this).get(LiveDataViewModel.class);


/*
* Set up Live Data Observers
* */
        final Observer<Integer> speedObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer integer) {
                speed = showVehicleSpeed(String.valueOf(integer));
                displayLog("Live Speed Data:\t" + speed);
            }
        };
        model.getCurrentSpeed().observe(this, speedObserver);

        final Observer<Integer> rpmObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                displayLog("Live RPM:\t" + String.valueOf(integer));
                rpm = showEngineRPM(String.valueOf(integer));
            }
        };
        model.getCurrentRpm().observe(this, rpmObserver);

        final Observer<String> rawObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                displayLog("Raw Read Data:\t" + s);
                read_txt.setText(s);
            }
        };
        model.getRawDataRead().observe(this, rawObserver);

        final Observer<String> rawObserverWrite = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                displayLog("Raw Write Data:\t" + s);
                write_txt.setText(s);
            }
        };
        model.getRawDataWrite().observe(this, rawObserverWrite);

        final Observer<String> setProtocol9141Observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentProtocol = showCurrentProtocol(s);
            }
        };
        model.getCurrentSetProtocol9141().observe(this, setProtocol9141Observer);

        final Observer<String> displayProtocolObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentProtocol = showCurrentProtocol(s);
            }
        };
        model.getCurrentDisplayProtocol().observe(this, displayProtocolObserver);

        final Observer<String> identifyObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentIdentify = s;
            }
        };
        model.getCurrentIdentify().observe(this, identifyObserver);

        final Observer<String> standardsObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                obdStandards = showObdStandardsSupported(s);
            }
        };
        model.getCurrentStandards().observe(this, standardsObserver);

        final Observer<String> canStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentCANStatus = s;
            }
        };
        model.getCurrentCANStatus().observe(this, canStatusObserver);

        final Observer<String> ppSummaryObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentPPSummary = s;
            }
        };
        model.getCurrentPPSummary().observe(this, ppSummaryObserver);

        final Observer<Integer> keywordsObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentKeywordsStatus = integer;
            }
        };
        model.getCurrentKeywordsStatus().observe(this, keywordsObserver);

        final Observer<Integer> activeStatusObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentActiveStatus = integer;
            }
        };
        model.getCurrentActiveStatus().observe(this, activeStatusObserver);

        final Observer<Integer> monitorAllStatusObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentMonitorAllStatus = integer;
            }
        };
        model.getCurrentMonitorAllStatus().observe(this, monitorAllStatusObserver);

        final Observer<Integer> setDefaultsObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentSetDefaultsStatus = integer;
            }
        };
        model.getCurrentSetDefaultsStatus().observe(this, setDefaultsObserver);

        final Observer<Integer> silentModeObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentSilentModeStatus = integer;
            }
        };
        model.getCurrentSilentModeStatus().observe(this, silentModeObserver);

        final Observer<Integer> arStatusObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentARStatus = integer;
            }
        };
        model.getCurrentAutoReceiveStatus().observe(this, arStatusObserver);

        final Observer<Integer> resetObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentResetStatus = integer;
            }
        };
        model.getCurrentResetStatus().observe(this, resetObserver);

        final Observer<Integer> voltageObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                voltage = showVoltage(String.valueOf(integer));
            }
        };
        model.getCurrentVoltage().observe(this, voltageObserver);

        final Observer<Integer> coolantTempObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                engineCoolantTemp = showEngineCoolantTemperature(String.valueOf(integer));
            }
        };
        model.getCurrentCoolantTemp().observe(this, coolantTempObserver);

        final Observer<Integer> loadObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                engineLoad = showEngineLoad(String.valueOf(integer));
            }
        };
        model.getCurrentLoad().observe(this, loadObserver);

        final Observer<Integer> baudRateObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentBaudRate = integer;
            }
        };
        model.getCurrentBaudRate().observe(this, baudRateObserver);



//-------------------------------------------------------------------------------------------------
/*
* Buttons
* */
        showToggleLayout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDialogFrag = new ToggleDialog();
                showToggleDialog((ToggleDialog) toggleDialogFrag);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempCMD = enterCMD_edt.getText().toString();
                Handler cmdHandler = new Handler();
                cmdHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tempCMD.isEmpty()) {
                                    Toast.makeText(MainActivity.this, "Enter a Command First", Toast.LENGTH_SHORT).show();
                                } else {
                                    new SendOBD2CMD(getApplicationContext(), tempCMD, mIOGateway);
                                    enterCMD_edt.setText("");
                                }
                            }
                        });
                    }
                });
            }
        });
        
        rawData_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rawDataFrag = new RawDataDialog();
                showRawDataDialog((RawDataDialog) rawDataFrag);
            }
        });

        viewHUD_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gaugeFrag = new GaugeDialog();
                showGaugeDialog((GaugeDialog) gaugeFrag);
            }
        });

        resetCMD_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sendResetCMD();
            }
        });

        sendCMD_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    repeatingSpeedCMD();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        showStatus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendDisplayProtocolCMD(MainActivity.this);
                //sendPrintSummaryCMD(getApplicationContext());
                // getSpeedManually(getApplicationContext(), true);
                try {
                    repeatingRPMCMD();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        allCMD_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendAllCommands();
                }
            }
        });

        basicCMD_tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        sendInitialCommands();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
    private synchronized void repeatingSpeedCMD() throws InterruptedException {
        Timer timer = new Timer();
        repeatingTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SendOBD2CMD(MainActivity.this, PIDConstants.VEHICLE_SPEED, mIOGateway);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(repeatingTimerTask, 1000, 5000);
    }

    private synchronized void repeatingRPMCMD() throws InterruptedException {
        Timer timer = new Timer();
        TimerTask repeatingRPMTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new SendOBD2CMD(MainActivity.this, PIDConstants.ENGINE_RPM, mIOGateway);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(repeatingRPMTimerTask, 1000, 5000);
    }


    private synchronized void checkIgnitionPosition() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(MainActivity.this, PIDConstants.CHECK_IGNITION_POSITION, mIOGateway);
            }
        });
    }

    private synchronized void sendIdentifyCMD() {
        Thread identifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String displayProtocol = "AT DP";
                sendOBD2CMD = new SendOBD2CMD(MainActivity.this, PIDConstants.IDENTIFY_YOURSELF, mIOGateway);
            }
        });
        identifyThread.start();
    }

    private synchronized void sendResetCMD() {
        String[] resetCMD = {PIDConstants.RESET, PIDConstants.PROTOCOL_AUTO,PIDConstants.DISABLE_KEYWORDS};
        Thread resetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                        for (String cmd: resetCMD) {
                            new SendOBD2CMD(MainActivity.this, cmd, mIOGateway);
                            displayLog("CMDs Sent");
                        }
            }
        });
        resetThread.start();
    }

    private synchronized void sendStatusRequestCMD() {
        String statusRequest = "AT IA"; // Check if ELM327 is still active
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(MainActivity.this, statusRequest, mIOGateway);
            }
        },30000); // Wait 30 seconds before (re)sending
    }

    private synchronized void sendPPSummaryCMD() {
        String printSummary = "AT PPS";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendOBD2CMD = new SendOBD2CMD(MainActivity.this, printSummary, mIOGateway);
            }
        });
    }

    private synchronized void sendMonitorAllCMD() {
        String monitorAll = "AT MA";
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendOBD2CMD = new SendOBD2CMD(MainActivity.this, monitorAll, mIOGateway);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                            try {
                                startInitializationSequence();
                                displayLog("Initialization Started...");
                            } catch (Exception e) {
                                e.printStackTrace();
                                displayErrorLog("Initialization Failed\t" + e.getMessage());
                            }
                            setupMonitor();
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
                    READ_BUFFER = new StringBuffer();
                    READ_BUFFER.append(readMessage);
                    displayLog("READ BUFFER:\n" + READ_BUFFER);
                    if (readMessage.contains("AT DP")) {
                        currentProtocol = showCurrentProtocol(readMessage);
                    }
                    //startInitializationSequence2(readMessage);
                    readMessage = readMessage.trim();
                    readMessage = readMessage.toUpperCase();
                    read_txt.setText("READ:\n" + readMessage);
                    char lastChar = ' ';
                    if (!inSimulatorMode) {
                        try {
                            lastChar = readMessage.charAt(readMessage.length() - 1);
                        } catch (Exception e) {
                            displayErrorLog("Read Error:\t" + e.getMessage());
                        }
                        if (lastChar == '>' & mPartialResponse.toString().length() > 0) {
                            try {
                                parseResponse(mPartialResponse.toString() + readMessage);
                            } catch (ScriptException e) {
                                displayErrorLog("mPartialResponse Error: \t" + e.getMessage());
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
                    appendedResponse_txt.setText("Appended:\n" + mSbCmdResp);
                    break;

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    write_txt.setText("WRITE:\n" + writeMessage);
                    mSbCmdResp.append("W>>");
                    mSbCmdResp.append(writeMessage);
                    mSbCmdResp.append("\n");
                    monitorCMDsSentToECU(writeMessage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            model.getRawDataWrite().setValue(writeMessage);
                        }
                    });

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

    public void monitorCMDsSentToECU(String writeBuffer) {
        WRITE_BUFFER = new StringBuffer();
        WRITE_BUFFER.append(writeBuffer);
        displayLog("Write Buffer Monitor:\n" + writeBuffer);
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
                sendAllCommands();
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
                return true;
            case R.id.menu_hud_mode:
                return true;
            case R.id.menu_refresh:
                queryPairedDevices();
                return true;

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

    private void showAlertDialog() {
        String[] items = {"AT TP5","AT MA","AT CSM0","AT KW0"};
        int checkedItem = 0;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Select Command");
        alertDialog.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendOBD2CMD = new SendOBD2CMD(getApplicationContext(), selection, mIOGateway);
                dialog.dismiss();
            }
        });

        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Handler handler = new Handler();
                switch (which) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();
                                selection = items[which];
                            }
                        });
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
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

    private void showToggleDialog(ToggleDialog toggleDialog) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        toggleDialogFrag = getSupportFragmentManager().findFragmentByTag(TAG_TOGGLE);
        if (toggleDialogFrag != null) {
            ft.remove(toggleDialogFrag);
        }
        ft.addToBackStack(null);

        toggleDialog.show(ft, "toggle_dialog");
    }

    private void showGaugeDialog(GaugeDialog gaugeDialog) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        gaugeFrag = getSupportFragmentManager().findFragmentByTag(TAG_GAUGE);
        if (gaugeFrag != null) {
            ft.remove(gaugeFrag);
        }
        ft.addToBackStack(null);

        gaugeDialog.show(ft, "gauge_dialog");

    }

    private void showRawDataDialog(RawDataDialog rawDataDialog) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        rawDataFrag = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_RAW);
        if (rawDataFrag != null) {
            ft.remove(rawDataFrag);
        }
        ft.addToBackStack(null);

        rawDataDialog.show(ft, "raw_dialog");
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
    public static void displayErrorLog(String msg) {
        Log.e(TAG_ERROR, msg);
    }

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
        displayLog("---Sent User CMDS---");
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (userCMDPointer < 0) {
            userCMDPointer = -1;
        }
        if (userCMDPointer >= USER_COMMANDS.length - 1) {
            userCMDPointer = -1;
        }
        userCMDPointer++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendOBD2CMD = new SendOBD2CMD(context, USER_COMMANDS[userCMDPointer], mIOGateway);
                    Log.i("CMDS", USER_COMMANDS[userCMDPointer]);
                } catch (Exception e) {
                    displayErrorLog(e.getMessage());
                }
            }
        });
    }

    private void sendAllCommands() {
        ALL_SENT = true;
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        if (allCMDPointer < 0) {
            allCMDPointer = -1;
        }

        if (allCMDPointer >= ALL_COMMANDS.length - 1) {
            allCMDPointer = -1;
        }
        allCMDPointer++;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               for (String cmd : ALL_COMMANDS) {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           displayLog("Cmd Added:\t" + cmd);
                           new SendOBD2CMD(MainActivity.this, cmd, mIOGateway);
                       }
                   });

                   try {
                       /* Add a delay between each command sent to allow time for a response*/
                       Thread.sleep(4000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }, 1000);
    }

    private void startInitializationSequence() throws InterruptedException {

        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String setProtocolToAuto = PIDConstants.PROTOCOL_AUTO;
                String setDefaultAutoProtocolTo9141 = "AT SP A3";
                String displayProtocol = PIDConstants.PROTOCOL;
                if (INITIALIZATION_COMMANDS.isEmpty()) {

                    INITIALIZATION_COMMANDS.add(displayProtocol);
                    counter2++;
                    for (String sendCommand : INITIALIZATION_COMMANDS) {

                        final SendInitializationOBD2CMD sendInitCmd = new SendInitializationOBD2CMD(
                                sendCommand,
                                MainActivity.this,
                                mIOGateway);
                        displayLog("Initialization CMD Sent:\t" + sendCommand + "\tNum:\t" + counter2);
                        INITIALIZATION_COMMANDS.clear();
                    }
                }
                try {
                    Thread.sleep(5000);
                    startInitializationSequence2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void startInitializationSequence2() {

        INITIALIZATION_COMMANDS.add(PIDConstants.VOLTAGE);
        INITIALIZATION_COMMANDS.add(PIDConstants.ENGINE_LOAD);
        INITIALIZATION_COMMANDS.add(PIDConstants.ENGINE_RPM);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            for (String sendCommand : INITIALIZATION_COMMANDS) {
                                counter++;
                                displayLog("-------------------Command " + counter + " Started--------------------");
                                new SendOBD2CMD(MainActivity.this, sendCommand, mIOGateway);
                                displayLog("CMD2 Sent:\t" + sendCommand);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            displayErrorLog("Raw initialization 2 failed\t" + e.getMessage());
                        }
                    }
                });
            }
            /*
             * delay between repeating the method loop
             * */
        }, 5000);
    }

    private void sendBasicCommands() {
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (String basicCommand : BASIC_COMMANDS) {
                            sendOBD2CMD = new SendOBD2CMD(MainActivity.this, basicCommand, mIOGateway);
                            displayLog("Basic CMDS:\t" + basicCommand);
                        }
                    }
                });
            }
        };
        handler.postDelayed(runnable, 10000);
    }

    private void sendInitialCommands() throws InterruptedException {
        INITIAL_SENT = true;
        if (inSimulatorMode) {
            displayMessage("You are in simulator mode!");
            return;
        }
        Thread initialThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initialCMDPointer++;
                        for (int i = 0; i < INITIAL_CMDS.length; i++) {
                            displayLog("Initial CMDS:\t" + INITIAL_CMDS[i]);
                            sendOBD2CMD = new SendOBD2CMD(MainActivity.this, INITIAL_CMDS[i], mIOGateway);
                        }
                    }
                });
            }
        });

        initialThread.start();

        if (initialThread.isAlive()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SendOBD2CMD(MainActivity.this, PIDConstants.PROTOCOL, mIOGateway);
                }
            });
        }
        initialThread.interrupt();
        displayLog("Initial Thread Status:\t" + String.valueOf(initialThread.isAlive()));


//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        for (int i = 0; i < INITIAL_CMDS.length; i++) {
//                            displayLog(INITIAL_CMDS[i]);
//                            sendOBD2CMD = new SendOBD2CMD(context, INITIAL_CMDS[i], mIOGateway);
//                        }
//                        displayLog("---Initial CMDS Sent---");
//                    }
//                });
//            }
//
//        };
//        handler.post(runnable);
    }

    @SuppressLint("SetTextI18n")
    private void parseResponse(String buffer) throws ScriptException {
        displayLog("RAW BUFFER Response : " + buffer);
        mSbCmdResp.append(buffer);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                model.getRawDataRead().setValue(String.valueOf(mSbCmdResp));
            }
        });

        if (buffer.contains("STOPPED")) {
           // Log.i("MA", String.valueOf(device) + "STOPPED");
            deviceStatus_txt.setText(R.string.stopped);
            deviceStatus_txt.setTextColor(getColor(R.color.halo_red));
        } else {
            deviceStatus_txt.setText("CONNECTED");
            deviceStatus_txt.setTextColor(getColor(R.color.green));
        }

        if (buffer.contains("010D")) {
            speed = showVehicleSpeed(buffer);
            kLine_speed.setText("MPH:\t" + speed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentSpeed().setValue(speed);
                }
            });
        }

        else if (buffer.contains("010C")) {
            rpm = showEngineRPM(buffer);
            kLine_rpm.setText("RPM:\t" + rpm);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentRpm().setValue(rpm);
                }
            });
        }

        else if (buffer.contains("ATTP")) {
            displayLog("ATTP RESPONSE:\t" + buffer);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentSetProtocol9141().setValue(buffer);
                }
            });
        }

        else if (buffer.contains("ATSP")) {
            currentProtocol = showCurrentProtocol(buffer);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentSetProtocol9141().setValue(buffer);
                }
            });
        }

        else if (buffer.contains("ATTP3OK")) {
            displayLog("-------- 9141 LOCATED ---------");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentSetProtocol9141().setValue(buffer);
                }
            });
        }

        else if (buffer.contains("ATDP")) {
            currentProtocol = showCurrentProtocol(buffer);
            displayLog("---CURRENT PROTOCOL---\t" + currentProtocol);
            protocol_txt.setText("Protocol:\t" + buffer);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentDisplayProtocol().setValue(buffer);
                }
            });
        }

        else if (buffer.contains("ATRV")) {
            try {
                voltage = showVoltage(buffer);
                voltage_txt.setText("Voltage:\t" + voltage);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        model.getCurrentVoltage().setValue(Integer.valueOf(buffer));
                    }
                });
            } catch (Exception e) {
                displayErrorLog(e.getMessage());
            }
        }

        else if (buffer.contains("0105")) {
            try {
                engineCoolantTemp = showEngineCoolantTemperature(buffer);
                coolant_txt.setText("Coolant Temp:\t" + engineCoolantTemp);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        model.getCurrentCoolantTemp().setValue(Integer.valueOf(buffer));
                    }
                });
            } catch (Exception e) {
                displayErrorLog("Coolant Error\t" + e.getMessage());
            }
        }

        else if (buffer.contains("011C")) {
            try {
                obdStandards = showObdStandardsSupported(buffer);
                standards_txt.setText("Standards:\t" + obdStandards);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        model.getCurrentStandards().setValue(buffer);
                    }
                });
            } catch (Exception e) {
                displayErrorLog("OBD Standards Error\t" + e.getMessage());
            }

        }

        else if (buffer.contains("0104")) {
            try {
                engineLoad = showEngineLoad(buffer);
                load_txt.setText("Engine Load:\t" + engineLoad);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        model.getCurrentLoad().setValue(Integer.valueOf(buffer));
                    }
                });
            } catch (Exception e) {
                displayErrorLog("Engine Load Error\t" + e.getMessage());
            }

        }

        else if (buffer.contains("ATMA")) {
            displayLog("----AT MA is Active----");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.getCurrentMonitorAllStatus().setValue(Integer.valueOf(buffer));
                }
            });
        }

        /*--------------------ToDO*//* set up observer for fuel level */
        else if (buffer.contains("012F")) {
            try {
                fuelLevel = showFuelLevel(buffer);
                fuelLevel_txt.setText("Fuel Level:\t" + fuelLevel);
            } catch (Exception e) {
                displayErrorLog("Fuel Error\t" + e.getMessage());
            }
        }

        else if (basicCMD_tb.isChecked()) {
            try {
                sendInitialCommands();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i =0; i < 100; i++) {
                displayLog("Basic Commands Sent" + i);
            }
        }

        else if (allCMD_tb.isChecked()) {
            sendAllCommands();
        }
    }

    private String decodeResponse(String response) {
        if (response.length() > 8) {
            try {
                return response.substring(8, response.length());
            } catch (Exception e) {
                displayErrorLog(e.getMessage());
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
                displayErrorLog(e.getMessage());
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
                displayErrorLog(e.getMessage());
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
                displayErrorLog(e.getMessage());
            }
        }
    }

    public String showCurrentProtocol(String buffer) {
        protocol_txt.setText(buffer);
        return currentProtocol;
    }

    // "011C"
    public String showObdStandardsSupported(String buffer) {
        String standardsSupported = buffer;
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
        String tmp = buffer;
        int load = 0;
        try {
            tmp = decodeResponse(buffer);
            load = convertToDecimal(tmp);
            engineLoad = load;
            displayLog("LOAD\t" + engineLoad);
        } catch (Exception e) {
            displayErrorLog("---Engine Load Error---" + e.getMessage());
        }
        return engineLoad;
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
                displayErrorLog(e.getMessage());
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
                displayErrorLog(e.getMessage());
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
                displayErrorLog(e.getMessage());
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
                fuelLevel = (A*100)/255;
                return (A*100)/255;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                displayErrorLog(e.getMessage());
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
                displayLog("Coolant Temp RAW : " + buffer);
                int celsius = convertToDecimal(buf);
                int fahrenheit = convertToFahrenheit(celsius);
                lastCoolantTemp = fahrenheit;
                engineCoolantTemp = fahrenheit;
                return engineCoolantTemp;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                displayErrorLog(e.getMessage());
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

                if (lastRPM > maxRPM) {
                    maxRPM = lastRPM;
                }
                rpm = lastRPM;
                tempSpeedometer.speedTo(lastRPM);
                return lastRPM;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                displayErrorLog("RPM Error:\t" + e.getMessage());
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
                speed = lastSpeed;
                return lastSpeed;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                displayErrorLog("Speed Error:\t" + e.getMessage());
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
                    displayErrorLog("Distance Traveled Error\t" + e.getMessage());
                }
                return (A * 256) + B;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                displayErrorLog("Distance Traveled Error\t" + e.getMessage());
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
        try {
            USER_COMMANDS = new String[]{"0100"};
            sendUserCommands(getApplicationContext());
        } catch (Exception e) {
            displayLog(e.getMessage());
        }
    }

    private void clearDTC_CMD() {
        String clearDTC_codes = "04";
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