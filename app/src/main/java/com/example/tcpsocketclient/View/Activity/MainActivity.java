package com.example.tcpsocketclient.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.View.Fragment.ClientSocketFragment;
import com.example.tcpsocketclient.View.Fragment.ServerSocketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    public static final String SERVER_IP = "192.168.1.15"; //server IP address
    public static final int SERVER_PORT = 2230;

    BottomNavigationView bottomNavigationView;

    //private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void initComponent(){
        bottomNavigationView =findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ClientSocketFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
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
            };

}