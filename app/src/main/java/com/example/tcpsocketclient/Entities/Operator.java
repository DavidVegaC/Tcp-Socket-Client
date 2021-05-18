package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Operator implements Serializable {

    @Expose
    @SerializedName("OperatorKey")
    public String OperatorKey;

    @Expose
    @SerializedName("OperatorName")
    public String OperatorName;

    public Operator(String operatorKey, String operatorName) {
        OperatorKey = operatorKey;
        OperatorName = operatorName;
    }

    public String getOperatorKey() {
        return OperatorKey;
    }

    public void setOperatorKey(String operatorKey) {
        OperatorKey = operatorKey;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }
}
