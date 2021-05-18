package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Plate implements Serializable {

    @Expose
    @SerializedName("VehicleCodePlate")
    public String VehicleCodePlate;

    @Expose
    @SerializedName("Status")
    public String Status;

    @Expose
    @SerializedName("Company")
    public String Company;

    public Plate(String vehicleCodePlate, String status, String company) {
        VehicleCodePlate = vehicleCodePlate;
        Status = status;
        Company = company;
    }

    public Plate(){

    }

    public String getVehicleCodePlate() {
        return VehicleCodePlate;
    }

    public void setVehicleCodePlate(String vehicleCodePlate) {
        VehicleCodePlate = vehicleCodePlate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }
}
