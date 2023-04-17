package com.denommeinc.vern.gui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.denommeinc.vern.R;
import com.denommeinc.vern.live_data.LiveDataViewModel;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.github.anastr.speedviewlib.SpeedView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GaugeDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GaugeDialog extends DialogFragment {
    SpeedView speedGauge;
    ProgressiveGauge rpmGauge;
    ImageButton closeGauges_ib;
    View view;
    LiveDataViewModel model;
    int speed = 0;
    int rpm = 0;

    public void closeFragment() {
        requireActivity().
                getSupportFragmentManager().
                beginTransaction().
                remove(this).
                commit();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GaugeDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GaugeDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static GaugeDialog newInstance(String param1, String param2) {
        GaugeDialog fragment = new GaugeDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        view = inflater.inflate(R.layout.dialog_gauge, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        model = new ViewModelProvider(requireActivity()).get(LiveDataViewModel.class);
        speedGauge = view.findViewById(R.id.speedView);
        rpmGauge = view.findViewById(R.id.progressiveGauge);

        model.getCurrentSpeed().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                speed = integer;
                Log.i("LIVE_DATA", "Live Speed:\t" + speed);
                try {
                    speedGauge.speedTo(speed);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LIVE_DATA_ERROR", e.getMessage());
                }
            }
        });
        model.getCurrentRpm().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                rpm = integer;
                Log.i("LIVE_DATA", "Live RPM:\t" + rpm);
                try {
                    rpmGauge.speedTo(rpm);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("LIVE_DATA_ERROR", e.getMessage());
                }
            }
        });


        closeGauges_ib = view.findViewById(R.id.closeGauges_ib);
        closeGauges_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        speedGauge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rpmGauge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}