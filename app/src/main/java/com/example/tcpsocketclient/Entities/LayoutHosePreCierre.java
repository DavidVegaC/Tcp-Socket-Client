package com.example.tcpsocketclient.Entities;

import android.widget.LinearLayout;

import java.util.List;

public class LayoutHosePreCierre {
    public String hoseName;
    public List<TransactionEntity> transactionEntities;
    public String totalM1;

    public LayoutHosePreCierre(List<TransactionEntity> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }

    public LayoutHosePreCierre(String hoseName,List<TransactionEntity> transactionEntities, String totalM1) {
        this.hoseName=hoseName;
        this.transactionEntities = transactionEntities;
        this.totalM1 = totalM1;
    }

    public String getHoseName() {
        return hoseName;
    }

    public void setHoseName(String hoseName) {
        this.hoseName = hoseName;
    }

    public List<TransactionEntity> getTransactionEntities() {
        return transactionEntities;
    }

    public void setTransactionEntities(List<TransactionEntity> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }

    public String getTotalM1() {
        return totalM1;
    }

    public void setTotalM1(String totalM1) {
        this.totalM1 = totalM1;
    }
}
