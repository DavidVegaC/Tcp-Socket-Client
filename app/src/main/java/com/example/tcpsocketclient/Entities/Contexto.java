package com.example.tcpsocketclient.Entities;

public class Contexto {
    private String Operacion;
    private String Entidad;

    public Contexto(String operacion, String entidad) {
        Operacion = operacion;
        Entidad = entidad;

    }

    public String getOperacion() {
        return Operacion;
    }

    public void setOperacion(String operacion) {
        Operacion = operacion;
    }

    public String getEntidad() {
        return Entidad;
    }

    public void setEntidad(String entidad) {
        Entidad = entidad;
    }

}
