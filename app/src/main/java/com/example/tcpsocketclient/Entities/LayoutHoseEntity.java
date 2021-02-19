package com.example.tcpsocketclient.Entities;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LayoutHoseEntity {

    public LinearLayout inflater;
    public int idBomba;

    public LayoutHoseEntity(LinearLayout inflater, int idBomba) {
        this.inflater = inflater;
        this.idBomba = idBomba;
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
}
