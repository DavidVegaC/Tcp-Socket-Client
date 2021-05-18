package com.example.tcpsocketclient.Storage.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.tcpsocketclient.Entities.Driver;
import com.example.tcpsocketclient.Entities.Hardware;
import com.example.tcpsocketclient.Entities.Hose;
import com.example.tcpsocketclient.Entities.Maestros;
import com.example.tcpsocketclient.Entities.MigrationEntity;
import com.example.tcpsocketclient.Entities.Operator;
import com.example.tcpsocketclient.Entities.PersonEntity;
import com.example.tcpsocketclient.Entities.Plate;
import com.example.tcpsocketclient.Entities.Transaction;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.Entities.TransactionsPendingEntity;
import com.example.tcpsocketclient.Entities.UserEntity;
import com.example.tcpsocketclient.Util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CRUDOperations {

    private MyDatabase helper;
    public CRUDOperations(SQLiteOpenHelper _helper) {
        super();
        // TODO Auto-generated constructor stub
        helper =(MyDatabase)_helper;
    }

    //TB_USER
    public int addUser(UserEntity userEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase(); //modo escritura
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_ID_TB_USER, userEntity.getIdUser());
        values.put(MyDatabase.KEY_ID_PERSON_TB_USER, userEntity.getIdPerson());
        values.put(MyDatabase.KEY_PERSON_NAME_TB_USER, userEntity.getPersonName());
        values.put(MyDatabase.KEY_FIRST_LAST_NAME_TB_USER, userEntity.getFirstLastName());
        values.put(MyDatabase.KEY_SECOND_LAST_NAME_TB_USER, userEntity.getSecondLastName());
        values.put(MyDatabase.KEY_PHOTOCHECK_TB_USER, userEntity.getPhotocheck());
        values.put(MyDatabase.KEY_USER_TB_USER, userEntity.getUUser().toUpperCase());
        values.put(MyDatabase.KEY_PASSWORD_TB_USER, userEntity.getUPassword());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_USER, userEntity.getRegistrationStatus());

        int row = (int) db.insert(MyDatabase.TB_USER, null, values);
        db.close();
        return row;
    }

    public int updateUser(UserEntity userEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_ID_PERSON_TB_USER, userEntity.getIdPerson());
        values.put(MyDatabase.KEY_PERSON_NAME_TB_USER, userEntity.getPersonName());
        values.put(MyDatabase.KEY_FIRST_LAST_NAME_TB_USER, userEntity.getFirstLastName());
        values.put(MyDatabase.KEY_SECOND_LAST_NAME_TB_USER, userEntity.getSecondLastName());
        values.put(MyDatabase.KEY_PHOTOCHECK_TB_USER, userEntity.getPhotocheck());
        values.put(MyDatabase.KEY_USER_TB_USER, userEntity.getUUser().toUpperCase());
        values.put(MyDatabase.KEY_PASSWORD_TB_USER, userEntity.getUPassword());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_USER, userEntity.getRegistrationStatus());

        int row =db.update(MyDatabase.TB_USER,
                values,
                MyDatabase.KEY_ID_TB_USER+"=?",
                new String[]{String.valueOf(userEntity.getIdUser())});
        db.close();

        return row;
    }

    public int deleteUser(UserEntity userEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row= db.delete(MyDatabase.TB_USER,
                MyDatabase.KEY_ID_TB_USER+"=?",
                new String[]{String.valueOf(userEntity.getIdUser())});
        db.close();
        return row;
    }

    public UserEntity getUser(int id)
    {
        UserEntity userEntity= new UserEntity();
        SQLiteDatabase db = helper.getReadableDatabase(); //modo lectura
        Cursor cursor = db.query(MyDatabase.TB_USER,
                new String[]{
                        MyDatabase.KEY_ID_SQLLITE_TB_USER,
                        MyDatabase.KEY_ID_TB_USER,
                        MyDatabase.KEY_ID_PERSON_TB_USER,
                        MyDatabase.KEY_PERSON_NAME_TB_USER,
                        MyDatabase.KEY_FIRST_LAST_NAME_TB_USER,
                        MyDatabase.KEY_SECOND_LAST_NAME_TB_USER,
                        MyDatabase.KEY_PHOTOCHECK_TB_USER,
                        MyDatabase.KEY_USER_TB_USER,
                        MyDatabase.KEY_PASSWORD_TB_USER,
                        MyDatabase.KEY_REGISTRATION_STATUS_TB_USER},
                MyDatabase.KEY_ID_TB_USER + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if(cursor!=null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int idSqlLite = Integer.parseInt(cursor.getString(0));
            int IdUser = Integer.parseInt(cursor.getString(1));
            int IdPerson = Integer.parseInt(cursor.getString(2));
            String PersonName = cursor.getString(3);
            String FirstLastName = cursor.getString(4);
            String SecondLastName = cursor.getString(5);
            String Photocheck = cursor.getString(6);
            String UUser = cursor.getString(7);
            String UPassword = cursor.getString(8);
            String RegistrationStatus = cursor.getString(9);

            userEntity= new UserEntity(idSqlLite, IdUser,IdPerson, PersonName,Photocheck,FirstLastName,SecondLastName,UUser,UPassword,RegistrationStatus);
        }

        return userEntity;
    }

    public UserEntity getUserForLogin(String user, String password)
    {
        UserEntity userEntity= new UserEntity();
        SQLiteDatabase db = helper.getReadableDatabase(); //modo lectura
        Cursor cursor = db.query(MyDatabase.TB_USER,
                new String[]{
                        MyDatabase.KEY_ID_SQLLITE_TB_USER,
                        MyDatabase.KEY_ID_TB_USER,
                        MyDatabase.KEY_ID_PERSON_TB_USER,
                        MyDatabase.KEY_PERSON_NAME_TB_USER,
                        MyDatabase.KEY_FIRST_LAST_NAME_TB_USER,
                        MyDatabase.KEY_SECOND_LAST_NAME_TB_USER,
                        MyDatabase.KEY_PHOTOCHECK_TB_USER,
                        MyDatabase.KEY_USER_TB_USER,
                        MyDatabase.KEY_PASSWORD_TB_USER,
                        MyDatabase.KEY_REGISTRATION_STATUS_TB_USER},
                MyDatabase.KEY_USER_TB_USER + "=? AND "+ MyDatabase.KEY_PASSWORD_TB_USER + "=?",
                new String[]{user,password}, null, null, null);
        if(cursor!=null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int idSqlLite = Integer.parseInt(cursor.getString(0));
            int IdUser = Integer.parseInt(cursor.getString(1));
            int IdPerson = Integer.parseInt(cursor.getString(2));
            String PersonName = cursor.getString(3);
            String FirstLastName = cursor.getString(4);
            String SecondLastName = cursor.getString(5);
            String Photocheck = cursor.getString(6);
            String UUser = cursor.getString(7);
            String UPassword = cursor.getString(8);
            String RegistrationStatus = cursor.getString(9);

            userEntity= new UserEntity(idSqlLite, IdUser, IdPerson, PersonName,Photocheck,FirstLastName,SecondLastName,UUser,UPassword,RegistrationStatus);
        }

        return userEntity;
    }

    public List<UserEntity> getAllUsers()
    {
        List<UserEntity> lst =new ArrayList<UserEntity>();
        String sql= "SELECT  * FROM " + MyDatabase.TB_USER;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                UserEntity userEntity =new UserEntity();
                userEntity.setIdSqlLite(Integer.parseInt(cursor.getString(0)));
                userEntity.setIdUser(Integer.parseInt(cursor.getString(1)));
                userEntity.setPersonName(cursor.getString(2));
                userEntity.setFirstLastName(cursor.getString(3));
                userEntity.setSecondLastName(cursor.getString(4));
                userEntity.setPhotocheck(cursor.getString(5));
                userEntity.setUUser(cursor.getString(6));
                userEntity.setUPassword(cursor.getString(7));
                userEntity.setRegistrationStatus(cursor.getString(8));

                lst.add(userEntity);
            }while(cursor.moveToNext());
        }
        return lst;
    }

    public int getUserCount()
    {
        int totalCount = 0;
        String sql= "SELECT * FROM "+MyDatabase.TB_USER;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        totalCount = cursor.getCount();
        cursor.close();

        return totalCount;
    }

    //ACTIVIDAD
    public int addTransaction(TransactionEntity transactionEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase(); //modo escritura
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_ID_TB_TRANSACTION, transactionEntity.getIdTransaction());
        values.put(MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION, transactionEntity.getNumeroTransaccion());
        values.put(MyDatabase.KEY_HARDWARE_ID_TB_TRANSACTION, transactionEntity.getIdHardware());
        values.put(MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION, transactionEntity.getIdBomba());
        values.put(MyDatabase.KEY_HOSE_NAME_TB_TRANSACTION, transactionEntity.getNombreManguera());
        values.put(MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION, transactionEntity.getIdVehiculo());
        values.put(MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION, transactionEntity.getPlaca());
        values.put(MyDatabase.KEY_VEHICLE_HOROMETER_TB_TRANSACTION, transactionEntity.getHorometro());
        values.put(MyDatabase.KEY_VEHICLE_KILOMETER_TB_TRANSACTION, transactionEntity.getKilometraje());
        values.put(MyDatabase.KEY_START_DATE_TB_TRANSACTION, transactionEntity.getFechaInicio());
        values.put(MyDatabase.KEY_START_HOUR_TB_TRANSACTION, transactionEntity.getHoraInicio());
        values.put(MyDatabase.KEY_END_DATE_TB_TRANSACTION, transactionEntity.getFechaFin());
        values.put(MyDatabase.KEY_END_HOUR_TB_TRANSACTION, transactionEntity.getHoraFin());
        values.put(MyDatabase.KEY_CONDUCTOR_KEY_TB_TRANSACTION, transactionEntity.getIdConductor());
        values.put(MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION, transactionEntity.getVolumen());
        values.put(MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION, transactionEntity.getTemperatura());
        values.put(MyDatabase.KEY_COMMENT_TB_TRANSACTION, transactionEntity.getComentario());
        values.put(MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION, transactionEntity.getNombreProducto());
        values.put(MyDatabase.KEY_OPERATOR_KEY_TB_TRANSACTION, transactionEntity.getIdOperador());
        values.put(MyDatabase.KEY_START_CONTOMETER_TB_TRANSACTION, transactionEntity.getContometroInicial());
        values.put(MyDatabase.KEY_END_CONTOMETER_TB_TRANSACTION, transactionEntity.getContometroFinal());
        values.put(MyDatabase.KEY_IMAGE_URI_TB_TRANSACTION, transactionEntity.getImageUri());
        values.put(MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION, transactionEntity.getIdUsuarioRegistro());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION, transactionEntity.getEstadoRegistro());
        values.put(MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION, transactionEntity.getEstadoMigracion());
        values.put(MyDatabase.KEY_SCENE_TB_TRANSACTION, transactionEntity.getEscenario());
        int row = (int) db.insert(MyDatabase.TB_TRANSACTION, null, values);
        db.close();
        return row;
    }

    public void incrementTicketStation(int numManguera){
        int numTicket=getNumTicketStation();
        SQLiteDatabase db = helper.getWritableDatabase(); //modo escritura
        String sqlStation ="UPDATE "+MyDatabase.TB_HARDWARE+" SET "+MyDatabase.KEY_LAST_TICKET_STATION_TB_HARDWARE +" = "+ numTicket;
        db.execSQL(sqlStation);

        String sqlHose ="UPDATE "+MyDatabase.TB_HOSE+" SET "+MyDatabase.KEY_LAST_TICKET_TB_HOSE +" = "+ numTicket;

        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_LAST_TICKET_TB_HOSE, numTicket);
        values.put(MyDatabase.KEY_LAST_QUANTITY_FUEL_TB_HOSE, 0.0);

        int row =db.update(MyDatabase.TB_HOSE,
                values,
                MyDatabase.KEY_NUMBER_HOSE_TB_HOSE+"=?",
                new String[]{String.valueOf(numManguera)});

        db.close();
    }

    public int updateTransaction(TransactionEntity transactionEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = null;
        values = new ContentValues();
        values.put(MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION, transactionEntity.getVolumen());
        values.put(MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION, transactionEntity.getTemperatura());
        values.put(MyDatabase.KEY_END_DATE_TB_TRANSACTION, transactionEntity.getFechaFin());
        values.put(MyDatabase.KEY_END_HOUR_TB_TRANSACTION, transactionEntity.getHoraFin());
        //values.put(MyDatabase.KEY_START_CONTOMETER_TB_TRANSACTION, transactionEntity.getContometroInicial());
        values.put(MyDatabase.KEY_END_CONTOMETER_TB_TRANSACTION, transactionEntity.getContometroFinal());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION, transactionEntity.getEstadoRegistro());

        int row =db.update(MyDatabase.TB_TRANSACTION,
                values,
                MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION+"=? AND "+MyDatabase.KEY_SCENE_TB_TRANSACTION +"=?",
                new String[]{String.valueOf(transactionEntity.numeroTransaccion), String.valueOf(transactionEntity.escenario) });

        values = new ContentValues();
        values.put(MyDatabase.KEY_LAST_QUANTITY_FUEL_TB_HOSE, transactionEntity.getVolumen());

        db.update(MyDatabase.TB_HOSE,
                values,
                MyDatabase.KEY_NUMBER_HOSE_TB_HOSE+"=?",
                new String[]{String.valueOf(transactionEntity.getIdBomba()) });

        db.close();

        return row;

    }

    public List<TransactionEntity> getTransactionByTransactionAndHose(String nroTransaccion, int escenario, String estado)
    {
        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();
        //String fecha = "'07/05/2020'";
        //String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // String todayX = "'"+fecha + "'";
        String sql = "";
        sql = "SELECT " +
                " RA." + MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_COMMENT_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_IMAGE_URI_TB_TRANSACTION +" , " +

                " RA." +MyDatabase.KEY_START_CONTOMETER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_CONTOMETER_TB_TRANSACTION +" , " +

                " RA." +MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION  +
                " FROM " + MyDatabase.TB_TRANSACTION + " RA " +
                " WHERE RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION + " = " + nroTransaccion+
                " AND RA." + MyDatabase.KEY_SCENE_TB_TRANSACTION + " = "+escenario+
                " AND RA."+ MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" = '"+estado+"' ";

        //" AND RA." + MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION + " = " + nroBomba;


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //Log.v("CRUDOPERATIONS",sql2);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                int idSqlite = cursor.getInt(0);
                int idTransaction = cursor.getInt(1);
                int ticketNumber = cursor.getInt(2);
                int hoseNumber = cursor.getInt(3);
                String hoseName = ""+cursor.getString(4);
                String startDate = ""+cursor.getString(5);
                String startHour = ""+cursor.getString(6);
                String endDate = ""+cursor.getString(7);
                String endHour = ""+cursor.getString(8);
                String vehicleId = ""+cursor.getString(9);
                String vehicleCodePlate = ""+cursor.getString(10);
                String fuelQuantity = ""+cursor.getString(11);
                String fuelTemperature = ""+cursor.getString(12);
                String productName = ""+cursor.getString(13);
                String comment = ""+cursor.getString(14);
                String imageUri = ""+cursor.getString(15);
                String startContometer = ""+cursor.getString(16);
                String endContometer = ""+cursor.getString(17);
                String idRegistrationUser = ""+cursor.getString(18);
                String registrationStatus = ""+cursor.getString(19);
                String migrationStatus = ""+cursor.getString(20);

                transactionEntity.setIdSqlite(idSqlite);
                transactionEntity.setIdTransaction(idTransaction);
                transactionEntity.setNumeroTransaccion(""+ticketNumber);
                transactionEntity.setIdBomba(hoseNumber);
                transactionEntity.setNombreManguera(hoseName);
                transactionEntity.setFechaInicio(startDate);
                transactionEntity.setHoraInicio(startHour);
                transactionEntity.setFechaFin(endDate);
                transactionEntity.setHoraFin(endHour);
                transactionEntity.setIdVehiculo(vehicleId);
                transactionEntity.setPlaca(vehicleCodePlate);
                transactionEntity.setVolumen(fuelQuantity);
                transactionEntity.setTemperatura(fuelTemperature);
                transactionEntity.setNombreProducto(productName);
                transactionEntity.setComentario(comment);
                transactionEntity.setImageUri(imageUri);
                transactionEntity.setContometroInicial(startContometer);
                transactionEntity.setContometroFinal(endContometer);
                transactionEntity.setIdUsuarioRegistro(idRegistrationUser);
                transactionEntity.setEstadoRegistro(registrationStatus);
                transactionEntity.setEstadoMigracion(migrationStatus);

                lst.add(transactionEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public List<TransactionEntity> getTransaction(String nroTransaccion, int escenario, String estado)
    {
        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();
        //String fecha = "'07/05/2020'";
        //String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // String todayX = "'"+fecha + "'";
        String sql = "";
        sql = "SELECT " +
                " RA." +MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_COMMENT_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_IMAGE_URI_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION  +
                " FROM " + MyDatabase.TB_TRANSACTION + " RA " +
                " WHERE RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" LIKE '%" + nroTransaccion + "%' " +
                " AND RA." +MyDatabase.KEY_SCENE_TB_TRANSACTION + " = "+escenario+
                " AND RA."+ MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" = '"+estado+"' "+
                " ORDER BY RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION + " DESC ";


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //Log.v("CRUDOPERATIONS",sql2);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                int idSqlite = cursor.getInt(0);
                int idTransaction = cursor.getInt(1);
                int ticketNumber = cursor.getInt(2);
                int hoseNumber = cursor.getInt(3);
                String hoseName = ""+cursor.getString(4);
                String startDate = ""+cursor.getString(5);
                String startHour = ""+cursor.getString(6);
                String endDate = ""+cursor.getString(7);
                String endHour = ""+cursor.getString(8);
                String vehicleId = ""+cursor.getString(9);
                String vehicleCodePlate = ""+cursor.getString(10);
                String fuelQuantity = ""+cursor.getString(11);
                String fuelTemperature = ""+cursor.getString(12);
                String productName = ""+cursor.getString(13);
                String comment = ""+cursor.getString(14);
                String imageUri = ""+cursor.getString(15);
                String idRegistrationUser = ""+cursor.getString(16);
                String registrationStatus = ""+cursor.getString(17);
                String migrationStatus = ""+cursor.getString(18);

                transactionEntity.setIdSqlite(idSqlite);
                transactionEntity.setIdTransaction(idTransaction);
                transactionEntity.setNumeroTransaccion(""+ticketNumber);
                transactionEntity.setIdBomba(hoseNumber);
                transactionEntity.setNombreManguera(hoseName);
                transactionEntity.setFechaInicio(startDate);
                transactionEntity.setHoraInicio(startHour);
                transactionEntity.setFechaFin(endDate);
                transactionEntity.setHoraFin(endHour);
                transactionEntity.setIdVehiculo(vehicleId);
                transactionEntity.setPlaca(vehicleCodePlate);
                transactionEntity.setVolumen(fuelQuantity);
                transactionEntity.setTemperatura(fuelTemperature);
                transactionEntity.setNombreProducto(productName);
                transactionEntity.setComentario(comment);
                transactionEntity.setImageUri(imageUri);
                transactionEntity.setIdUsuarioRegistro(idRegistrationUser);
                transactionEntity.setEstadoRegistro(registrationStatus);
                transactionEntity.setEstadoMigracion(migrationStatus);

                lst.add(transactionEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public List<TransactionEntity> getTransactionforDuplicateTicket(String nroTransaccion, String fechaTransaccion,int estadoMigracionTransaccion, String estado, int escenario)
    {

        String sqlMigration = "";

        switch (estadoMigracionTransaccion){
            case 0: sqlMigration = " ";
                break;
            case 1: sqlMigration = " AND RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION + " = '"+"M"+"' ";
                break;
            case 2: sqlMigration = " AND RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION + " IS NULL ";
                break;
            default:
                sqlMigration = "";
                break;
        }

        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();
        //String fecha = "'07/05/2020'";
        //String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // String todayX = "'"+fecha + "'";
        String sql = "";
        sql = "SELECT " +
                " RA." +MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_COMMENT_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_IMAGE_URI_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION  +
                " FROM " + MyDatabase.TB_TRANSACTION + " RA " +
                " WHERE RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" LIKE '%" + nroTransaccion + "%' " +
                " AND RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION + " = '"+fechaTransaccion+"'"+
                " AND RA." +MyDatabase.KEY_SCENE_TB_TRANSACTION + " = "+escenario+
                " AND RA."+ MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" = '"+estado+"' "+
                sqlMigration +
                " ORDER BY RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION + " DESC ";


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //Log.v("CRUDOPERATIONS",sql2);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                int idSqlite = cursor.getInt(0);
                int idTransaction = cursor.getInt(1);
                int ticketNumber = cursor.getInt(2);
                int hoseNumber = cursor.getInt(3);
                String hoseName = ""+cursor.getString(4);
                String startDate = ""+cursor.getString(5);
                String startHour = ""+cursor.getString(6);
                String endDate = ""+cursor.getString(7);
                String endHour = ""+cursor.getString(8);
                String vehicleId = ""+cursor.getString(9);
                String vehicleCodePlate = ""+cursor.getString(10);
                String fuelQuantity = ""+cursor.getString(11);
                String fuelTemperature = ""+cursor.getString(12);
                String productName = ""+cursor.getString(13);
                String comment = ""+cursor.getString(14);
                String imageUri = ""+cursor.getString(15);
                String idRegistrationUser = ""+cursor.getString(16);
                String registrationStatus = ""+cursor.getString(17);
                String migrationStatus = ""+cursor.getString(18);

                transactionEntity.setIdSqlite(idSqlite);
                transactionEntity.setIdTransaction(idTransaction);
                transactionEntity.setNumeroTransaccion(""+ticketNumber);
                transactionEntity.setIdBomba(hoseNumber);
                transactionEntity.setNombreManguera(hoseName);
                transactionEntity.setFechaInicio(startDate);
                transactionEntity.setHoraInicio(startHour);
                transactionEntity.setFechaFin(endDate);
                transactionEntity.setHoraFin(endHour);
                transactionEntity.setIdVehiculo(vehicleId);
                transactionEntity.setPlaca(vehicleCodePlate);
                transactionEntity.setVolumen(fuelQuantity);
                transactionEntity.setTemperatura(fuelTemperature);
                transactionEntity.setNombreProducto(productName);
                transactionEntity.setComentario(comment);
                transactionEntity.setImageUri(imageUri);
                transactionEntity.setIdUsuarioRegistro(idRegistrationUser);
                transactionEntity.setEstadoRegistro(registrationStatus);
                transactionEntity.setEstadoMigracion(migrationStatus);

                lst.add(transactionEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public List<TransactionEntity> getPendingMigrationTransaction(int escenario)
    {
        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();

        String sql = "";
        sql = "SELECT " +
                " RA." +MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HARDWARE_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_CONDUCTOR_KEY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_OPERATOR_KEY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_HOROMETER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_KILOMETER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_IMAGE_URI_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +
                " FROM " + MyDatabase.TB_TRANSACTION + " RA " +
                " WHERE RA."+ MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION + " IS NULL "+
                " AND RA." + MyDatabase.KEY_SCENE_TB_TRANSACTION + " = "+escenario +
                " AND RA." + MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION + " = 'P' ";


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //Log.v("CRUDOPERATIONS",sql2);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                int idSqlite = cursor.getInt(0);
                int ticketNumber = cursor.getInt(1);
                int idHardware = cursor.getInt(2);
                int hoseNumber = cursor.getInt(3);

                String conductorKey =""+cursor.getString(4);
                String operatorKey =""+cursor.getString(5);
                String vehicleCodePlate = ""+cursor.getString(6);
                String vehicleHorometer = ""+cursor.getString(7);
                String vehicleKilometer = ""+cursor.getString(8);
                String fuelQuantity = ""+cursor.getString(9);
                String fuelTemperature = ""+cursor.getString(10);
                String imageUri = ""+cursor.getString(11);
                String startDate = ""+cursor.getString(12);
                String startHour = ""+cursor.getString(13);
                String endDate = ""+cursor.getString(14);
                String endHour = ""+cursor.getString(15);
                String idRegistrationUser = ""+cursor.getString(16);
                String registrationStatus = ""+cursor.getString(17);

                transactionEntity.setIdSqlite(idSqlite);
                transactionEntity.setNumeroTransaccion(""+ticketNumber);
                transactionEntity.setIdHardware(idHardware);
                transactionEntity.setIdBomba(hoseNumber);
                transactionEntity.setIdConductor(conductorKey);
                transactionEntity.setIdOperador(operatorKey);
                transactionEntity.setPlaca(vehicleCodePlate);
                transactionEntity.setHorometro(vehicleHorometer);
                transactionEntity.setKilometraje(vehicleKilometer);
                transactionEntity.setFechaInicio(startDate);
                transactionEntity.setHoraInicio(startHour);
                transactionEntity.setFechaFin(endDate);
                transactionEntity.setHoraFin(endHour);
                transactionEntity.setVolumen(fuelQuantity);
                transactionEntity.setTemperatura(fuelTemperature);
                transactionEntity.setImageUri(imageUri);
                transactionEntity.setIdUsuarioRegistro(idRegistrationUser);
                transactionEntity.setEstadoRegistro(registrationStatus);

                //Log.v("Imagen", Utils.getStringImagen(context, Uri.parse(imageUri)));

                lst.add(transactionEntity);

            }while(cursor.moveToNext());
        }


        return lst;
    }

    public List<TransactionEntity> getTransactionByHose(String dateTimeStart, String dateTimeEnd, int numberHose, int scene)
    {
        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();
        String sql = "";
        sql = "SELECT " +
                " RA." +MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_COMMENT_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION  +

                " FROM " + MyDatabase.TB_TRANSACTION + " RA" +
                " WHERE RA." + MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION + " = " + numberHose +
                " AND RA."+ MyDatabase.KEY_SCENE_TB_TRANSACTION + " = " + scene +
                " AND RA."+ MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION + " =  'P' " +
                " AND RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" || ' ' || RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" BETWEEN '"+dateTimeStart +"' AND '"+dateTimeEnd+"' "+
                " ORDER BY RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION + " DESC";



        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor=null;

        cursor = db.rawQuery(sql, null);

        //Log.v("CRUDOPERATIONS",sql2);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                int idSqlite = cursor.getInt(0);
                int idTransaction = cursor.getInt(1);
                int ticketNumber = cursor.getInt(2);
                int hoseNumber = cursor.getInt(3);
                String hoseName = ""+cursor.getString(4);
                String startDate = ""+cursor.getString(5);
                String startHour = ""+cursor.getString(6);
                String endDate = ""+cursor.getString(7);
                String endHour = ""+cursor.getString(8);
                String vehicleId = ""+cursor.getString(9);
                String vehicleCodePlate = ""+cursor.getString(10);
                String fuelQuantity = ""+cursor.getString(11);
                String fuelTemperature = ""+cursor.getString(12);
                String productName = ""+cursor.getString(13);
                String comment = ""+cursor.getString(14);
                String idRegistrationUser = ""+cursor.getString(15);
                String registrationStatus = ""+cursor.getString(16);
                String migrationStatus = ""+cursor.getString(17);

                transactionEntity.setIdSqlite(idSqlite);
                transactionEntity.setIdTransaction(idTransaction);
                transactionEntity.setNumeroTransaccion(""+ticketNumber);
                transactionEntity.setIdBomba(hoseNumber);
                transactionEntity.setFechaInicio(startDate);
                transactionEntity.setHoraInicio(startHour);
                transactionEntity.setFechaFin(endDate);
                transactionEntity.setHoraFin(endHour);
                transactionEntity.setIdVehiculo(vehicleId);
                transactionEntity.setPlaca(vehicleCodePlate);
                transactionEntity.setVolumen(fuelQuantity);
                transactionEntity.setTemperatura(fuelTemperature);
                transactionEntity.setComentario(comment);
                transactionEntity.setNombreProducto(productName);
                transactionEntity.setIdUsuarioRegistro(idRegistrationUser);
                transactionEntity.setEstadoRegistro(registrationStatus);
                transactionEntity.setEstadoMigracion(migrationStatus);

                lst.add(transactionEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public List<TransactionEntity> getTransactionByBomba(int nroBomba)
    {
        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();
        //String fecha = "'07/05/2020'";
        //String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // String todayX = "'"+fecha + "'";
        String sql = "";
        sql = "SELECT " +
                " RA." +MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_START_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_DATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_END_HOUR_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_COMMENT_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +" , " +
                " RA." +MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION  +

                " FROM " + MyDatabase.TB_TRANSACTION +
                " RA WHERE RA." + MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION + " = " + nroBomba +
                " ORDER BY RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION + " DESC LIMIT 1";


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //Log.v("CRUDOPERATIONS",sql2);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                int idSqlite = cursor.getInt(0);
                int idTransaction = cursor.getInt(1);
                int ticketNumber = cursor.getInt(2);
                int hoseNumber = cursor.getInt(3);
                String startDate = ""+cursor.getString(4);
                String startHour = ""+cursor.getString(5);
                String endDate = ""+cursor.getString(6);
                String endHour = ""+cursor.getString(7);
                String vehicleId = ""+cursor.getString(8);
                String vehicleCodePlate = ""+cursor.getString(9);
                String fuelQuantity = ""+cursor.getString(10);
                String fuelTemperature = ""+cursor.getString(11);
                String productName = ""+cursor.getString(12);
                String comment = ""+cursor.getString(13);
                String idRegistrationUser = ""+cursor.getString(14);
                String registrationStatus = ""+cursor.getString(15);
                String migrationStatus = ""+cursor.getString(16);

                transactionEntity.setIdSqlite(idSqlite);
                transactionEntity.setIdTransaction(idTransaction);
                transactionEntity.setNumeroTransaccion(""+ticketNumber);
                transactionEntity.setIdBomba(hoseNumber);
                transactionEntity.setFechaInicio(startDate);
                transactionEntity.setHoraInicio(startHour);
                transactionEntity.setFechaFin(endDate);
                transactionEntity.setHoraFin(endHour);
                transactionEntity.setIdVehiculo(vehicleId);
                transactionEntity.setPlaca(vehicleCodePlate);
                transactionEntity.setVolumen(fuelQuantity);
                transactionEntity.setTemperatura(fuelTemperature);
                transactionEntity.setComentario(comment);
                transactionEntity.setNombreProducto(productName);
                transactionEntity.setIdUsuarioRegistro(idRegistrationUser);
                transactionEntity.setEstadoRegistro(registrationStatus);
                transactionEntity.setEstadoMigracion(migrationStatus);

                lst.add(transactionEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public int updateStatusMigrationTransactions(String idsSqliteTransactions){

        idsSqliteTransactions = idsSqliteTransactions.replace("[","(");
        idsSqliteTransactions = idsSqliteTransactions.replace("]",")");

        Log.v("ArrayString",idsSqliteTransactions);

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = null;
        values = new ContentValues();
        values.put(MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION, "M");

        int row =db.update(MyDatabase.TB_TRANSACTION,
                values,
                MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION+" IN " + idsSqliteTransactions,
                null);

        return row;
    }

    public int updateStatusMigrationTransaction(int idSqliteTransaction){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = null;
        values = new ContentValues();
        values.put(MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION, "M");

        int row =db.update(MyDatabase.TB_TRANSACTION,
                values,
                MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION+" = " + idSqliteTransaction,
                null);

        return row;
    }


    public int addMigration(MigrationEntity migrationEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase(); //modo escritura
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_MIGRATION_START_DATE_TB_MIGRATION, migrationEntity.getMigrationStartDate());
        values.put(MyDatabase.KEY_DESCRIPTION_TB_MIGRATION, migrationEntity.getMigrationDescription());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_MIGRATION, migrationEntity.getRegistrationStatus());
        values.put(MyDatabase.KEY_REGISTRATION_USER_TB_MIGRATION, migrationEntity.getRegistrationUser());
        values.put(MyDatabase.KEY_SCENE_TB_MIGRATION, migrationEntity.getMigrationScene());
        values.put(MyDatabase.KEY_MIGRATION_END_DATE_TB_MIGRATION, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));

        int row = (int) db.insert(MyDatabase.TB_MIGRATION, null, values);
        db.close();
        return row;
    }



    public int updateStatusTransaction(int idSqLite, String status)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION, idSqLite);

        int row =db.update(MyDatabase.TB_TRANSACTION,
                values,
                MyDatabase.KEY_ID_SQLLITE_TB_TRANSACTION+"=?",
                new String[]{String.valueOf(status)});
        db.close();

        return row;
    }

    //TB_PERSON
    public int addPerson(PersonEntity personEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase(); //modo escritura
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_ID_TB_PERSON, personEntity.getIdPerson());
        values.put(MyDatabase.KEY_PERSON_NAME_TB_PERSON, personEntity.getPersonName());
        values.put(MyDatabase.KEY_FIRST_LAST_NAME_TB_PERSON, personEntity.getFirstLastName());
        values.put(MyDatabase.KEY_SECOND_LAST_NAME_TB_PERSON, personEntity.getSecondLastName());
        values.put(MyDatabase.KEY_PHOTOCHECK_TB_PERSON, personEntity.getPhotocheck());
        values.put(MyDatabase.KEY_DOCUMENT_NUMBER_TB_PERSON, personEntity.getDocumentNumber());
        values.put(MyDatabase.KEY_DOCUMENT_PATHBASE64_TB_PERSON, personEntity.getPathFileBase64());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_PERSON, personEntity.getRegistrationStatus());

        int row = (int) db.insert(MyDatabase.TB_PERSON, null, values);
        db.close();
        return row;
    }

    public int updatePerson(PersonEntity personEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDatabase.KEY_PERSON_NAME_TB_PERSON, personEntity.getPersonName());
        values.put(MyDatabase.KEY_FIRST_LAST_NAME_TB_PERSON, personEntity.getFirstLastName());
        values.put(MyDatabase.KEY_SECOND_LAST_NAME_TB_PERSON, personEntity.getSecondLastName());
        values.put(MyDatabase.KEY_PHOTOCHECK_TB_PERSON, personEntity.getPhotocheck());
        values.put(MyDatabase.KEY_DOCUMENT_NUMBER_TB_PERSON, personEntity.getDocumentNumber());
        values.put(MyDatabase.KEY_DOCUMENT_PATHBASE64_TB_PERSON, personEntity.getPathFileBase64());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_PERSON, personEntity.getRegistrationStatus());

        int row =db.update(MyDatabase.TB_PERSON,
                values,
                MyDatabase.KEY_ID_TB_PERSON+"=?",
                new String[]{String.valueOf(personEntity.getIdPerson())});
        db.close();

        return row;
    }

    public int deletePerson(PersonEntity personEntity)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int row= db.delete(MyDatabase.TB_PERSON,
                MyDatabase.KEY_ID_TB_PERSON+"=?",
                new String[]{String.valueOf(personEntity.getIdPerson())});
        db.close();
        return row;
    }

    public PersonEntity getPerson(int id)
    {
        PersonEntity personEntity= new PersonEntity();
        SQLiteDatabase db = helper.getReadableDatabase(); //modo lectura
        Cursor cursor = db.query(MyDatabase.TB_PERSON,
                new String[]{
                        MyDatabase.KEY_ID_SQLLITE_TB_PERSON,
                        MyDatabase.KEY_ID_TB_PERSON,
                        MyDatabase.KEY_PERSON_NAME_TB_PERSON,
                        MyDatabase.KEY_FIRST_LAST_NAME_TB_PERSON,
                        MyDatabase.KEY_SECOND_LAST_NAME_TB_PERSON,
                        MyDatabase.KEY_PHOTOCHECK_TB_PERSON,
                        MyDatabase.KEY_DOCUMENT_NUMBER_TB_PERSON,
                        MyDatabase.KEY_DOCUMENT_PATHBASE64_TB_PERSON,
                        MyDatabase.KEY_REGISTRATION_STATUS_TB_PERSON},
                MyDatabase.KEY_ID_TB_PERSON + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if(cursor!=null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int idSqlLite = Integer.parseInt(cursor.getString(0));
            int IdPerson = Integer.parseInt(cursor.getString(1));
            String PersonName = cursor.getString(2);
            String FirstLastName = cursor.getString(3);
            String SecondLastName = cursor.getString(4);
            String Photocheck = cursor.getString(5);
            String DocumentNumber = cursor.getString(6);
            String PathBase64 = cursor.getString(7);
            String RegistrationStatus = cursor.getString(8);

            personEntity= new PersonEntity(idSqlLite, IdPerson, PersonName,FirstLastName,SecondLastName,Photocheck,DocumentNumber,RegistrationStatus, PathBase64);
        }

        return personEntity;
    }

    public PersonEntity getPersonByPhotocheck(String photocheck)
    {
        PersonEntity personEntity= new PersonEntity();
        SQLiteDatabase db = helper.getReadableDatabase(); //modo lectura
        Cursor cursor = db.query(MyDatabase.TB_PERSON,
                new String[]{
                        MyDatabase.KEY_ID_SQLLITE_TB_PERSON,
                        MyDatabase.KEY_ID_TB_PERSON,
                        MyDatabase.KEY_PERSON_NAME_TB_PERSON,
                        MyDatabase.KEY_FIRST_LAST_NAME_TB_PERSON,
                        MyDatabase.KEY_SECOND_LAST_NAME_TB_PERSON,
                        MyDatabase.KEY_PHOTOCHECK_TB_PERSON,
                        MyDatabase.KEY_DOCUMENT_NUMBER_TB_PERSON,
                        MyDatabase.KEY_DOCUMENT_PATHBASE64_TB_PERSON,
                        MyDatabase.KEY_REGISTRATION_STATUS_TB_PERSON},
                MyDatabase.KEY_PHOTOCHECK_TB_PERSON + "=?",
                new String[]{String.valueOf(photocheck)}, null, null, null);
        if(cursor!=null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int idSqlLite = Integer.parseInt(cursor.getString(0));
            int IdPerson = Integer.parseInt(cursor.getString(1));
            String PersonName = cursor.getString(2);
            String FirstLastName = cursor.getString(3);
            String SecondLastName = cursor.getString(4);
            String Photocheck = cursor.getString(5);
            String DocumentNumber = cursor.getString(6);
            String PathBase64 = cursor.getString(7);
            String RegistrationStatus = cursor.getString(8);

            personEntity= new PersonEntity(idSqlLite, IdPerson, PersonName,FirstLastName,SecondLastName,Photocheck,DocumentNumber,RegistrationStatus, PathBase64);
        }

        return personEntity;
    }

    public List<PersonEntity> getAllPerson()
    {
        List<PersonEntity> lst =new ArrayList<PersonEntity>();
        String sql= "SELECT  * FROM " + MyDatabase.TB_PERSON;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                PersonEntity personEntity =new PersonEntity();
                personEntity.setIdPersonSqlLite(Integer.parseInt(cursor.getString(0)));
                personEntity.setIdPerson(Integer.parseInt(cursor.getString(1)));
                personEntity.setPersonName(cursor.getString(2));
                personEntity.setFirstLastName(cursor.getString(3));
                personEntity.setSecondLastName(cursor.getString(4));
                personEntity.setPhotocheck(cursor.getString(5));
                personEntity.setDocumentNumber(cursor.getString(6));
                personEntity.setPathFileBase64(cursor.getString(7));
                personEntity.setRegistrationStatus(cursor.getString(8));

                lst.add(personEntity);
            }while(cursor.moveToNext());
        }
        return lst;
    }

    public List<PersonEntity> getAllActivePerson(String busqueda)
    {
        List<PersonEntity> lst =new ArrayList<PersonEntity>();
        String sql= "SELECT  * FROM " + MyDatabase.TB_PERSON + " WHERE " + MyDatabase.KEY_REGISTRATION_STATUS_TB_PERSON + " = 'A'" +
                " AND (" + MyDatabase.KEY_PERSON_NAME_TB_PERSON + " LIKE '%" + busqueda + "%' OR " +
                MyDatabase.KEY_FIRST_LAST_NAME_TB_PERSON + " LIKE '%" + busqueda + "%' OR " +
                MyDatabase.KEY_SECOND_LAST_NAME_TB_PERSON + " LIKE '%" + busqueda + "%' OR " +
                MyDatabase.KEY_PHOTOCHECK_TB_PERSON + " LIKE '%" + busqueda + "%' OR " +
                MyDatabase.KEY_DOCUMENT_NUMBER_TB_PERSON + " LIKE '%" + busqueda + "%' )";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                PersonEntity personEntity =new PersonEntity();
                personEntity.setIdPersonSqlLite(Integer.parseInt(cursor.getString(0)));
                personEntity.setIdPerson(Integer.parseInt(cursor.getString(1)));
                personEntity.setPersonName(cursor.getString(2));
                personEntity.setFirstLastName(cursor.getString(3));
                personEntity.setSecondLastName(cursor.getString(4));
                personEntity.setPhotocheck(cursor.getString(5));
                personEntity.setDocumentNumber(cursor.getString(6));
                personEntity.setPathFileBase64(cursor.getString(7));
                personEntity.setRegistrationStatus(cursor.getString(8));

                lst.add(personEntity);
            }while(cursor.moveToNext());
        }
        return lst;
    }

    public int getPersonCount()
    {
        int totalCount = 0;
        String sql= "SELECT * FROM "+MyDatabase.TB_PERSON;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        totalCount = cursor.getCount();
        cursor.close();

        return totalCount;
    }

    //LIMPIAR MAESTROS
    public void clearTablesMasters() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM "+ MyDatabase.TB_DRIVER);
        db.execSQL("DELETE FROM "+ MyDatabase.TB_OPERATOR);
        db.execSQL("DELETE FROM "+ MyDatabase.TB_PLATE);
    }

    //INSERTAR MAESTROS
    public void insertMastersSQLite(Maestros maestros){
        //dbHelper.onUpgrade();
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values;
        int lastTicket;

        //llenar Drivers
        if(maestros.getDrivers() != null) {
            for (Driver driver : maestros.getDrivers()) {
                values = new ContentValues();
                values.put(MyDatabase.KEY_DRIVER_KEY_TB_DRIVER, driver.getDriverKey());
                values.put(MyDatabase.KEY_DRIVER_NAME_TB_DRIVER, driver.getDriverName());
                db.insert(MyDatabase.TB_DRIVER, null, values);
            }
        }

        //llenar Operators
        if(maestros.getOperators() != null) {
            for (Operator operator : maestros.getOperators()) {
                values = new ContentValues();
                values.put(MyDatabase.KEY_OPERATOR_KEY_TB_OPERATOR, operator.getOperatorKey());
                values.put(MyDatabase.KEY_OPERATOR_NAME_TB_OPERATOR, operator.getOperatorName());
                db.insert(MyDatabase.TB_OPERATOR, null, values);
            }
        }

        //llenar Plates
        if(maestros.getPlates() != null) {
            for (Plate plate : maestros.getPlates()) {
                values = new ContentValues();
                values.put(MyDatabase.KEY_CODE_TB_PLATE, plate.getVehicleCodePlate());
                values.put(MyDatabase.KEY_COMPANY_TB_PLATE, plate.getCompany());
                values.put(MyDatabase.KEY_STATUS_TB_PLATE, plate.getStatus());
                db.insert(MyDatabase.TB_PLATE, null, values);
            }
        }

        //llenar Hardwares
        lastTicket=0;
        if(maestros.getHardwares() != null) {
            for (Hardware hardware : maestros.getHardwares()) {
                lastTicket = getLastTicketHardwareById(hardware);
                if (lastTicket == -1) {
                    values = new ContentValues();
                    values.put(MyDatabase.KEY_ID_TB_HARDWARE, hardware.getHardwareId());
                    values.put(MyDatabase.KEY_NAME_STATION_TB_HARDWARE, hardware.getStationName());
                    values.put(MyDatabase.KEY_LAST_TICKET_STATION_TB_HARDWARE, hardware.getLastTicket());
                    db.insert(MyDatabase.TB_HARDWARE, null, values);
                } else if (hardware.LastTicket > lastTicket) {
                    //UPDATE
                    values = new ContentValues();
                    values.put(MyDatabase.KEY_LAST_TICKET_STATION_TB_HARDWARE, hardware.getLastTicket());

                    db.update(MyDatabase.TB_HARDWARE,
                            values,
                            MyDatabase.KEY_ID_TB_HARDWARE + "=?",
                            new String[]{String.valueOf(hardware.getHardwareId())});
                }

            }
        }

        //llenar Hoses
        lastTicket=0;
        if(maestros.getHoses() != null) {
            for (Hose hose : maestros.getHoses()) {
                lastTicket = getLastTicketHoseByNumber(hose);
                if (lastTicket == -1) {
                    values = new ContentValues();
                    values.put(MyDatabase.KEY_NUMBER_HOSE_TB_HOSE, hose.getHoseNumber());
                    values.put(MyDatabase.KEY_NAME_HOSE_TB_HOSE, hose.getHoseName());
                    values.put(MyDatabase.KEY_NAME_PRODUCT_TB_HOSE, hose.getNameProduct());
                    values.put(MyDatabase.KEY_LAST_TICKET_TB_HOSE, hose.getLastTicket());
                    values.put(MyDatabase.KEY_LAST_QUANTITY_FUEL_TB_HOSE, hose.getFuelQuantity());
                    values.put(MyDatabase.KEY_ID_HARDWARE_TB_HOSE, hose.getHardwareId());
                    db.insert(MyDatabase.TB_HOSE, null, values);
                } else if (hose.LastTicket > lastTicket) {

                    //UPDATE
                    values = new ContentValues();
                    values.put(MyDatabase.KEY_LAST_TICKET_TB_HOSE, hose.getLastTicket());

                    db.update(MyDatabase.TB_HOSE,
                            values,
                            MyDatabase.KEY_NUMBER_HOSE_TB_HOSE + "=?",
                            new String[]{String.valueOf(hose.getHoseNumber())});
                }
            }
        }


        db.close();
    }

    private int getLastTicketHardwareById(Hardware hardware){
        int retorno=-1;

        String sql = "";
        sql =   " SELECT " + MyDatabase.KEY_LAST_TICKET_STATION_TB_HARDWARE +
                " FROM " + MyDatabase.TB_HARDWARE + " HA  WHERE  HA."+ MyDatabase.KEY_ID_TB_HARDWARE + " = "+hardware.HardwareId;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                retorno =  cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        return retorno;
    }

    private int getLastTicketHoseByNumber(Hose hose){
        int retorno=-1;

        List<Hose> lst =new ArrayList<Hose>();
        String sql = "";
        sql =   " SELECT " + MyDatabase.KEY_LAST_TICKET_TB_HOSE +
                " FROM " + MyDatabase.TB_HOSE + " HO  WHERE  HO."+ MyDatabase.KEY_NUMBER_HOSE_TB_HOSE + " = "+hose.getHoseNumber();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                retorno =  cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        return retorno;
    }

    public List<Driver> getAllDriver()
    {
        List<Driver> lst =new ArrayList<Driver>();
        String sql= "SELECT  * FROM " + MyDatabase.TB_DRIVER;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                Driver driverEntity =new Driver();
                driverEntity.setDriverKey(cursor.getString(1));
                driverEntity.setDriverName(cursor.getString(2));
                lst.add(driverEntity);
            }while(cursor.moveToNext());
        }
        return lst;
    }

    public List<Plate> getAllPlate()
    {
        List<Plate> lst =new ArrayList<Plate>();
        String sql= "SELECT  * FROM " + MyDatabase.TB_PLATE;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                Plate plateEntity =new Plate();
                plateEntity.setVehicleCodePlate(cursor.getString(1));
                plateEntity.setStatus(cursor.getString(2));
                plateEntity.setCompany(cursor.getString(3));
                lst.add(plateEntity);
            }while(cursor.moveToNext());
        }
        return lst;
    }

    public List<Hose> getAllHose()
    {
        List<Hose> lst =new ArrayList<Hose>();
        String sql= "SELECT  * FROM " + MyDatabase.TB_HOSE;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                Hose hoseEntity =new Hose();
                hoseEntity.setHoseNumber(cursor.getInt(1));
                hoseEntity.setHoseName(cursor.getString(2));
                hoseEntity.setNameProduct(cursor.getString(3));
                hoseEntity.setLastTicket(cursor.getInt(4));
                hoseEntity.setFuelQuantity(cursor.getDouble(5));
                hoseEntity.setHardwareId(cursor.getInt(6));
                lst.add(hoseEntity);
            }while(cursor.moveToNext());
        }
        return lst;
    }

    public List<Plate> getPlate(String codePlate)
    {
        List<Plate> lst =new ArrayList<Plate>();
        //String fecha = "'07/05/2020'";
        //String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // String todayX = "'"+fecha + "'";
        String sql = "";
        sql = "SELECT " +
                " PL." +MyDatabase.KEY_CODE_TB_PLATE +" , " +
                " PL." +MyDatabase.KEY_COMPANY_TB_PLATE +" , " +
                " PL." +MyDatabase.KEY_STATUS_TB_PLATE +
                " FROM " + MyDatabase.TB_PLATE + " PL WHERE PL." + MyDatabase.KEY_CODE_TB_PLATE +
                " LIKE '%" + codePlate + "%' " ;

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                Plate plateEntity =new Plate();
                String plateCode = ""+cursor.getString(0);
                String plateCompany = ""+cursor.getString(1);
                String plateStatus = ""+cursor.getString(2);

                plateEntity.setVehicleCodePlate(plateCode);
                plateEntity.setCompany(plateCompany);
                plateEntity.setStatus(plateStatus);

                lst.add(plateEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public List<TransactionEntity> getDuplicatePlate(String codePlate, int scene)
    {
        List<TransactionEntity> lst =new ArrayList<TransactionEntity>();
        //String fecha = "'07/05/2020'";
        //String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        // String todayX = "'"+fecha + "'";
        String sql = "";
        sql = "SELECT " +
                " TR." +MyDatabase.KEY_HOSE_NAME_TB_TRANSACTION +
                " FROM " + MyDatabase.TB_TRANSACTION + " TR "+
                " WHERE TR." + MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION + " = '" + codePlate + "' " +
                " AND TR." + MyDatabase.KEY_SCENE_TB_TRANSACTION + " = "+ scene +
                " AND TR." + MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION + " = 'N'"  ;


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                TransactionEntity transactionEntity =new TransactionEntity();
                String nameHose = ""+cursor.getString(0);
                transactionEntity.setNombreManguera(nameHose);

                lst.add(transactionEntity);
            }while(cursor.moveToNext());
        }
        return lst;

    }

    public int getNumTicketStation(){
        int retorno = 0;

        String sql= "SELECT "+ MyDatabase.KEY_LAST_TICKET_STATION_TB_HARDWARE +" FROM " + MyDatabase.TB_HARDWARE;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                retorno= Integer.parseInt(cursor.getString(0))+1;
            }while(cursor.moveToNext());
        }

        return retorno;
    }

    public List<MigrationEntity> getLastMigrationSuccess(int scene){
        List<MigrationEntity> lst = new ArrayList<MigrationEntity>();;

        String sql = "";
        sql = "SELECT " +
                " MI." + MyDatabase.KEY_MIGRATION_START_DATE_TB_MIGRATION + " , " +
                " MI." + MyDatabase.KEY_MIGRATION_END_DATE_TB_MIGRATION + " , " +
                " MI." + MyDatabase.KEY_REGISTRATION_STATUS_TB_MIGRATION +
                " FROM " + MyDatabase.TB_MIGRATION + " MI "+
                " WHERE MI." + MyDatabase.KEY_SCENE_TB_MIGRATION + " = "+ scene +
                " AND MI." + MyDatabase.KEY_REGISTRATION_STATUS_TB_MIGRATION + " = 'P' "+
                " ORDER BY MI." + MyDatabase.KEY_ID_SQLLITE_TB_MIGRATION + " DESC " +
                " LIMIT 1";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                MigrationEntity migrationEntity =new MigrationEntity();
                String startDate = ""+cursor.getString(0);
                String endDate = ""+cursor.getString(1);
                String status = ""+cursor.getString(2);

                migrationEntity.setMigrationStartDate(startDate);
                migrationEntity.setMigrationEndDate(endDate);
                migrationEntity.setRegistrationStatus(status);

                lst.add(migrationEntity);
            }while(cursor.moveToNext());
        }

        return lst;
    }


}

