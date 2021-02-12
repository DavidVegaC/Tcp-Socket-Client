package com.example.tcpsocketclient;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.Util.Wifi.EmbeddedPtcl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TcpClient {

    public static final String TAG = TcpClient.class.getSimpleName();
    //public static final String SERVER_IP = "192.168.1.110"; //server IP address
    //public static final int SERVER_PORT = 2230;
    public static  String SERVER_IP = "192.168.1.1";
    public static  int SERVER_PORT = 2234;
    public static String LOCAL_PORT = "";
    // message to send to the server
    private String mServerMessage;
    public String msgError = "";
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    //declare socket
    Socket socket=null;
    // used to send messages
    private OutputStream mBufferOut;
    //private DataOutputStream mBufferOut=null;

    // used to read messages from the server
    private InputStream mBufferIn;
    //private DataInputStream mBufferIn;
    private byte[] bufferTemporal = new byte[300];

    //Datos usados para la recepción
    private int indByte=0;
    private int longitudTemp=0;
    int longitudTramaRecepcion = 0;
    private int[] bufferRecepcion = new int[300];

    byte[] bufferTransmision= new byte[300];
    List<TransactionEntity> hoseEntities = new ArrayList<>();
    int numBombas=0;


    //string hexString = "028c000106010000454d42454444454400000000000000005465038b7aa8313233343536373839000004010203040101024739302d312020202020473930202020202020200201024739302d322020202020473930202020202020200301024739302d312020202020473930202020202020200401024739302d31202020202047393020202020202020db03";

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener, String DirIP, int puerto, String LocalIP) {
        SERVER_IP=DirIP;
        SERVER_PORT=puerto;
        mMessageListener = listener;
        LOCAL_PORT=LocalIP;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    Log.d(TAG, "Sending: " + message);
                    try {
                        mBufferOut.write(1);
                        mBufferOut.flush();
                    } catch (IOException e) {
                       Log.e("Error Salida",e.getMessage(),e);
                    }

                    //mBufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() throws IOException {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {
        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting...");

            InetAddress localAddr = InetAddress.getByName(LOCAL_PORT);
            //create a socket to make the connection with the server

            socket = new Socket(serverAddr, SERVER_PORT, localAddr, 0);
            //socket.setSoTimeout(200);

            Log.d("TCP Client", "C: Connectado..");
            try {
                int bytes;
                //sends the message to the server
                mBufferOut = socket.getOutputStream();

                //receives the message which the server sends back
                mBufferIn = socket.getInputStream();



                //in this while the client listens for the messages sent by the server
                int tamBytes=0;
                while (mRun) {
                    //mBufferIn = new DataInputStream(socket.getInputStream());
                    /*mServerMessage = mBufferIn.readLine();

                    Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");*/
                    bufferTemporal = new byte[300];
                    if(!socket.isClosed()) {
                        bytes=0;
                        //mBufferIn.read(lenBytes, 0, 10);
                        bytes=mBufferIn.read(bufferTemporal);
                        /*String readMessage = new String(bufferTemporal, 0, bytes);
                        Log.v("buffer", "" + readMessage);
                        // Send the obtained bytes to the UI Activity via handler
                        mensaje=byteArrayToHexString(bufferTemporal,bytes);
                        Log.v("", "" + mensaje);
                        */

                        recepciontTwoEasyFuel(bufferTemporal,bytes);


                        //mMessageListener.messageReceived(mensaje);

                    }

                }
            }  catch (IOException e) {
                //e.printStackTrace();
                //Log.e("TCP", "B: Error"+e.getClass()+ " - "+e.getMessage() + " - "+e.getCause(), e);
                msgError ="Se perdió la conexión con el socket del servidor";
                mMessageListener.messageReceived(null);
            }catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (UnknownHostException e) {
            //e.printStackTrace();
            Log.e("TCP", e.getMessage(), e);
        } catch (IOException e) {
            //e.printStackTrace();
            //Log.e("TCP", "B: Error"+e.getClass()+ " - "+e.getMessage() + " - "+e.getCause(), e);
            msgError ="No se logro establecer conexion con el socket del servidor";
            mMessageListener.messageReceived(null);
        }catch(Exception e){
            Log.e("TCP", "C: Error", e);
        }


    }


    public void write(int opcode) {
        bufferTransmision = new byte[300];
        int longitud=0;
        switch(opcode){

            //TRAMA DE CONFIGURACION
            case EmbeddedPtcl.b_ext_configuracion:
                longitud = EmbeddedPtcl.aceptarTramaConfiguracion(bufferTransmision, bufferRecepcion[3],bufferRecepcion[4],bufferRecepcion[5]);
                Log.v("", "" + "Transmision   "+ byteArrayToHexString(bufferTransmision,0x0b));
                break;

        }

        try {
            mBufferOut.write(bufferTransmision,0,longitud);
            //timerNoComunicacion(1500);
        } catch (IOException e) {
            //if you cannot write, close the application
            Log.e("WriteSOcket",e.getMessage(),e);

        }
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    private void recepciontTwoEasyFuel(byte[] temporal, int cantidad)  {
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

                                    //write(bufferRecepcion[4]);
                                    procesarTramaEasyFuel();
                                    String mostrar = armarMensajeMuestra();
                                    mMessageListener.messageReceived("\n"+byteArrayToHexString(bufferRecepcion,longitudTemp) + "\n" +mostrar);
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

        //return hasPacket;
    }

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

                    write(EmbeddedPtcl.b_ext_configuracion); //0x06
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

    private static String byteArrayToHexIntGeneral(final int[] bytes, int cantidad) {
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

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            if(!str.equals("00")){
                output.append((char) Integer.parseInt(str, 16));
            }

        }

        return output.toString();
    }

}