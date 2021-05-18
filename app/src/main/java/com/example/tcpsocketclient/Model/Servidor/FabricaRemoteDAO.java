package com.example.tcpsocketclient.Model.Servidor;

public class FabricaRemoteDAO implements FabricaDAO {

    @Override
    public ObjetoDAO crearObjetoDAO() {
        return new RemoteObjetoDAO();
    }
}