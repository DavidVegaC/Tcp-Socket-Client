package com.example.tcpsocketclient.View.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.tcpsocketclient.Entities.Hose;
import com.example.tcpsocketclient.Entities.LayoutHoseEntity;
import com.example.tcpsocketclient.Entities.LayoutHosePreCierre;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.DB.CRUDOperations;
import com.example.tcpsocketclient.Storage.DB.MyDatabase;
import com.example.tcpsocketclient.Util.Bluetooth.ImpresionPreCierreAsync;
import com.example.tcpsocketclient.Util.Bluetooth.ImpresionTicketAsync;
import com.example.tcpsocketclient.Util.Bluetooth.PrinterBluetooth;
import com.example.tcpsocketclient.Util.CustomProgressDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreCierreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreCierreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewGroup rootView;
    private FragmentActivity fragmentActivity;

    private PrinterBluetooth printerBluetooth;
    private int escenario=0;

    private String dateTimeStartReport="";
    private String dateTimeEndReport="";

    private List<Hose> hoses;
    private ViewGroup layout;
    private List<LayoutHosePreCierre> layoutsHosesPreCierre;

    private  CRUDOperations crudOperations;
    private CustomProgressDialog mDialog;

    private EditText txtDateStartPreCierre, txtDateEndPreCierre, txtTimeStartPreCierre, txtTimeEndPreCierre;
    private TextView txt_total_estacion_pre_cierre;
    private RadioButton radioDetalle, radioResumen;
    private LinearLayout lyGenerarReportePreCierre, lyImpresionPreCierre;


    private SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat =new SimpleDateFormat("HH:mm");

    private int tipoReporte=0;
    private double galonesTotalPreCierre=0.0;

    private TextView txt_nombre_manguera;
    private TextView txt_total_nombre_manguera;
    private TextView txt_total_abastecimiento_manguera;
    private LinearLayout ly_transacciones_manguera;
    private LinearLayout ly_cabecera_manguera_pre_cierre;

    public PreCierreFragment() {
        // Required empty public constructor
    }

    public PreCierreFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity=fragmentActivity;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreCierreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreCierreFragment newInstance(String param1, String param2) {
        PreCierreFragment fragment = new PreCierreFragment();
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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pre_cierre, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponent();
        generateReport();
    }

    public void setEscenario(int escenario){
        this.escenario=escenario;
    }

    private void initComponent(){
        radioDetalle = (RadioButton) rootView.findViewById(R.id.radioDetalle);
        radioResumen = (RadioButton) rootView.findViewById(R.id.radioResumen);
        lyGenerarReportePreCierre = (LinearLayout) rootView.findViewById(R.id.lyGenerarReportePreCierre);
        lyImpresionPreCierre = (LinearLayout) rootView.findViewById(R.id.lyImpresionPreCierre);

        txt_total_estacion_pre_cierre = (TextView) rootView.findViewById(R.id.txt_total_estacion_pre_cierre);
        txtDateStartPreCierre = (EditText) rootView.findViewById(R.id.txtDateStartPreCierre);
        txtTimeStartPreCierre = (EditText) rootView.findViewById(R.id.txtTimeStartPreCierre);
        txtDateEndPreCierre = (EditText) rootView.findViewById(R.id.txtDateEndPreCierre);
        txtTimeEndPreCierre = (EditText) rootView.findViewById(R.id.txtTimeEndPreCierre);

        Date dateNow = new Date();

        txtDateStartPreCierre.setText(dateFormat.format(dateNow));
        txtTimeStartPreCierre.setText("00:00");

        txtDateEndPreCierre.setText(dateFormat.format(dateNow));
        txtTimeEndPreCierre.setText(timeFormat.format(dateNow));

        txtDateStartPreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtDateStartPreCierre);
            }
        });

        txtTimeStartPreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(txtTimeStartPreCierre);
            }
        });

        txtDateEndPreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtDateStartPreCierre);
            }
        });

        txtTimeEndPreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(txtTimeEndPreCierre);
            }
        });

        lyGenerarReportePreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateReport();
            }
        });

        lyImpresionPreCierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printReport();
            }
        });

        layoutsHosesPreCierre = new ArrayList<>();
        galonesTotalPreCierre=0.0;

        mDialog = new CustomProgressDialog(rootView.getContext());
        crudOperations = new CRUDOperations(new MyDatabase(getContext()));
        printerBluetooth = new PrinterBluetooth();
        hoses = crudOperations.getAllHose();

        layout = (ViewGroup) rootView.findViewById(R.id.LayoutContentInformationHoses);

    }

    private void generateReport(){
        galonesTotalPreCierre=0.0;
        String dateStartReport = txtDateStartPreCierre.getText().toString();
        String timeStartReport = txtTimeStartPreCierre.getText().toString()+":00";
        String dateEndReport = txtDateEndPreCierre.getText().toString();
        String timeEndReport = txtTimeEndPreCierre.getText().toString()+":00";

        dateTimeStartReport =dateStartReport + " "+ timeStartReport;
        dateTimeEndReport =dateEndReport + " "+ timeEndReport;

        if(radioResumen.isChecked()){
            tipoReporte=1;
        }else if(radioDetalle.isChecked()){
            tipoReporte=2;
        }

        layout.removeAllViews();
        layoutsHosesPreCierre = new ArrayList<>();
        List<TransactionEntity> transactionEntitiesByHose;

        double totalGalonesManguera;

        for (Hose hose: hoses) {
            totalGalonesManguera=0.0;
            //transactionEntitiesByHose=new ArrayList<>();
            LayoutInflater inflater = LayoutInflater.from(rootView.getContext());

            LinearLayout hoseLayout = (LinearLayout) inflater.inflate(R.layout.layout_hose_detalle_pre_cierre, null, false);
            txt_total_nombre_manguera = hoseLayout.findViewById(R.id.txt_total_nombre_manguera);
            txt_nombre_manguera = hoseLayout.findViewById(R.id.txt_nombre_manguera);
            txt_total_abastecimiento_manguera= hoseLayout.findViewById(R.id.txt_total_abastecimiento_manguera);
            ly_transacciones_manguera = hoseLayout.findViewById(R.id.ly_transacciones_manguera);
            ly_cabecera_manguera_pre_cierre = hoseLayout.findViewById(R.id.ly_cabecera_manguera_pre_cierre);

            txt_nombre_manguera.append(hose.HoseName);
            txt_total_nombre_manguera.append(hose.HoseName);

            transactionEntitiesByHose=crudOperations.getTransactionByHose(dateTimeStartReport,dateTimeEndReport,hose.HoseNumber,escenario);
            Log.v("Transactions "+hose.HoseNumber,""+ transactionEntitiesByHose.size());

            for (TransactionEntity transactionEntity: transactionEntitiesByHose) {
                LinearLayout detalleTransactionLayout = (LinearLayout) inflater.inflate(R.layout.layout_detalle_transaccion_pre_cierre, null, false);
                TextView txt_ticket_transaction = detalleTransactionLayout.findViewById(R.id.ticket_Transaccion);
                TextView txt_placa_transaccion = detalleTransactionLayout.findViewById(R.id.placa_transaccion);
                TextView galones_transaccion = detalleTransactionLayout.findViewById(R.id.galones_transaccion);

                txt_ticket_transaction.setText(transactionEntity.numeroTransaccion);
                txt_placa_transaccion.setText(transactionEntity.placa);
                galones_transaccion.setText(transactionEntity.volumen);

                ly_transacciones_manguera.addView(detalleTransactionLayout);
                totalGalonesManguera += Double.parseDouble(transactionEntity.volumen);
            }

            txt_total_abastecimiento_manguera.setText(String.format("%.2f", totalGalonesManguera));

            if(tipoReporte==1){
                ly_cabecera_manguera_pre_cierre.setVisibility(View.GONE);
            }
            layout.addView(hoseLayout);

            LayoutHosePreCierre layoutHosePreCierre = new LayoutHosePreCierre(hose.HoseName,transactionEntitiesByHose,String.format("%.2f", totalGalonesManguera));
            layoutsHosesPreCierre.add(layoutHosePreCierre);
            galonesTotalPreCierre += totalGalonesManguera;
        }

        txt_total_estacion_pre_cierre.setText(String.format("%.2f", galonesTotalPreCierre));
    }

    private void printReport(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ImpresionPreCierreAsync(tipoReporte, dateTimeStartReport, dateTimeEndReport, String.format("%.2f", galonesTotalPreCierre), layoutsHosesPreCierre, printerBluetooth, mDialog, fragmentActivity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private void showDatePickerDialog(final EditText etDate) {
        final Calendar c = Calendar.getInstance();
        int mYear, mMonth, mDay, mHour, mMinute;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        final String selectedDate = String.format("%02d",day) + "-" + String.format("%02d",(month+1))+ "-" + year;
                        etDate.setText(selectedDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText etTime) {
        int  mHour, mMinute,mSeconds;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSeconds = c.get(Calendar.SECOND);

        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final String selectedTime = String.format("%02d", hourOfDay)+
                                ":" + String.format("%02d", minute);
                                //+":" + String.format("%02d", 0);
                        etTime.setText(selectedTime);
                    }
                },mHour, mMinute,true);

        mTimePicker.show();
    }

}