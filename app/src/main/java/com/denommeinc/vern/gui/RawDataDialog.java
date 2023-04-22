package com.denommeinc.vern.gui;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.denommeinc.vern.R;
import com.denommeinc.vern.live_data.LiveDataViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RawDataDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RawDataDialog extends DialogFragment {

    View view;
    LiveDataViewModel model;
    TextView liveDataSpeed_txt;
    TextView liveDataRpm_txt;
    TextView rawData_txt;
    ImageButton dataDialogClose_btn;
    int speed = 0;
    int rpm = 0;
    String rawData = null;
    ScrollView scrollView;

    public RawDataDialog() {
        // Required empty public constructor
    }


    public static RawDataDialog newInstance() {
        RawDataDialog fragment = new RawDataDialog();

        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_raw_data, container, false);
        liveDataSpeed_txt = view.findViewById(R.id.liveData_txt);
        liveDataRpm_txt = view.findViewById(R.id.liveDataRpm_txt);
        rawData_txt = view.findViewById(R.id.rawData_txt);
        rawData_txt.setMovementMethod(new ScrollingMovementMethod());
        dataDialogClose_btn = view.findViewById(R.id.dataDialogClose_ib);
        model = new ViewModelProvider(requireActivity()).get(LiveDataViewModel.class);
        liveDataSpeed_txt.setText(String.valueOf(speed));
        liveDataRpm_txt.setText(String.valueOf(rpm));
        rawData_txt.setText(rawData);
        scrollView = view.findViewById(R.id.scrollView);



        model.getCurrentSpeed().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Integer integer) {
                speed = integer;
                Log.i("LIVE_DATA", "Live Speed:\t" + speed);

                try {
                    liveDataSpeed_txt.setText(String.valueOf(speed));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LIVE_DATA_ERROR", e.getMessage());
                }
            }
        });
        model.getCurrentRpm().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Integer integer) {
                rpm = integer;
                Log.i("LIVE_DATA", "Live RPM:\t" + rpm);
                try {
                    liveDataRpm_txt.setText(String.valueOf(rpm));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LIVE_DATA_ERROR", e.getMessage());
                }
            }
        });
        model.getRawDataRead().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                rawData = s;
                Log.i("RAW_DATA", s);
                try {
                    rawData_txt.setText(s);
                    rawData_txt.setMovementMethod(new ScrollingMovementMethod());
                    //scrollView.smoothScrollTo(0, scrollView.getBottom());
                    //scrollView.scrollTo(0, scrollView.getBottom());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LIVE_DATA_ERROR", e.getMessage());
                }
            }
        });

        dataDialogClose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        return view;
    }

}