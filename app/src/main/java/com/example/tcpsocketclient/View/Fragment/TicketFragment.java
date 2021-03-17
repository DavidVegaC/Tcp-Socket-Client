package com.example.tcpsocketclient.View.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcpsocketclient.Adapters.RVAdapterForTransaction;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.DB.CRUDOperations;
import com.example.tcpsocketclient.Storage.DB.MyDatabase;
import com.example.tcpsocketclient.Util.Bluetooth.PrinterBluetooth;
import com.example.tcpsocketclient.Util.CustomProgressDialog;
import com.example.tcpsocketclient.Util.PrinterCommands;
import com.example.tcpsocketclient.Util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewGroup rootView;

    private ConnectedThreadPrinter mConnectedThreadPrinter;
    private static String  MAC_PRINTER = "DC:0D:30:87:1C:80";
    //private static String  MAC_PRINTER = "DC:0D:30:87:17:FC";
    private static final UUID BTMODULEUUID_PRINTER = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private byte[] FEED_LINE = {10};
    private PrinterBluetooth printerBluetooth;

    SwipeRefreshLayout SwipeTicketFragment;
    ImageView btnBuscarTicketFragment;
    EditText txtTicketFragment;
    TextView txtMensajeTickets;
    RecyclerView rvListaTicket;
    AlertDialog dialog;
    CustomProgressDialog mDialog;
    ValidarImpresoraAsync validarImpresoraAsync;

    CRUDOperations crudOperations;

    Thread thread;
    Handler handler = new Handler();
    //List<TransactionEntity> listTransaction = null;

    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ticket, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//
//        btnImprimirTicketM1 = (Button) getView().findViewById(R.id.btnImprimirTicketM1);
//        btnAbrirCnImpresora = (Button) getView().findViewById(R.id.btnAbrirCnImpresora);


        SwipeTicketFragment =  rootView.findViewById(R.id.SwipeTicketFragment);
        btnBuscarTicketFragment =  rootView.findViewById(R.id.btnBuscarTicketFragment);
        txtTicketFragment =  rootView.findViewById(R.id.txtTicketFragment);
        txtMensajeTickets =  rootView.findViewById(R.id.txtMensajeTickets);

        rvListaTicket =  rootView.findViewById(R.id.rvListaTicket);
        rvListaTicket.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvListaTicket.setLayoutManager(llm);
        crudOperations = new CRUDOperations(new MyDatabase(getContext()));

        mDialog = new CustomProgressDialog(getContext());

        loadTransaction();
        SwipeTicketFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTransaction();
            }
        });
        btnBuscarTicketFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTransaction();

            }
        });

        printerBluetooth = new PrinterBluetooth();

