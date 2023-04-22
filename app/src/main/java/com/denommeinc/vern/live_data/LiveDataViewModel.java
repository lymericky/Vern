package com.denommeinc.vern.live_data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveDataViewModel extends ViewModel {
    private MutableLiveData<Integer> currentSpeed;
    private MutableLiveData<Integer> currentRpm;

    private MutableLiveData<String> rawDataRead;
    private MutableLiveData<String> rawDataWrite;

    private MutableLiveData<String> currentSetProtocol9141;
    private MutableLiveData<String> currentDisplayProtocol;
    private MutableLiveData<String> currentIdentify;
    private MutableLiveData<String> currentStandards;
    private MutableLiveData<String> currentCANStatus;
    private MutableLiveData<String> currentPPSummary;

    private MutableLiveData<Integer> currentKeywordsStatus;
    private MutableLiveData<Integer> currentActiveStatus;
    private MutableLiveData<Integer> currentMonitorAllStatus;
    private MutableLiveData<Integer> currentSetDefaultsStatus;
    private MutableLiveData<Integer> currentSilentModeStatus;
    private MutableLiveData<Integer> currentAutoReceiveStatus;
    private MutableLiveData<Integer> currentResetStatus;
    private MutableLiveData<Integer> currentVoltage;
    private MutableLiveData<Integer> currentCoolantTemp;
    private MutableLiveData<Integer> currentLoad;
    private MutableLiveData<Integer> currentBaudRate;


    public MutableLiveData<Integer> getCurrentSpeed() {
        if (currentSpeed == null) {
            currentSpeed = new MutableLiveData<>();
        }
        return currentSpeed;
    }
    public MutableLiveData<Integer> getCurrentRpm() {
        if (currentRpm == null) {
            currentRpm = new MutableLiveData<>();
        }
        return currentRpm;
    }

    public MutableLiveData<String> getRawDataRead() {
        if (rawDataRead == null) {
            rawDataRead = new MutableLiveData<>();
        }
        return rawDataRead;
    }

    public MutableLiveData<String> getRawDataWrite() {
        if (rawDataWrite == null) {
            rawDataWrite = new MutableLiveData<>();
        }
        return rawDataWrite;
    }

    public MutableLiveData<String> getCurrentSetProtocol9141() {
        if (currentSetProtocol9141 == null) {
            currentSetProtocol9141 = new MutableLiveData<>();
        }
        return currentSetProtocol9141;
    }

    public MutableLiveData<String> getCurrentDisplayProtocol() {
        if (currentDisplayProtocol == null) {
            currentDisplayProtocol = new MutableLiveData<>();
        }
        return currentDisplayProtocol;
    }

    public MutableLiveData<String> getCurrentIdentify() {
        if (currentIdentify == null) {
            currentIdentify = new MutableLiveData<>();
        }
        return currentIdentify;
    }

    public MutableLiveData<String> getCurrentStandards() {
        if (currentStandards == null) {
            currentStandards = new MutableLiveData<>();
        }
        return currentStandards;
    }

    public MutableLiveData<String> getCurrentCANStatus() {
        if (currentCANStatus == null) {
            currentCANStatus = new MutableLiveData<>();
        }
        return currentCANStatus;
    }

    public MutableLiveData<String> getCurrentPPSummary() {
        if (currentPPSummary == null) {
            currentPPSummary = new MutableLiveData<>();
        }
        return currentPPSummary;
    }

    public MutableLiveData<Integer> getCurrentKeywordsStatus() {
        if (currentKeywordsStatus == null) {
            currentKeywordsStatus = new MutableLiveData<>();
        }
        return currentKeywordsStatus;
    }

    public MutableLiveData<Integer> getCurrentActiveStatus() {
        if (currentActiveStatus == null) {
            currentActiveStatus = new MutableLiveData<>();
        }
        return currentActiveStatus;
    }

    public MutableLiveData<Integer> getCurrentMonitorAllStatus() {
        if (currentMonitorAllStatus == null) {
            currentMonitorAllStatus = new MutableLiveData<>();
        }
        return currentMonitorAllStatus;
    }

    public MutableLiveData<Integer> getCurrentSetDefaultsStatus() {
        if (currentSetDefaultsStatus == null) {
            currentSetDefaultsStatus = new MutableLiveData<>();
        }
        return currentSetDefaultsStatus;
    }

    public MutableLiveData<Integer> getCurrentSilentModeStatus() {
        if (currentSilentModeStatus == null) {
            currentSilentModeStatus = new MutableLiveData<>();
        }
        return currentSilentModeStatus;
    }

    public MutableLiveData<Integer> getCurrentAutoReceiveStatus() {
        if (currentAutoReceiveStatus == null) {
            currentAutoReceiveStatus = new MutableLiveData<>();
        }
        return currentAutoReceiveStatus;
    }

    public MutableLiveData<Integer> getCurrentResetStatus() {
        if (currentResetStatus == null) {
            currentResetStatus = new MutableLiveData<>();
        }
        return currentResetStatus;
    }

    public MutableLiveData<Integer> getCurrentVoltage() {
        if (currentVoltage == null) {
            currentVoltage = new MutableLiveData<>();
        }
        return currentVoltage;
    }

    public MutableLiveData<Integer> getCurrentCoolantTemp() {
        if (currentCoolantTemp == null) {
            currentCoolantTemp = new MutableLiveData<>();
        }
        return currentCoolantTemp;
    }

    public MutableLiveData<Integer> getCurrentLoad() {
        if (currentLoad == null) {
            currentLoad = new MutableLiveData<>();
        }
        return currentLoad;
    }

    public MutableLiveData<Integer> getCurrentBaudRate() {
        if (currentBaudRate == null) {
            currentBaudRate = new MutableLiveData<>();
        }
        return currentBaudRate;
    }
}
