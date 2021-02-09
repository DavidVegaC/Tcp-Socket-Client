package com.example.tcpsocketclient.View.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    private EditText ed1,edIP, edPort;
    private Button bt1, bt2,bt3;
    private TextView tv1;
    private String mensaje = "";
    TcpClient mTcpClient;
    private boolean conexion  =false;
    private String mensajeError = "";
    private String strDireccionIP = "";
    private int intPORT = 0;

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
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_client_socket,container,false);
        initComponent();
        return rootView;
    }

    private void initComponent(){
        ed1= (EditText)rootView.findViewById(R.id.ed1);
        edIP= (EditText)rootView.findViewById(R.id.edIP);
        edPort= (EditText)rootView.findViewById(R.id.edPort);

        bt1 = (Button)rootView.findViewById(R.id.SendButton);
        bt2 = (Button)rootView.findViewById(R.id.CloseButton);
        bt3 = (Button)rootView.findViewById(R.id.ConnectedButton);
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
                if(edIP.getText().toString().trim().equals("") || edPort.getText().toString().trim().equals("")){
                    Toast.makeText(rootView.getContext(), "Debe completar tanto la DirecciónIP como el Puerto.", Toast.LENGTH_SHORT).show();
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




}