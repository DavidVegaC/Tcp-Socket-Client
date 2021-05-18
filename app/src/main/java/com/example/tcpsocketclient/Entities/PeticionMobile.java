package com.example.tcpsocketclient.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeticionMobile {
    public static final String CONTEXTO = "Contexto";
    public static final String DOCUMENTO_ENTRADA = "DocumentoEntrada";

    @Expose
    @SerializedName(CONTEXTO)
    private Contexto contexto;

    @Expose
    @SerializedName(DOCUMENTO_ENTRADA)
    private String documentoEntrada;

    public PeticionMobile(Contexto contexto, String documentoEntrada) {
        super();
        this.contexto = contexto;
        this.documentoEntrada = documentoEntrada;
    }
}

