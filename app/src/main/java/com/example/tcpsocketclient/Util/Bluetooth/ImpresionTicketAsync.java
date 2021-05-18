package com.example.tcpsocketclient.Util.Bluetooth;

//Sección Imprimir Bluetooth

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Util.CustomProgressDialog;
import com.example.tcpsocketclient.Util.PrinterCommands;
import com.example.tcpsocketclient.Util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImpresionTicketAsync extends AsyncTask<String,String, Boolean> {

    private TransactionEntity transactionEntity ;
    private PrinterBluetooth printerBluetooth;
    private ConnectedThreadPrinter mConnectedThreadPrinter;
    private  byte[] FEED_LINE = {10};
    private CustomProgressDialog mDialog;
    private Context activity;
    private String mensajeError="";

    public ImpresionTicketAsync(TransactionEntity transactionEntity, PrinterBluetooth printerBluetooth, CustomProgressDialog mDialog, Context activity) {
        super();
        this.transactionEntity = transactionEntity;
        this.printerBluetooth = printerBluetooth;
        this.mDialog=mDialog;
        this.activity=activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mDialog!=null)
            mDialog.showProgressDialog("Cargando impresora...");
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;

        if (printerBluetooth.btAdapter == null) {
            // Device does not support Bluetooth
            mensajeError="Dispositivo no soporta bluetooth.";
            result=false;
        }else{

            if(!printerBluetooth.btAdapter.isEnabled()) {
                mensajeError="Favor de activar su Bluetooth.";
                return  false;
            }

            printerBluetooth.cerrarConexionBTSocketImpresora();
            result = printerBluetooth.crearConexionBTSocketImpresora();
            if(printerBluetooth.btSocketPrinter.isConnected()){
                mConnectedThreadPrinter = new ConnectedThreadPrinter(printerBluetooth.btSocketPrinter);
                mConnectedThreadPrinter.start();
                imprimirTicket(transactionEntity);

                try {
                    Thread.sleep(2000);
                    printerBluetooth.cerrarConexionBTSocketImpresora();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                mensajeError="No se puede conectar con la impresora identificada con MAC: "+ printerBluetooth.MAC_PRINTER + ". Favor de validar si está encendida o ha sido previamente configurada.";
            }
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if(mDialog!=null)
            mDialog.dismissProgressDialog();
        if(!s){
            Toast.makeText(activity,mensajeError,Toast.LENGTH_LONG).show();
        }
    }

    private void imprimirTicket(TransactionEntity entity){
        Log.d("Leo11","11111");
        String CurrentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        printLogoASSAC();
        printUnicodeC1();
        printText("Generado el " +CurrentDate);
        printUnicodeC2();
        printText("Estacion: Pionero" );
        printText("Despacho: Automatico");
        printUnicodeC2();
        printText("Ticket: " +entity.getNumeroTransaccion());
        printText("Fecha: " +entity.getFechaInicio());
        printText("Inicio: " +entity.getHoraInicio());
        printText("Fin: " +entity.getHoraFin());
        printUnicodeC2();
        printText("Bomba: " +entity.getNombreManguera());
        printText("Galones: " +entity.getVolumen());
        printUnicodeC2();
        printText("Placa: " +entity.getPlaca());
        printText("Producto: " +entity.getNombreProducto());
        printUnicodeC1();
        printNewLine();
        printNewLine();
    }

    private void printUnicodeC(){
        printText(Utils.UNICODE_TEXT);
    }

    private void printUnicodeC1(){
        printText(Utils.UNICODE_TEXT_3D);
    }

    private void printUnicodeC2(){
        printText(Utils.UNICODE_TEXT_2D);
    }

    //print text
    private void printText(String msg) {
        // Print normal text
        mConnectedThreadPrinter.write(msg);
        printNewLine();
    }

    //print byte[]
    private void printText(byte[] msg) {
        // Print normal text
        mConnectedThreadPrinter.write(msg);
        printNewLine();
    }

    //print new line
    private void printNewLine() {
        mConnectedThreadPrinter.write(FEED_LINE);
    }

    //print photo
    public void printLogoASSAC() {
        int img = R.drawable.logo_assac_gcm_blanco_negro_128px;
        try {
            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                mConnectedThreadPrinter.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public class ConnectedThreadPrinter extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThreadPrinter(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+e.getMessage());
            }catch (Exception ex){
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+ex.getMessage());
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            int bytes;
            byte[] buffer = new byte[256];

            // Keep looping to listen for received messages
            while (true) {

            }
        }

        //write method
        public void write(String msg) {
            Log.d("Javier","22222");
            int longitud=0;

            try {
                mmOutStream.write(msg.getBytes());
                //timerNoComunicacion(1500);
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(activity, "La Conexión falló", Toast.LENGTH_LONG).show();

                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage());

            }catch (Exception ex){
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+ex.getMessage());
            }

        }
        //write method
        public void write(byte[] msg) {
            try {
                mmOutStream.write(msg);
                //timerNoComunicacion(1500);
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(activity, "La Conexión falló", Toast.LENGTH_LONG).show();
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage());

            }catch (Exception ex){
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+ex.getMessage());
            }
        }
    }

}



