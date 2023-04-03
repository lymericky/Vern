package com.denommeinc.vern.utility;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.denommeinc.vern.R;
import com.denommeinc.vern.elm327.BluetoothIOGateway;

import java.util.Arrays;


public class SendOBD2CMD {
    String sendMsg;
    Context context;
    BluetoothIOGateway gateway;

    public SendOBD2CMD(Context context, String sendMsg, BluetoothIOGateway gateway) {
        this.context = context;
        this.sendMsg = sendMsg;
        this.gateway = gateway;

        if (gateway.getState() != BluetoothIOGateway.STATE_CONNECTED) {
            Toast.makeText(context, R.string.bt_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        String strCMD = sendMsg;
        strCMD += '\r'; // return button

        byte[] byteCMD = strCMD.getBytes();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                gateway.write(byteCMD);
            }
        };
        handler.post(runnable);
    }
}
