package com.example.tcpsocketclient;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectTask {
    final int handlerState = 0;

    private Socket btSocket = null;
    public static  String SERVER_IP = "192.168.1.110";
    public static  int SERVER_PORT = 2230;
    private StringBuilder DataStringIN = new StringBuilder();

    //private ConnectedThread mConnectedThread;
    private static String address = null;
    private int indByte=0;
    private int longitudTemp=0;

    private byte[] bufferTransmision = new byte [300];
    //private byte[] bufferRecepcion = new byte[300];
    private int[] bufferRecepcion = new int[300];
    private byte[] bufferTemporal = new byte[300];
    private String trama;
    private CountDownTimer timer;

    String tramaPrueba;
    byte[] tramaPruebaByte;

    //davidd
    Handler bluetoothIn;
    Thread thread;
    Handler handler = new Handler();


    private ConnectedThread mConnectedThread;

    public ConnectTask(){

        bluetoothIn = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {
                    //if message is what we want
                    /*if(recepciontTwoEasyFuel((byte[])msg.obj,msg.arg1)){           //Modificar metodo para nuevo protocolo
                        procesarTramaEasyFuel(msg.arg1);
                    }*/
                }
            }
        };

        actualizarTiempo();
        crearConexionBTSocket();
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
                            //txtFechaMF.setText(date);
                            Log.d("ConnectTask ",date);
                        }
                    });
                }
            }
        });
        thread.start();
    }
    //CREA UNA NUEVA INSTANCIA DE BLUETOOTH SOCKET
    private void crearConexionBTSocket(){
        //Get MAC address from the Common class
        // address = Const.address;
        //address = PreferencesHelper.getBluetoothAddress(getActivity());


        //Create device and set the MAC address
        //Log.v("Dirección", "Conexión al dispositivo con dirección : " + address);


        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting...");
            //create a socket to make the connection with the server
            btSocket = new Socket(serverAddr, SERVER_PORT);
            //Toast.makeText(getActivity(), "Creación de socket exitosa con el dispositivo: "+address, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
            //Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+address + " falló", Toast.LENGTH_LONG).show();
            //mListener.goToBluetoothConfiguration(); //Go back to the main view
            Log.e("TCP", e.getMessage(), e);
        }
        // Establish the Bluetooth socket connection.
        try
        {
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

        } catch (Exception e) {
            Log.d("TCP Client", e.getMessage());
        }

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
                    Log.v("", "" + byteArrayToHexString(bufferTemporal,bytes));

                    //bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    Log.v("CATCH", "" + e.getMessage());
                    //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.run / mensaje: "+e.getMessage(),"1");
                    break;
                }
            }
        }

        //write method
        public void write(int opcode) {
            bufferTransmision = new byte[300];
            int longitud=0;
            switch(opcode){

            }

            try {
                mmOutStream.write(bufferTransmision,0,longitud);
                //timerNoComunicacion(1500);
            } catch (IOException e) {
                //if you cannot write, close the application
                //Toast.makeText(getActivity(), "La Conexión falló", Toast.LENGTH_LONG).show();
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage(),"1");
                //mListener.goToBluetoothConfiguration();
            }
        }
    }

    private String byteArrayToHexString(final byte[] bytes, int cantidad) {
        StringBuilder sb = new StringBuilder();
        //for(byte b : bytes){
        for(int i = 0; i<cantidad; i++){
            sb.append(String.format("%02x", bytes[i]&0xff));
            //sb.append((char)(0xFF&bytes[i]));
        }
        return sb.toString();
    }

}