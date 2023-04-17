package com.denommeinc.vern.live_data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveDataViewModel extends ViewModel {
    private MutableLiveData<Integer> currentSpeed;
    private MutableLiveData<Integer> currentRpm;
    private MutableLiveData<String> rawData;

    public MutableLiveData<Integer> getCurrentSpeed() {
        if (currentSpeed == null) {
            currentSpeed = new MutableLiveData<Integer>();
        }
        return currentSpeed;
    }
    public MutableLiveData<Integer> getCurrentRpm() {
        if (currentRpm == null) {
            currentRpm = new MutableLiveData<Integer>();
        }
        return currentRpm;
    }

    public MutableLiveData<String> getRawData() {
        if (rawData == null) {
            rawData = new MutableLiveData<String>();
        }
        return rawData;
    }
}
