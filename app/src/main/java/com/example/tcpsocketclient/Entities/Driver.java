package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Driver implements Serializable {

    @Expose
    @SerializedName("DriverKey")
    public String DriverKey;

    @Expose
    @SerializedName("DriverName")
    public String DriverName;

    public Driver(String driverKey, String driverName) {
        DriverKey = driverKey;
        DriverName = driverName;
    }

    public Driver() {
    }

    public String getDriverKey() {
        return DriverKey;
    }

    public void setDriverKey(String driverKey) {
        DriverKey = driverKey;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }
}
