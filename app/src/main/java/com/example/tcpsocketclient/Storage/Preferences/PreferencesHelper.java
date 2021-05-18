
package com.example.tcpsocketclient.Storage.Preferences;
import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesHelper {

    private static final String LOGINPREFERENCES = "LOGINPREFERENCES";
    private static final String PREFERENCES_ID_USER = LOGINPREFERENCES + ".IdUsuario";
    private static final String PREFERENCES_ID_PERSON= LOGINPREFERENCES + ".IdPerson";
    private static final String PREFERENCES_USERNAME = LOGINPREFERENCES + ".username";
    private static final String PREFERENCES_PASSWORD = LOGINPREFERENCES + ".password";
    private static final String PREFERENCES_NAME = LOGINPREFERENCES + ".name";
    private static final String PREFERENCES_LASTNAME = LOGINPREFERENCES + ".password";
    private static final String PREFERENCES_REGISTRATIONDATE = LOGINPREFERENCES + ".regisDate";

    //ADDITIONAL CODE
    private static final String PREFERENCES_OPERATIONNAME = LOGINPREFERENCES + ".operationName";
    private static final String PREFERENCES_ACCESSPOINT = LOGINPREFERENCES + ".accessPoint";
    private static final String BLUETOOTH_ADDRESS = LOGINPREFERENCES + ".bluetoothAddress";

    private PreferencesHelper() {
        //no instance
    }

    public static void signOut(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(PREFERENCES_USERNAME);
        editor.remove(PREFERENCES_PASSWORD);
        editor.remove(PREFERENCES_NAME);
        editor.remove(PREFERENCES_PASSWORD);
        editor.remove(PREFERENCES_REGISTRATIONDATE);
        editor.remove(PREFERENCES_OPERATIONNAME);
        editor.remove(PREFERENCES_ACCESSPOINT);
        editor.apply();
    }

    public static void saveSession(Context context, int idUser, int idPerson, String username, String password, String name, String lastName, String date, String operationName, String accessPoint)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(PREFERENCES_ID_USER, idUser);
        editor.putInt(PREFERENCES_ID_PERSON, idPerson);
        editor.putString(PREFERENCES_USERNAME, username);
        editor.putString(PREFERENCES_PASSWORD, password);
        editor.putString(PREFERENCES_NAME, name);
        editor.putString(PREFERENCES_LASTNAME, lastName);
        editor.putString(PREFERENCES_REGISTRATIONDATE, date);
        editor.putString(PREFERENCES_OPERATIONNAME, operationName);
        editor.putString(PREFERENCES_ACCESSPOINT, accessPoint);
        editor.apply();
    }

    public static void saveBluetoothConn(Context context, String btAddress)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(BLUETOOTH_ADDRESS, btAddress);
        editor.apply();
    }
    public static String getBluetoothAddress(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        String BluetoothAddress= sharedPreferences.getString(BLUETOOTH_ADDRESS,null);

        return BluetoothAddress;
    }

    public static boolean isBluetoothCon(Context context) {
        final SharedPreferences preferences = getSharedPreferences(context);
        return preferences.contains(BLUETOOTH_ADDRESS);
    }

    public static void updateSession(Context context, String operationName, String accessPoint)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(PREFERENCES_OPERATIONNAME, operationName);
        editor.putString(PREFERENCES_ACCESSPOINT, accessPoint);
        editor.apply();
    }

    public static String getUserSession(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        String username= sharedPreferences.getString(PREFERENCES_USERNAME,null);

        return username;
    }
    public static String getNameUserSession(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        String name= sharedPreferences.getString(PREFERENCES_NAME,null)+" "+sharedPreferences.getString(PREFERENCES_LASTNAME,null);

        return name;
    }

    public static int getIdUserSession(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        int idUser= sharedPreferences.getInt(PREFERENCES_ID_USER,1);

        return idUser;
    }

    public static int getIdPersonSession(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        int idPerson= sharedPreferences.getInt(PREFERENCES_ID_PERSON,1);

        return idPerson;
    }

    public static String getOperationName(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        String operationName= sharedPreferences.getString(PREFERENCES_OPERATIONNAME,null);

        return operationName;
    }

    public static String getAccessPoint(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        String accessPoint= sharedPreferences.getString(PREFERENCES_ACCESSPOINT,null);

        return accessPoint;
    }

    public static boolean isSignedIn(Context context) {
        final SharedPreferences preferences = getSharedPreferences(context);
        return preferences.contains(PREFERENCES_USERNAME) &&
                preferences.contains(PREFERENCES_PASSWORD);
    }

    private static final String SCENEPREFERENCES = "SCENEPREFERENCES";
    private static final String SCENEPREFERENCES_ID_SCENE = SCENEPREFERENCES + ".IdScene";

    public static void saveScene(Context context, int idScene)
    {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(SCENEPREFERENCES_ID_SCENE, idScene);
        editor.apply();
    }

    public static int getIdScene(Context context)
    {
        SharedPreferences sharedPreferences= getSharedPreferences(context);
        int idScene= sharedPreferences.getInt(SCENEPREFERENCES_ID_SCENE,0);
        return idScene;
    }


    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SCENEPREFERENCES, Context.MODE_PRIVATE);
    }
}
