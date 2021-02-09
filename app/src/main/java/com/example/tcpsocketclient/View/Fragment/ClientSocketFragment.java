package com.example.tcpsocketclient.View.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.TcpClient;
import com.example.tcpsocketclient.View.Activity.MainActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import static android.Manifest.permission_group.LOCATION;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientSocketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientSocketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewGroup rootView;

    private EditText ed1, edIP, edPort;
    private Button bt1, bt2, bt3;
    private TextView tv1;
    private String mensaje = "";
    TcpClient mTcpClient;
    private boolean conexion = false;
    private String mensajeError = "";
    private String strDireccionIP = "";
    private int intPORT = 0;
    private WifiManager wifi;

    public ClientSocketFragment() {
        // Required empty public constructor
    }

    public static ClientSocketFragment newInstance(String param1, String param2) {
        ClientSocketFragment fragment = new ClientSocketFragment();
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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_client_socket, container, false);
        initComponent();
        return rootView;
    }

    private void initComponent() {
        ed1 = (EditText) rootView.findViewById(R.id.ed1);
        edIP = (EditText) rootView.findViewById(R.id.edIP);
        edPort = (EditText) rootView.findViewById(R.id.edPort);

        bt1 = (Button) rootView.findViewById(R.id.SendButton);
        bt2 = (Button) rootView.findViewById(R.id.CloseButton);
        bt3 = (Button) rootView.findViewById(R.id.ConnectedButton);
        tv1 = (TextView) rootView.findViewById(R.id.textView2);
        tv1.setMovementMethod(new ScrollingMovementMethod());
        bt2.setEnabled(false);
        //new ConnectTask().execute("");



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myFancyMethod(v);
                //sendMessage();
                //Toast.makeText(MainActivity.this, "pasooo111", Toast.LENGTH_SHORT).show();

                sendMessage();
                //tv1.setText(bytesToHex(HEX_ARRAY));


            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myFancyMethod(v);
                //closeTCP();
                //Toast.makeText(MainActivity.this, "pasooo2222", Toast.LENGTH_SHORT).show();
                closeConecction();
                bt3.setEnabled(true);
                bt2.setEnabled(false);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myFancyMethod(v);
                //closeTCP();
                //Toast.makeText(MainActivity.this, "pasooo2222", Toast.LENGTH_SHORT).show();
                //

                //isConnectedWifi(rootView.getContext());
                //Log.d("williams","hola" + estadoWifi + " - " + wifi.getConnectionInfo().getSSID() +"-"+wifi.getConnectionInfo().getMacAddress());

                //getLocalIpAddress();
                /*if(edIP.getText().toString().trim().equals("") || edPort.getText().toString().trim().equals("")){
                    Toast.makeText(rootView.getContext(), "Debe completar tanto la DirecciónIP como el Puerto.", Toast.LENGTH_SHORT).show();
                }else{
                    strDireccionIP =edIP.getText().toString().trim();
                    intPORT =Integer.parseInt(edPort.getText().toString().trim());
                    new ConnectTask().execute("");
                    bt3.setEnabled(false);
                    bt2.setEnabled(true);
                    edIP.setEnabled(false);
                    edPort.setEnabled(false);
                }*/


                    if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(),
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }else{
                        setWIFIActive();
                    }
                }
        });
    }




    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    //publishProgress(message);
                    /*String texto = tv1.getText().toString();
                    tv1.setText(texto + " " + message);*/
                    //Toast.makeText(, "El mensaje está vacio.", Toast.LENGTH_SHORT).show();
                    //Log.d("Daviddd" , message);
                    //mensaje = mensaje + " " +message;
                    //;
                    if(message == null){
                        conexion = false;
                        mensajeError = mTcpClient.msgError;
                    }else{
                        conexion = true;
                        mensaje = " " +message;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            //tv1.setText(mensaje);

                            if(conexion){
                                tv1.append(mensaje);
                            }else{
                                Toast.makeText(rootView.getContext(),mensajeError , Toast.LENGTH_SHORT).show();
                                closeConecction();
                                edIP.setEnabled(true);
                                edPort.setEnabled(true);
                                bt3.setEnabled(true);
                                bt2.setEnabled(false);
                            }
                        }
                    });
                }
            },strDireccionIP,intPORT);
            mTcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
        }

    }


    public void setWIFIActive(){
        wifi = (WifiManager) rootView.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifi.getConnectionInfo();
        boolean estadoWifi = wifi.isWifiEnabled();

        if (estadoWifi) {
            //String redSSID = ;

            int ipAddress = connectionInfo.getIpAddress();
            String holaaa = connectionInfo.toString();

            String ipString = Formatter.formatIpAddress(ipAddress);

            if (connectionInfo.getSSID().equals("\"EMBEDDED DEMO\"")) {

                        /*WifiConfiguration conf = new WifiConfiguration();
                        conf.SSID = redSSID;
                        conf.wepKeys[0] = "\"123456789\"";
                        conf.wepTxKeyIndex = 0;
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        conf.priority = 1999999999;
                        wifi.addNetwork(conf);
                        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(rootView.getContext(), "No pasoooo",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<WifiConfiguration> list = wifi.getConfiguredNetworks();
                        for( WifiConfiguration i : list ) {
                            Log.d("diegooo", "hola -"+ i.SSID);
                            if(i.SSID != null && i.SSID.equals(redSSID)) {
                                wifi.disconnect();
                                wifi.enableNetwork(i.networkId, true);
                                wifi.reconnect();
                                break;
                            }
                        }*/
                Toast.makeText(rootView.getContext(), "Conectado a red WIFI EMBEDDED DEMO.IP="+ipString,
                        Toast.LENGTH_LONG).show();
                Log.d("davidddd","hola -" + wifi.isWifiEnabled() + " - " + connectionInfo.getSSID());

                /*if(edIP.getText().toString().trim().equals("") || edPort.getText().toString().trim().equals("")){
                    Toast.makeText(rootView.getContext(), "Debe completar tanto la DirecciónIP como el Puerto.", Toast.LENGTH_SHORT).show();
                }else{
                    strDireccionIP =edIP.getText().toString().trim();
                    intPORT =Integer.parseInt(edPort.getText().toString().trim());
                    new ConnectTask().execute("");
                    bt3.setEnabled(false);
                    bt2.setEnabled(true);
                    edIP.setEnabled(false);
                    edPort.setEnabled(false);
                }*/


            }else{
                Toast.makeText(rootView.getContext(), "Red Desconocida. Debe conectarse a la red EMBEDDED DEMO. IP:"+ipString,
                        Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(rootView.getContext(), "No existe conectividad a WIFI",
                    Toast.LENGTH_LONG).show();
        }

        isConnectedWifi(rootView.getContext());
    }

    public void sendMessage(){
        String message = ed1.getText().toString().trim();

        if (mTcpClient != null) {
            if(!message.equals("")){
                mTcpClient.sendMessage(message);
            }else{
                Toast.makeText(rootView.getContext(), "El mensaje está vacio.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(rootView.getContext(), "No existe conexión abierta.", Toast.LENGTH_SHORT).show();
        }

    }

    public void closeConecction(){
        if (mTcpClient != null) {
            try {
                mTcpClient.stopClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mTcpClient= null;
        }else{
            Toast.makeText(rootView.getContext(), "No existe conexión abierta.", Toast.LENGTH_SHORT).show();
        }
    }

    public void isConnectedWifi(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Log.d("daviddd",networkInfo.getExtraInfo());
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("MIAPP", "Estás online");

            Log.d("MIAPP", " Estado actual: " + networkInfo.getState());

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // Estas conectado a un Wi-Fi
                Log.d("MIAPP", " Nombre red Wi-Fi: " + networkInfo.getReason());
                Toast.makeText(rootView.getContext(), "Existe conectividad a WIFI",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Existe conectividad con datos moviles.",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            Log.d("MIAPP", "Estás offline");
            Toast.makeText(rootView.getContext(), "No existe conectividad a WIFI",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void getLocalIpAddress() {

        try {
            for (Enumeration< NetworkInterface > en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration <InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        Log.d("IP: ", inetAddress.getHostAddress().toString());
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("iperror:", ex.toString());
        }
    }


}