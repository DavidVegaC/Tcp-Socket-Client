package com.example.tcpsocketclient.Model.Servidor;

import android.content.Context;

public interface ObjetoDAO {
    String KEY_DATOS_REMOTE = "KEY_DATOS_REMOTE";
    void ejecutar(Context context, DAOListener listener, String operacion, String entidad, String documentoEntrada);
}
