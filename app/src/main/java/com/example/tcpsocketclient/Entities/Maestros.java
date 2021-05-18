package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Maestros implements Serializable {


    @Expose
    @SerializedName("Hardwares")
    public List<Hardware> Hardwares;

    @Expose
    @SerializedName("Drivers")
    public List<Driver> Drivers;

    @Expose
    @SerializedName("Hoses")
    public List<Hose> Hoses;

    @Expose
    @SerializedName("Operators")
    public List<Operator> Operators;

    @Expose
    @SerializedName("Plates")
    public List<Plate> Plates;

    public Maestros() {
        List<Hardware> Hardwares = new ArrayList<>();
        List<Driver> Drivers= new ArrayList<>();
        List<Hose> Hoses= new ArrayList<>();
        List<Operator> Operators= new ArrayList<>();
        List<Plate> Plates= new ArrayList<>();
    }

    public Maestros(List<Hardware> hardwares, List<Driver> drivers, List<Hose> hoses, List<Operator> operators, List<Plate> plates) {
        Hardwares = hardwares;
        Drivers = drivers;
        Hoses = hoses;
        Operators = operators;
        Plates = plates;
    }


    public List<Hardware> getHardwares() {
        return Hardwares;
    }

    public void setHardwares(List<Hardware> hardwares) {
        Hardwares = hardwares;
    }

    public List<Driver> getDrivers() {
        return Drivers;
    }

    public void setDrivers(List<Driver> Drivers) {
        this.Drivers = Drivers;
    }

    public List<Hose> getHoses() {
        return Hoses;
    }

    public void setHoses(List<Hose> hoses) {
        Hoses = hoses;
    }

    public List<Operator> getOperators() {
        return Operators;
    }

    public void setOperators(List<Operator> operators) {
        Operators = operators;
    }

    public List<Plate> getPlates() {
        return Plates;
    }

    public void setPlates(List<Plate> plates) {
        Plates = plates;
    }
}
