package com.example.tcpsocketclient.View.Fragment;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.DB.CRUDOperations;
import com.example.tcpsocketclient.Storage.DB.MyDatabase;
import com.example.tcpsocketclient.Storage.Preferences.PreferencesHelper;
import com.example.tcpsocketclient.Util.Bluetooth.PrinterBluetooth;
import com.example.tcpsocketclient.Util.CustomProgressDialog;
import com.example.tcpsocketclient.Util.Internet.NetworkUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigurationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewGroup rootView;
    private Spinner spnEscenario;
    private MaterialButton btnGuardarConfiguracion;
    private Activity activityContext ;
    private int idScenario;
    private int  idItemSpinner;

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    public ConfigurationFragment(Activity activityContext) {
        this.activityContext = activityContext;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigurationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigurationFragment newInstance(String param1, String param2) {
        ConfigurationFragment fragment = new ConfigurationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_configuration, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponent();
    }

    private void initComponent() {
        spnEscenario = rootView.findViewById(R.id.spnEscenario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.escenarios_array, R.layout.spinner_theme_1);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_theme_1);
        // Apply the adapter to the spinner
        spnEscenario.setAdapter(adapter);

        idScenario = PreferencesHelper.getIdScene(activityContext);
        spnEscenario.setSelection(idScenario);

        btnGuardarConfiguracion = rootView.findViewById(R.id.btnGuardarConfiguracion);

        btnGuardarConfiguracion = rootView.findViewById(R.id.btnGuardarConfiguracion);

        btnGuardarConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validarDatos()){
                    PreferencesHelper.saveScene(activityContext,idItemSpinner);
                    idScenario = PreferencesHelper.getIdScene(activityContext);
                    if(idScenario==idItemSpinner){
                        Toast.makeText(activityContext, "Configuración de escenario guardada con éxito.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activityContext, "Ocurrió un error.\nIntentarlo nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Boolean validarDatos(){
        Boolean response = false;
        idItemSpinner = spnEscenario.getSelectedItemPosition();
        if(idItemSpinner==0){
            response=true;
            Toast.makeText(activityContext, "Debe seleccionar un escenario.", Toast.LENGTH_SHORT).show();
        }

        return response;
    }


}