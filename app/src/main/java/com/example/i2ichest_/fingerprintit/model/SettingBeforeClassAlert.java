package com.example.i2ichest_.fingerprintit.model;

import java.io.Serializable;

public class SettingBeforeClassAlert implements Serializable {
    private String hour;
    private String minutes;
    private boolean onOff;

    public SettingBeforeClassAlert() {
    }

    public SettingBeforeClassAlert(String hour, String minutes, boolean onOff) {
        this.hour = hour;
        this.minutes = minutes;
        this.onOff = onOff;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
