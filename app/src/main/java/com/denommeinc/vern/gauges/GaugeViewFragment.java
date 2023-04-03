package com.denommeinc.vern.gauges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.denommeinc.vern.MainActivity;
import com.denommeinc.vern.R;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GaugeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GaugeViewFragment extends Fragment {
    SpeedView speedView;
    ProgressiveGauge progressiveGauge;
    FloatingActionButton fab;

    public void closeFragment() {
        requireActivity().
                getSupportFragmentManager().
                beginTransaction().
                remove(this).
                commit();
    }

    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GaugeViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GaugeViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GaugeViewFragment newInstance(String param1, String param2) {
        GaugeViewFragment fragment = new GaugeViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gauge_view, container, false);
        speedView = view.findViewById(R.id.speedView);
        speedView.speedTo(MainActivity.getLastSpeed());
        progressiveGauge = view.findViewById(R.id.progressiveGauge);
        progressiveGauge.speedTo(MainActivity.getLastRPM());
        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedView.speedTo(0);
                progressiveGauge.speedTo(0);
            }
        });

        speedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speedView.speedTo(MainActivity.lastSpeed);
            }
        });
        progressiveGauge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressiveGauge.speedTo(MainActivity.lastRPM);
            }
        });
        return view;
    }
}