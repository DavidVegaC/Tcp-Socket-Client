package com.example.tcpsocketclient.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imrankst1221@gmail.com
 *
 */

public class Utils {
    // UNICODE 0x23 = '#'
    public static final byte[] UNICODE_TEXT = new byte[] {0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23};
    // UNICODE 0x3D = '='
    public static final byte[] UNICODE_TEXT_3D = new byte[] {0x3D, 0x3D, 0x3D,
            0x3D, 0x3D, 0x3D,0x3D, 0x3D, 0x3D,0x3D, 0x3D, 0x3D,0x3D, 0x3D, 0x3D,
            0x3D, 0x3D, 0x3D,0x3D, 0x3D, 0x3D,0x3D, 0x3D, 0x3D,0x3D, 0x3D, 0x3D,
            0x3D, 0x3D, 0x3D};
    // UNICODE 0x2D = '-'
    public static final byte[] UNICODE_TEXT_2D = new byte[] {0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D,0x2D, 0x2D, 0x2D,0x2D, 0x2D, 0x2D,0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D,0x2D, 0x2D, 0x2D,0x2D, 0x2D, 0x2D,0x2D, 0x2D, 0x2D,
            0x2D, 0x2D, 0x2D};

    public static final byte[] ALIGN_LEFT =new byte[]{0x1B, 'a',0x00};
    public static final byte[] ALIGN_CENTER =new byte[]{0x1B, 'a', 0x01};
    public static final byte[] ALIGN_RIGHT =new byte[]{0x1B, 'a', 0x02};

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = { "0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111" };

    public static byte[] decodeBitmap(Bitmap bmp){
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;


        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
            Log.e("decodeBitmap error", " width is too large");
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            Log.e("decodeBitmap error", " height is too large");
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString+widthHexString+heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);

                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;

    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }

        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //Para NFC

    public static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }
        System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

    public static String toHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(int i = bytes.length -1; i >= 0; i--){
            int b = bytes[i] & 0xff;
            if(b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if(i > 0){
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    //Para Foto
    public static String getStringImagen(Context context,Uri imageUri){
        String encodedImagen="";

        try
        {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] imageBytes = baos.toByteArray();
            //byte[] imageBytes = decodeBitmap(bmp);
            encodedImagen = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        }catch(Exception e){
            //Toast.makeText(rootView.getContext(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.v("Parseando Imagen", e.getMessage());
        }

        return encodedImagen;
    }

    //Para FechaSQLITE
    public static String dateToDateSQLite(String date){
        String dateSQLite="";

        dateSQLite= date.substring(6,10)+"-"+date.substring(3,5)+"-"+date.substring(0,2);

        return dateSQLite;
    }


}
