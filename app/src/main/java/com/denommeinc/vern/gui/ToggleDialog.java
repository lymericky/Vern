package com.denommeinc.vern.gui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.denommeinc.vern.R;

import java.util.Objects;


public class ToggleDialog extends DialogFragment {

    int selection;
    View view;
    ToggleButton speedCMD,
            rpmCMD,
            protocol9141CMD,
            displayProtocolCMD,
            keywordsCMD,
            statusCMD,
            monitorAllCMD,
            identifyCMD,
            setDefaultsCMD,
            standardsCMD,
            canStatusCMD,
            silentModeCMD,
            ppSummaryCMD,
            automaticReceiveCMD,
            resetCMD,
            voltageCMD,
            coolantCMD,
            loadCMD;

    Button setBaud_btn;

    TextView speedResult_txt,
            rpmResult_txt,
            protocol9141Result_txt,
            displayProtocolResult_txt,
            keywordResult_txt,
            statusResult_txt,
            monitorAllResult_txt,
            identifyResult_txt,
            setDefaultResult_txt,
            standardsResult_txt,
            canStatusResult_txt,
            silentModeResult_txt,
            ppSummaryResult_txt,
            ARResult_txt,
            resetResult_txt,
            voltageResult_txt,
            coolantResult_txt,
            loadResult_txt,
            read_txt,
            write_txt,
            baudRate_txt;

    RadioGroup baudRadioGroup;
    RadioButton ib10_rb,
            ib12_rb,
            ib15_rb,
            ib48_rb,
            ib96_rb;



    public ToggleDialog() {
        // Required empty public constructor
    }

    public static ToggleDialog newInstance() {
        ToggleDialog fragment = new ToggleDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setLayout(params.width, params.height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_toggle_dialog, container, false);

        setBaud_btn = view.findViewById(R.id.setBaud_btn);
        speedCMD = view.findViewById(R.id.speedCMD);
        rpmCMD = view.findViewById(R.id.rpmCMD);
        protocol9141CMD = view.findViewById(R.id.protocol9141CMD);
        displayProtocolCMD = view.findViewById(R.id.displayProtocolCMD);
        keywordsCMD = view.findViewById(R.id.keywordsCMD);
        statusCMD = view.findViewById(R.id.statusCMD);
        monitorAllCMD = view.findViewById(R.id.monitorAllCMD);
        identifyCMD = view.findViewById(R.id.identifyCMD);
        setDefaultsCMD = view.findViewById(R.id.setDefaultsCMD);
        standardsCMD = view.findViewById(R.id.standardsCMD);
        canStatusCMD = view.findViewById(R.id.canStatusCMD);
        silentModeCMD = view.findViewById(R.id.silentModeCMD);
        ppSummaryCMD = view.findViewById(R.id.ppSummaryCMD);
        automaticReceiveCMD = view.findViewById(R.id.automaticReceiveCMD);
        resetCMD = view.findViewById(R.id.resetCMD);
        voltageCMD = view.findViewById(R.id.voltageCMD);
        coolantCMD = view.findViewById(R.id.coolantCMD);
        loadCMD = view.findViewById(R.id.loadCMD);

        speedResult_txt = view.findViewById(R.id.speedResult_txt);
        rpmResult_txt = view.findViewById(R.id.rpmResult_txt);
        protocol9141Result_txt = view.findViewById(R.id.protocol9141Result_txt);
        displayProtocolResult_txt = view.findViewById(R.id.displayProtocolResult_txt);
        keywordResult_txt = view.findViewById(R.id.keywordsResult_txt);
        statusResult_txt = view.findViewById(R.id.statusResult_txt);
        monitorAllResult_txt = view.findViewById(R.id.monitorAllResult_txt);
        identifyResult_txt = view.findViewById(R.id.identifyResult_txt);
        setDefaultResult_txt = view.findViewById(R.id.setDefaultsResult_txt);
        standardsResult_txt = view.findViewById(R.id.standardsResult_txt);
        canStatusResult_txt = view.findViewById(R.id.canStatusResult_txt);
        silentModeResult_txt = view.findViewById(R.id.silentModeResult_txt);
        ppSummaryResult_txt = view.findViewById(R.id.ppSummaryResult_txt);
        ARResult_txt = view.findViewById(R.id.ARResult_txt);
        resetResult_txt = view.findViewById(R.id.resetResult_txt);
        voltageResult_txt = view.findViewById(R.id.voltageResult_txt);
        coolantResult_txt = view.findViewById(R.id.coolantResult_txt);
        loadResult_txt = view.findViewById(R.id.loadResult_txt);
        read_txt = view.findViewById(R.id.read_txt);
        write_txt = view.findViewById(R.id.write_txt);
        baudRate_txt = view.findViewById(R.id.baudRate_txt);

        baudRadioGroup = view.findViewById(R.id.baudRadioGroup);
        ib10_rb = view.findViewById(R.id.ib10_rb);
        ib10_rb.setChecked(true);
        ib12_rb = view.findViewById(R.id.ib12_rb);
        ib15_rb = view.findViewById(R.id.ib15_rb);
        ib48_rb = view.findViewById(R.id.ib48_rb);
        ib96_rb = view.findViewById(R.id.ib96_rb);

        baudRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ib10_rb:
                        Log.i("TOGGLE", "Selected 10");
                        selection = 10;
                        break;
                    case R.id.ib12_rb:
                        Log.i("TOGGLE", "Selected 12");
                        selection = 12;
                        break;
                    case R.id.ib15_rb:
                        Log.i("TOGGLE", "Selected 15");
                        selection = 15;
                        break;
                    case R.id.ib48_rb:
                        Log.i("TOGGLE", "Selected 48");
                        selection = 48;
                        break;
                    case R.id.ib96_rb:
                        Log.i("TOGGLE", "Selected 96");
                        selection = 96;
                        break;
                    default:
                        selection = checkedId;
                }

            }

        });

        setBaud_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Selection: \t" + String.valueOf(selection), Toast.LENGTH_SHORT).show();
                Log.i("TOGGLE", "Selection:\t" + String.valueOf(selection));
            }
        });


        speedCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        rpmCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        protocol9141CMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        displayProtocolCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        keywordsCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        statusCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        monitorAllCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        identifyCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        setDefaultsCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        standardsCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        canStatusCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        silentModeCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        ppSummaryCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        automaticReceiveCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        resetCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        voltageCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        coolantCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        loadCMD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });



        return view;
    }


}