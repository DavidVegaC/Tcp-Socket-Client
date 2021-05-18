package com.example.tcpsocketclient.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.Preferences.PreferencesHelper;
import com.example.tcpsocketclient.Util.CustomAnimation;
import com.example.tcpsocketclient.Util.NavigationFragment;
import com.example.tcpsocketclient.View.Fragment.EscenarioEmbeddedAnillosLLaverosFragment;
import com.example.tcpsocketclient.View.Fragment.ConfigurationFragment;
import com.example.tcpsocketclient.View.Fragment.EscenarioEmbeddedFragment;
import com.example.tcpsocketclient.View.Fragment.EscenarioSinEmbeddedFragment;
import com.example.tcpsocketclient.View.Fragment.PreCierreFragment;
import com.example.tcpsocketclient.View.Fragment.TicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private int idScenario;

    //ADDITIONAL CODE
    private LinearLayout btnEstacion;
    private LinearLayout btnDuplicadoTicket;
    private LinearLayout btnConfigurarEstacion = null;
    private LinearLayout btnPreCierre;
    public EscenarioEmbeddedAnillosLLaverosFragment escenarioEmbeddedAnillosLLaverosFragment = null;
    public EscenarioSinEmbeddedFragment escenarioSinEmbeddedFragment = null;
    public EscenarioEmbeddedFragment escenarioEmbeddedFragment = null;
    public TicketFragment ticketFragment = null;
    public PreCierreFragment preCierreFragment = null;
    private Activity activityContext =this;

    private ConfigurationFragment configurationFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void initComponent(){
        btnEstacion = findViewById(R.id.btnEstacion);
        btnDuplicadoTicket = findViewById(R.id.btnDuplicadoTicket);
        btnConfigurarEstacion = findViewById(R.id.btnConfigurarEstacion);
        btnPreCierre = findViewById(R.id.btnPreCierre);

        escenarioSinEmbeddedFragment = new EscenarioSinEmbeddedFragment(this);
        ticketFragment = new TicketFragment(this);
        preCierreFragment =  new PreCierreFragment(this);

        idScenario = PreferencesHelper.getIdScene(activityContext);

        btnEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idScenario = PreferencesHelper.getIdScene(activityContext);
                if(idScenario == 0){
                    goToConfigurationFragment();
                }else{
                    goToEmbeddedFragment();
                }
            }
        });

        btnDuplicadoTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idScenario = PreferencesHelper.getIdScene(activityContext);
                if(idScenario == 0){
                    goToConfigurationFragment();
                }else{
                    goToTicketFragment();
                }
            }
        });

        btnConfigurarEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToConfigurationFragment();
            }
        });

        btnPreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idScenario = PreferencesHelper.getIdScene(activityContext);
                if(idScenario == 0){
                    goToConfigurationFragment();
                }else{
                    goToPreCierreFragment();
                }
            }
        });


        if(idScenario == 0){
            goToConfigurationFragment();
        }else{
            goToEmbeddedFragment();
        }
        //bottomNavigationView =findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new EscenarioEmbeddedAnillosLLaverosFragment()).commit();
    }

    public void goToTicketFragment(){
        ticketFragment.setEscenario(idScenario);
        NavigationFragment.addFragment(null, ticketFragment, "ticketFragment", this,
                R.id.main_activity_content, false, CustomAnimation.LEFT_RIGHT);
    }

    public void goToEmbeddedFragment(){
        Fragment selectFragment = null;
        String tag= "";
        switch (idScenario){
            case 1:
                escenarioEmbeddedAnillosLLaverosFragment = new EscenarioEmbeddedAnillosLLaverosFragment();
                selectFragment = escenarioEmbeddedAnillosLLaverosFragment;
                tag ="connecctionEmbAnilloLlaveroFragment";
                break;
            case 2:
                escenarioEmbeddedFragment = new EscenarioEmbeddedFragment();
                selectFragment = escenarioEmbeddedFragment;
                tag ="connecctionEmbFragment";
                break;
            case 3:
                selectFragment = escenarioSinEmbeddedFragment;
                tag ="appSinEmbFragment";
                break;
        }

        NavigationFragment.addFragment(null, selectFragment, tag, this,
                R.id.main_activity_content, false, CustomAnimation.LEFT_RIGHT);

    }

    public void goToConfigurationFragment(){
        configurationFragment =  new ConfigurationFragment(activityContext);
        NavigationFragment.addFragment(null, configurationFragment, "ConfigurationFragment", this,
                R.id.main_activity_content, false, CustomAnimation.LEFT_RIGHT);
    }

    public void goToPreCierreFragment(){
        preCierreFragment.setEscenario(idScenario);
        NavigationFragment.addFragment(null, preCierreFragment, "PreCierreFragment", this,
                R.id.main_activity_content, false, CustomAnimation.LEFT_RIGHT);
    }

    //1 . ACTIVA EL ADAPTADOR DE NFC PARA QUE ESTÉ EN MODO ESCUCHA DE DISPOSITIVOS
    public void enableForegroundDispatchSystem() {

        if(escenarioEmbeddedAnillosLLaverosFragment !=null){
            Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            IntentFilter[] intentFilters = new IntentFilter[]{};

            escenarioEmbeddedAnillosLLaverosFragment.mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

        }

    }

    //2 . DESACTIVA EL ADAPTADOR DE NFC PARA QUE ESTÉ EN MODO ESCUCHA DE DISPOSITIVOS
    private void disableForegroundDispatchSystem() {
        escenarioSinEmbeddedFragment.mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Metodo llamado cuando se produce un nuevo inten, es decir,
        //cuando el cliente acerca una tag al dispositivo
        Log.v("TAG", "REGISTRO NUEVO INTENT");
        if (escenarioSinEmbeddedFragment.mNfcAdapter != null){
            escenarioSinEmbeddedFragment.processIntent(intent);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Es necesario que la actividad se desarrolle en segundo
        //plano o se producirá una excepción
        //setupForegroundDispatch(this, mNfcAdapter);
        idScenario = PreferencesHelper.getIdScene(activityContext);

        if(idScenario!=0){
            switch (idScenario){
                case 1:
                    if(escenarioEmbeddedAnillosLLaverosFragment != null) {
                        if (escenarioEmbeddedAnillosLLaverosFragment.mNfcAdapter != null) {
                            //disableForegroundDispatchSystem();
                            escenarioEmbeddedAnillosLLaverosFragment.mNfcAdapter.disableForegroundDispatch(this);
                        }
                    }
                    break;
                case 2:
                    break;
                case 3:
                    if(escenarioSinEmbeddedFragment !=null) {
                        if (escenarioSinEmbeddedFragment.mNfcAdapter != null) {
                            //disableForegroundDispatchSystem();
                            escenarioSinEmbeddedFragment.disableForegroundDispatchSystem();
                        }
                    }
                    break;
            }

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        drawer_layout.closeDrawer(GravityCompat.START);
//
        return true;
    }
}