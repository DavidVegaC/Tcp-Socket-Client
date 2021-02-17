package com.example.tcpsocketclient.Util.Internet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class NetworkUtil {

    public Context ctx;
    private WifiManager wifi;
    private final String nombreRedSocket = "EMBEDDED";
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
        WifiConfiguration wifiConf = new WifiConfiguration();
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
        wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        //WifiConfiguration wifiConf = new WifiConfiguration();

        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        WifiInfo connectionInfo = wifi.getConnectionInfo();

        if(connectionInfo.getSSID().equals("\"" + netSSID + "\"")){
            return true;
        }

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                /*if (!wifi.isWifiEnabled()) {
                    wifi.setWifiEnabled(true);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }*/
                retorno=true;
                break;
            }
        }



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


}
