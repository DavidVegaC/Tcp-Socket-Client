package com.example.tcpsocketclient.Util.Internet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.tcpsocketclient.R;
import com.example.tcpsocketclient.Util.Const;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class NetworkUtil {

    /************VARIABLES************/
    private static final String TAG = "NetworkUtil";
    ProgressDialog progressDialog;

    public static int NET_CNNT_SERVICE_WEB_OK = 1; // NetworkAvailable
    public static int NET_CNNT_SERVICE_WEB_TIMEOUT = 2; // no NetworkAvailable
    public static int NET_NOT_PREPARE = 3; // Net no ready
    public static int NET_ERROR = 4; //net error
    private static int TIMEOUT = 15000; // TIMEOUT

    private String SERVICE_WEB_URL = Const.HTTP_SITE;

    //variables propias

    public Context ctx;
    private WifiManager wifi;
    private final String nombreRedSocket = "EMBEDDED";
    public String mensajeError="";
    private int networkId = 0;



    public  NetworkUtil(Context context){
        ctx = context;
    }

    public boolean setWIFIActive() {
        boolean validarWIFI=true;
        WifiManager wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        boolean estadoWifi = wifi.isWifiEnabled();

        if (estadoWifi) {
            WifiInfo connectionInfo = wifi.getConnectionInfo();

            if (connectionInfo.getSSID().equals("\"" + nombreRedSocket + "\"")) {
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);
                //IPLocal=ipString;

                        /*WifiConfiguration conf = new WifiConfiguration();
                        conf.SSID = redSSID;
                        conf.wepKeys[0] = "\"123456789\"";
                        conf.wepTxKeyIndex = 0;
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        conf.priority = 1999999999;
                        wifi.addNetwork(conf);
                        List<WifiConfiguration> list = wifi.getConfiguredNetworks();
                        for( WifiConfiguration i : list ) {
                            Log.d("diegooo", "hola -"+ i.SSID);
                            if(i.SSID != null && i.SSID.equals(redSSID)) {
                                wifi.disconnect();
                                wifi.enableNetwork(i.networkId, true);
                                wifi.reconnect();
                                break;
                            }
                        }*/
                //Toast.makeText(rootView.getContext(), "Conectado a red WIFI "+nombreRedSocket+".",Toast.LENGTH_LONG).show();
                //Log.d("davidddd","hola -" + wifi.isWifiEnabled() + " - " + connectionInfo.getSSID());


                //cm.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "android.net.conn.CONNECTIVITY_CHANGE");
                //ºcm.set

                if (isOutputWifi()) {
                    validarWIFI=false;
                    //connectSocket();
                } else {
                    Toast.makeText(ctx, "Debe desactivar sus datos móviles.", Toast.LENGTH_LONG).show();
                }



            } else {
                Toast.makeText(ctx, "No se detectó conexión a la red " + nombreRedSocket + ".",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ctx, "Activar WIFI y conectarse a la red " + nombreRedSocket + ".",
                    Toast.LENGTH_LONG).show();
        }

        return validarWIFI;
    }

    public boolean isOutputWifi() {
        boolean retorno = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Log.d("daviddd",networkInfo.getExtraInfo());
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // Estas conectado a un Wi-Fi
                //Toast.makeText(rootView.getContext(), "Existe conectividad a WIFI", Toast.LENGTH_LONG).show();
                retorno = true;
            }
        }

        return retorno;
    }

    public boolean connectToHotspot(String netSSID, String netPass) {
        boolean retorno=false;
        /*WifiConfiguration wifiConf = new WifiConfiguration();
        wifiConf.SSID = "\"" + netSSID + "\"";
        wifiConf.preSharedKey = "\"" + netPass + "\"";
        wifiConf.hiddenSSID = true;
        wifiConf.priority = 100;
        wifiConf.status = WifiConfiguration.Status.ENABLED;
        wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);*/

        wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        //WifiConfiguration wifiConf = new WifiConfiguration();

        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        WifiInfo connectionInfo = wifi.getConnectionInfo();

        if(connectionInfo.getSSID().equals("\"" + netSSID + "\"")&& connectionInfo.getSupplicantState() == SupplicantState.COMPLETED){
            return true;
        }

        /*if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            retorno=false;
        }

        //List<WifiConfiguration> list = wifi.getConfiguredNetworks();
        List<ScanResult> scanResultList = wifi.getScanResults();
        for( ScanResult i : scanResultList ) {
            if(i.SSID != null && i.SSID.equals(netSSID)) {
                networkId = wifi.addNetwork(wifiConf);
                wifi.disconnect();
                wifi.enableNetwork(networkId, true);
                wifi.reconnect();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                if(wifi.getConnectionInfo().equals("\"" + netSSID + "\"")&&wifi.getConnectionInfo().getSupplicantState() == SupplicantState.COMPLETED){
                    return true;
                }else{
                    mensajeError="Contraseña incorrecta para la red "+netSSID+".";
                    return false;
                }

                //if (!wifi.isWifiEnabled()) {
                    //wifi.setWifiEnabled(true);
                    //try {
                    //    Thread.sleep(2000);
                    //} catch (InterruptedException ie) {
                    //    ie.printStackTrace();
                    //}
                //}

            //}
        }*/



        //if(wif)
/*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        List<ScanResult> scanResultList = wifi.getScanResults();


        for (ScanResult result : scanResultList) {
            removeWifiNetwork(result.SSID);
            if (result.SSID.equals(netSSID)) {

                String mode = getSecurityMode(result);

                if (mode.equalsIgnoreCase("OPEN")) {

                    wifiConf.SSID = "\"" + netSSID + "\"";
                    wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    networkId = wifi.addNetwork(wifiConf);
                    wifi.enableNetwork(networkId, true);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    if (!wifi.isWifiEnabled())
                        wifi.setWifiEnabled(true);
                    return true;

                } else if (mode.equalsIgnoreCase("WEP")) {

                    wifiConf.SSID = "\"" + netSSID + "\"";
                    wifiConf.wepKeys[0] = "\"" + netPass + "\"";
                    wifiConf.wepTxKeyIndex = 0;
                    wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    networkId = wifi.addNetwork(wifiConf);
                    wifi.enableNetwork(networkId, true);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    if (!wifi.isWifiEnabled())
                        wifi.setWifiEnabled(true);
                    return true;

                } else {
                    wifiConf.SSID = "\"" + netSSID + "\"";
                    wifiConf.preSharedKey = "\"" + netPass + "\"";
                    wifiConf.hiddenSSID = true;
                    wifiConf.priority = 40;
                    wifiConf.status = WifiConfiguration.Status.ENABLED;
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                    networkId = wifi.addNetwork(wifiConf);
                    if (networkId > 0) {
                        wifi.disconnect();
                        wifi.enableNetwork(networkId, true);
                        wifi.reconnect();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                        if (!wifi.isWifiEnabled())
                            wifi.setWifiEnabled(true);
                        return true;
                    }
                    return false;
                }
            }
        }
*/

        //mensajeError="No se detectó a la red "+netSSID+" cerca de usted.";
        mensajeError="No se detectó conexión a la red WIFI "+netSSID+"." ;
        return retorno;
    }

    void removeNetwork() {
        wifi.removeNetwork(networkId);
        wifi.saveConfiguration();
    }

    private void removeWifiNetwork(String ssid) {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
        if (configs != null) {
            for (WifiConfiguration config : configs) {
                if (config.SSID.contains(ssid)) {
                    wifi.disableNetwork(config.networkId);
                    wifi.removeNetwork(config.networkId);
                }
            }
        }
        wifi.saveConfiguration();
    }

    private String getSecurityMode(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] modes = {"WPA", "EAP", "WEP"};
        for (int i = modes.length - 1; i >= 0; i--) {
            if (cap.contains(modes[i])) {
                return modes[i];
            }
        }
        return "OPEN";
    }


    /**************METODOS**************/

    /***METODO PARA HABILITAR EL WIFI SI ESTA APAGADO***/
    public void loadNetwork(final Context context){

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);

            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppThemeAssacDialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(Const.STR_ENABLE_WIFI);
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            loadNetwork(context);
                            progressDialog.dismiss();
                        }
                    }, 8000);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }

    /**VALIDA SI TIENE CONEXION POR LOS 3 CASOS**/
    public static boolean hasConnect(Context context){
        boolean wifiConnected = isWIFIConnected(context);
        boolean mobileConnected = isMOBILEConnected(context);
        boolean wiredConnected = isWiredConnected(context);
        if(!wifiConnected && !mobileConnected && !wiredConnected){
            // If there is no connection, the user is informed that there is no network currently.
            return false;
        }
        return true;
    }

    /**VALIDA SI TIENE CONEXION POR ETHERNET**/
    public static boolean isWiredConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**VALIDA SI TIENE CONEXION POR PAQUETE DE DATOS**/
    public static boolean isMOBILEConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo !=null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    /**VALIDA SI TIENE CONEXION POR WIFI**/
    public static boolean isWIFIConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo !=null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    /*
     * Return the current state of the network
     * */
    /*public static int getNetState(Context context, String addresURL) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
                if (networkinfo != null) {
                    if (networkinfo.isAvailable() && networkinfo.isConnected()) {
                        if (!connectionNetwork(addresURL)) {
                            return NET_CNNT_SERVICE_WEB_TIMEOUT;
                        } else {
                            return NET_CNNT_SERVICE_WEB_OK;
                        }
                    } else {
                        return NET_NOT_PREPARE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NET_ERROR;
    }*/


    /**  **/
    public static boolean connectionNetwork(String addresURL, Context context) {

//        AndroidConfigurationRepository repository = new AndroidConfigurationRepository(context);
//        List<AndroidConfigurationORMEntity> lista = repository.getAAll();
        int timeout = 15000;

//        if (lista!=null){
//            if (lista.size() > 0){
//                timeout = (lista.get(0).getTimeoutNetwork() * 1000);
//            }
//        }

        boolean result = false;
        HttpURLConnection httpUrl = null;
        try {
            httpUrl = (HttpURLConnection) new URL(addresURL)
                    .openConnection();
            httpUrl.setConnectTimeout(timeout);
            httpUrl.connect();
            result = true;
        } catch (IOException e) {
            Log.e("CONECTION", e.getMessage());
            result = false;
        } finally {
            if (null != httpUrl) {
                httpUrl.disconnect();
            }
            httpUrl = null;
        }
        return result;
    }

    /**
     *  wifi is enable
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);

        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) ||
                (mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS));
    }

    /**
     * <p>
     * getLocalIpAddress(never used)
     * </p>
     */
    public static String getLocalIpAddress() {
        String ret = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ret = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Get the IP address of the local WiFi, tv and littleServer used by me
     *
     * @param context
     * @return
     */
    public InetAddress getLocalIpAddress(Context  context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        InetAddress address = null;
        try {
            address = InetAddress.getByName(String.format(Locale.ENGLISH,
                    "%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }


}
