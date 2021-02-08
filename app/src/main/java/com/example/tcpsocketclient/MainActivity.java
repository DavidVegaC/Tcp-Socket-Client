package com.example.tcpsocketclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText ed1,edIP, edPort;
    private Button bt1, bt2,bt3;
    private TextView tv1;
    private String mensaje = "";
    TcpClient mTcpClient;
    private boolean conexion  =false;
    private String mensajeError = "";
    private String strDireccionIP = "";
    private int intPORT = 0;


    public static final String SERVER_IP = "192.168.1.15"; //server IP address
    public static final int SERVER_PORT = 2230;

    //private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1= (EditText)findViewById(R.id.ed1);
        edIP= (EditText)findViewById(R.id.edIP);
        edPort= (EditText)findViewById(R.id.edPort);

        bt1 = (Button)findViewById(R.id.SendButton);
        bt2 = (Button)findViewById(R.id.CloseButton);
        bt3 = (Button)findViewById(R.id.ConnectedButton);
        tv1 = (TextView) findViewById(R.id.textView2);
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
                if(edIP.getText().toString().trim().equals("") || edPort.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Debe completar tanto la DirecciónIP como el Puerto.", Toast.LENGTH_SHORT).show();
                }else{
                    strDireccionIP =edIP.getText().toString().trim();
                    intPORT =Integer.parseInt(edPort.getText().toString().trim());
                    new ConnectTask().execute("");
                    bt3.setEnabled(false);
                    bt2.setEnabled(true);
                    edIP.setEnabled(false);
                    edPort.setEnabled(false);
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            //tv1.setText(mensaje);

                            if(conexion){
                                tv1.append(mensaje);
                            }else{
                                Toast.makeText(MainActivity.this,mensajeError , Toast.LENGTH_SHORT).show();
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


    public void sendMessage(){
        String message = ed1.getText().toString().trim();

        if (mTcpClient != null) {
            if(!message.equals("")){
                mTcpClient.sendMessage(message);
            }else{
                Toast.makeText(this, "El mensaje está vacio.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No existe conexión abierta.", Toast.LENGTH_SHORT).show();
        }

    }

    public void closeConecction(){
        if (mTcpClient != null) {
            mTcpClient.stopClient();
            mTcpClient= null;
        }else{
            Toast.makeText(this, "No existe conexión abierta.", Toast.LENGTH_SHORT).show();
        }
    }




}