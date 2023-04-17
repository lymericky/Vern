package com.denommeinc.vern.live_data;

import android.location.GnssAntennaInfo;

public class KLineManager {
    int rpm = 0;
    int speed = 0;
    String rawData;

    public KLineManager(int rpm, int speed, String rawData) {
        this.rpm = rpm;
        this.speed = speed;
        this.rawData = rawData;
    }

    public void requestKlineUpdates() {

    }

    public void removeUpdates() {

    }
}
