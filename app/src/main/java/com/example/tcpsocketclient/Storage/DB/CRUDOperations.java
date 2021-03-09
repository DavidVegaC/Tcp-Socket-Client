package com.example.tcpsocketclient.Storage.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tcpsocketclient.Entities.PersonEntity;
import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.Entities.UserEntity;

import java.util.ArrayList;
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
        values.put(MyDatabase.KEY_HOSE_NUMBER_TB_TRANSACTION, transactionEntity.getIdBomba());
        values.put(MyDatabase.KEY_START_DATE_TB_TRANSACTION, transactionEntity.getFechaInicio());
        values.put(MyDatabase.KEY_START_HOUR_TB_TRANSACTION, transactionEntity.getHoraInicio());
        values.put(MyDatabase.KEY_END_DATE_TB_TRANSACTION, transactionEntity.getFechaFin());
        values.put(MyDatabase.KEY_END_HOUR_TB_TRANSACTION, transactionEntity.getHoraFin());
        values.put(MyDatabase.KEY_VEHICLE_ID_TB_TRANSACTION, transactionEntity.getIdVehiculo());
        values.put(MyDatabase.KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION, transactionEntity.getPlaca());
        values.put(MyDatabase.KEY_FUEL_QUANTITY_TB_TRANSACTION, transactionEntity.getVolumen());
        values.put(MyDatabase.KEY_FUEL_TEMPERATURE_TB_TRANSACTION, transactionEntity.getTemperatura());
        values.put(MyDatabase.KEY_COMMENT_TB_TRANSACTION, transactionEntity.getComentario());
        values.put(MyDatabase.KEY_PRODUCT_NAME_TB_TRANSACTION, transactionEntity.getNombreProducto());
        values.put(MyDatabase.KEY_REGISTRATION_USER_TB_TRANSACTION, transactionEntity.getIdUsuarioRegistro());
        values.put(MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION, "P");
        values.put(MyDatabase.KEY_MIGRATION_STATUS_TB_TRANSACTION, transactionEntity.getEstadoMigracion());

        int row = (int) db.insert(MyDatabase.TB_TRANSACTION, null, values);
        db.close();
        return row;
    }


    public List<TransactionEntity> getTransactionByTransactionAndHose(String nroTransaccion)
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
                " FROM " + MyDatabase.TB_TRANSACTION + " RA " +
                " WHERE RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION + " = " + nroTransaccion;
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

    public List<TransactionEntity> getTransaction(String nroTransaccion)
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

                " FROM " + MyDatabase.TB_TRANSACTION + " RA WHERE RA." + MyDatabase.KEY_TICKET_NUMBER_TB_TRANSACTION +
                " LIKE '%" + nroTransaccion + "%'" +
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

    public List<TransactionEntity> getPendingTransaction()
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

                " FROM " + MyDatabase.TB_TRANSACTION + " RA WHERE RA." + MyDatabase.KEY_REGISTRATION_STATUS_TB_TRANSACTION +
                " = 'P'" ;


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






}

