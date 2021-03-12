package com.example.tcpsocketclient.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Util.CustomAnimation;
import com.example.tcpsocketclient.Util.NavigationFragment;
import com.example.tcpsocketclient.View.Fragment.ClientSocketFragment;
import com.example.tcpsocketclient.View.Fragment.ServerSocketFragment;
import com.example.tcpsocketclient.View.Fragment.TicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //ADDITIONAL CODE
    private LinearLayout btnEstacion;
    private LinearLayout btnDuplicadoTicket;
    private ClientSocketFragment clientSocketFragment = null;
    private TicketFragment ticketFragment = null;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void initComponent(){
        btnEstacion = findViewById(R.id.btnEstacion);
        btnDuplicadoTicket = findViewById(R.id.btnDuplicadoTicket);

        btnEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEmbeddedFragment();
            }
        });

        btnDuplicadoTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTicketFragment();
            }
        });

        goToEmbeddedFragment();
        //bottomNavigationView =findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ClientSocketFragment()).commit();
    }

    public void goToTicketFragment(){
        clientSocketFragment = null;
        ticketFragment=new TicketFragment();
        NavigationFragment.addFragment(null, ticketFragment, "ticketFragment", this,
                R.id.main_activity_content, false, CustomAnimation.LEFT_RIGHT);

    }

    public void goToEmbeddedFragment(){
        ticketFragment = null;
        clientSocketFragment = new ClientSocketFragment();
        NavigationFragment.addFragment(null, clientSocketFragment, "connecctionEmbFragment", this,
                R.id.main_activity_content, false, CustomAnimation.LEFT_RIGHT);
    }

    /*private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_client_socket:
                            selectFragment = new ClientSocketFragment();
                            break;
                        case R.id.nav_server_socket:
                            selectFragment = new ServerSocketFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectFragment).commit();
                    return true;
                }
            };*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        drawer_layout.closeDrawer(GravityCompat.START);
//
        return true;
    }
}