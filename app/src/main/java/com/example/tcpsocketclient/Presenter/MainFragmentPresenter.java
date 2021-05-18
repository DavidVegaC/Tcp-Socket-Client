package com.example.tcpsocketclient.Presenter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.tcpsocketclient.Base.PresenterFragment;
import com.example.tcpsocketclient.Entities.Transaction;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.Interface.MainView;
import com.example.tcpsocketclient.Model.MainInteractor;
import com.example.tcpsocketclient.Util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragmentPresenter extends PresenterFragment implements MainInteractor.onDetailsFetched {

    public final String KEY_LOBTENERMAESTROS = "KEY_LOBTENERMAESTROS";
    public final String KEY_LREGISTROTRANSACCION = "KEY_LREGISTROTRANSACCION";

    private MainView view;
    private MainInteractor mainInteractor;

    public MainFragmentPresenter(@NonNull MainView view, MainInteractor mainInteractor){
        this.view = view;
        this.mainInteractor = mainInteractor;
    }

    public void ObtenerMaestros(Activity activity){
        mainInteractor.ObtenerMaestros(activity.getApplicationContext(),this);
    }

    public void RegistrarTransacciones(Activity activity, TransactionEntity transaction){
        transaction.setImageDecodeString(Utils.getStringImagen(activity, Uri.parse(transaction.imageUri)));

        List<TransactionEntity> transactionEntities = new ArrayList<TransactionEntity>();
        transactionEntities.add(transaction);

        mainInteractor.RegistrarTransacciones(activity.getApplicationContext(),this, transactionEntities);
    }

    @Override
    public void onMainOk(HashMap<String, Object> map) {
        if (map.containsKey(mainInteractor.KEY_LOBTENERMAESTROS)) {
            map.put(KEY_LOBTENERMAESTROS, map.get(mainInteractor.KEY_LOBTENERMAESTROS));
        }else if (map.containsKey(mainInteractor.KEY_LREGISTROTRANSACCION)) {
            map.put(KEY_LREGISTROTRANSACCION, map.get(mainInteractor.KEY_LREGISTROTRANSACCION));
        }
        view.onActivityFragmentOk(map);
    }

    @Override
    public void onMainError(HashMap<String, Object> map) {
        if (map.containsKey(mainInteractor.KEY_LOBTENERMAESTROS)) {
            map.put(KEY_LOBTENERMAESTROS, map.get(mainInteractor.KEY_LOBTENERMAESTROS));
        }else if (map.containsKey(mainInteractor.KEY_LREGISTROTRANSACCION)) {
            map.put(KEY_LREGISTROTRANSACCION, map.get(mainInteractor.KEY_LREGISTROTRANSACCION));
        }
        view.onActivityFragmentError(map);
    }

}
