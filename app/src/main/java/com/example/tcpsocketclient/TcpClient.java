package com.example.tcpsocketclient;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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


public class TcpClient {

    public static final String TAG = TcpClient.class.getSimpleName();
    //public static final String SERVER_IP = "192.168.1.110"; //server IP address
    //public static final int SERVER_PORT = 2230;
    public static  String SERVER_IP = "192.168.1.1";
    public static  int SERVER_PORT = 2234;
    // message to send to the server
    private String mServerMessage;
    public String msgError = "";
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    //private PrintWriter mBufferOut;
    private DataOutputStream mBufferOut=null;

    // used to read messages from the server
    private InputStream mBufferIn;
    //private DataInputStream mBufferIn;
    private byte[] bufferTemporal = new byte[300];

    //private tamaño trama final
    private final int tamanoTramaPrueba=11;

    //Datos usados para la recepción
    private int indByte=0;
    private int longitudTemp=0;
    int longitudTramaRecepcion = 0;
    private int[] bufferRecepcion = new int[300];

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener, String DirIP, int puerto) {
        SERVER_IP=DirIP;
        SERVER_PORT=puerto;
        mMessageListener = listener;
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
                        mBufferOut.writeUTF(message);
                        mBufferOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
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

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);
            //socket.setSoTimeout(200);

            Log.d("TCP Client", "C: Connectado..");
            try {
                int bytes;
                //sends the message to the server
                //mBufferOut = new PrintStream(socket.getOutputStream(), true);
                //mBufferOut = new PrintWriter(socket.getOutputStream());
                mBufferOut = new DataOutputStream(
                        socket.getOutputStream());
                        //receives the message which the server sends back
                mBufferIn = socket.getInputStream();

                //byte[] buffer = new byte[256];
                //in this while the client listens for the messages sent by the server
                int tamBytes=0;
                String mensaje="";
                String mostrarTextView="";
                while (mRun) {
                    //mBufferIn = new DataInputStream(socket.getInputStream());
                    /*mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }
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
                        mostrarTextView+=mensaje;
                        Log.v("", "" + mensaje);
                        tamBytes+=bytes;

                        if(tamBytes==tamanoTramaPrueba){
                            mMessageListener.messageReceived(mostrarTextView+ "\n");
                            tamBytes=0;
                            mostrarTextView="";
                        }else if (tamBytes>tamanoTramaPrueba){
                            int contiene=tamBytes/tamanoTramaPrueba;
                            tamBytes=tamBytes%tamanoTramaPrueba;
                            for(int i=0;i<contiene;i++){
                                mMessageListener.messageReceived(mostrarTextView.substring(0,tamanoTramaPrueba*4)+"\n");
                                mostrarTextView=mostrarTextView.substring(tamanoTramaPrueba*4);
                            }

                        }*/

                        recepciontTwoEasyFuel(bufferTemporal,bytes);


                        //mMessageListener.messageReceived(mensaje);
                        /*if (bytes == 11 && mMessageListener != null ) {
                            //String received = new String(buffer, 0, bytes);
                            //received += " - "+bytes+" - ";
                            //for(int i=0;i<10;i++){
                            //    int positive = lenBytes[i] & 0xff;
                            //    received += "["+positive+"] ";
                            //}
                            //received +=byteArrayToHex(lenBytes);
                            String received = "";
                            received += byteArrayToHexString(bufferTemporal,bytes) + "\n";
                            mMessageListener.messageReceived(received);
                            Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + received + "'");
                        }else if(bytes  >= 0){
                            mMessageListener.messageReceived("Error al recibir bytes "+ bytes + "  "+byteArrayToHex(bufferTemporal,bytes)+"\n");
                            //Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + received + "'");
                        }*/

                    }

                }
            }  catch (IOException e) {
                //e.printStackTrace();
                //Log.e("TCP", "B: Error"+e.getClass()+ " - "+e.getMessage() + " - "+e.getCause(), e);
                msgError ="Se perdió la conexion con el socket del servidor";
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

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
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
                                    mMessageListener.messageReceived(byteArrayToHexString(bufferRecepcion,longitudTemp) + "\n");
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

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("[%02x]", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

}