//
//        btnAbrirCnImpresora.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                crearConexionBTSocketImpresora();
//            }
//        });
//
//        btnCerrarCnImpresora.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cerrarConexionBTSocketImpresora();
//            }
//        });

        //crearConexionBTSocketImpresora();
    }

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

    public void loadTransaction(){
        //listTransaction = new ArrayList<TransactionEntity>();
        /*TransactionEntity hoseEntity = new TransactionEntity();
        hoseEntity.setIdBomba(1);
        hoseEntity.setNumeroBomba(1);
        hoseEntity.setNumeroTransaccion("123");
        hoseEntity.setPlaca("ABC-123");
        hoseEntity.setVolumen("5.12");
        hoseEntity.setTemperatura("58.94");
        hoseEntity.setFechaInicio("09/09/2020");
        hoseEntity.setHoraInicio("09:38:20");
        hoseEntity.setFechaFin("09/09/2020");
        hoseEntity.setHoraFin("12:38:20");
        hoseEntity.setNombreProducto("D2");
        listTransaction.add(hoseEntity);
        //crudOperations.addTransaction(hoseEntity);
        hoseEntity = new TransactionEntity();
        hoseEntity.setIdBomba(2);
        hoseEntity.setNumeroBomba(2);
        hoseEntity.setNumeroTransaccion("456");
        hoseEntity.setPlaca("ABC-125");
        hoseEntity.setVolumen("60.25");
        hoseEntity.setTemperatura("50.94");
        hoseEntity.setFechaInicio("09/09/2020");
        hoseEntity.setHoraInicio("09:38:20");
        hoseEntity.setFechaFin("09/09/2020");
        hoseEntity.setHoraFin("12:38:20");
        hoseEntity.setNombreProducto("D2");
        listTransaction.add(hoseEntity);*/
        //crudOperations.addTransaction(hoseEntity);
        String pNroTransaccion = txtTicketFragment.getText().toString().trim();
        final List<TransactionEntity> lst =crudOperations.getTransaction(pNroTransaccion);

        if(lst.size()> 0){
            txtMensajeTickets.setVisibility(View.GONE);
            rvListaTicket.setVisibility(View.VISIBLE);
            RVAdapterForTransaction adapter = new RVAdapterForTransaction(lst);
            adapter.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    String nroTicket = "" + lst.get(rvListaTicket.getChildAdapterPosition(v)).getNumeroTransaccion();

                    final TextView myView = new TextView(getContext());
                    myView.setText("¿Seguro de imprimir el ticket "+ nroTicket + " ?");
                    myView.setTextSize(15);

                    final ImageView imageView = new ImageView(getContext());
                    int img = R.drawable.ic_baseline_white_30;
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), img);
                    imageView.setImageBitmap(bmp);

                    LinearLayout layout1       = new LinearLayout(getContext());
                    layout1.setOrientation(LinearLayout.HORIZONTAL);
                    layout1.addView(imageView);
                    layout1.addView(myView);
                    layout1.setGravity(Gravity.CENTER);
                    //layout1.setBackgroundColor(Color.BLUE);
                    layout1.setMinimumHeight(40);


                    dialog = new AlertDialog.Builder(getContext(), R.style.AppThemeAssacDialog).create();

                    dialog.setTitle("Impresión de Ticket");
                    //dialog.setTitle(Html.fromHtml("<font color='#fff'>Impresión de Ticket</font>"));
                    //dialog.setMessage("¿Seguro de imprimir el ticket "+ nroTicket + " ?");
                    dialog.setView(layout1);
                    dialog.setCancelable(false);
                    dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getContext(),"OK",Toast.LENGTH_LONG).show();
                            //validarImpresora(lst.get(rvListaTicket.getChildAdapterPosition(v)));
                            //Log.d("David",listTransaction.get(rvListaTicket.getChildAdapterPosition(v)).getPlaca());
                            new ValidarImpresoraAsync(lst.get(rvListaTicket.getChildAdapterPosition(v))).execute();
                        }
                    });
                    dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getContext(),"CANCEL",Toast.LENGTH_LONG).show();

                        }
                    });

                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.md_text_white);
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.md_text_white);
                        }
                    });
                    dialog.show();
                }
            });
            rvListaTicket.setAdapter(adapter);
        }else{
                txtMensajeTickets.setVisibility(View.VISIBLE);
                rvListaTicket.setVisibility(View.GONE);
        }

        SwipeTicketFragment.setRefreshing(false);
    }

    public class ValidarImpresoraAsync extends AsyncTask<String,String, Boolean> {

        private TransactionEntity transactionEntity ;

        public ValidarImpresoraAsync(TransactionEntity entity) {
            super();
            transactionEntity = entity;
            // do stuff
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.showProgressDialog("Cargando impresora...");
            Log.d("David1","holaaa");
        }


        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("David2","holaaa");
            boolean result = false;

            if (printerBluetooth.btAdapter == null) {
                // Device does not support Bluetooth
                //Toast.makeText(rootView.getContext(), "Dispositivo no soporta bluetooth.", Toast.LENGTH_SHORT).show();
                mostrarMensajeUsuario("Dispositivo no soporta bluetooth.");
                //getActivity().finish();
                result=true;
            }else{

                if(!printerBluetooth.btAdapter.isEnabled()) {
                    //Log.d(TAG, "enableDisableBT: enabling BT.");
                   /*Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBTIntent);*/
                    mostrarMensajeUsuario("Favor de activar su Bluetooth.");
                    result=true;
                    return  result;
                }

                printerBluetooth.cerrarConexionBTSocketImpresora();
                result = printerBluetooth.crearConexionBTSocketImpresora();
                if(printerBluetooth.btSocketPrinter.isConnected()){
                    mConnectedThreadPrinter = new TicketFragment.ConnectedThreadPrinter(printerBluetooth.btSocketPrinter);
                    mConnectedThreadPrinter.start();
                    imprimirTicket(transactionEntity);

                    try {
                        Thread.sleep(2000);
                        printerBluetooth.cerrarConexionBTSocketImpresora();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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
        protected void onPostExecute(Boolean s) {
            mDialog.dismissProgressDialog();
            if(!s){
                Toast.makeText(getContext(),"No se puede conectar con la impresora identificada con MAC: "+ printerBluetooth.MAC_PRINTER + ". Favor de validar si está encendida o ha sido previamente configurada.",Toast.LENGTH_LONG).show();
            }
            Log.v("Daviddd","99999");
        }


    }

    private void imprimirTicket(TransactionEntity entity){
        Log.d("Leo11","11111");
        String CurrentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        /*printUnicodeC();
        printText("Estacion: Pionero" );*/
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
        printText("Bomba: " +entity.getIdBomba());
        printText("Galones: " +entity.getVolumen());
        printUnicodeC2();
        printText("Placa: " +entity.getPlaca());
        printText("Producto: " +entity.getNombreProducto());
        printUnicodeC1();
        printNewLine();
        printNewLine();
        Log.d("Leo11","22222");
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
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
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


    private class ConnectedThreadPrinter extends Thread {

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
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+e.getMessage(),"1");
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
                Toast.makeText(getActivity(), "La Conexión falló", Toast.LENGTH_LONG).show();
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage(),"1");
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage());
                //mListener.goToBluetoothConfiguration();
            }catch (Exception ex){
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+ex.getMessage());
            }

        }
        //write method
        public void write(byte[] msg) {
            Log.d("Javier","1111");
            int longitud=0;

            try {
                mmOutStream.write(msg);
                //timerNoComunicacion(1500);
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getActivity(), "La Conexión falló", Toast.LENGTH_LONG).show();
                //Const.saveErrorData(getActivity(),new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage(),"1");
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread.write / mensaje: "+e.getMessage());
                //mListener.goToBluetoothConfiguration();
            }catch (Exception ex){
                Log.e(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),"método: ConnectedThread / mensaje: "+ex.getMessage());
            }
        }
    }


}