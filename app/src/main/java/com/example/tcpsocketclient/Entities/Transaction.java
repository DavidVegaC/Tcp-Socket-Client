package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {

    @Expose
    @SerializedName("HardwareId")
    public int HardwareId;

    @Expose
    @SerializedName("DriverKey")
    public String DriverKey;

    @Expose
    @SerializedName("OperatorKey")
    public String OperatorKey;

    @Expose
    @SerializedName("FuelQuantity")
    public Double FuelQuantity;

    @Expose
    @SerializedName("FuelTemperature")
    public Double FuelTemperature;

    @Expose
    @SerializedName("HoseNumber")
    public int HoseNumber;

    @Expose
    @SerializedName("TicketNumber")
    public int TicketNumber;

    @Expose
    @SerializedName("CodePlate")
    public String CodePlate;

    @Expose
    @SerializedName("Horometer")
    public Double Horometer;

    @Expose
    @SerializedName("Kilometer")
    public Double Kilometer;

    @Expose
    @SerializedName("DateTimeStart")
    public String DateTimeStart;

    @Expose
    @SerializedName("DateTimeEnd")
    public String DateTimeEnd;

    public Transaction(int hardwareId, String driverKey, String operatorKey, Double fuelQuantity, Double fuelTemperature, int hoseNumber, int ticketNumber, String codePlate, Double horometer, Double kilometer, String dateTimeStart, String dateTimeEnd) {
        HardwareId = hardwareId;
        DriverKey = driverKey;
        OperatorKey = operatorKey;
        FuelQuantity = fuelQuantity;
        FuelTemperature = fuelTemperature;
        HoseNumber = hoseNumber;
        TicketNumber = ticketNumber;
        CodePlate = codePlate;
        Horometer = horometer;
        Kilometer = kilometer;
        DateTimeStart = dateTimeStart;
        DateTimeEnd = dateTimeEnd;
    }

    public int getHardwareId() {
        return HardwareId;
    }

    public void setHardwareId(int hardwareId) {
        HardwareId = hardwareId;
    }

    public String getDriverKey() {
        return DriverKey;
    }

    public void setDriverKey(String driverKey) {
        DriverKey = driverKey;
    }

    public String getOperatorKey() {
        return OperatorKey;
    }

    public void setOperatorKey(String operatorKey) {
        OperatorKey = operatorKey;
    }

    public Double getFuelQuantity() {
        return FuelQuantity;
    }

    public void setFuelQuantity(Double fuelQuantity) {
        FuelQuantity = fuelQuantity;
    }

    public Double getFuelTemperature() {
        return FuelTemperature;
    }

    public void setFuelTemperature(Double fuelTemperature) {
        FuelTemperature = fuelTemperature;
    }

    public int getHoseNumber() {
        return HoseNumber;
    }

    public void setHoseNumber(int hoseNumber) {
        HoseNumber = hoseNumber;
    }

    public int getTicketNumber() {
        return TicketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        TicketNumber = ticketNumber;
    }

    public String getCodePlate() {
        return CodePlate;
    }

    public void setCodePlate(String codePlate) {
        CodePlate = codePlate;
    }

    public Double getHorometer() {
        return Horometer;
    }

    public void setHorometer(Double horometer) {
        Horometer = horometer;
    }

    public Double getKilometer() {
        return Kilometer;
    }

    public void setKilometer(Double kilometer) {
        Kilometer = kilometer;
    }

    public String getDateTimeStart() {
        return DateTimeStart;
    }

    public void setDateTimeStart(String dateTimeStart) {
        DateTimeStart = dateTimeStart;
    }

    public String getDateTimeEnd() {
        return DateTimeEnd;
    }

    public void setDateTimeEnd(String dateTimeEnd) {
        DateTimeEnd = dateTimeEnd;
    }
}
