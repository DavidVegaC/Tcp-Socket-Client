package com.example.tcpsocketclient.View.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.tcpsocketclient.Entities.Driver;
import com.example.tcpsocketclient.Entities.Plate;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Storage.DB.CRUDOperations;
import com.example.tcpsocketclient.View.Activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormDialogTransaction extends AlertDialog {

    private Activity context;
    private FragmentActivity fragmentActivity;
    private MainActivity mainActivity;
    private EscenarioSinEmbeddedFragment fragment;
    private Button btnCancelar, btnRegistrar;

    private ImageView  btnPlacaNFC;
    private TextView textPlacaNFC;
    private LinearLayout lyPlacaNFC;

    private LinearLayout lyTomarFotoVehiculo, lyVerFotoVehiculo;

    private Spinner spPlates, spDrivers;
    private EditText etPlate, etNameDriver, etCompanyPlate, etHorometro, etKilometro, etQuantityFuel, etTemperatureFuel, etStartContometer, etStartContometerRead, etEndContometer ;

    private LinearLayout lyVehiculo, lyConductor, lyTransaccion, lyTicketTransaction, lyImpresion;
    private TextView tvNameHose, tvNumTicket;
    private TextView txtCabeceraImpresion, txtFechaTransaccionImpresion, txtCantidadGalonesImpresion, txtPlacaImpresion;
    private CRUDOperations crudOperations;
    private String Tag_Button, MensajeError;
    private int idBomba=0;

    public String TAG_LAYOUT_VIEW="SIN_ABASTECER";

    //Insertar Tabla Transaccion
    private long nroTicketTransaction=0;
    private int numBomba=0;
    private String txtPlaca="";
    private String txtManguera="";
    private String txtProducto="";
    private String idConductor="";
    private String idOperator="";
    private String txtKilometraje="";
    private String txtHorometro="";
    private String fechaInicio="";
    private String horaInicio="";
    private String txtVolumen="";
    private String txtTemperatura="";
    private String txtContometroInicial="";
    private String txtContometroFinal="";
    private String fechaFin="";
    private String horaFin="";
    private String idUsuario="1";

    private Uri imageUri=null;
    private ViewImageExtended viewImageExtended;


    Double volumen;
    Double temperatura;
    Double contometroInicial;
    Double contometroFinal;

    private int escenario=0;
    private int idHardware=15;

    AlertDialog dialog;

    private TransactionEntity transactionEntity;

    //ArrayList
    List<Driver> Drivers;
    List<Plate> Plates;

    protected FormDialogTransaction(@NonNull Context context) {
        super(context);
    }

    protected FormDialogTransaction(@NonNull Context context, FragmentActivity fragmentActivity, int themeResId, CRUDOperations crudOperations, int idBomba, int escenario) {
        super(context, themeResId);
        this.context = (Activity) context;
        this.fragmentActivity = fragmentActivity;
        this.crudOperations = crudOperations;
        this.idBomba =idBomba;
        this.escenario=escenario;

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        } else {
            throw new IllegalArgumentException("context is not FormDialogListener");
        }
        TAG_LAYOUT_VIEW="SIN_ABASTECER";
    }

    protected FormDialogTransaction(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected FormDialogTransaction(@NonNull Context context, int themeResId, Fragment fragment) {
        super(context, themeResId);
        this.context = (Activity) context;
        this.fragment = (EscenarioSinEmbeddedFragment)fragment;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState)
    {

        //View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_formulario_transaction, null);
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_formulario_transaction, null);
        //setTitle("Registro Transaction");
        setView(v);
        initComponent(v);
        llenarCombos();
        super.onCreate(savedInstanceState);
    }

    private void initComponent(View v){
        btnCancelar = (Button) v.findViewById(R.id.btnCancelar);
        btnRegistrar = (Button) v.findViewById(R.id.btnRegistrar);
        //btnPlacaNFC = (Button) v.findViewById(R.id.btnPlacaNFC);

        btnPlacaNFC = (ImageView) v.findViewById(R.id.btnPlacaNFC);
        textPlacaNFC = (TextView) v.findViewById(R.id.textPlacaNFC);
        lyPlacaNFC = (LinearLayout)  v.findViewById(R.id.lyPlacaNFC);

        etPlate = (EditText)  v.findViewById(R.id.etPlate);
        etNameDriver = (EditText)  v.findViewById(R.id.etNameDriver);
        etCompanyPlate = (EditText)  v.findViewById(R.id.etCompanyPlate);
        etHorometro = (EditText)  v.findViewById(R.id.etHorometro);
        etKilometro= (EditText)  v.findViewById(R.id.etKilometro);
        etQuantityFuel = (EditText)  v.findViewById(R.id.etQuantityFuel);
        etTemperatureFuel = (EditText)  v.findViewById(R.id.etTemperatureFuel);
        etStartContometer= (EditText)  v.findViewById(R.id.etStartContometer);
        etStartContometerRead = (EditText)  v.findViewById(R.id.etStartContometerRead);
        etEndContometer = (EditText)  v.findViewById(R.id.etEndContometer);
        tvNameHose = (TextView) v.findViewById(R.id.tvNameHose);
        tvNumTicket = (TextView) v.findViewById(R.id.tvNumTicket);

        txtCabeceraImpresion = (TextView) v.findViewById(R.id.txtCabeceraImpresion);
        txtFechaTransaccionImpresion = (TextView) v.findViewById(R.id.txtFechaTransaccionImpresion);
        txtCantidadGalonesImpresion = (TextView) v.findViewById(R.id.txtCantidadGalonesImpresion);
        txtPlacaImpresion = (TextView) v.findViewById(R.id.txtPlacaImpresion);

        lyVehiculo = (LinearLayout)  v.findViewById(R.id.lyVehiculo);
        lyConductor = (LinearLayout)  v.findViewById(R.id.lyConductor);
        lyTransaccion = (LinearLayout)  v.findViewById(R.id.lyTransaccion);
        lyTicketTransaction = (LinearLayout)  v.findViewById(R.id.lyTicketTransaction);
        lyImpresion = (LinearLayout)  v.findViewById(R.id.lyImpresion);

        lyTomarFotoVehiculo = (LinearLayout)  v.findViewById(R.id.lyTomarFotoVehiculo);
        lyVerFotoVehiculo = (LinearLayout)  v.findViewById(R.id.lyVerFotoVehiculo);

        //spPlates = (Spinner) v.findViewById(R.id.spPlates);
        //spDrivers = (Spinner) v.findViewById(R.id.spDrivers);

        //SETEAR EL NOMBRE DE MANGUERA
        //SETEAR EL NUMERO DE TRANSACCION DE LA ESTACION

        /*btnPlacaNFC.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        //builder.dismiss();
                        //dialogFormTransaction.dismiss();
                        //lecturaNFCPlaca();
                        mainActivity.escenarioSinEmbeddedFragment.lecturaNFCPlaca();
                    }
                }
        );*/

        lyPlacaNFC.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Crear Cuenta...
                        //builder.dismiss();
                        //dialogFormTransaction.dismiss();
                        //lecturaNFCPlaca();
                        mainActivity.escenarioSinEmbeddedFragment.lecturaNFCPlaca();
                    }
                }
        );

        lyTomarFotoVehiculo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.escenarioSinEmbeddedFragment.imagePickDialog();
                    }
                }
        );

        lyVerFotoVehiculo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("andres","entroo");
                        if(imageUri != null){
                            mostrarImagenExtendida();
                        }else{
                            Toast.makeText(context, "Todavía no se ha tomado foto del Vehículo.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );


        btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }
        );

        btnRegistrar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tag_Button = btnRegistrar.getTag().toString();

                        switch (Tag_Button){
                            case "Tag_Placa":
                                if(validarDatosVehiculo()){
                                    etStartContometerRead.setText(""+contometroInicial);
                                    lyVehiculo.setVisibility(View.GONE);
                                    lyConductor.setVisibility(View.VISIBLE);
                                    Tag_Button="Tag_Conductor";
                                    btnRegistrar.setTag("Tag_Conductor");
                                    btnCancelar.setText("ANTERIOR");
                                    btnRegistrar.setText("INICIAR");
                                }else{
                                    Toast.makeText(context, MensajeError, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "Tag_Conductor":
                                Tag_Button="Tag_Transaccion";
                                btnRegistrar.setTag("Tag_Transaccion");
                                startTransaction();
                                //mainActivity.escenarioSinEmbeddedFragment.setearPlacaManguera(idBomba,txtPlaca);
                                //mainActivity.escenarioSinEmbeddedFragment.cambioEstado(idBomba,3);
                                mainActivity.escenarioSinEmbeddedFragment.updateHose(idBomba,transactionEntity,3);
                                lyConductor.setVisibility(View.GONE);
                                lyTransaccion.setVisibility(View.VISIBLE);
                                //btnCancelar.setVisibility(View.INVISIBLE);
                                btnCancelar.setText("CANCELAR");
                                btnRegistrar.setText("CONTINUAR");
                                //btnRegistrar.setBackgroundColor(Color.GREEN);
                                TAG_LAYOUT_VIEW="ABASTECIENDO";
                                dismiss();
                                break;
                            case "Tag_Transaccion":
                                if(validarDatosTransaccion()){
                                    if(volumen==0.0 || temperatura==0.0){
                                       createDialog();
                                    }else{
                                        finalizarTransaccion();
                                    }

                                }else{
                                    Toast.makeText(context, MensajeError, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "Tag_Impresion":
                                mainActivity.escenarioSinEmbeddedFragment.imprimirBluetooth(transactionEntity);
                                break;
                        }


                    }
                }
        );
    }

    private void mostrarImagenExtendida(){
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

    private void createDialog(){
        final TextView myView = new TextView(getContext());
        myView.setText("¿Seguro de guardar Galones: "+ volumen + " | Temperatura: C° "+temperatura +" ?");
        myView.setTextSize(15);

        final ImageView imageView = new ImageView(getContext());
        int img = R.drawable.ic_baseline_white_30;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), img);
        imageView.setImageBitmap(bmp);

        LinearLayout layout1       = new LinearLayout(getContext());
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        layout1.addView(imageView);
        layout1.addView(myView);
        layout1.setGravity(Gravity.CENTER);
        //layout1.setBackgroundColor(Color.BLUE);
        layout1.setMinimumHeight(40);


        dialog = new AlertDialog.Builder(getContext(), R.style.AppThemeAssacDialog).create();

        dialog.setTitle("Ticket "+transactionEntity.numeroTransaccion);

        dialog.setView(layout1);
        dialog.setCancelable(false);
        dialog.setButton(Dialog.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finalizarTransaccion();
            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.md_white_1000);
                dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.md_white_1000);
            }
        });
        dialog.show();
    }

    private void startTransaction() {
        //Placa , conductor, nro Ticket
        Date dateStart = new Date();
        fechaInicio = new SimpleDateFormat("dd-MM-yyyy").format(dateStart);
        horaInicio = new SimpleDateFormat("HH:mm:ss").format(dateStart);
        transactionEntity = new TransactionEntity();
        transactionEntity.setIdHardware(idHardware);
        transactionEntity.setIdBomba(numBomba);
        transactionEntity.setNombreManguera(txtManguera);
        transactionEntity.setNombreProducto(txtProducto);
        transactionEntity.setNumeroTransaccion(""+nroTicketTransaction);
        transactionEntity.setHorometro(""+Double.parseDouble(txtHorometro));
        transactionEntity.setKilometraje(""+Double.parseDouble(txtKilometraje));
        transactionEntity.setFechaInicio(fechaInicio);
        transactionEntity.setHoraInicio(horaInicio);
        transactionEntity.setPlaca(txtPlaca);
        transactionEntity.setIdConductor(idConductor);
        transactionEntity.setIdOperador(idOperator);
        transactionEntity.setIdUsuarioRegistro(idUsuario);
        transactionEntity.setEstadoRegistro("N");
        transactionEntity.setEscenario(escenario);
        transactionEntity.setImageUri(imageUri.toString());
        txtContometroInicial= String.format("%.2f", contometroInicial);
        transactionEntity.setContometroInicial(txtContometroInicial);
        int idSqlTransaction = crudOperations.addTransaction(transactionEntity);
        Log.v("NumTransaction",""+idSqlTransaction);
        crudOperations.incrementTicketStation(numBomba);

    }

    private void endTransaction() {
        //Placa , conductor, nro Ticket
        Date dateStart = new Date();
        fechaFin = new SimpleDateFormat("dd-MM-yyyy").format(dateStart);
        horaFin = new SimpleDateFormat("HH:mm:ss").format(dateStart);
        transactionEntity.setVolumen(txtVolumen);
        transactionEntity.setTemperatura(txtTemperatura);
        //transactionEntity.setContometroInicial(txtContometroInicial);
        transactionEntity.setContometroFinal(txtContometroFinal);
        transactionEntity.setFechaFin(fechaFin);
        transactionEntity.setHoraFin(horaFin);
        transactionEntity.setEstadoRegistro("P");
        int UpdateTransaction= crudOperations.updateTransaction(transactionEntity);
        Log.v("UpdateTransaction",""+UpdateTransaction);

    }

    private void finalizarTransaccion(){
        txtVolumen= String.format("%.2f", volumen);
        txtTemperatura=String.format("%.2f", temperatura);
        //txtContometroInicial= String.format("%.2f", contometroInicial);
        txtContometroFinal= String.format("%.2f", contometroFinal);
        endTransaction();
        mainActivity.escenarioSinEmbeddedFragment.updateHose(idBomba,transactionEntity,0);
        Tag_Button="Tag_Impresion";
        btnRegistrar.setTag("Tag_Impresion");
        btnCancelar.setText("SALIR");
        btnRegistrar.setText("IMPRIMIR");

        txtCabeceraImpresion.setText("Ticket: " + transactionEntity.getNumeroTransaccion() + " | " + "Estación: Pionero " );
        txtFechaTransaccionImpresion.setText(transactionEntity.getFechaInicio() + " | Inicio: " + transactionEntity.getHoraInicio() +" - Fin: "+ transactionEntity.getHoraFin());
        txtCantidadGalonesImpresion.setText("" + transactionEntity.getVolumen() + " gal" + " | Temperatura: °C " + transactionEntity.getTemperatura());
        txtPlacaImpresion.setText("" + transactionEntity.getPlaca());

        lyTicketTransaction.setVisibility(View.GONE);
        lyTransaccion.setVisibility(View.GONE);
        lyImpresion.setVisibility(View.VISIBLE);

        TAG_LAYOUT_VIEW="ABASTECIDO";

    }

    private boolean validarDatosTransaccion(){
        boolean response=false;

        txtVolumen=etQuantityFuel.getText().toString().trim();
        try{
            volumen = Double.parseDouble(txtVolumen);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Cantidad.";
            return response;
        }

        txtTemperatura=etTemperatureFuel.getText().toString().trim();
        try{
            temperatura = Double.parseDouble(txtTemperatura);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Temperatura.";
            return response;
        }

        /*txtContometroInicial=etStartContometerRead.getText().toString().trim();
        try{
            contometroInicial = Double.parseDouble(txtContometroInicial);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Contómetro Inicial o en su defecto colocar 0.";
            return response;
        }*/

        txtContometroFinal=etEndContometer.getText().toString().trim();
        try{
            contometroFinal = Double.parseDouble(txtContometroFinal);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Contómetro Final o en su defecto colocar 0.";
            return response;
        }

        if(contometroInicial!=0.0 || contometroFinal!=0.0 ){
            if(contometroFinal<=contometroInicial){
                MensajeError="El contómetro final debe ser mayor al contómetro Inicial.";
                return response;
            }

        }

        response=true;

        return response;
    }

    private boolean validarDatosVehiculo(){
        boolean response=false;

        txtPlaca=etPlate.getText().toString().trim();
        if(txtPlaca.equals("")){
            MensajeError="Debe completar el campo Placa.";
            return response;
        }

        if(imageUri == null){
            MensajeError="Debe tomar una foto del Vehículo.";
            return response;
        }

        txtHorometro=etHorometro.getText().toString().trim();
        try{
            Double Horometro = Double.parseDouble(txtHorometro);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Horómetro.";
            return response;
        }

        txtKilometraje=etKilometro.getText().toString().trim();
        try{
            Double Kilometro = Double.parseDouble(txtKilometraje);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Kilómetro.";
            return response;
        }

        txtContometroInicial=etStartContometer.getText().toString().trim();
        try{
            contometroInicial = Double.parseDouble(txtContometroInicial);
        }catch(Exception e){
            MensajeError="Ingresar un valor correcto para el campo Contómetro Inicial o en su defecto colocar 0.";
            return response;
        }

        List<TransactionEntity> lst = crudOperations.getDuplicatePlate(txtPlaca,escenario);

        if(lst.size()!=0){
            MensajeError="La placa "+ txtPlaca + " se encuentra en proceso de abastecimiento en la manguera "+lst.get(0).nombreManguera;
            return response;
        }

        response=true;

        return response;
    }

    private void llenarCombos(){
        llenarDrivers();
        llenarPlates();
    }

    private void llenarDrivers(){
        Drivers = new ArrayList<Driver>();
        ArrayList<String> DriverString = new ArrayList<String>();

        Drivers=crudOperations.getAllDriver();

        for (Driver driver:Drivers) {
            DriverString.add(driver.getDriverName());
        }

        /*ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.spinner_theme_1,DriverString);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_theme_1);
        spDrivers.setAdapter(adapter);*/

    }

    private void llenarPlates(){
        Plates = new ArrayList<Plate>();
        ArrayList<String> PlateString = new ArrayList<String>();

        Plates=crudOperations.getAllPlate();
        for (Plate plate:Plates) {
            PlateString.add(plate.getVehicleCodePlate());
        }

        /*ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.spinner_theme_1,PlateString);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_theme_1);
        spPlates.setAdapter(adapter);*/
    }

    private void limpiarFormulario(){
        etPlate.setText("");
        etCompanyPlate.setText("");
        etHorometro.setText("");
        etKilometro.setText("");
        imageUri=null;
        cambiarBotonSinScanear();
        lyVerFotoVehiculo.setBackgroundResource(R.drawable.bg_para_boton_inactivo);
        mainActivity.escenarioSinEmbeddedFragment.disableForegroundDispatchSystem();
        mainActivity.escenarioSinEmbeddedFragment.cambioEstado(idBomba,0);
    }

    public void escribirNumeroTicket(){
        nroTicketTransaction = crudOperations.getNumTicketStation();
        tvNumTicket.setText("TICKET NRO. "+ String.format("%06d", nroTicketTransaction));
    }

    public void escribirManguera(String manguera, int numeroBomba, String producto, int IdHardware){
        tvNameHose.append(" "+manguera);
        numBomba=numeroBomba;
        txtManguera=manguera;
        txtProducto=producto;
        idHardware=IdHardware;
    }

    public void escribirPlaca(String plate){
        Log.v("Prueba",plate);
        List<Plate> lst =new ArrayList<Plate>();
        lst = crudOperations.getPlate(plate);

        if(lst.size()>0){
            if(lst.get(0).getStatus().equals("A")){
                etPlate.setText(lst.get(0).VehicleCodePlate);
                etCompanyPlate.setText(lst.get(0).Company);
            }else{
                Toast.makeText(context, "La placa "+plate+" se encuentra INACTIVA. \nNo puede ser usada.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, "No se detectó  a la placa "+plate+" en el sistema.", Toast.LENGTH_LONG).show();
        }

        //cambiarBotonSinScanear();
    }

    public void cambiarBotonScaneando(){
        //btnPlacaNFC.setText("ESCANEANDO");
        textPlacaNFC.setText("ESCANEANDO");
        //lyPlacaNFC.setBackgroundColor(Color.GREEN);
        lyPlacaNFC.setBackgroundResource(R.drawable.bg_para_boton_usando);
        lyPlacaNFC.setEnabled(false);
    }

    public void cambiarBotonSinScanear(){
        //btnPlacaNFC.setText("ESCANEAR");
        textPlacaNFC.setText("ESCANEAR");
        lyPlacaNFC.setBackgroundResource(R.drawable.bg_para_boton);
        lyPlacaNFC.setEnabled(true);
    }

    public void escribirUriImagen(Uri imageUri){
        this.imageUri=imageUri;
        lyVerFotoVehiculo.setBackgroundResource(R.drawable.bg_para_boton_usando);
    }

    public void restaurarTransaccionDialogo(TransactionEntity entity){
        Tag_Button="Tag_Transaccion";
        btnRegistrar.setTag("Tag_Transaccion");

        etStartContometerRead.setText(entity.contometroInicial);
        contometroInicial = Double.parseDouble(entity.contometroInicial);
        transactionEntity = new TransactionEntity();
        transactionEntity.setIdBomba(numBomba);
        transactionEntity.setNumeroTransaccion(entity.numeroTransaccion);
        transactionEntity.setPlaca(entity.placa);
        transactionEntity.setFechaInicio(entity.fechaInicio);
        transactionEntity.setHoraInicio(entity.horaInicio);
        transactionEntity.setEscenario(escenario);
        transactionEntity.setNombreManguera(entity.nombreManguera);
        transactionEntity.setNombreProducto(entity.nombreProducto);
        transactionEntity.setContometroInicial(entity.contometroInicial);

        tvNumTicket.setText("TICKET NRO. "+ String.format("%06d", Integer.parseInt(entity.numeroTransaccion)));
        lyVehiculo.setVisibility(View.GONE);
        lyConductor.setVisibility(View.GONE);
        lyTransaccion.setVisibility(View.VISIBLE);
        btnCancelar.setText("CANCELAR");
        btnRegistrar.setText("CONTINUAR");

        TAG_LAYOUT_VIEW="ABASTECIENDO";
    }

    @Override
    public void dismiss() {
        Tag_Button = btnRegistrar.getTag().toString();
        switch (Tag_Button){
            case "Tag_Placa":
                limpiarFormulario();
                super.dismiss();
                break;
            case "Tag_Conductor":
                lyConductor.setVisibility(View.GONE);
                lyVehiculo.setVisibility(View.VISIBLE);
                Tag_Button="Tag_Placa";
                btnRegistrar.setTag("Tag_Placa");
                btnCancelar.setText("CANCELAR");
                btnRegistrar.setText("SIGUIENTE");
                break;
            case "Tag_Transaccion":
                super.dismiss();
                break;
            case "Tag_Impresion":
                super.dismiss();
                break;
        }
    }
}
