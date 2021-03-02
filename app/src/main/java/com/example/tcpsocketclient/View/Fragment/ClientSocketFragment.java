package com.example.tcpsocketclient.View.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcpsocketclient.ConnectTask;
import com.example.tcpsocketclient.Entities.LayoutHoseEntity;
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
    //public static  String SERVER_IP = "192.168.1.118";
    public static  String SERVER_IP = "192.168.4.22";
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

    //private String SSID="TP-LINK_AP_F2D8";
    private String SSID="EMBEDDED";
    private String Password="123456789";
    //private String Password="6XGE8bA5Ka8oRqzhkfCm";

    private ViewGroup layout;

    private int hijoLayout=0;
    //List<LinearLayout> inflaters= new ArrayList<>();
    List<LayoutHoseEntity> layoutsHose;

    //Trabajar con las mangueras
    LinearLayout ly_cuadrante;
    LinearLayout ly_cuadrante_estado_pausa;
    LinearLayout ly_cuadrante_estado_pausa2;
    LinearLayout ly_cuadrante_estado_abasteciendo;
    LinearLayout ly_cuadrante_estado_abasteciendo2;
    TextView txt_ultimo_galon_p2;
    TextView txt_ultimo_ticket_p2;
    TextView txt_nombre;
    ImageView iv_estado_abastecimiento;
    TextView   txt_ultimo_ticket;
    TextView   txt_Estado_abastecimiento;
    TextView   txt_galones;
    TextView   txt_placa;
    TextView   txt_producto;
    String ultimoGalonBomba = "0.00";

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
    }

    private void initComponent() {

        //tv1 = (TextView) rootView.findViewById(R.id.textView2);
        //tv1.setMovementMethod(new ScrollingMovementMethod());
        //new ConnectTask().execute("");

        //prueba con Handler y Socket


        layout = (ViewGroup) rootView.findViewById(R.id.LayoutContentHoses);

        layoutsHose = new ArrayList<>();


        handlerSocket = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {
                    //if message is what we want
                    if(recepciontTwoEasyFuel((byte[])msg.obj,msg.arg1)){           //Modificar metodo para nuevo protocolo
                        procesarTramaEasyFuel();
                        //String mostrar = armarMensajeMuestra();

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
                    Log.v("", "" + "Longitud   "+ longitud);
                    Log.v("", "" + "Transmision   "+ byteArrayToHexString(bufferTransmision,0x0b));
                    break;
                case EmbeddedPtcl.b_ext_cambio_estado:
                    if(bufferRecepcion[5]!=0x03){
                        longitud = EmbeddedPtcl.ackWifi(bufferTransmision,bufferRecepcion[3],bufferRecepcion[4],bufferRecepcion[5],bufferRecepcion[6],1,0);
                        Log.v("", "" + "Longitud   "+ longitud);
                        Log.v("", "" + "Transmision   " + byteArrayToHexString(bufferTransmision,0x0c));
                    }
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

    //CONEXIÓN A LA RED EMBEEDED
    private void connectWIFI(){
        validarWIFI = networkUtil.connectToHotspot(SSID,Password);
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }*/
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
            mostrarMensajeUsuario(networkUtil.mensajeError);
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
                                //Log.v("", "" + "Longitud   " + longitudTramaRecepcion);
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
                                    pintarBytes = byteArrayToHexString2(bufferRecepcion,longitudTemp);
                                    Log.v("", "" + "Longitud   " + longitudTemp);
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
                    agregarImagenEstaciones();
                    inicializarMangueras();
                    clientTCPThread.write(EmbeddedPtcl.b_ext_configuracion); //0x06

                    //inicializarMangueras();
                    /*try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }*/

                    //cambiarEstadoIniciaAbastecimiento(1);

                }
                break;

            //CAMBIO DE ESTADO
            case 0x01:
                int indiceLayoutHose=0;
                //Capturar idBomba
                int[] arrayIdBomba = new int[1];
                int idBomba = 0;
                arrayIdBomba[0] = bufferRecepcion[7];
                idBomba = Integer.parseInt(byteArrayToHexIntGeneral(arrayIdBomba,1));

                for(int i=0;i<hoseEntities.size();i++ ){
                    if(hoseEntities.get(i).idBomba==idBomba) {
                        indiceLayoutHose = i;
                        break;
                    }
                }

                switch (bufferRecepcion[5]){
                    case 0x01:
                        //Cambio de estado
                        if(hoseEntities.size() > 0){
                            cambioEstado(indiceLayoutHose, bufferRecepcion[8]);
                        }
                        clientTCPThread.write(EmbeddedPtcl.b_ext_cambio_estado);//0x01
                        break;
                    case 0x02:
                        //Estado actual de Mangueras
                        cambioEstado(indiceLayoutHose, bufferRecepcion[8]);
                        clientTCPThread.write(EmbeddedPtcl.b_ext_cambio_estado);//0x01
                        //mConnectedThread.write(EmbeddedPtcl.b_ext_cambio_estado); //0x01
                        break;
                    case 0x03:
                        //Cambio de Pulsos
                        /*switch (bufferRecepcion[9]){
                            case 0x01:
                                // FLUJO
                                if(hoseEntities.size() > 0){
                                    cambiarPulsos(indiceLayoutHose);
                                }
                                break;
                            case 0x02:
                                // INICIO NO FLUJO
                                if(hoseEntities.size() > 0){
                                    cambiarEstadoSinFlujo(indiceLayoutHose);
                                }
                                break;
                            case 0x03:
                                // NO FLUJO
                                if(hoseEntities.size() > 0){
                                    cambiarEstadoCierreHook(indiceLayoutHose);
                                }
                                break;
                        }*/
                        break;
                    case 0x04:
                        //Vehiculo Leido
                        if(hoseEntities.size() > 0){
                            cambiarPlaca(indiceLayoutHose);
                        }

                        clientTCPThread.write(EmbeddedPtcl.b_ext_cambio_estado);//0x01
                        break;
                    case 0x07:
                        //Ultima transaccion

                        if(hoseEntities.size() > 0){

                            /*for(int j = 0; j< hoseEntities.size(); j++){
                                if(hoseEntities.get(j).idBomba == idBomba){
                                    //llenarDatosTransaccion(hoseEntities.get(j));
                                    break;
                                }
                            }*/

                            llenarDatosTransaccion(hoseEntities.get(indiceLayoutHose),indiceLayoutHose);

                        }
                        clientTCPThread.write(EmbeddedPtcl.b_ext_cambio_estado);//0x01
                        //mConnectedThread.write(EmbeddedPtcl.b_ext_cambio_estado); //0x01
                        break;
                }
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


        //Log.v("NOMBRE EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaNombreEmbedded,tramaNombreEmbedded.length)));
        //**********************************************************
        //Capturar MAC TABLET
        int[] tramaMACTablet= new int[6];
        c = 0;
        for(int i = 24; i<= 29;  i++){
            tramaMACTablet[c] = bufferRecepcion[i];
            c++;
        }
        //Log.v("MAC TABLET EMBEDDED", "" + hexToAscii(byteArrayToHexString(tramaMACTablet,tramaMACTablet.length)));


        //**********************************************************
        //Capturar Contraseña Red Host
        int[] contrasenaHostEmbedded = new int[11];
        c = 0;
        for(int i = 30; i<= 40;  i++){
            contrasenaHostEmbedded[c] = bufferRecepcion[i];
            c++;
        }

        //Log.v("CONTRASENA RED EMBEDDED", "" + hexToAscii(byteArrayToHexString(contrasenaHostEmbedded,contrasenaHostEmbedded.length)));


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

    //GENERAR MANGUERAS DINAMICAMENTE SEGÚN CANTIDAD DE MANGUERAS
    @SuppressLint("InlinedApi")
    private void agregarImagenEstaciones()
    {
        layout.removeAllViews();
        //inflaters = new ArrayList<>();
        layoutsHose = new ArrayList<>();
        int id=0;
        int idBomba=0;
        for(int i=0;i <hoseEntities.size();i++){
            idBomba =hoseEntities.get(i).idBomba;
            LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
            id = R.layout.layout_hose;
            LinearLayout hoseLayout = (LinearLayout) inflater.inflate(id, null, false);
            txt_nombre = hoseLayout.findViewById(R.id.txt_nombre);
            txt_nombre.append(String.valueOf(idBomba));
            layout.addView(hoseLayout,i);
            //inflaters.add(hoseLayout);
            LayoutHoseEntity layoutHose = new LayoutHoseEntity(hoseLayout,idBomba);
            layoutsHose.add(layoutHose);
            //layout.addView(hoseLayout);
        }
        //hijoLayout++;
    }

    //Cambio de estados de Abastecimiento

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambioEstado(int indiceLayoutHose, int pEstadoActual){

        if(pEstadoActual == EmbeddedPtcl.v_estado_sin_abastecimiento){
            cambiarEstadoSinAbastecimiento(indiceLayoutHose);
        } else if(pEstadoActual == EmbeddedPtcl.v_estado_inicia_abastecimiento){
            cambiarEstadoIniciaAbastecimiento(indiceLayoutHose);
        }else if(pEstadoActual == EmbeddedPtcl.v_estado_autoriza_abastecimiento){
            cambiarEstadoAutorizarAbastecimiento(indiceLayoutHose);
        } else if(pEstadoActual == EmbeddedPtcl.v_estado_termina_abastecimiento){
            cambiarEstadoTerminaAbastecimiento(indiceLayoutHose);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarEstadoSinAbastecimiento(int indiceLayoutHose){

        //TransactionEntity entity = obtenerBombaActual(pIdBomba);
        txt_Estado_abastecimiento = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_Estado_abastecimiento);
        iv_estado_abastecimiento = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.iv_estado_abastecimiento);
        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);
        ly_cuadrante_estado_pausa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_pausa);
        ly_cuadrante_estado_pausa2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_pausa2);
        ly_cuadrante_estado_abasteciendo = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_abasteciendo);
        ly_cuadrante_estado_abasteciendo2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_abasteciendo2);

        txt_Estado_abastecimiento.setText("Disponible");
        iv_estado_abastecimiento.setImageResource(R.drawable.ic_station_yellow_64);
        txt_Estado_abastecimiento.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_galones.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_ultimo_ticket.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_placa.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_producto.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_pausa));

        ly_cuadrante_estado_pausa.setVisibility(View.VISIBLE);
        ly_cuadrante_estado_pausa2.setVisibility(View.VISIBLE);

        //txt_ultimo_ticket_m1_p2.setText(""+contadorTicketBomba1);
        ly_cuadrante_estado_abasteciendo.setVisibility(View.GONE);
        ly_cuadrante_estado_abasteciendo2.setVisibility(View.GONE);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarEstadoIniciaAbastecimiento(int indiceLayoutHose){
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_Estado_abastecimiento = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_Estado_abastecimiento);
        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);
        ly_cuadrante_estado_pausa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_pausa);
        ly_cuadrante_estado_pausa2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_pausa2);
        ly_cuadrante_estado_abasteciendo = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_abasteciendo);
        ly_cuadrante_estado_abasteciendo2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante_estado_abasteciendo2);
        txt_ultimo_galon_p2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_galon_p2);
        txt_ultimo_ticket_p2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket_p2);
        iv_estado_abastecimiento = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.iv_estado_abastecimiento);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);


        txt_Estado_abastecimiento.setText("Llamando");
        iv_estado_abastecimiento.setImageResource(R.drawable.ic_fuel_llamando_64);
        txt_Estado_abastecimiento.setTextColor(getResources().getColor(R.color.md_orange_assac));
        txt_galones.setTextColor(getResources().getColor(R.color.md_orange_assac));
        txt_ultimo_ticket.setTextColor(getResources().getColor(R.color.md_orange_assac));
        txt_placa.setTextColor(getResources().getColor(R.color.md_orange_assac));
        txt_producto.setTextColor(getResources().getColor(R.color.md_orange_assac));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_llamando));
        txt_galones.setText("0.00");
        txt_placa.setText("-");
        txt_producto.setText("-");
        ly_cuadrante_estado_pausa.setVisibility(View.GONE);
        ly_cuadrante_estado_pausa2.setVisibility(View.GONE);
        ly_cuadrante_estado_abasteciendo.setVisibility(View.VISIBLE);
        ly_cuadrante_estado_abasteciendo2.setVisibility(View.VISIBLE);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarEstadoAutorizarAbastecimiento(int indiceLayoutHose){
        txt_Estado_abastecimiento = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_Estado_abastecimiento);
        iv_estado_abastecimiento = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.iv_estado_abastecimiento);
        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);

        txt_Estado_abastecimiento.setText("Abasteciendo");
        iv_estado_abastecimiento.setImageResource(R.drawable.ic_fuel_abasteciendo_64);
        txt_Estado_abastecimiento.setTextColor(getResources().getColor(R.color.md_green_assac));
        txt_galones.setTextColor(getResources().getColor(R.color.md_green_assac));
        txt_ultimo_ticket.setTextColor(getResources().getColor(R.color.md_green_assac));
        txt_placa.setTextColor(getResources().getColor(R.color.md_green_assac));
        txt_producto.setTextColor(getResources().getColor(R.color.md_green_assac));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_autorizado));

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarEstadoTerminaAbastecimiento(int indiceLayoutHose){
        txt_Estado_abastecimiento = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_Estado_abastecimiento);
        iv_estado_abastecimiento = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.iv_estado_abastecimiento);
        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);


        txt_Estado_abastecimiento.setText("Disponible");
        iv_estado_abastecimiento.setImageResource(R.drawable.ic_station_yellow_64);
        txt_Estado_abastecimiento.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_galones.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_ultimo_ticket.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_placa.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        txt_producto.setTextColor(getResources().getColor(R.color.md_yellow_assac));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_pausa));

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarEstadoSinFlujo(int indiceLayoutHose){

        txt_Estado_abastecimiento = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_Estado_abastecimiento);
        iv_estado_abastecimiento = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.iv_estado_abastecimiento);
        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);

        txt_Estado_abastecimiento.setText("No Flujo");
        iv_estado_abastecimiento.setImageResource(R.drawable.ic_fuel_abasteciendo_64);
        txt_Estado_abastecimiento.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_galones.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_ultimo_ticket.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_placa.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_producto.setTextColor(getResources().getColor(R.color.md_red_assac));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_sin_flujo));

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarEstadoCierreHook(int indiceLayoutHose){

        txt_Estado_abastecimiento = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_Estado_abastecimiento);
        iv_estado_abastecimiento = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.iv_estado_abastecimiento);
        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);

        txt_Estado_abastecimiento.setText("Cierre Hook");
        iv_estado_abastecimiento.setImageResource(R.drawable.ic_fuel_abasteciendo_64);
        txt_Estado_abastecimiento.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_galones.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_ultimo_ticket.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_placa.setTextColor(getResources().getColor(R.color.md_red_assac));
        txt_producto.setTextColor(getResources().getColor(R.color.md_red_assac));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_sin_flujo));

    }

    //CARGAR ULTIMA TRANSACCION POR MANGUERA
    public void llenarDatosTransaccion(TransactionEntity entity, int indiceLayoutHose){
        //**********************************************************
        int contador = 0;
        Log.v("INICIO","**********************************************************");

        Log.v("Bomba",String.valueOf(entity.getIdBomba()));
        //**********************************************************
        //EstadoActual
        int[] tramaEstadoActual = new int[1];
        contador = 0;
        for(int i = 8; i<= 8;  i++){
            tramaEstadoActual[contador] = bufferRecepcion[i];
            contador++;
        }
        //String estadoActual = byteArrayToHexString(tramaEstadoActual,tramaEstadoActual.length);
        int estadoActual = Integer.parseInt(byteArrayToHexIntGeneral(tramaEstadoActual,1));
        entity.setEstadoActual(estadoActual);

        Log.v("Estado",String.valueOf(entity.getEstadoActual()));
        cambioEstado(indiceLayoutHose, estadoActual);

        //**********************************************************
        //Capturar Nro Transaccion
        int[] tramaNroTransaccion = new int[3];
        contador = 0;
        for(int i = 9; i<= 11;  i++){
            tramaNroTransaccion[contador] = bufferRecepcion[i];
            contador++;
        }
        String nroTransaccion = "" + byteArrayToHexInt(tramaNroTransaccion,tramaNroTransaccion.length);
        entity.setNumeroTransaccion(nroTransaccion);
        Log.v("Nro. Transacción",entity.getNumeroTransaccion());

        //**********************************************************
        //Capturar Fecha Inicio
        int[] tramaFechaInicio = new int[1];
        tramaFechaInicio[0] = bufferRecepcion[12];
        String dia = "" + byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length);
        tramaFechaInicio[0] = bufferRecepcion[13];
        String mes = "" + byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length);
        tramaFechaInicio[0] = bufferRecepcion[14];
        String anio = "20" + byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length);
        tramaFechaInicio[0] = bufferRecepcion[17];
        String hora = "" + byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length);
        tramaFechaInicio[0] = bufferRecepcion[16];
        String minuto = "" + byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length);
        tramaFechaInicio[0] = bufferRecepcion[15];
        String segundo = "" + byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length);

        String fechaInicio = dia + "/" + mes + "/" + anio + " ";
        String horaInicio =  hora + ":" + minuto + ":" + segundo;
        //fechaInicio = "" + hexToAscii(byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length));
        entity.setFechaInicio(fechaInicio);
        entity.setHoraInicio(horaInicio);
        Log.v("Fecha Inicio",entity.getFechaInicio());
        Log.v("Hora Inicio",entity.getHoraInicio());

        //**********************************************************
        //Capturar Turno
        int[] tramaTurno = new int[2];
        contador = 0;
        for(int i = 18; i<= 19;  i++){
            tramaTurno[contador] = bufferRecepcion[i];
            contador++;
        }
        int turno = byteArrayToHexInt2(tramaTurno,tramaTurno.length);
        entity.setTurno(turno);
        Log.v("Turno",""+entity.getTurno());

        //**********************************************************
        //Numero de Tanque
        int[] tramaNumeroTanque = new int[1];
        contador = 0;
        for(int i = 21; i<= 21;  i++){
            tramaNumeroTanque[contador] = bufferRecepcion[i];
            contador++;
        }

        int numeroTanque = Integer.parseInt(byteArrayToHexIntGeneral(tramaNumeroTanque,1));
        entity.setNumeroTanque(numeroTanque);

        Log.v("Tanque",""+entity.getNumeroTanque());

        //**********************************************************
        //Tipo de Vehiculo
        int[] tramaTipoVehiculo = new int[1];
        contador = 0;
        for(int i = 22; i<= 22;  i++){
            tramaTipoVehiculo[contador] = bufferRecepcion[i];
            contador++;
        }

        int tipoVehiculo = Integer.parseInt(byteArrayToHexIntGeneral(tramaTipoVehiculo,1));
        entity.setTipoVehiculo(tipoVehiculo);

        Log.v("Tipo Vehiculo",""+entity.getTipoVehiculo());


        //**********************************************************
        //Capturar IdVehiculo
        int[] tramaIdVehiculo = new int[8];
        contador = 0;
        for(int i = 23; i<= 30;  i++){
            tramaIdVehiculo[contador] = bufferRecepcion[i];
            contador++;
        }
        String IdVehiculo = hexToAscii(byteArrayToHexString(tramaIdVehiculo,tramaIdVehiculo.length));
        entity.setIdVehiculo(IdVehiculo);

        Log.v("IdVehiculo",entity.getIdVehiculo());

        //**********************************************************
        //Capturar Placa
        int[] tramaPlaca = new int[10];
        contador = 0;
        for(int i = 31; i<= 40;  i++){
            tramaPlaca[contador] = bufferRecepcion[i];
            contador++;
        }
        String placa = hexToAscii(byteArrayToHexString(tramaPlaca,tramaPlaca.length));
        entity.setPlaca(placa);

        Log.v("Placa",entity.getPlaca());

        //**********************************************************
        //Capturar Kilometro

        int[] tramaKilometroParteEntera = new int[3];
        contador = 0;
        for(int i = 41; i<= 43;  i++){
            tramaKilometroParteEntera[contador] = bufferRecepcion[i];
            contador++;
        }
        int kilometroParteEntera = byteArrayToHexInt(tramaKilometroParteEntera,tramaKilometroParteEntera.length);

        int[] tramaKilometroParteDecimal = new int[1];
        contador = 0;
        for(int i = 44; i<= 44;  i++){
            tramaKilometroParteDecimal[contador] = bufferRecepcion[i];
            contador++;
        }
        int kilometroParteDecimal = byteArrayToHexInt(tramaKilometroParteDecimal,tramaKilometroParteDecimal.length);

        //double kilometro = kilometroParteEntera + kilometroParteDecimal*0.1;
        double kilometro = Double.valueOf(kilometroParteEntera + "."+kilometroParteDecimal);


        entity.setKilometraje(kilometro);

        Log.v("Kilometro",""+kilometro);

        //**********************************************************
        //Capturar Horometro

        int[] tramaHorometroParteEntera = new int[3];
        contador = 0;
        for(int i = 45; i<= 47;  i++){
            tramaHorometroParteEntera[contador] = bufferRecepcion[i];
            contador++;
        }
        int horometroParteEntera = byteArrayToHexInt(tramaHorometroParteEntera,tramaHorometroParteEntera.length);

        int[] tramaHorometroParteDecimal = new int[1];
        contador = 0;
        for(int i = 48; i<= 48;  i++){
            tramaKilometroParteDecimal[contador] = bufferRecepcion[i];
            contador++;
        }

        double horometro = 0.0;

        if(tramaHorometroParteDecimal[0]==0xFF){
            horometro = horometroParteEntera/10D;
        }else{
            int horometroParteDecimal = byteArrayToHexInt(tramaKilometroParteDecimal,tramaKilometroParteDecimal.length);
            //horometro = horometroParteEntera + horometroParteDecimal*0.1;
            horometro = Double.valueOf(horometroParteEntera + "."+horometroParteDecimal);
        }

        entity.setHorometro(horometro);

        Log.v("Horometro",""+horometro);

        //**********************************************************
        //Capturar IdConductor
        int[] tramaIdConductor = new int[8];
        contador = 0;
        for(int i = 49; i<= 56;  i++){
            tramaIdConductor[contador] = bufferRecepcion[i];
            contador++;
        }
        String IdConductor = hexToAscii(byteArrayToHexString(tramaIdConductor,tramaIdConductor.length));
        entity.setIdConductor(IdConductor);

        Log.v("IdConductor",entity.getIdConductor());

        //**********************************************************
        //Capturar IdOperador
        int[] tramaIdOperador = new int[8];
        contador = 0;
        for(int i = 57; i<= 64;  i++){
            tramaIdOperador[contador] = bufferRecepcion[i];
            contador++;
        }
        String IdOperador = hexToAscii(byteArrayToHexString(tramaIdOperador,tramaIdOperador.length));
        entity.setIdOperador(IdOperador);

        Log.v("IdOperador",entity.getIdOperador());

        //**********************************************************
        //Tipo de Transacción
        int[] tramaTipoTransaccion = new int[1];
        contador = 0;
        for(int i = 65; i<= 65;  i++){
            tramaTipoTransaccion[contador] = bufferRecepcion[i];
            contador++;
        }

        int tipoTransaccion = Integer.parseInt(byteArrayToHexIntGeneral(tramaTipoTransaccion,1));
        entity.setTipoTransaccion(tipoTransaccion);

        Log.v("Tipo Transacción",""+entity.getTipoTransaccion());

        //**********************************************************
        //Capturar Latitud
        int[] tramaLatitud = new int[12];
        contador = 0;
        for(int i = 66; i<= 77;  i++){
            tramaLatitud[contador] = bufferRecepcion[i];
            contador++;
        }
        String latitud = hexToAscii(byteArrayToHexString(tramaLatitud,tramaLatitud.length));
        entity.setLatitud(latitud);

        Log.v("Latitud",entity.getLatitud());

        //**********************************************************
        //Capturar Longitud
        int[] tramaLongitud = new int[12];
        contador = 0;
        for(int i = 78; i<= 89;  i++){
            tramaLongitud[contador] = bufferRecepcion[i];
            contador++;
        }
        String longitud = hexToAscii(byteArrayToHexString(tramaLongitud,tramaLongitud.length));
        entity.setLongitud(longitud);

        Log.v("Longitud",entity.getLongitud());

        //**********************************************************
        //Capturar Tipo Error Pre-Seteo
        int[] tramaTipoErrorPreseteo = new int[1];
        contador = 0;
        for(int i = 90; i<= 90;  i++){
            tramaTipoErrorPreseteo[contador] = bufferRecepcion[i];
            contador++;
        }

        int tipoErrorPreseteo= Integer.parseInt(byteArrayToHexIntGeneral(tramaTipoErrorPreseteo,1));
        entity.setTipoErrorPreseteo(tipoErrorPreseteo);

        Log.v("Tipo Error Preseteo",""+entity.getTipoErrorPreseteo());

        //**********************************************************
        //Capturar Volumen Autorizado
        int[] tramaVolumenAutorizado = new int[2];
        contador = 0;
        for(int i = 91; i<= 92;  i++){
            tramaVolumenAutorizado[contador] = bufferRecepcion[i];
            contador++;
        }
        int volumenAutorizado = byteArrayToHexInt2(tramaVolumenAutorizado,tramaVolumenAutorizado.length);
        entity.setVolumenAutorizado(volumenAutorizado);
        Log.v("Volumen Autorizado",""+entity.getVolumenAutorizado());

        //**********************************************************
        //Capturar Volumen Aceptado
        int[] tramaVolumenAceptado = new int[2];
        contador = 0;
        for(int i = 93; i<= 94;  i++){
            tramaVolumenAceptado[contador] = bufferRecepcion[i];
            contador++;
        }
        int volumenAceptado = byteArrayToHexInt2(tramaVolumenAceptado,tramaVolumenAceptado.length);
        entity.setVolumenAceptado(volumenAceptado);
        Log.v("Volumen Aceptado",""+entity.getVolumenAceptado());

        //**********************************************************
        //Capturar Código Cliente
        int[] tramaCodigoCliente = new int[2];
        contador = 0;
        for(int i = 100; i<= 101;  i++){
            tramaCodigoCliente[contador] = bufferRecepcion[i];
            contador++;
        }
        int codigoCliente = byteArrayToHexInt2(tramaCodigoCliente,tramaCodigoCliente.length);
        entity.setCodigoCliente(codigoCliente);
        Log.v("Codigo Cliente",""+entity.getCodigoCliente());

        //**********************************************************
        //Capturar codigo area
        int[] tramaCodigoArea = new int[1];
        contador = 0;
        for(int i = 102; i<= 102;  i++){
            tramaCodigoArea[contador] = bufferRecepcion[i];
            contador++;
        }

        int CodigoArea = Integer.parseInt(byteArrayToHexIntGeneral(tramaCodigoArea,1));
        entity.setCodigoArea(CodigoArea);

        Log.v("Codigo Area",""+entity.getCodigoArea());

        //**********************************************************
        //Capturar tipo TAG
        int[] tramaTipoTAG = new int[1];
        contador = 0;
        for(int i = 103; i<= 103;  i++){
            tramaTipoTAG[contador] = bufferRecepcion[i];
            contador++;
        }

        int tipoTAG = Integer.parseInt(byteArrayToHexIntGeneral(tramaTipoTAG,1));
        entity.setTipoTag(tipoTAG);

        Log.v("Tipo TAG",""+entity.getTipoTag());

        //**********************************************************
        //Capturar Volumen Abastecido
        int[] tramaVolumen = new int[9];
        contador = 0;
        for(int i = 104; i<= 112;  i++){
            tramaVolumen[contador] = bufferRecepcion[i];
            contador++;
        }
        String volumen = ""+ hexToAscii(byteArrayToHexString(tramaVolumen,tramaVolumen.length));
        String[] parts = volumen.split("\\.");
        if(parts.length > 1) {
            volumen = parts[0] + "." + parts[1].substring(0,(0+entity.getCantidadDecimales()));
        }

        entity.setVolumen(volumen);

        Log.v("Volumen Abastecido",entity.getVolumen());

        //**********************************************************
        //Capturar Temperatura
        int[] tramaTemperatura = new int[5];
        contador = 0;
        for(int i = 113; i<= 117;  i++){
            tramaTemperatura[contador] = bufferRecepcion[i];
            contador++;
        }
        String temperatura = "" + hexToAscii(byteArrayToHexString(tramaTemperatura,tramaTemperatura.length));
        temperatura =  temperatura.substring(0,temperatura.length()-1);
        entity.setTemperatura(temperatura);

        Log.v("Temperatura",entity.getTemperatura());

        //**********************************************************
        //Capturar Fecha Fin
        int[] tramaFechaFin = new int[1];
        tramaFechaFin[0] = bufferRecepcion[118];
        String diaFin = "" + byteArrayToHexString(tramaFechaFin,tramaFechaFin.length);
        tramaFechaFin[0] = bufferRecepcion[119];
        String mesFin = "" + byteArrayToHexString(tramaFechaFin,tramaFechaFin.length);
        tramaFechaFin[0] = bufferRecepcion[120];
        String anioFin = "20" + byteArrayToHexString(tramaFechaFin,tramaFechaFin.length);
        tramaFechaFin[0] = bufferRecepcion[123];
        String horaFin = "" + byteArrayToHexString(tramaFechaFin,tramaFechaFin.length);
        tramaFechaFin[0] = bufferRecepcion[122];
        String minutoFin = "" + byteArrayToHexString(tramaFechaFin,tramaFechaFin.length);
        tramaFechaFin[0] = bufferRecepcion[121];
        String segundoFin = "" + byteArrayToHexString(tramaFechaFin,tramaFechaFin.length);

        String fechaFin = diaFin + "/" + mesFin + "/" + anioFin + " " ;
        String horaFin1 = horaFin + ":" + minutoFin + ":" + segundoFin;
        //fechaInicio = "" + hexToAscii(byteArrayToHexString(tramaFechaInicio,tramaFechaInicio.length));
        entity.setFechaFin(fechaFin);
        entity.setHoraFin(horaFin1);

        Log.v("Fecha Fin",entity.getFechaFin());
        Log.v("Hora Fin",entity.getHoraFin());

        //**********************************************************
        //Capturar Tipo de Cierre
        int[] tramaTipoCierre = new int[1];
        contador = 0;
        for(int i = 124; i<= 124;  i++){
            tramaTipoCierre[contador] = bufferRecepcion[i];
            contador++;
        }

        int tipoCierre = Integer.parseInt(byteArrayToHexIntGeneral(tramaTipoCierre,1));
        entity.setTipoCierre(tipoCierre);

        Log.v("Tipo Cierre",""+entity.getTipoCierre());

        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        txt_ultimo_galon_p2 = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_galon_p2);
        txt_ultimo_ticket_p2= layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket_p2);

        txt_producto.setText(entity.getNombreProducto());
        txt_placa.setText(entity.getPlaca());
        txt_galones.setText(entity.getVolumen());
        txt_ultimo_galon_p2.setText(entity.getVolumen());
        txt_ultimo_ticket.setText(entity.getNumeroTransaccion());
        txt_ultimo_ticket_p2.setText(entity.getNumeroTransaccion());


        //guardarTransaccionBD(entity);

    }

    //CAMBIAR PLACA ACTUAL
    public void cambiarPlaca(int indiceLayoutHose){
        //Capturar Nombre Host Bluetooth
        int[] tramaPlaca = new int[10];
        String placa = "";
        int c = 0;
        for(int i = 9; i<= 18;  i++){
            tramaPlaca[c] = bufferRecepcion[i];
            c++;
        }
        placa = hexToAscii(byteArrayToHexString(tramaPlaca,tramaPlaca.length));

        int contador;

        int[] tramaAutorizadoPlaca = new int[1];
        contador = 0;
        for(int i = 8; i<= 8;  i++){
            tramaAutorizadoPlaca[contador] = bufferRecepcion[i];
            contador++;
        }
        //String estadoActual = byteArrayToHexString(tramaEstadoActual,tramaEstadoActual.length);
        int autorizado = Integer.parseInt(byteArrayToHexIntGeneral(tramaAutorizadoPlaca,1));

        Log.v("Autorizado",""+ autorizado);

        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);

        txt_placa.setText(placa);
        txt_producto.setText(hoseEntities.get(indiceLayoutHose).getNombreProducto());
    }

    //cambiar pulsos
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cambiarPulsos(int indiceLayoutHose){
        cambiarEstadoAutorizarAbastecimiento(indiceLayoutHose);

        txt_galones = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_galones);

        int[] bufferGalones = new int[300];
        bufferGalones[0] = bufferRecepcion[10];
        bufferGalones[1] = bufferRecepcion[11];
        bufferGalones[2] = bufferRecepcion[12];
        Log.v("TEXT 1", "" + byteArrayToHexString(bufferGalones,3));
        Log.v("INT 1", "" + byteArrayToHexIntGeneral(bufferGalones,3));
        //og.v("INT 1", "" + byteArrayToHexString(bufferRecepcion,bufferRecepcion.));
        ultimoGalonBomba = ""+ (byteArrayToHexIntB(bufferGalones,3));
        txt_galones.setText(ultimoGalonBomba);

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

    private static int byteArrayToHexInt(final int[] bytes, int cantidad) {
        int a = 0;
        double x = 0;
        int indBuffer = 0;
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            //a=a<<8;
            //a=a|((int)(0xFF&bytes[i]));
            //if(i ==0){
            //a = bytes[i];
            //}
            //if(i>0) {
            //bytes[i] = (int) bytes[i] << 8;
            //}
            //a = a|(0xFF& bytes[i]);

            a = a | (bytes[indBuffer])<<(i*8);
            indBuffer++;
        }
        a=a&0x00FFFFFF;

        return  a;
    }

    private static int byteArrayToHexInt2(final int[] bytes, int cantidad) {
        int a = 0;
        double x = 0;
        //for(byte b : bytes){
        for(int i = cantidad-1; i>=0; i--){
            a=a<<8;
            a=a|((int)(0xFF&bytes[i]));
        }
        a=a&0x00FFFFFF;

        return  a;
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

    private static String byteArrayToHexString2(final int[] bytes, int cantidad) {
        StringBuilder sb = new StringBuilder();
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            sb.append(String.format("[%02x]", bytes[i]&0xff));
            //sb.append((char)(0xFF&bytes[i]));
        }
        return sb.toString();
    }

    private static String byteArrayToHexIntB(final int[] bytes, int cantidad) {
        int a = 0;
        double x = 0;
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            a=a<<8;
            a=a|((int)(0xFF&bytes[i]));
        }
        a=a&0x00FFFFFF;
        x = a/10.0;
        //a = a/10;
        //DecimalFormat form = new DecimalFormat("0.00");
        //return String.format("%.2f", a);
        return "" + x;//form.format(x);
        //return ""+a;
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

    public void inicializarMangueras(){

        /*ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_apagado));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_apagado));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_apagado));
        ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_apagado));*/

        /*for(int i = 0; i< hoseEntities.size(); i++){
            ly_cuadrante = inflaters.get(i).findViewById(R.id.ly_cuadrante);
            ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_apagado));
        }*/

        for(int i = 0; i< hoseEntities.size(); i++){
            ly_cuadrante = layoutsHose.get(i).inflater.findViewById(R.id.ly_cuadrante);
            ly_cuadrante.setBackground(getResources().getDrawable(R.drawable.bg_para_cuadrante_manguera_estado_pausa));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clientTCPThread.cancel(true); //In case the task is currently running
    }

}