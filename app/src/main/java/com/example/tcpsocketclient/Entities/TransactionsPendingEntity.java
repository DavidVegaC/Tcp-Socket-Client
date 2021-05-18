package com.example.tcpsocketclient.Entities;

import java.util.List;

public class TransactionsPendingEntity {

    public List<TransactionEntity> transactionEntityList;
    public List<String> idsSqliteStringList;

    public TransactionsPendingEntity() {
    }

    public TransactionsPendingEntity(List<TransactionEntity> transactionEntityList, List<String> idsSqliteStringList) {
        this.transactionEntityList = transactionEntityList;
        this.idsSqliteStringList = idsSqliteStringList;
    }
}
