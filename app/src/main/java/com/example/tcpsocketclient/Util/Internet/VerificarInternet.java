package com.example.tcpsocketclient.Util.Internet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Util.Const;


public class VerificarInternet extends AsyncTask<Void, Void, Boolean> {

    /**VARIABLES**/
    private ProgressDialog progressDialog;
    private Context context;
    private Verificar verificar;


    public interface Verificar{
        void ConexionExitosa();
        void ConexionFallida();
    }

    public VerificarInternet(Context context, Verificar verificar){
        this.context = context;
        this.verificar = verificar;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        return new NetworkUtil(context).connectionNetwork(Const.HTTP_SITE, context);
    }

    @Override
    protected void onPreExecute(){

        // preparamos el cuadro de dialogo
        progressDialog = new ProgressDialog(context, R.style.AppThemeAssacDialog);
        progressDialog.setMessage(Const.STR_VERIFY_CONECTION_SERVER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean response){
        super.onPostExecute(response);

        progressDialog.dismiss();

        if (response){
            verificar.ConexionExitosa();
        }else{
            verificar.ConexionFallida();
        }
    }
}
