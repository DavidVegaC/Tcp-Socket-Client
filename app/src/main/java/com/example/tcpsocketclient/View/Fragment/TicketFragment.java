package com.example.tcpsocketclient.View.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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
import android.widget.Toast;

import com.example.tcpsocketclient.Adapters.RVAdapterForTransaction;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.DB.CRUDOperations;
import com.example.tcpsocketclient.Storage.DB.MyDatabase;
import com.example.tcpsocketclient.Util.Bluetooth.ImpresionTicketAsync;
import com.example.tcpsocketclient.Util.Bluetooth.PrinterBluetooth;
import com.example.tcpsocketclient.Util.CustomProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private PrinterBluetooth printerBluetooth;
    private int escenario=0;

    SwipeRefreshLayout SwipeTicketFragment;
    LinearLayout btnBuscarTicketFragment;
    EditText txtTicketFragment;
    EditText txtDateTransaction;
    TextView txtMensajeTickets;
    RadioButton radio1,radio2,radio3;
    RecyclerView rvListaTicket;

    AlertDialog dialog;
    CustomProgressDialog mDialog;
    ImpresionTicketAsync impresionTicketAsync;

    CRUDOperations crudOperations;

    LinearLayout ly_migracion_ambos,ly_migracion1, ly_migracion2;

    Thread thread;
    Handler handler = new Handler();
    private FragmentActivity fragmentActivity;

    //List<TransactionEntity> listTransaction = null;

    private ViewImageExtended viewImageExtended;

    public TicketFragment() {
        // Required empty public constructor
    }

    public TicketFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity=fragmentActivity;
    }

    public TicketFragment(FragmentActivity fragmentActivity, int escenario) {
        this.fragmentActivity=fragmentActivity;
        this.escenario=escenario;
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

        initComponent();

        loadTransaction();

    }


    private void initComponent(){
        SwipeTicketFragment =  rootView.findViewById(R.id.SwipeTicketFragment);
        btnBuscarTicketFragment =  rootView.findViewById(R.id.btnBuscarTicketFragment);
        txtTicketFragment =  rootView.findViewById(R.id.txtTicketFragment);
        txtMensajeTickets =  rootView.findViewById(R.id.txtMensajeTickets);

        radio1 =  rootView.findViewById(R.id.radio1);
        radio2 =  rootView.findViewById(R.id.radio2);
        radio3 =  rootView.findViewById(R.id.radio3);

        txtDateTransaction = rootView.findViewById(R.id.txtDateTransaction);
        txtDateTransaction.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        rvListaTicket =  rootView.findViewById(R.id.rvListaTicket);
        rvListaTicket.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvListaTicket.setLayoutManager(llm);
        crudOperations = new CRUDOperations(new MyDatabase(getContext()));
        mDialog = new CustomProgressDialog(getContext());

        printerBluetooth = new PrinterBluetooth();

        ly_migracion_ambos = rootView.findViewById(R.id.ly_migracion_ambos);
        ly_migracion1 = rootView.findViewById(R.id.ly_migracion1);
        ly_migracion2 = rootView.findViewById(R.id.ly_migracion2);

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

        txtDateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(fragmentActivity, "Holaaa", Toast.LENGTH_SHORT).show();
                showDatePickerDialog(txtDateTransaction);
            }
        });

        ly_migracion_ambos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio1.setChecked(true);
            }
        });

        ly_migracion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2.setChecked(true);

            }
        });

        ly_migracion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio3.setChecked(true);
            }
        });



    }

    public void setEscenario(int escenario){
        this.escenario=escenario;
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

        int estadoMigracionTransaccion=0;

        if(radio1.isChecked()){
            estadoMigracionTransaccion=0;
        }else if(radio2.isChecked()){
            estadoMigracionTransaccion=1;
        }else if(radio3.isChecked()){
            estadoMigracionTransaccion=2;
        }

        String pNroTransaccion = txtTicketFragment.getText().toString().trim();
        String fechaTransaccion = txtDateTransaction.getText().toString().trim();

        //final List<TransactionEntity> lst =crudOperations.getTransaction(pNroTransaccion, escenario,"P");

        Log.v("Parametros ", estadoMigracionTransaccion + " - "+pNroTransaccion+" - "+fechaTransaccion);

        final List<TransactionEntity> lst =crudOperations.getTransactionforDuplicateTicket(pNroTransaccion, fechaTransaccion,estadoMigracionTransaccion,"P",escenario);


        if(lst.size()> 0){
            txtMensajeTickets.setVisibility(View.GONE);
            rvListaTicket.setVisibility(View.VISIBLE);
            final RVAdapterForTransaction adapter = new RVAdapterForTransaction(lst);
            adapter.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(final View v) {

                    if(v.getId()==R.id.lyVerFotoVehiculo){
                        Log.v("Foto",""+Integer.parseInt(v.getTag().toString()));
                        String imageUri = lst.get(Integer.parseInt(v.getTag().toString())).getImageUri();
                        if(imageUri!= null)
                            mostrarImagenExtendida(Uri.parse(imageUri));
                        else
                            Toast.makeText(getContext(),"No se ha detectado foto guardada.",Toast.LENGTH_LONG).show();

                    }else {
                        String nroTicket = "" + lst.get(rvListaTicket.getChildAdapterPosition(v)).getNumeroTransaccion();

                        final TextView myView = new TextView(getContext());
                        myView.setText("¿Seguro de imprimir el ticket " + nroTicket + " ?");
                        myView.setTextSize(15);

                        final ImageView imageView = new ImageView(getContext());
                        int img = R.drawable.ic_baseline_white_30;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), img);
                        imageView.setImageBitmap(bmp);

                        LinearLayout layout1 = new LinearLayout(getContext());
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
                                //new ImpresionTicketAsync(lst.get(rvListaTicket.getChildAdapterPosition(v))).execute();
                                new ImpresionTicketAsync(lst.get(rvListaTicket.getChildAdapterPosition(v)), printerBluetooth, mDialog, getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        });
                        dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(),"CANCEL",Toast.LENGTH_LONG).show();

                            }
                        });

                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.md_text_white);
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.md_text_white);
                            }
                        });
                        dialog.show();
                    }
                }
            });
            rvListaTicket.setAdapter(adapter);
        }else{
                txtMensajeTickets.setVisibility(View.VISIBLE);
                rvListaTicket.setVisibility(View.GONE);
        }

        SwipeTicketFragment.setRefreshing(false);
    }

    private void mostrarImagenExtendida(Uri imageUri){
        if(viewImageExtended == null || viewImageExtended.getDialog() == null || !viewImageExtended.getDialog().isShowing()){
            // Si estas en un fragment y pasaste el activity en el constructor
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            Bundle arguments = new Bundle();
            // Aqui le pasas el bitmap de la imagen
            arguments.putParcelable("PICTURE_SELECTED", imageUri);
            viewImageExtended = ViewImageExtended.newInstance(arguments);
            viewImageExtended.show(fm, "ViewImageExtended");
        }
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

}