package com.example.tcpsocketclient.View.Fragment;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private int networkId = 0;

    private final String nombreRedSocket = "EMBEDDED";
    //private final String nombreRedSocket = "TP-LINK_AP_F2D8";

    private String IPLocal= "";

    private LocationManager ubicacion;


    //variables prueba con handler
    private byte[] bufferTemporal = new byte[300];
    private ConnectedThread mConnectedThread;
    Handler handlerSocket;
    Thread thread;
    Handler handler = new Handler();
    final int handlerState = 0;

    //Datos usados para la recepción
    private int indByte=0;
    private int longitudTemp=0;
    int longitudTramaRecepcion = 0;
    private int[] bufferRecepcion = new int[300];

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

                //sendMessage();
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
                edIP.setEnabled(true);
                edPort.setEnabled(true);
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

                if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                } else {

                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){
                        if(estadoGPS()){
                            setWIFIActive();
                        }else{
                            Toast.makeText(rootView.getContext(), "Debe activar su ubicación.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        setWIFIActive();
                    }
                    //boolean connect= connectToHotspot("EMBEDDED DEMO", "123456789");
                }
            }
        });

        //prueba con Handler y Socket
        /*handlerSocket = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {
                    //if message is what we want
                    if(recepciontTwoEasyFuel((byte[])msg.obj,msg.arg1)){           //Modificar metodo para nuevo protocolo
                        //procesarTramaEasyFuel(msg.arg1);
                        Toast.makeText(rootView.getContext(),byteArrayToHexString(bufferRecepcion,longitudTemp) + "\n",Toast.LENGTH_LONG);
                    }
                }
            }
        };*/

        //actualizarTiempo();
        //crearConexionBTSocket();

    }

    public class ConnectThread extends AsyncTask<String, String, TcpClient> {
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
                    if (message == null) {
                        conexion = false;
                        mensajeError = mTcpClient.msgError;
                    } else {
                        conexion = true;
                        mensaje = " " + message;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            //tv1.setText(mensaje);

                            if (conexion) {
                                tv1.append(mensaje);
                            } else {
                                Toast.makeText(rootView.getContext(), mensajeError, Toast.LENGTH_SHORT).show();
                                closeConecction();
                                edIP.setEnabled(true);
                                edPort.setEnabled(true);
                                bt3.setEnabled(true);
                                bt2.setEnabled(false);
                            }
                        }
                    });
                }
            }, strDireccionIP, intPORT,IPLocal);
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

    public void setWIFIActive() {
        wifi = (WifiManager) rootView.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        boolean estadoWifi = wifi.isWifiEnabled();

        if (estadoWifi) {
            WifiInfo connectionInfo = wifi.getConnectionInfo();

            if (connectionInfo.getSSID().equals("\"" + nombreRedSocket + "\"")) {
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);
                IPLocal=ipString;

                        /*WifiConfiguration conf = new WifiConfiguration();
                        conf.SSID = redSSID;
                        conf.wepKeys[0] = "\"123456789\"";
                        conf.wepTxKeyIndex = 0;
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        conf.priority = 1999999999;
                        wifi.addNetwork(conf);
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
                //Toast.makeText(rootView.getContext(), "Conectado a red WIFI "+nombreRedSocket+".",Toast.LENGTH_LONG).show();
                //Log.d("davidddd","hola -" + wifi.isWifiEnabled() + " - " + connectionInfo.getSSID());


                //cm.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "android.net.conn.CONNECTIVITY_CHANGE");
                //ºcm.set

                if (isOutputWifi(rootView.getContext())) {
                    connectSocket();
                } else {
                    Toast.makeText(rootView.getContext(), "Debe desactivar sus datos móviles.", Toast.LENGTH_LONG).show();
                }



            } else {
                Toast.makeText(rootView.getContext(), "No se detectó conexión a la red " + nombreRedSocket + ".",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(rootView.getContext(), "Activar WIFI y conectarse a la red " + nombreRedSocket + ".",
                    Toast.LENGTH_LONG).show();
        }


    }

    public void connectSocket() {
        if (edIP.getText().toString().trim().equals("") || edPort.getText().toString().trim().equals("")) {
            Toast.makeText(rootView.getContext(), "Debe completar tanto la DirecciónIP como el Puerto.", Toast.LENGTH_SHORT).show();
        } else {
            strDireccionIP = edIP.getText().toString().trim();
            intPORT = Integer.parseInt(edPort.getText().toString().trim());
            new ConnectThread().execute("");
            bt3.setEnabled(false);
            bt2.setEnabled(true);
            edIP.setEnabled(false);
            edPort.setEnabled(false);
        }
    }

    public void sendMessage() {
        String message = ed1.getText().toString().trim();

        if (mTcpClient != null) {
            if (!message.equals("")) {
                mTcpClient.sendMessage(message);
            } else {
                Toast.makeText(rootView.getContext(), "El mensaje está vacio.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(rootView.getContext(), "No existe conexión abierta.", Toast.LENGTH_SHORT).show();
        }

    }

    public void closeConecction() {
        if (mTcpClient != null) {
            try {
                mTcpClient.stopClient();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mTcpClient = null;
            tv1.setText("");
        } else {
            Toast.makeText(rootView.getContext(), "No existe conexión abierta.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOutputWifi(Context context) {
        boolean retorno = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Log.d("daviddd",networkInfo.getExtraInfo());
        if (networkInfo != null && networkInfo.isConnected()) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // Estas conectado a un Wi-Fi
                //Toast.makeText(rootView.getContext(), "Existe conectividad a WIFI", Toast.LENGTH_LONG).show();
                retorno = true;
            }

        } else {
            Log.d("MIAPP", "Estás offline");
            Toast.makeText(rootView.getContext(), "No existe conectividad a Internet",
                    Toast.LENGTH_LONG).show();
        }

        return retorno;
    }

    public void getLocalIpAddress() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
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

    private boolean estadoGPS(){
        ubicacion = (LocationManager)rootView.getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if(!ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return false;
        }

        return true;
    }


    //prueba de conectarse por WIFI
    boolean connectToHotspot(String netSSID, String netPass) {
        wifi = (WifiManager) rootView.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConf = new WifiConfiguration();

        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
        }

        List<ScanResult> scanResultList = wifi.getScanResults();
        for (ScanResult result : scanResultList) {

            removeWifiNetwork(result.SSID);
            if (result.SSID.equals(netSSID)) {

                String mode = getSecurityMode(result);

                if (mode.equalsIgnoreCase("OPEN")) {

                    wifiConf.SSID = "\"" + netSSID + "\"";
                    wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    networkId = wifi.addNetwork(wifiConf);
                    wifi.enableNetwork(networkId, true);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    if (!wifi.isWifiEnabled())
                        wifi.setWifiEnabled(true);
                    return true;

                } else if (mode.equalsIgnoreCase("WEP")) {

                    wifiConf.SSID = "\"" + netSSID + "\"";
                    wifiConf.wepKeys[0] = "\"" + netPass + "\"";
                    wifiConf.wepTxKeyIndex = 0;
                    wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    networkId = wifi.addNetwork(wifiConf);
                    wifi.enableNetwork(networkId, true);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    if (!wifi.isWifiEnabled())
                        wifi.setWifiEnabled(true);
                    return true;

                } else {
                    wifiConf.SSID = "\"" + netSSID + "\"";
                    wifiConf.preSharedKey = "\"" + netPass + "\"";
                    wifiConf.hiddenSSID = true;
                    wifiConf.priority = 40;
                    wifiConf.status = WifiConfiguration.Status.ENABLED;
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                    networkId = wifi.addNetwork(wifiConf);
                    if (networkId > 0) {
                        wifi.disconnect();
                        wifi.enableNetwork(networkId, true);
                        wifi.reconnect();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                        if (!wifi.isWifiEnabled())
                            wifi.setWifiEnabled(true);
                        return true;
                    }
                    return false;
                }
            }
        }

        return false;
    }

    void removeNetwork() {
        wifi.removeNetwork(networkId);
        wifi.saveConfiguration();
    }

    private String getSecurityMode(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] modes = {"WPA", "EAP", "WEP"};
        for (int i = modes.length - 1; i >= 0; i--) {
            if (cap.contains(modes[i])) {
                return modes[i];
            }
        }
        return "OPEN";
    }

    private void removeWifiNetwork(String ssid) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
        if (configs != null) {
            for (WifiConfiguration config : configs) {
                if (config.SSID.contains(ssid)) {
                    wifi.disableNetwork(config.networkId);
                    wifi.removeNetwork(config.networkId);
                }
            }
        }
        wifi.saveConfiguration();
    }

    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(Socket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+e.getMessage(),"1");
                Log.e("Crear Hilo","método: ConnectedThread / mensaje: "+e.getMessage(),e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            int bytes;
            byte[] buffer = new byte[256];

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bufferTemporal = new byte[300];
                    bytes = mmInStream.read(bufferTemporal);        	//read bytes from input buffer
                    //bytes = mmInStream.read(buffer);
                    //Log.v("1. mmInStream bytes", "" + bytes);
                    //Log.v("2. buffer vacio 256", "" + buffer);
                    //String readMessage = new String(buffer, 0, bytes);
                    //Log.v("buffer", "" + readMessage);
                    // Send the obtained bytes to the UI Activity via handler
                    //Log.v("", "" + byteArrayToHexString(bufferTemporal,bytes));

                    //bluetoothIn.obtainMessage(handlerState, bytes, -1, bufferTemporal).sendToTarget();


                    //bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    //Log.v("CATCH", "" + e.getMessage());
                    //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.run / mensaje: "+e.getMessage(),"1");
                    Log.e("Crear Hilo","método: ConnectedThread.run / mensaje: "+e.getMessage(),e);
                    break;
                }
            }
        }


    }

    private boolean recepciontTwoEasyFuel(byte[] temporal, int cantidad){
        //Log.v("", "" + byteArrayToHexString(temporal,cantidad));
        boolean hasPacket=false;
        int datoTemporal;
        int i;

        if(cantidad>0) {
            for(i=0;i<cantidad;i++){
                datoTemporal=0xFF&temporal[i];

                if(indByte==0){
                    if(datoTemporal==0x02){
                        bufferRecepcion[indByte]=datoTemporal;
                        indByte++;
                    }
                    else{
                        longitudTemp=0;
                        indByte=0;
                    }
                }
                else{
                    if(indByte==1 || indByte==2){
                        if(indByte==1){
                            longitudTemp = datoTemporal;
                            bufferRecepcion[indByte]=datoTemporal;
                            indByte++;
                        }
                        else{
                            if(indByte==2){
                                longitudTemp = (longitudTemp)|(((short)datoTemporal)<<8);
                                //GUARDAR LONGITUD
                                longitudTramaRecepcion = longitudTemp;
                                Log.v("", "" + "Longitud   " + longitudTramaRecepcion);
                                bufferRecepcion[indByte] = datoTemporal;
                                indByte++;
                                if((longitudTemp>500)||(longitudTemp<8)){
                                    longitudTemp = 0;
                                    indByte = 0;
                                }
                            }
                        }
                    }
                    else{
                        if(indByte>2){
                            bufferRecepcion[indByte] = datoTemporal;
                            indByte++;
                            if(indByte==longitudTemp){
                                if(datoTemporal==0x03){
                                    hasPacket=true;
                                    Log.v("", "" + "Recepcion   " + byteArrayToHexString(bufferRecepcion,longitudTemp));
                                    //mMessageListener.messageReceived(byteArrayToHexString(bufferRecepcion,longitudTemp) + "\n");
                                    //mConnectedThread.write(EmbeddedPtcl.b_ext_configuracion);
                                    //showText(""+ byteArrayToHexString(bufferRecepcion,longitudTemp));
                                    indByte=0;
                                    longitudTemp=0;
                                }
                                else{
                                    indByte=0;
                                    longitudTemp=0;
                                    bufferRecepcion = new int[300];
                                    //ALERTAR QUE LA TRAMA ES INVÁLIDA
                                }
                            }
                        }
                    }
                }

            }
        }
        else{
            //NO SE RECIBIERON DATOS
            Log.v("recepcionEasyFuel", "NO SE RECIBIERON DATOS");
        }

        return hasPacket;
    }

    private static String byteArrayToHexString(final int[] bytes, int cantidad) {
        StringBuilder sb = new StringBuilder();
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            sb.append(String.format("%02x", bytes[i]&0xff));
            //sb.append((char)(0xFF&bytes[i]));
        }
        return sb.toString();
    }

    private void actualizarTiempo(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(1000);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                            Log.d("Handler: ",date);
                            //txtFechaMF.setText(date);
                        }
                    });
                }
            }
        });
        thread.start();
    }
    //CREA UNA NUEVA INSTANCIA DE BLUETOOTH SOCKET
    /*private void crearConexionBTSocket(){
        //Get MAC address from the Common class
        // address = Const.address;
        address = PreferencesHelper.getBluetoothAddress(getActivity());


        //Create device and set the MAC address
        Log.v("Dirección", "Conexión al dispositivo con dirección : " + address);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
            Toast.makeText(getActivity(), "Creación de socket exitosa con el dispositivo: "+address, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
            Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+address + " falló", Toast.LENGTH_LONG).show();
            mListener.goToBluetoothConfiguration(); //Go back to the main view
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

        } catch (IOException e) {
            try
            {
                btSocket.close();
                Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
                mListener.goToBluetoothConfiguration(); //Go back to the main view
                Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+address + " falló", Toast.LENGTH_LONG).show();
            } catch (IOException e2)
            {
                Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
                mListener.goToBluetoothConfiguration(); //Go back to the main view
                Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+address + " falló", Toast.LENGTH_LONG).show();
                //insert code to deal with this
            }
        }

    }*/

}