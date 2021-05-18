package com.example.tcpsocketclient.Util.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.tcpsocketclient.View.Fragment.TicketFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PrinterBluetooth {
    public BluetoothSocket btSocketPrinter = null;
    public BluetoothAdapter btAdapter = null;
    public BluetoothDevice device=null;
    public static final String  MAC_PRINTER = "DC:0D:30:87:1C:80";
    //private static String  MAC_PRINTER = "DC:0D:30:87:17:FC";
    public static final UUID BTMODULEUUID_PRINTER = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public PrinterBluetooth(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //CREA UNA NUEVA INSTANCIA DE BLUETOOTH SOCKET
    public boolean crearConexionBTSocketImpresora()  {
        boolean response = true;
        //Get MAC address from the Common class

        //Create device and set the MAC address

        try {
            device = btAdapter.getRemoteDevice(MAC_PRINTER);
        }catch (Exception e) {
            Log.e("ErrorDevice:",e.getMessage());
            //e.printStackTrace();
            response=false;
            return response;
        }

        try {
            btSocketPrinter = createBluetoothSocketPrinter(device);
            //Toast.makeText(getActivity(), "Creación de socket exitosa con  la impresora: "+MAC_PRINTER, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
            Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método1: crearConexionBTSocket / mensaje: "+e.getMessage());
            //Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+MAC_PRINTER + " falló", Toast.LENGTH_LONG).show();
            //mListener.goToBluetoothConfiguration(); //Go back to the main view
            response=false;
            return response;
        }
        // Establish the Bluetooth socket connection.
        /*try
        {
            Log.v("ErrorBluetooht:","David5");
            btSocketPrinter.connect();
            Log.v("ErrorBluetooht:","David6");
            mConnectedThreadPrinter = new ConnectedThreadPrinter(btSocketPrinter);
            mConnectedThreadPrinter.start();

        } catch (IOException e) {
            try
            {
                btSocketPrinter.close();
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método2: crearConexionBTSocket / mensaje: "+e.getMessage());
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
                //mListener.goToBluetoothConfiguration(); //Go back to the main view
                //Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+MAC_PRINTER + " falló", Toast.LENGTH_LONG).show();
            } catch (IOException e2)
            {
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage());
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: crearConexionBTSocket / mensaje: "+e.getMessage(),"1");
                //mListener.goToBluetoothConfiguration(); //Go back to the main view
                //Toast.makeText(getActivity(), "La creacción del Socket con el dispositivo "+MAC_PRINTER + " falló", Toast.LENGTH_LONG).show();
                //insert code to deal with this
            }

        }*/
        try {
            btSocketPrinter.connect();
            Log.v("00000","Connected1");
            /*mConnectedThreadPrinter = new TicketFragment.ConnectedThreadPrinter(btSocketPrinter);
            mConnectedThreadPrinter.start();*/
        } catch (IOException e) {
            Log.e("",e.getMessage());
            try {
                Log.v("11111","trying fallback...");

                btSocketPrinter =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                btSocketPrinter.connect();
                Log.e("222222","Connected2");
                //mConnectedThreadPrinter = new TicketFragment.ConnectedThreadPrinter(btSocketPrinter);
                //mConnectedThreadPrinter.start();

            }
            catch (Exception e2) {
                Log.e("Error", "Fallback no se pudo conectar");
                response=false;
                try{
                    btSocketPrinter.close();
                }catch(Exception e3){
                    Log.v("Error","No se pudo cerrar conexion");
                    return response;
                }


            }
            /*response=false;
            try{
                btSocketPrinter.close();
            }catch(Exception e3){
                //Log.v("Error","No se pudo cerrar conexion");
                Log.e("Error", "Couldn't establish Bluetooth connection!");
                return response;
            }*/


        }
        return response;

    }

    //DESTRUIR INSTANCIA DE BLUETOOTH SOCKET
    public void cerrarConexionBTSocketImpresora(){
        try
        {
            if(btSocketPrinter != null){
                //Don't leave Bluetooth sockets open when leaving activity
                btSocketPrinter.close();
                //Toast.makeText(getActivity(), "Comunicación con el dispositivo "+MAC_PRINTER +" terminada", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            //insert code to deal with this
            //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: cerrarConexionBTSocket / mensaje: "+e.getMessage(),"1");
            Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: PrinterBluetooth / cerrarConexionBTSocket / mensaje: "+e.getMessage());
        }
    }

    public BluetoothSocket createBluetoothSocketPrinter(BluetoothDevice device) throws IOException {
        //creates secure outgoing connecetion with BT device using UUID
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID_PRINTER);
    }



}
