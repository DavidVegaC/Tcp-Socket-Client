package com.example.tcpsocketclient.Storage.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "BDASSACACTIVITYCONTROL_v1";

    public static final String TB_USER = "tb_user";
    public static final String TB_TRANSACTION = "tb_transaction";
    public static final String TB_PERSON = "tb_person";

    //Columnas de la Tabla USUARIO
    public static final String KEY_ID_SQLLITE_TB_USER = "idSqlLiteUser";
    public static final String KEY_ID_TB_USER = "IdUser";
    public static final String KEY_ID_PERSON_TB_USER = "IdPerson";
    public static final String KEY_PERSON_NAME_TB_USER = "PersonName";
    public static final String KEY_FIRST_LAST_NAME_TB_USER = "FirstLastName";
    public static final String KEY_SECOND_LAST_NAME_TB_USER = "SecondLastName";
    public static final String KEY_PHOTOCHECK_TB_USER = "Photocheck";
    public static final String KEY_USER_TB_USER = "UUser";
    public static final String KEY_PASSWORD_TB_USER = "UPassword";
    public static final String KEY_REGISTRATION_STATUS_TB_USER = "RegistrationStatus";

    //Columnas de la Tabla PERSONA
    public static final String KEY_ID_SQLLITE_TB_PERSON = "idSqlLitePerson";
    public static final String KEY_ID_TB_PERSON = "IdPerson";
    public static final String KEY_PERSON_NAME_TB_PERSON = "PersonName";
    public static final String KEY_FIRST_LAST_NAME_TB_PERSON = "FirstLastName";
    public static final String KEY_SECOND_LAST_NAME_TB_PERSON = "SecondLastName";
    public static final String KEY_PHOTOCHECK_TB_PERSON = "Photocheck";
    public static final String KEY_DOCUMENT_NUMBER_TB_PERSON = "DocumentNumber";
    public static final String KEY_DOCUMENT_PATHBASE64_TB_PERSON = "PathFileBase64";
    public static final String KEY_REGISTRATION_STATUS_TB_PERSON = "RegistrationStatus";



    //Columnas de la Tabla TRANSACTION
    public static final String KEY_ID_SQLLITE_TB_TRANSACTION = "idSqlLiteTransaction";
    public static final String KEY_ID_TB_TRANSACTION = "IdTransaction";
    public static final String KEY_TICKET_NUMBER_TB_TRANSACTION = "TicketNumber";
    public static final String KEY_HOSE_NUMBER_TB_TRANSACTION = "HoseNumber";
    public static final String KEY_START_DATE_TB_TRANSACTION= "StartDateTransaction";
    public static final String KEY_START_HOUR_TB_TRANSACTION= "StartHourTransaction";
    public static final String KEY_END_DATE_TB_TRANSACTION = "EndDateTransaction";
    public static final String KEY_END_HOUR_TB_TRANSACTION= "EndHourTransaction";
    public static final String KEY_VEHICLE_ID_TB_TRANSACTION = "VehicleId";
    public static final String KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION = "VehicleCodePlate";
    public static final String KEY_FUEL_QUANTITY_TB_TRANSACTION= "FuelQuantity";
    public static final String KEY_FUEL_TEMPERATURE_TB_TRANSACTION = "FuelTemperature";
    public static final String KEY_COMMENT_TB_TRANSACTION = "Comment";
    public static final String KEY_PRODUCT_NAME_TB_TRANSACTION = "ProductName";
    public static final String KEY_REGISTRATION_USER_TB_TRANSACTION = "RegistrationUser";
    public static final String KEY_REGISTRATION_STATUS_TB_TRANSACTION= "RegistrationStatus";
    public static final String KEY_MIGRATION_STATUS_TB_TRANSACTION = "MigrationStatus";



    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub
        String sql= "CREATE TABLE " + TB_USER + "("
                + KEY_ID_SQLLITE_TB_USER + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + KEY_ID_TB_USER + " INTEGER,"
                + KEY_ID_PERSON_TB_USER + " INTEGER,"
                + KEY_PERSON_NAME_TB_USER + " TEXT,"
                + KEY_FIRST_LAST_NAME_TB_USER + " TEXT,"
                + KEY_SECOND_LAST_NAME_TB_USER + " TEXT,"
                + KEY_PHOTOCHECK_TB_USER + " TEXT,"
                + KEY_USER_TB_USER + " TEXT,"
                + KEY_PASSWORD_TB_USER + " TEXT,"
                + KEY_REGISTRATION_STATUS_TB_USER + " TEXT" + ")";
        db.execSQL(sql);

        String sqlE= "CREATE TABLE " + TB_PERSON + "("
                + KEY_ID_SQLLITE_TB_PERSON + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + KEY_ID_TB_PERSON + " INTEGER,"
                + KEY_PERSON_NAME_TB_PERSON + " TEXT,"
                + KEY_FIRST_LAST_NAME_TB_PERSON + " TEXT,"
                + KEY_SECOND_LAST_NAME_TB_PERSON + " TEXT,"
                + KEY_PHOTOCHECK_TB_PERSON + " TEXT,"
                + KEY_DOCUMENT_NUMBER_TB_PERSON + " TEXT,"
                + KEY_DOCUMENT_PATHBASE64_TB_PERSON + " TEXT,"
                + KEY_REGISTRATION_STATUS_TB_PERSON + " TEXT" + ")";
        db.execSQL(sqlE);

        String sqlB= "CREATE TABLE " + TB_TRANSACTION + "("
                + KEY_ID_SQLLITE_TB_TRANSACTION + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + KEY_ID_TB_TRANSACTION + " INTEGER,"
                + KEY_TICKET_NUMBER_TB_TRANSACTION + " INTEGER,"
                + KEY_HOSE_NUMBER_TB_TRANSACTION + " INTEGER,"
                + KEY_START_DATE_TB_TRANSACTION + " TEXT,"
                + KEY_START_HOUR_TB_TRANSACTION + " TEXT,"
                + KEY_END_DATE_TB_TRANSACTION + " TEXT,"
                + KEY_END_HOUR_TB_TRANSACTION + " TEXT,"
                + KEY_VEHICLE_ID_TB_TRANSACTION + " TEXT,"
                + KEY_VEHICLE_CODE_PLATE_TB_TRANSACTION + " TEXT,"
                + KEY_FUEL_QUANTITY_TB_TRANSACTION + " TEXT,"
                + KEY_FUEL_TEMPERATURE_TB_TRANSACTION + " TEXT,"
                + KEY_COMMENT_TB_TRANSACTION + " TEXT,"
                + KEY_PRODUCT_NAME_TB_TRANSACTION + " TEXT,"
                + KEY_REGISTRATION_USER_TB_TRANSACTION + " TEXT,"
                + KEY_REGISTRATION_STATUS_TB_TRANSACTION + " TEXT,"
                + KEY_MIGRATION_STATUS_TB_TRANSACTION + " TEXT" + ")";
        db.execSQL(sqlB);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql= "DROP TABLE IF EXISTS " + TB_USER;
        db.execSQL(sql);
        String sqlB= "DROP TABLE IF EXISTS " + TB_TRANSACTION;
        db.execSQL(sqlB);
        String sqlE= "DROP TABLE IF EXISTS " + TB_PERSON;
        db.execSQL(sqlE);
    }
}
