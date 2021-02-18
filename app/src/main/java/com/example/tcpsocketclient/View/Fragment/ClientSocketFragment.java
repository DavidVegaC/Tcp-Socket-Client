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

import androidx.annotation.Nullable;
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

import com.example.tcpsocketclient.ConnectTask;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.TcpClient;
import com.example.tcpsocketclient.Util.Internet.NetworkUtil;
import com.example.tcpsocketclient.Util.Wifi.EmbeddedPtcl;
import com.example.tcpsocketclient.View.Activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private TextView tv1;

    private String IPLocal= "";

    private LocationManager ubicacion;

    //variables prueba con handler
    private byte[] bufferTemporal = new byte[300];
    Handler handlerSocket;
    final int handlerState = 0;
    public static  String SERVER_IP = "192.168.1.15";
    public static  int SERVER_PORT = 2230;

    private ClientTCPThread clientTCPThread;

    //Datos usados para la recepción
    private int indByte=0;
    private int longitudTemp=0;
    int longitudTramaRecepcion = 0;
    private int[] bufferRecepcion = new int[300];


    private boolean validarWIFI = true;
    private int tiempoEspera=0;


    byte[] bufferTransmision= new byte[300];

    //variables para la recepción de bombas activas
    int numBombas=0;
    List<TransactionEntity> hoseEntities = new ArrayList<>();
    private String pintarBytes="";

    private NetworkUtil networkUtil;

    private String SSID="MOVISTAR_1B9E";
    private String Password="6XGE8bA5Ka8oRqzhkfCm";

    private ViewGroup layout;


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
        //initComponent();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponent();



        clientTCPThread =new ClientTCPThread();
        clientTCPThread.execute();
        //actualizarTiempo();
        //crearConexionWIFISocket();

        /*while(validarWIFI){
            try{
                Thread.sleep(0);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            tiempoEspera=10000;
            if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    validateLocation();
                    //validarWIFI=false;
                }
            } else {
                validateLocation();
                //validarWIFI=false;
                //boolean connect= connectToHotspot("EMBEDDED DEMO", "123456789");
            }
        }

        if(!validarWIFI){
            Toast.makeText(rootView.getContext(), "Se validaron los campos.", Toast.LENGTH_LONG).show();
        }*/
    }

    private void initComponent() {

        //tv1 = (TextView) rootView.findViewById(R.id.textView2);
        //tv1.setMovementMethod(new ScrollingMovementMethod());
        //new ConnectTask().execute("");

        //prueba con Handler y Socket
        handlerSocket = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {
                    //if message is what we want
                    if(recepciontTwoEasyFuel((byte[])msg.obj,msg.arg1)){           //Modificar metodo para nuevo protocolo
                        procesarTramaEasyFuel();
                        String mostrar = armarMensajeMuestra();
                        //tv1.append("\n"+pintarBytes + "\n" +mostrar);
                        //Log.d("Daviddd", "Pasooo");
                        //Toast.makeText(rootView.getContext(),byteArrayToHexString(bufferRecepcion,longitudTemp) + "\n",Toast.LENGTH_LONG);
                    }
                }
            }
        };

        networkUtil= new NetworkUtil(rootView.getContext());
    }

    /*public class ConnectThread extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    //publishProgress(message);
                    //String texto = tv1.getText().toString();
                    //tv1.setText(texto + " " + message);
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

    }*/

    public void mostrarMensajeUsuario(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                //tv1.setText(mensaje);
                Toast.makeText(rootView.getContext(), mensaje,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    public class ClientTCPThread extends AsyncTask<String, String, Boolean> {
        //public String SERVER_IP = "192.168.1.15";
        //public int SERVER_PORT = 2230;
        private OutputStream mBufferOut;
        //private DataOutputStream mBufferOut=null;

        // used to read messages from the server
        private InputStream mBufferIn;
        private Socket wifiSocket = null;
        private boolean mRun=true;
        public int longitud;

        @Override
        protected Boolean doInBackground(String... message) {
            boolean result = false;

            while(validarWIFI){
                try{
                    Thread.sleep(tiempoEspera);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                tiempoEspera=6000;
                if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(),
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        validateLocation();
                        //validarWIFI=false;
                    }
                } else {
                    validateLocation();
                    //validarWIFI=false;
                    //boolean connect= connectToHotspot("EMBEDDED DEMO", "123456789");
                }
            }

            /*if(!validarWIFI) {
                Toast.makeText(getActivity(), "Se validaron los campos.", Toast.LENGTH_LONG).show();
            }*/

            //return true;
            try {

                Log.d("TCP Client", "C: Connecting...");
                SocketAddress sockaddr = new InetSocketAddress(SERVER_IP, SERVER_PORT);
                wifiSocket = new Socket();
                wifiSocket.connect(sockaddr, 5000);

                Log.d("TCP Client", "C: Connectado..");
                mostrarMensajeUsuario("Se conectó al Socket con éxito.");
                try {
                    int bytes;
                    //sends the message to the server
                    mBufferOut = wifiSocket.getOutputStream();

                    //receives the message which the server sends back
                    mBufferIn = wifiSocket.getInputStream();

                    //in this while the client listens for the messages sent by the server
                    int tamBytes=0;
                    while (mRun) {
                        //mBufferIn = new DataInputStream(socket.getInputStream());
                    /*mServerMessage = mBufferIn.readLine();

                    Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");*/
                        bufferTemporal = new byte[300];
                        if(!wifiSocket.isClosed()) {
                            bytes=0;
                            //mBufferIn.read(lenBytes, 0, 10);
                            bytes=mBufferIn.read(bufferTemporal);
                        /*String readMessage = new String(bufferTemporal, 0, bytes);
                        Log.v("buffer", "" + readMessage);
                        // Send the obtained bytes to the UI Activity via handler
                        mensaje=byteArrayToHexString(bufferTemporal,bytes);
                        Log.v("", "" + mensaje);
                        */
                            handlerSocket.obtainMessage(handlerState, bytes, -1, bufferTemporal).sendToTarget();


                            //mMessageListener.messageReceived(mensaje);

                        }

                    }
                }  catch (IOException e) {
                    //e.printStackTrace();
                    //Log.e("TCP", "B: Error"+e.getClass()+ " - "+e.getMessage() + " - "+e.getCause(), e);
                    result = true;
                    mRun=false;
                    //Toast.makeText(rootView.getContext(), "Se perdió la conexión con el socket del servidor", Toast.LENGTH_SHORT).show();
                    mostrarMensajeUsuario("Se perdió la conexión con el socket del servidor");
                }catch (Exception e) {
                    result = true;
                    Log.e("TCP", "S: Error", e);
                } finally {
                    //the socket must be closed. It is not possible to reconnect to this socket
                    // after it is closed, which means a new socket instance has to be created.
                    wifiSocket.close();
                    mRun=false;
                }

            } catch (UnknownHostException e) {
                //e.printStackTrace();
                Log.e("TCP", e.getMessage(), e);
            } catch (IOException e) {
                //e.printStackTrace();
                //Log.e("TCP", "B: Error"+e.getClass()+ " - "+e.getMessage() + " - "+e.getCause(), e);
                //Toast.makeText(getActivity(), "No se logró establecer conexión con el socket del servidor", Toast.LENGTH_SHORT).show();
                mostrarMensajeUsuario("No se logró establecer conexión con el socket del servidor");
            }catch(Exception e){
                Log.e("TCP", "C: Error", e);
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.i("AsyncTask", "onPostExecute: Completed with an Error.");
                //Toast.makeText(rootView.getContext(), , Toast.LENGTH_SHORT).show();
                mostrarMensajeUsuario("No se logro establecer conexión con el socket del servidor");
            } else {
                Log.i("AsyncTask", "onPostExecute: Completed.");
            }
        }


        public void write(int opcode) {
            bufferTransmision = new byte[300];
            longitud=0;
            switch(opcode){

                //TRAMA DE CONFIGURACION
                case EmbeddedPtcl.b_ext_configuracion:
                    longitud = EmbeddedPtcl.aceptarTramaConfiguracion(bufferTransmision, bufferRecepcion[3],bufferRecepcion[4],bufferRecepcion[5]);
                    Log.v("", "" + "Transmision   "+ byteArrayToHexString(bufferTransmision,0x0b));
                    Log.v("", "" + "Longitud   "+ longitud);
                    break;

            }

            if (wifiSocket.isConnected()) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                mBufferOut.write(bufferTransmision,0,longitud);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                //timerNoComunicacion(1500);
        }




    }

    //Verificación de Ubicación
    private void validateLocation(){
        /*if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){

        }else{
            connectWIFI();
        }*/

        if(estadoGPS()){
            connectWIFI();
        }else{
            mostrarMensajeUsuario("Debe activar su ubicación.");
        }
    }

    //validar si la ubicación está activa
    private boolean estadoGPS(){
        ubicacion = (LocationManager)rootView.getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if(!ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return false;
        }

        return true;
    }

    //Conexión a la red EMBEEDED
    private void connectWIFI(){
        validarWIFI = networkUtil.connectToHotspot(SSID,Password);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        if(validarWIFI){
            if (networkUtil.isOutputWifi()) {
                validarWIFI=false;
                //connectSocket();
            } else {
                //Toast.makeText(getActivity(), "Debe desactivar sus datos móviles.", Toast.LENGTH_LONG).show();
                mostrarMensajeUsuario("Debe desactivar sus datos móviles.");
            }
        }else{
            validarWIFI=true;
            mostrarMensajeUsuario("No se detectó a la red "+SSID+" cerca de usted.");
        }
    }

    //INTERPRETAR TRAMA RECIBIDA
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
                                    pintarBytes = byteArrayToHexString(bufferRecepcion,longitudTemp);
                                    Log.v("", "" + "Recepcion   " + pintarBytes);
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

    //PROCESAR TRAMA COMPLETA RECIBIDA
    public void procesarTramaEasyFuel(){
        switch (bufferRecepcion[4]){
            //TRAMA DE CONFIGURACION
            case 0x06:
                if(bufferRecepcion[5] == 0x01 || bufferRecepcion[5] == 0x02){
                    //TRAMA DE CONFIGURACION
                    //Log.v("NOMBRE EMBEDDED", "" + hexToAscii(byteArrayToHexString(bufferRecepcion,bufferRecepcion.length)));
                    //Log.v("NOMBRE EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaNombreEmbedded,tramaNombreEmbedded.length)));
                    //Log.v("TRAMA EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaMACEmbedded,tramaMACEmbedded.length)));
                    //Log.v("PING HOST EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaPingHost,tramaPingHost.length)));
                    //Log.v("NÚMERO DE BOMBAS", "" + byteArrayToHexInt(numeroBombas,1));

                    llenarDatosConfiguracion();

                    clientTCPThread.write(EmbeddedPtcl.b_ext_configuracion); //0x06
                }
                break;

            //CAMBIO DE ESTADO
            case 0x01:

                break;

            case 0x07:
                switch(bufferRecepcion[5]){
                    case 0x01:
                        break;
                }
                break;
        }
    }

    //CARGAR DATOS BOMBAS ACTIVAS RECIBIDAS
    public void llenarDatosConfiguracion(){

        hoseEntities = new ArrayList<>();
        //**********************************************************

        //Capturar Nombre Host WIFI
        int[] tramaNombreEmbedded = new int[16];
        int c = 0;
        for(int i = 8; i<= 23;  i++){
            tramaNombreEmbedded[c] = bufferRecepcion[i];
            c++;
        }

        Log.v("NOMBRE EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaNombreEmbedded,tramaNombreEmbedded.length)));
        //**********************************************************
        //Capturar MAC TABLET
        int[] tramaMACTablet= new int[6];
        c = 0;
        for(int i = 24; i<= 29;  i++){
            tramaMACTablet[c] = bufferRecepcion[i];
            c++;
        }
        Log.v("MAC TABLET EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaMACTablet,tramaMACTablet.length)));


        //**********************************************************
        //Capturar Contraseña Red Host
        int[] contrasenaHostEmbedded = new int[11];
        c = 0;
        for(int i = 30; i<= 40;  i++){
            contrasenaHostEmbedded[c] = bufferRecepcion[i];
            c++;
        }

        Log.v("CONTRASENA RED EMBEDDED", "" + hexToAscii(byteArrayToHexString(contrasenaHostEmbedded,contrasenaHostEmbedded.length)));


        //**********************************************************
        //Capturar Nro Bombas
        int[] numeroBombas = new int[1];
        numeroBombas[0] = bufferRecepcion[41];
        numBombas = Integer.parseInt(byteArrayToHexIntGeneral(numeroBombas,1));
        //**********************************************************
        //int pIinicial = 42;

        //obtener solo IDbombas
        int[] idBombas = new int[numBombas];

        int pIinicial = 42;
        for(int i = 0; i< numBombas;  i++){
            idBombas[i] = bufferRecepcion[pIinicial];
            pIinicial+=1;
        }

        for(int i = 0; i< numBombas; i++){
            TransactionEntity transactionEntity = new TransactionEntity();
            transactionEntity.setEstadoRegistro("P");
            //**********************************************************
            //Capturar idBomba
            int[] idBomba = new int[1];
            idBomba[0] = bufferRecepcion[pIinicial];
            transactionEntity.setIdBomba(Integer.parseInt((byteArrayToHexIntGeneral(idBomba,1))));
            //**********************************************************
            //Capturar idProducto
            int[] idProducto = new int[1];
            idProducto[0] = bufferRecepcion[pIinicial + 1];
            transactionEntity.setIdProducto(Integer.parseInt(byteArrayToHexIntGeneral(idProducto,1)));
            //**********************************************************
            //Capturar cantidadDecimales
            int[] cantidadDecimales = new int[1];
            cantidadDecimales[0] = bufferRecepcion[pIinicial + 2];
            transactionEntity.setCantidadDecimales(Integer.parseInt(byteArrayToHexIntGeneral(cantidadDecimales,1)));
            //**********************************************************
            //Capturar cantidadDecimales
            int[] nombreManguera = new int[10];
            int contadorMangueraInicial = pIinicial + 3;
            int contadorMangueraFinal = contadorMangueraInicial + 9;
            int contadorIteracionesManguera = 0;
            for(int j=contadorMangueraInicial; j<=contadorMangueraFinal; j++){
                nombreManguera[contadorIteracionesManguera] = bufferRecepcion[j];
                contadorIteracionesManguera ++;
            }
            transactionEntity.setNombreManguera(hexToAscii(byteArrayToHexString(nombreManguera,nombreManguera.length)));

            int[] nombreProducto = new int[10];
            int contadorProductoInicial = pIinicial + 13;
            int contadorProductoFinal = contadorProductoInicial + 9;
            int contadorIteracionesProducto = 0;
            for(int k=contadorProductoInicial; k<=contadorProductoFinal; k++){
                nombreProducto[contadorIteracionesProducto] = bufferRecepcion[k];
                contadorIteracionesProducto ++;
            }
            transactionEntity.setNombreProducto(hexToAscii(byteArrayToHexString(nombreProducto,nombreProducto.length)));

            hoseEntities.add(transactionEntity);
            pIinicial = pIinicial + 23;
        }

    }


    private String byteArrayToHexIntGeneral(final int[] bytes, int cantidad) {
        int a = 0;
        double x = 0;
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            a=a<<8;
            a=a|((int)(0xFF&bytes[i]));
        }
        a=a&0x00FFFFFF;

        return "" + a;
    }

    private String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            if(!str.equals("00")){
                output.append((char) Integer.parseInt(str, 16));
            }

        }

        return output.toString();
    }

    private String byteArrayToHexString(final byte[] bytes, int cantidad) {
        StringBuilder sb = new StringBuilder();
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            sb.append(String.format("[%02x]", bytes[i]&0xFF));
            //sb.append((char)(0xFF&bytes[i]));
        }
        return sb.toString();
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


    public String armarMensajeMuestra(){
        String retorno = "";

        retorno += "\nBOMBAS ESPERADAS : "+numBombas+"\n";

        for(int i = 0; i< hoseEntities.size(); i++){
            retorno += "\nIDBOMBA = "+hoseEntities.get(i).getIdBomba()+"\n";
            retorno += "IDPRODUCTO = "+hoseEntities.get(i).getIdProducto()+"\n";
            retorno += "DECIMALES = "+hoseEntities.get(i).getCantidadDecimales()+"\n";
            retorno += "MANGUERA = "+hoseEntities.get(i).getNombreManguera()+"\n";
            retorno += "PRODUCTO = "+hoseEntities.get(i).getNombreProducto()+"\n";
        }

        return retorno;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clientTCPThread.cancel(true); //In case the task is currently running
    }

}