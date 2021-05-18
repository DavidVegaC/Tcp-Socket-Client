package com.example.tcpsocketclient.Model.Servidor;

import android.content.Context;
import android.widget.Toast;

import com.example.tcpsocketclient.Entities.Contexto;
import com.example.tcpsocketclient.Entities.PeticionMobile;
import com.example.tcpsocketclient.Entities.Respuesta;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteObjetoDAO implements ObjetoDAO {

    //Insertar método con retrofit
    public static final JsonPlaceHolderAPi jsonPlaceHolderAPi = ClienteRetrofit.retrofit.create(JsonPlaceHolderAPi.class);

    @Override
    public void ejecutar(final Context contexto, final DAOListener listener, final String operacion, final String entidad, String documentoEntrada) {

            PeticionMobile request = new PeticionMobile(new Contexto(operacion, entidad), documentoEntrada);

            Call<Respuesta> call = jsonPlaceHolderAPi.postPlates(request);

            call.enqueue(new Callback<Respuesta>() {
                HashMap<String, Object> respuesta = new HashMap<String, Object>();
                @Override
                public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                    if(!response.isSuccessful()){
                        //mJsonTxtView.setText("Codigo"+response.code());
                        Toast.makeText(contexto, "Codigo : "+response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {

                        Respuesta respuestaServicio = response.body();
                        int msgCode = respuestaServicio.getMsg_code();

                        if (msgCode == 1) {
                            String message = respuestaServicio.getMsg_detail();
                            //respuesta.put(AdministradorErrores.KEY_ERROR_CODE, AdministradorErrores.SERVICIO);
                            //respuesta.put(AdministradorErrores.KEY_ERROR_MSG, message);
                            //JSONArray jsData = new JSONArray(response.getString("data"));
                            respuesta.put(KEY_DATOS_REMOTE, message);

                            listener.OperacionError(respuesta);
                        }else if(msgCode == 0){
                            Object json = new JSONTokener(respuestaServicio.getData()).nextValue();
                            if (json instanceof JSONObject) {
                                JSONObject jsData = new JSONObject(respuestaServicio.getData());
                                respuesta.put(KEY_DATOS_REMOTE, jsData);
                            } else { //if (json instanceof JSONArray){
                                //JSONArray jsData = new JSONArray(response.getString("data"));
                                respuesta.put(KEY_DATOS_REMOTE, json);
                            }
                            listener.OperacionOk(respuesta);
                        }


                    } catch (JSONException e) {
                        //respuesta.put(AdministradorErrores.KEY_ERROR_CODE, AdministradorErrores.PARSEO_JSON);
                        //respuesta.put(AdministradorErrores.KEY_ERROR_MSG, e.getMessage());
                        //listener.OperacionError(respuesta);
                        respuesta.put(KEY_DATOS_REMOTE,  " "+e.getMessage());
                        listener.OperacionError(respuesta);
                        //e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Respuesta> call, Throwable t) {
                    //Toast.makeText(contexto, "Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                    //respuesta.put(KEY_DATOS_REMOTE, "Problemas con el servidor. Comunicarse con Informática.");
                    respuesta.put(KEY_DATOS_REMOTE,  "Error: "+t.getMessage());
                    listener.OperacionError(respuesta);
                }
            });

    }
}
