package com.denommeinc.vern.utility;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.denommeinc.vern.R;
import com.denommeinc.vern.elm327.BluetoothIOGateway;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SendNewOBD2CMD {
    String sendMsg;
    Context context;
    BluetoothIOGateway gateway;
    Runnable runnable;
    int delay = 5;
    int counter = 0;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();



    public SendNewOBD2CMD(Context context, String sendMsg, BluetoothIOGateway gateway) {
        this.sendMsg = sendMsg;
        this.context = context;
        this.gateway = gateway;

        if (gateway.getState() != BluetoothIOGateway.STATE_CONNECTED) {
            Toast.makeText(context, R.string.bt_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        String strCMD = sendMsg;
        strCMD += '\r'; // return button

        Log.i("RAW_CMD","StrCMD:\t" + strCMD);

        byte[] byteCMD = strCMD.getBytes();

        runnable = new Runnable() {
            @Override
            public void run() {
                for (counter = 0; counter < 1; counter++) {
                    Log.i("COUNTER", "total count:\t" + counter);
                    gateway.write(byteCMD);
                }
            }
        };
        scheduler.schedule(runnable, delay, TimeUnit.SECONDS);
        scheduler.shutdown();
    }
}
