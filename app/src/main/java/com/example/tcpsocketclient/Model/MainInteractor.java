package com.example.tcpsocketclient.Model;

import android.content.Context;
import android.util.Log;

import com.example.tcpsocketclient.Entities.Maestros;
import com.example.tcpsocketclient.Entities.Transaction;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.Model.Servidor.DAOListener;
import com.example.tcpsocketclient.Model.Servidor.FabricaDAO;
import com.example.tcpsocketclient.Model.Servidor.FabricaRemoteDAO;
import com.example.tcpsocketclient.Model.Servidor.ObjetoDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainInteractor {

    public final String KEY_LOBTENERMAESTROS = "KEY_LOBTENERMAESTROS";
    public final String KEY_LREGISTROTRANSACCION = "KEY_LREGISTROTRANSACCION";
    public FabricaDAO mFabricaRemote = new FabricaRemoteDAO();
    ObjetoDAO objetoDAORemote = mFabricaRemote.crearObjetoDAO();


    public interface onDetailsFetched{
        void onMainOk(HashMap<String, Object> map);
        void onMainError(HashMap<String, Object> map);
    }

    public void ObtenerMaestros(final Context context, final MainInteractor.onDetailsFetched listener){
        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String documentoEntrada ="";
        //Toast.makeText(context, documentoEntrada, Toast.LENGTH_SHORT).show();
        objetoDAORemote.ejecutar(context, new DAOListener() {
            @Override
            public void OperacionOk(HashMap<String, Object> res) {
                Object jsData = res.get(ObjetoDAO.KEY_DATOS_REMOTE);
                Type maestroType = new TypeToken<Maestros>(){}.getType();
                final Maestros maestros = gson.fromJson(jsData.toString(), maestroType);
                res.put(KEY_LOBTENERMAESTROS, maestros);
                listener.onMainOk(res);
            }
            @Override
            public void OperacionError(HashMap<String, Object> res) {
                String mensajeError = (String) res.get(ObjetoDAO.KEY_DATOS_REMOTE);
                res.put(KEY_LOBTENERMAESTROS, mensajeError);
                listener.onMainError(res);
            }
        }, "ListarMaestrosApp", "Transaction", documentoEntrada);
    }

    public void RegistrarTransacciones(final Context context, final MainInteractor.onDetailsFetched listener, List<TransactionEntity> transactionEntityList){
        final Gson gson = new Gson();
        String documentoEntrada =gson.toJson(transactionEntityList);
        Log.v("Doc.Entrada =",documentoEntrada);
        objetoDAORemote.ejecutar(context, new DAOListener() {
            @Override
            public void OperacionOk(HashMap<String, Object> res) {
                Object jsData = res.get(ObjetoDAO.KEY_DATOS_REMOTE);
                Type stringType = new TypeToken<String>(){}.getType();
                final String mensaje = gson.fromJson(jsData.toString(), stringType);
                res.put(KEY_LREGISTROTRANSACCION, mensaje);
                listener.onMainOk(res);
            }
            @Override
            public void OperacionError(HashMap<String, Object> res) {
                String mensajeError = (String) res.get(ObjetoDAO.KEY_DATOS_REMOTE);
                res.put(KEY_LREGISTROTRANSACCION, mensajeError);
                listener.onMainError(res);
            }
        }, "CrearTransaccionApp", "Transaction", documentoEntrada);

    }

}
