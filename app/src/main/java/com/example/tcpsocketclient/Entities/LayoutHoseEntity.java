package com.example.tcpsocketclient.Entities;

import android.widget.LinearLayout;

import com.example.tcpsocketclient.View.Fragment.FormDialogTransaction;

import java.util.ArrayList;
import java.util.List;

public class LayoutHoseEntity {

    public LinearLayout inflater;
    public int idBomba;
    public FormDialogTransaction formDialogTransaction;

    public LayoutHoseEntity(LinearLayout inflater, int idBomba) {
        this.inflater = inflater;
        this.idBomba = idBomba;
    }

    public LayoutHoseEntity(LinearLayout inflater, int idBomba, FormDialogTransaction formDialogTransaction) {
        this.inflater = inflater;
        this.idBomba = idBomba;
        this.formDialogTransaction = formDialogTransaction;
    }

    public LayoutHoseEntity() {
    }

    public LinearLayout getInflater() {
        return inflater;
    }

    public void setInflater(LinearLayout inflater) {
        this.inflater = inflater;
    }

    public int getIdBomba() {
        return idBomba;
    }

    public void setIdBomba(int idBomba) {
        this.idBomba = idBomba;
    }

    public FormDialogTransaction getFormDialogTransaction() {
        return formDialogTransaction;
    }

    public void setFormDialogTransaction(FormDialogTransaction formDialogTransaction) {
        this.formDialogTransaction = formDialogTransaction;
    }
}
