package com.example.tcpsocketclient.View.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcpsocketclient.Entities.Hose;
import com.example.tcpsocketclient.Entities.LayoutHoseEntity;
import com.example.tcpsocketclient.Entities.Maestros;
import com.example.tcpsocketclient.Entities.MigrationEntity;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.Interface.MainView;
import com.example.tcpsocketclient.Model.MainInteractor;
import com.example.tcpsocketclient.Presenter.MainFragmentPresenter;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.DB.CRUDOperations;
import com.example.tcpsocketclient.Storage.DB.MyDatabase;
import com.example.tcpsocketclient.Util.Bluetooth.PrinterBluetooth;
import com.example.tcpsocketclient.Util.Bluetooth.ImpresionTicketAsync;
import com.example.tcpsocketclient.Util.Const;
import com.example.tcpsocketclient.Util.CustomProgressDialog;
import com.example.tcpsocketclient.Util.Internet.ValidateConn;
import com.example.tcpsocketclient.Util.Utils;
import com.example.tcpsocketclient.Util.Wifi.EmbeddedPtcl;
import com.example.tcpsocketclient.View.Activity.MainActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EscenarioSinEmbeddedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EscenarioSinEmbeddedFragment extends Fragment implements MainView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "sicronizacion";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentActivity fragmentActivity;

    private ViewGroup rootView;
    protected MainFragmentPresenter mPresenter;

    //Trabajar con las mangueras variables
    private ViewGroup layout;

    List<LayoutHoseEntity> layoutsHose;

    LinearLayout ly_cuadrante;
    LinearLayout ly_cuadrante_estado_pausa;
    LinearLayout ly_cuadrante_estado_pausa2;
    LinearLayout ly_cuadrante_estado_abasteciendo;
    LinearLayout ly_cuadrante_estado_abasteciendo2;
    TextView txt_ultimo_galon_p2;
    TextView txt_ultimo_ticket_p2;
    TextView txt_nombre;
    ImageView iv_estado_abastecimiento;
    TextView txt_ultimo_ticket;
    TextView txt_Estado_abastecimiento;
    TextView txt_galones;
    TextView txt_placa;
    TextView txt_producto;

    //variables para la recepción de bombas activas
    int numBombas = 0;
    List<TransactionEntity> hoseEntities = new ArrayList<>();
    List<Hose> hoseConfigurationImage;

    private AlertDialog dialogFormTransaction;
    private FormDialogTransaction formDialogTransaction = null;

    //variable para lectura NFC
    public NfcAdapter mNfcAdapter;
    private AlertDialog dialog;
    private CustomProgressDialog mDialog;
    private String plateNFC = "";
    public static final String TAG = "NfcTag";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    CRUDOperations crudOperations;

    private PrinterBluetooth printerBluetooth;

    private final int escenario = 3;

    //variables para sincronizar data con servidor
    List<TransactionEntity> transactionsPending;
    MigrationEntity migrationEntity;
    Thread threadServicio, threadMigracion;
    Handler handlerServicio = new Handler();
    Handler handlerMigracion = new Handler();
    private boolean sincronizacionActiva = false;

    //Variables migración
    List<MigrationEntity> migrationEntities;
    TextView txt_ultima_migracion;
    LinearLayout  ly_migracion_estado;
    private int indexListTransactions=0;

    //Matrices de Permisos para foto
    private Uri imageUri;

    private ValidateConn validateConn;

    public EscenarioSinEmbeddedFragment() {
        // Required empty public constructor
    }

    public EscenarioSinEmbeddedFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EscenarioSinEmbeddedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EscenarioSinEmbeddedFragment newInstance(String param1, String param2) {
        EscenarioSinEmbeddedFragment fragment = new EscenarioSinEmbeddedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragmentPresenter createPresenter(@NonNull Context context) {
        return new MainFragmentPresenter(this, new MainInteractor());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            sincronizacionActiva = getArguments().getBoolean(ARG_PARAM3);
        }

        if (savedInstanceState != null) {
            sincronizacionActiva = getArguments().getBoolean(ARG_PARAM3);
        }
        mPresenter = createPresenter(this.getContext());
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_escenario_sin_embedded, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponent();
    }

    private void initComponent() {
        txt_ultima_migracion = (TextView) rootView.findViewById(R.id.txt_ultima_migracion);
        ly_migracion_estado = (LinearLayout)  rootView.findViewById(R.id.ly_migracion_estado);
        layout = (ViewGroup) rootView.findViewById(R.id.LayoutContentHoses);

        layoutsHose = new ArrayList<>();
        //NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        mDialog = new CustomProgressDialog(rootView.getContext());
        crudOperations = new CRUDOperations(new MyDatabase(fragmentActivity));

        hoseConfigurationImage = crudOperations.getAllHose();
        printerBluetooth = new PrinterBluetooth();
        validateConn = new ValidateConn(fragmentActivity, "2");

        llenarDatosConfiguracion();
        agregarImagenEstaciones();
        if (!sincronizacionActiva) {
            activarSincronizarData();
            sincronizacionActiva = true;
        }

        activarSincronizarMigracion();

    }

    private void activarSincronizarData() {
        threadServicio = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handlerServicio.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Entra Hilo", "1");
                            validateConn.mstartConn();
                        }
                    });
                    try {

                        Thread.sleep(3 * 60 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadServicio.start();
    }

    private void activarSincronizarMigracion(){
        final SimpleDateFormat formatDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        threadMigracion = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    handlerMigracion.post(new Runnable() {
                        @Override
                        public void run() {
                            //crudOperations
                            migrationEntities=crudOperations.getLastMigrationSuccess(escenario);
                            if(migrationEntities.size()>0){
                                //Log.v("Lastdate ",migrationEntities.get(0).getMigrationStartDate());
                                String lastdate=migrationEntities.get(0).getMigrationStartDate();

                                Date nowDate = new Date();
                                Date lastMigrationDate=null;
                                try {
                                    lastMigrationDate = formatDateTime.parse(lastdate);
                                } catch (ParseException e) {
                                    //e.printStackTrace();
                                    Log.e("Convert String to Date ",e.getMessage());
                                }

                                //obtienes la diferencia de las fechas
                                long difference = nowDate.getTime() - lastMigrationDate.getTime();//diferencia en milisegundos

                                long diffSeconds = difference / 1000; //diferencia en segundos
                                long diffMinutes = difference / (60 * 1000); //diferencia en minutos
                                long diffHours = difference / (60 * 60 * 1000); //diferencia en horas

                                //Fuente: https://www.iteramos.com/pregunta/26316/-calcular-la-diferencia-de-fecha-y-hora-en-java-

                                Log.v("Fecha Start ",lastMigrationDate.toString());
                                Log.v("Fecha actual ",nowDate.toString());
                                Log.v("Difeencia",""+difference);

                                txt_ultima_migracion.setText(lastdate);

                                if(diffMinutes<=60){
                                    sincronizacionNormal();
                                }else if(diffMinutes>60 && diffMinutes <=120){
                                    sincronizacionRegular();
                                }else{
                                    sincronizacionBaja();
                                }

                            }
                        }
                    });
                    try{
                        Thread.sleep(10*1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        threadMigracion.start();
    }

    private void sincronizacionNormal(){
        ly_migracion_estado.setBackgroundResource(R.drawable.bg_para_migracion_3);
    }

    private void sincronizacionRegular(){
        ly_migracion_estado.setBackgroundResource(R.drawable.bg_para_migracion_2);
    }

    private void sincronizacionBaja(){
        ly_migracion_estado.setBackgroundResource(R.drawable.bg_para_migracion_1);
    }

    //CARGAR DATOS BOMBAS ACTIVAS RECIBIDAS
    public void llenarDatosConfiguracion(){
        hoseConfigurationImage = crudOperations.getAllHose();
        hoseEntities = new ArrayList<>();
        List<TransactionEntity> lstTransacction =new ArrayList<TransactionEntity>();

        for(int i = 0; i< hoseConfigurationImage.size(); i++){
            TransactionEntity transactionEntity = new TransactionEntity();

            transactionEntity.setIdBomba(i+1);
            transactionEntity.setNumeroBomba(hoseConfigurationImage.get(i).HoseNumber);
            transactionEntity.setNombreManguera(hoseConfigurationImage.get(i).HoseName);
            transactionEntity.setNumeroTransaccion(""+hoseConfigurationImage.get(i).LastTicket);
            transactionEntity.setVolumen(String.format("%.2f", hoseConfigurationImage.get(i).FuelQuantity));
            transactionEntity.setNombreProducto(hoseConfigurationImage.get(i).NameProduct);
            transactionEntity.setIdHardware(hoseConfigurationImage.get(i).HardwareId);

            lstTransacction = crudOperations.getTransactionByTransactionAndHose(""+hoseConfigurationImage.get(i).LastTicket,escenario,"N");

            if(lstTransacction.size()==0)
                transactionEntity.setEstadoRegistro("P");
            else{
                transactionEntity.setEstadoRegistro("N");
                transactionEntity.setPlaca(lstTransacction.get(0).placa);
                transactionEntity.setVolumen("0.00");
                transactionEntity.setNumeroTransaccion(lstTransacction.get(0).numeroTransaccion);
                transactionEntity.setFechaInicio(lstTransacction.get(0).fechaInicio);
                transactionEntity.setHoraInicio(lstTransacction.get(0).horaInicio);
                transactionEntity.setNombreProducto(lstTransacction.get(0).nombreProducto);
                transactionEntity.setNombreManguera(lstTransacction.get(0).nombreManguera);
                transactionEntity.setContometroInicial(lstTransacction.get(0).contometroInicial);
            }

            hoseEntities.add(transactionEntity);
        }

    }

    //GENERAR MANGUERAS DINAMICAMENTE SEGÚN CANTIDAD DE MANGUERAS

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
            final int finalIdBomba = i;
            txt_nombre = hoseLayout.findViewById(R.id.txt_nombre);
            txt_nombre.setText(""+hoseEntities.get(i).getNombreManguera());
            txt_ultimo_ticket_p2=hoseLayout.findViewById(R.id.txt_ultimo_ticket_p2);
            txt_ultimo_ticket_p2.setText(""+hoseEntities.get(i).getNumeroTransaccion());
            txt_ultimo_ticket=hoseLayout.findViewById(R.id.txt_ultimo_ticket);
            txt_ultimo_ticket.setText(""+hoseEntities.get(i).getNumeroTransaccion());
            txt_ultimo_galon_p2=hoseLayout.findViewById(R.id.txt_ultimo_galon_p2);
            txt_ultimo_galon_p2.setText(""+hoseEntities.get(i).getVolumen());
            txt_producto =hoseLayout.findViewById(R.id.txt_producto);
            Log.v("David1",hoseEntities.get(i).getNombreProducto());
            txt_producto.setText(""+hoseEntities.get(i).getNombreProducto());
            ly_cuadrante = hoseLayout.findViewById(R.id.ly_cuadrante);
            ly_cuadrante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(rootView.getContext(), "CLick e la bomba "+finalIdBomba, Toast.LENGTH_SHORT).show();
                    //dialogFormTransaction = createFormRegisterTransaction();
                    //dialogFormTransaction.show();


                    //mDialog.showProgressDialog("Cargando Formulario Bomba "+finalIdBomba);
                    /*formDialogTransaction = new FormDialogTransaction(getActivity(),R.style.AppThemeAssacFormularioDialog,crudOperations,finalIdBomba);
                    formDialogTransaction.create();
                    formDialogTransaction.escribirManguera(hoseEntities.get(finalIdBomba).nombreManguera);
                    formDialogTransaction.show();*/
                    formDialogTransaction = layoutsHose.get(finalIdBomba).formDialogTransaction;

                    switch (formDialogTransaction.TAG_LAYOUT_VIEW){
                        case "SIN_ABASTECER":
                            cambioEstado(finalIdBomba,2);
                            formDialogTransaction.escribirNumeroTicket();
                            break;
                        case "ABASTECIENDO":
                            break;
                        case "ABASTECIDO":
                            formDialogTransaction = new FormDialogTransaction(getActivity(),fragmentActivity,R.style.AppThemeAssacFormularioDialog,crudOperations,finalIdBomba,escenario);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                formDialogTransaction.create();
                                formDialogTransaction.escribirManguera(hoseEntities.get(finalIdBomba).nombreManguera, hoseEntities.get(finalIdBomba).numeroBomba, hoseEntities.get(finalIdBomba).nombreProducto, hoseEntities.get(finalIdBomba).idHardware);
                                formDialogTransaction.escribirNumeroTicket();
                                cambioEstado(finalIdBomba,2);
                            }
                            layoutsHose.get(finalIdBomba).formDialogTransaction=formDialogTransaction;
                            break;
                    }

                    formDialogTransaction.show();
                    //mDialog.dismissProgressDialog();
                }
            });

            layout.addView(hoseLayout,i);
            formDialogTransaction = new FormDialogTransaction(getActivity(),fragmentActivity,R.style.AppThemeAssacFormularioDialog,crudOperations,finalIdBomba, escenario);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                formDialogTransaction.create();
                formDialogTransaction.escribirManguera(hoseEntities.get(finalIdBomba).nombreManguera, hoseEntities.get(finalIdBomba).numeroBomba, hoseEntities.get(finalIdBomba).nombreProducto,hoseEntities.get(finalIdBomba).idHardware);
                if(hoseEntities.get(i).estadoRegistro.equals("N"))
                    //Log.v("David",hoseEntities.get(i).numeroTransaccion);
                    formDialogTransaction.restaurarTransaccionDialogo(hoseEntities.get(i));
            }


            LayoutHoseEntity layoutHose = new LayoutHoseEntity(hoseLayout,finalIdBomba,formDialogTransaction);

            layoutsHose.add(layoutHose);

            if(hoseEntities.get(i).estadoRegistro.equals("P"))
                cambioEstado(i,0);
            else{
                cambioEstado(i,2);
                updateHose(i,hoseEntities.get(i),3);
            }

        }

    }

    public void setearPlacaManguera(int indiceLayoutHose, String placa){
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_placa.setText(placa);
    }

    //Cambio de estados de Abastecimiento|
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

        //txt_galones.setText(hoseEntities.get(indiceLayoutHose).getVolumen());

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
        //txt_producto.setText("-");
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
        txt_ultimo_galon_p2 = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_galon_p2);
        txt_ultimo_ticket = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_placa = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_producto = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_producto);
        ly_cuadrante = layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.ly_cuadrante);

        txt_Estado_abastecimiento.setText("Disponible");
        txt_galones.setText(hoseEntities.get(indiceLayoutHose).getVolumen());
        txt_ultimo_galon_p2.setText(hoseEntities.get(indiceLayoutHose).getVolumen());
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

    public void updateHose(int indiceLayoutHose, TransactionEntity transactionEntity, int estado){
        String volumenHose=  transactionEntity.volumen;
        String numeroTransaccion = transactionEntity.numeroTransaccion;
        hoseEntities.get(indiceLayoutHose).setVolumen(volumenHose);
        hoseEntities.get(indiceLayoutHose).setNumeroTransaccion(numeroTransaccion);
        txt_ultimo_ticket_p2=(TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket_p2);
        txt_ultimo_ticket=(TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_ticket);
        txt_ultimo_galon_p2=(TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_ultimo_galon_p2);
        txt_placa = (TextView) layoutsHose.get(indiceLayoutHose).inflater.findViewById(R.id.txt_placa);
        txt_placa.setText(transactionEntity.placa);
        txt_ultimo_ticket_p2.setText(numeroTransaccion);
        txt_ultimo_ticket.setText(numeroTransaccion);
        txt_ultimo_galon_p2.setText(volumenHose);
        cambioEstado(indiceLayoutHose,estado);
    }

    public void lecturaNFCPlaca(){
        if (mNfcAdapter == null) {
            // ES NECESARIO QUE EL DISPOSITIVO SOPORTE NFC
            mostrarMensajeUsuario("El dispositivo no soporta NFC.");
        }else{
            if (!mNfcAdapter.isEnabled()) {
                mostrarMensajeUsuario("Activa NFC en el dispositivo para continuar.");
            }else{
                formDialogTransaction.cambiarBotonScaneando();
                mostrarMensajeUsuario("Listo para leer NFC.");
                enableForegroundDispatchSystem();
            }
        }
    }

    public void enableForegroundDispatchSystem() {

        Intent intent = new Intent(getContext(), MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        mNfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilters, null);

    }

    public void disableForegroundDispatchSystem() {
        mNfcAdapter.disableForegroundDispatch(fragmentActivity);
    }

    public void processIntent(Intent intent) {
        Log.v(TAG, "ENTRÓ handleIntent");
        String action = intent.getAction();


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.v(TAG, "ENTRÓ ACTION_NDEF_DISCOVERED");
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                //new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Log.v(TAG, "ENTRÓ ACTION_TECH_DISCOVERED");
            //Toast.makeText(getApplicationContext(),  Toast.LENGTH_LONG).show();
            mostrarMensajeUsuario("TECH");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    //new NdefReaderTask().execute(tag);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            //tv1.setText(mensaje);
                            new NdefReaderTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    });
                    break;
                }
            }
        }else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Log.v(TAG, "ACTION_TAG_DISCOVERED");
            //Toast.makeText(getApplicationContext(), "TAG", Toast.LENGTH_LONG).show();
            //mostrarMensajeUsuario("TAG");
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            //new NdefReaderTask().execute(tag);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    //tv1.setText(mensaje);
                    new NdefReaderTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,tag);
                }
            });
        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        private Boolean readCorrect=true;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            plateNFC="";
            mDialog.showProgressDialog("Obteniendo datos por NFC...");
        }

        @Override
        protected String doInBackground(Tag... params) {
            Log.v(TAG, "NdefReaderTask:doInBackground");
            Tag tag = params[0];

            //LECTURA DE ID
            String tagInfo = "";


            byte[] tagId = tag.getId();

            //LECTURA DE DATOS
            NfcV tag5 = NfcV.get(tag);

            int blockAddress = 10;
            byte[] cmd = new byte[] {
                    (byte)0x20,  // FLAGS
                    (byte)0x20,  // READ_SINGLE_BLOCK
                    0, 0, 0, 0, 0, 0, 0, 0, // ID TAG
                    (byte)(blockAddress & 0x0ff)
            };
            //ESCRITURA
            byte[] cmd5 = new byte[] {
                    (byte)0x60,  // FLAGS
                    (byte)0x21,  // WRITE_SINGLE_BLOCK
                    0, 0, 0, 0, 0, 0, 0, 0, //ID TAG
                    (byte)(blockAddress & 0x0ff),
                    (byte)0x4c,  // data block that you want to write (same length as the blocks that you read) Deben ser 4 bytes por bloque
                    (byte)0x45,
                    (byte)0x4f,
                    (byte)0x4f,
            };
            System.arraycopy(tagId, 0, cmd5, 2, 8);
            System.arraycopy(tagId, 0, cmd, 2, 8);

            try {
                tag5.connect();
                byte[] responseDataDevice;
                String responseDataTexto = "";
                //String tagX = "";
                String placa =  "";
                String conductor = "";

                //responseDataDevice = tag5.transceive(cmd);
                for(int x = 0; x< blockAddress; x++){
                    byte[] comandos = new byte[] {
                            (byte)0x20,  // FLAGS
                            (byte)0x20,  // READ_SINGLE_BLOCK
                            0, 0, 0, 0, 0, 0, 0, 0,
                            (byte)(x & 0x0ff)
                    };
                    System.arraycopy(tagId, 0, comandos, 2, 8);
                    responseDataDevice = tag5.transceive(comandos);

                    /*for(int i=1; i<responseDataDevice.length; i++){
                        tagX +=  Integer.toHexString(responseDataDevice[i] & 0xFF) + " ";
                    }
                    tagX += "\n";*/
                    for(int i=1; i<responseDataDevice.length; i++){
                        if(x >= 0 && x <= 2 ){
                            plateNFC += Utils.convertHexToString(Integer.toHexString(responseDataDevice[i] & 0xFF)).trim();
                        }
                        /*if(x >= 6 && x <= 10){
                            conductor += Utils.convertHexToString(Integer.toHexString(responseDataDevice[i] & 0xFF)) + " ";
                        }*/

                        //tagX +=  convertHexToString(Integer.toHexString(responseDataDevice[i] & 0xFF)) + " ";
                    }

                    if(x==0){
                        plateNFC +="-";
                    }

                    responseDataTexto += Utils.toHex(responseDataDevice) +"\n";
                    //tagX += "\n";
                }

                //tagInfo+= "Datos de Bloques: \n"+ tagX;
                tagInfo+= "Datos de Bloques: \n"+ "Placa: "+ placa +"\nConductor: "+ conductor;
                Log.v(TAG,tagInfo += "\n");
                //mostrarMensajeUsuario(tagInfo);

//                String tagX = "";
//                for(int i=0; i<tagId.length; i++){
//                    tagX += Integer.toHexString(tagId[i] & 0xFF) + " ";
//                }
                Log.v(TAG, tagInfo);

            } catch (IOException e) {
                //e.printStackTrace();
                Log.v(TAG, e.getMessage());
                readCorrect=false;
            } finally {
                try {
                    tag5.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return tagInfo;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();
            // Codificación
            String textEncoding;
            if ((payload[0] & 128) != 0) textEncoding = "UTF-16";
            else textEncoding = "UTF-8";

            // Lenguaje
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            mDialog.dismissProgressDialog();
            //Log.v(TAG, "NdefReaderTask:onPostExecute");
            if (readCorrect == true) {
                //Utilizamos el resultado de la tarea asincrona para pasar
                //asistencia. Podemos ejecutar directamente aqui el
                //envio de datos al servidor o añadir un botón.
                //Log.v(TAG,result);
                formDialogTransaction.escribirPlaca(plateNFC);
            }else{
                //Toast.makeText(getApplicationContext(), "No se completó correctamente la lectura por NFC.\n Intentelo nuevamente.", Toast.LENGTH_LONG).show();
                mostrarMensajeUsuario("No se completó correctamente la lectura por NFC.\nIntentelo nuevamente.");
            }
            formDialogTransaction.cambiarBotonSinScanear();
        }
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

    public void cargarDataServidor(){
        transactionsPending=crudOperations.getPendingMigrationTransaction(escenario);
        migrationEntity = new MigrationEntity();
        migrationEntity.setMigrationStartDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        migrationEntity.setRegistrationUser("1");
        migrationEntity.setMigrationScene(escenario);

        if(transactionsPending.size()>0){
            indexListTransactions=0;
            //row=crudOperations.updateStatusMigrationTransactions(transactionsPendingEntity.idsSqliteStringList.toString());
            //mDialog.showProgressDialog("Registrando Transacciones Pendientes.");
            while(indexListTransactions!=transactionsPending.size()){
                mPresenter.RegistrarTransacciones(fragmentActivity,transactionsPending.get(indexListTransactions));
                indexListTransactions++;
            }
        }

        obtenerMaestros();

    }

    private void obtenerMaestros(){
        //cargando = ProgressDialog.show(rootView.getContext(), "Obteniendo Maestros","Espere por favor");
        //mDialog.showProgressDialog("Obteniendo Maestros...");
        mPresenter.ObtenerMaestros(fragmentActivity);
    }

    public void imprimirBluetooth(final TransactionEntity transactionEntity){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                //tv1.setText(mensaje);
                new ImpresionTicketAsync(transactionEntity,printerBluetooth,mDialog,getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    //Tomar Foto
    public void imagePickDialog() {
        /*String[] options = {"Camara","Galería"};
        //dialogo
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());

        //titulo
        builder.setTitle("Seleccionar imagen");

        //establecer elementos / opciones
        builder.setItems(options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //manejar clicks
                        switch(i){
                            case 0:{
                                //click en camara
                                if(!checkCameraPermission()){
                                    requestCameraPermission();
                                }else{
                                    //permiso ya otorgado
                                    PickFromCamera();
                                }
                            }
                            break;
                            case 1: {
                                if (!checkStoragePermission()) {
                                    requestStoragePermission();
                                } else {
                                    //permiso ya otorgado
                                    PickFromGallery();
                                }
                            }
                            break;
                        }

                    }
                }
        );

        //crear y mostrar dialogo
        builder.create().show();*/
        if(!checkCameraPermission()){
            requestCameraPermission();
            /*if(checkCameraPermission()){
                PickFromCamera();
            }*/
        }else{
            //permiso ya otorgado
            PickFromCamera();
        }
    }

    private void PickFromGallery() {
        //intennto de elegir la imagen de la galería, la imagen se devolverá en el método onActivityResult
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Const.IMAGE_PICK_GALLERY_CODE);
    }

    private void PickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Titulo de la imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");
        //put image uri
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Intento de abrir la cámara para la imagen
        Intent cameraIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, Const.IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        //Comprobar permiso de almacenamiento

        boolean result = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), Const.storagePermissions, Const.STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //Comprobar permiso de camara y almacenamiento
        boolean result = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.CAMERA
        ) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), Const.cameraPermissions, Const.CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.v("Camera","1    "+ requestCode);
        switch (requestCode){
            case Const.CAMERA_REQUEST_CODE:{
                Log.v("Camera","2");
                if(grantResults.length>0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        //ambos permisos permititdos
                        PickFromCamera();
                    }
                    else{
                        Toast.makeText(rootView.getContext(), "Se requieren permisos de cámara y almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case Const.STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted){
                        //ambos permisos eprmititdos
                        PickFromGallery();
                    }
                    else{
                        Toast.makeText(rootView.getContext(), "Se requiere permiso de almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Image picked FROM CAMERA OR GALLERY WILL BE RECEIVED HERE
        if(resultCode == RESULT_OK ){
            if(requestCode == Const.IMAGE_PICK_GALLERY_CODE){
                imageUri = data.getData();
            }
            try {
                //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                //ivRegisterPlate.setImageBitmap(bitmap);
                Log.v("ImageURI", imageUri.toString());
                formDialogTransaction.escribirUriImagen(imageUri);
            }catch (Exception e){
                Toast.makeText(rootView.getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityFragmentOk(HashMap<String, ?> map) {
        if(map.containsKey(mPresenter.KEY_LOBTENERMAESTROS)){
            //limpiarFormulario();
            Maestros maestros = (Maestros)map.get(mPresenter.KEY_LOBTENERMAESTROS);
            crudOperations.clearTablesMasters();
            crudOperations.insertMastersSQLite(maestros);
            migrationEntity.setRegistrationStatus("P");
            migrationEntity.setMigrationDescription("Sincronización Exitosa.");
            int row = crudOperations.addMigration(migrationEntity);
            Log.v("Insertado Exito", ""+row);
            Log.v("Hose COnfiguration Size", ""+hoseConfigurationImage.size());
            if(hoseConfigurationImage.size()==0){
                llenarDatosConfiguracion();
                agregarImagenEstaciones();
            }

        }else if(map.containsKey(mPresenter.KEY_LREGISTROTRANSACCION)){
            String mensaje = (String)map.get(mPresenter.KEY_LREGISTROTRANSACCION);
            crudOperations.updateStatusMigrationTransaction(transactionsPending.get(indexListTransactions-1).idSqlite);
        }
    }

    @Override
    public void onActivityFragmentError(HashMap<String, ?> map) {
        if (map.containsKey(mPresenter.KEY_LOBTENERMAESTROS)) {
            String mensaje = (String) map.get(mPresenter.KEY_LOBTENERMAESTROS);

            migrationEntity.setRegistrationStatus("E");
            migrationEntity.setMigrationDescription("Error Obteniendo Maestros. "+mensaje);
            int row=crudOperations.addMigration(migrationEntity);
            Log.v("Insertado Fallo1", ""+row);
            Log.v("Maestros", ""+mensaje);

        } else if (map.containsKey(mPresenter.KEY_LREGISTROTRANSACCION)) {
            String mensaje = (String) map.get(mPresenter.KEY_LREGISTROTRANSACCION);
            migrationEntity.setRegistrationStatus("E");
            migrationEntity.setMigrationDescription("Error Insertando transaccion pendiente."+mensaje);
            int row=crudOperations.addMigration(migrationEntity);
            Log.v("Insertado Fallo2", ""+row);
            Log.v("Transacciones", ""+mensaje);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_PARAM3, sincronizacionActiva);
    }
}