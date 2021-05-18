package com.example.tcpsocketclient.Model.Servidor;



import com.example.tcpsocketclient.Entities.PeticionMobile;
import com.example.tcpsocketclient.Entities.Respuesta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPi {

    @POST("api/transaction")
    Call<Respuesta> postPlates(@Body PeticionMobile body);

}
