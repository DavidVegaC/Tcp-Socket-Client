package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hose implements Serializable {

    @Expose
    @SerializedName("HoseNumber")
    public int HoseNumber;

    @Expose
    @SerializedName("HoseName")
    public String HoseName;

    @Expose
    @SerializedName("HardwareId")
    public int HardwareId;

    @Expose
    @SerializedName("LastTicket")
    public int LastTicket;

    @Expose
    @SerializedName("FuelQuantity")
    public Double FuelQuantity;

    @Expose
    @SerializedName("NameProduct")
    public String NameProduct;

    public Hose(int hoseNumber, String hoseName, int hardwareId, int lastTicket, Double fuelQuantity, String nameProduct) {
        HoseNumber = hoseNumber;
        HoseName = hoseName;
        HardwareId = hardwareId;
        LastTicket = lastTicket;
        FuelQuantity =fuelQuantity;
        NameProduct = nameProduct;
    }

    public Hose(){

    }

    public int getHoseNumber() {
        return HoseNumber;
    }

    public void setHoseNumber(int hoseNumber) {
        HoseNumber = hoseNumber;
    }

    public String getHoseName() {
        return HoseName;
    }

    public void setHoseName(String hoseName) {
        HoseName = hoseName;
    }

    public int getHardwareId() {
        return HardwareId;
    }

    public void setHardwareId(int hardwareId) {
        HardwareId = hardwareId;
    }

    public int getLastTicket() {
        return LastTicket;
    }

    public void setLastTicket(int lastTicket) {
        LastTicket = lastTicket;
    }

    public Double getFuelQuantity() {
        return FuelQuantity;
    }

    public void setFuelQuantity(Double fuelQuantity) {
        FuelQuantity = fuelQuantity;
    }

    public String getNameProduct() {
        return NameProduct;
    }

    public void setNameProduct(String nameProduct) {
        NameProduct = nameProduct;
    }


}

