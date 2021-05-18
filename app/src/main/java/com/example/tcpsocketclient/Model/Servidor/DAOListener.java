package com.example.tcpsocketclient.Model.Servidor;

import java.util.HashMap;

public interface DAOListener {
    void OperacionOk(HashMap<String, Object> res);
    void OperacionError(HashMap<String, Object> res);
}
