package com.denommeinc.vern.utility;

import static com.denommeinc.vern.MainActivity.USER_COMMANDS;
import static com.denommeinc.vern.MainActivity.inSimulatorMode;
import static com.denommeinc.vern.MainActivity.userCMDPointer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.denommeinc.vern.elm327.BluetoothIOGateway;

import java.util.Objects;

public class SendUserCommands {

    public SendUserCommands(Context context, BluetoothIOGateway gateway) {
        SendOBD2CMD sendOBD2CMD;
        if (inSimulatorMode) {
            Toast.makeText(context, "You are in simulator mode!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userCMDPointer < 0) {
            userCMDPointer = -1;
        }


        if (USER_COMMANDS != null && userCMDPointer >= USER_COMMANDS.length - 1) {
            userCMDPointer = 1;
        }
        if (USER_COMMANDS != null) {
                userCMDPointer++;
                sendOBD2CMD = new SendOBD2CMD(context, USER_COMMANDS[userCMDPointer], gateway);
                Log.i("CMD", "SENDING ---USER--- COMMANDS..." + userCMDPointer);

        }
    }
}
