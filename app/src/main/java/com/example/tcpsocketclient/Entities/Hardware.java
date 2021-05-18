package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hardware implements Serializable {

    @Expose
    @SerializedName("HardwareId")
    public int HardwareId;

    @Expose
    @SerializedName("StationName")
    public String StationName;

    @Expose
    @SerializedName("LastTicket")
    public int LastTicket;

    public Hardware(int hardwareId, String stationName, int lastTicket) {
        HardwareId = hardwareId;
        StationName = stationName;
        LastTicket = lastTicket;
    }

    public int getHardwareId() {
        return HardwareId;
    }

    public void setHardwareId(int hardwareId) {
        HardwareId = hardwareId;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public int getLastTicket() {
        return LastTicket;
    }

    public void setLastTicket(int lastTicket) {
        LastTicket = lastTicket;
    }
}

