package com.example.tcpsocketclient.Util.Wifi;

/**
 * Created by Proyectos on 21/06/2017.
 */

public class EmbeddedPtcl {
    public final static int b_ext_solicita_estado = 0x0A;
    public final static int b_ext_leer_num_trans = 0x16;

    /*ESTADO CONFIGURACION TABLET*/

    public final static int b_ext_configuracion = 0x06;
    public final static int b_ext_cambio_estado = 0x01;


    //ESTADO MPC - VALOR DE OPCODE
    public final static int v_opc_MPC = 0x67;

    //ESTADO MPC - VALOR DE OPCODE
    public final static int v_opc_TR_Procesada = 0x69;

    //NO HAY ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_XOR_sin_abastecimiento = 0x6f;
    //INICIA ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_XOR_inicia_abastecimiento = 0x6d;
    //AUTORIZA ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_XOR_autoriza_abastecimiento = 0x6c;
    //TERMINA ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_XOR_termina_abastecimiento = 0x73;
    //ABASTECIMIENTO PROCESADO - VALOR DE XOR(Código de Verificación)
    public final static int v_XOR_abastecimiento_procesado = 0x16;

    //ABASTECIMIENTO PROCESADO - VALOR DE XOR(Código de Verificación)
    public final static int v_estado_sin_abastecimiento = 0x00;
    //INICIA ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_estado_inicia_abastecimiento = 0x02;
    //AUTORIZA ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_estado_autoriza_abastecimiento = 0x03;
    //TERMINA ABASTECIMIENTO - VALOR DE XOR(Código de Verificación)
    public final static int v_estado_termina_abastecimiento = 0x04;

    public static int crcEasyFuel2(byte[] buffer){
        int crc=0;
        int longitud=((int)buffer[1])|(((int)buffer[2])<<8);
        int i;
        for(i=0;i<longitud-2;i++){
            crc=crc^buffer[i];
        }
        return crc;
    }

    public static int crcEasyFuel1(byte[] buffer){
        int crc=0;
        int longitud=(int)buffer[1];
        int i;
        for(i=0;i<longitud-2;i++){
            crc=crc^buffer[i];
        }
        return crc;
    }

    public static int EmbeddedPtclSolicitaEstado(byte[] buffer, int direccion){
        int longitud=7;
        buffer[0]=0x02;
        buffer[1]=(byte)(longitud>>0);
        buffer[2]=(byte)(longitud>>8);
        buffer[3]=(byte)direccion;
        buffer[4]=(byte)b_ext_solicita_estado;
       // buffer[5]=(byte)crcEasyFuel2(buffer);
        buffer[6]=0x03;
        return longitud;
    }

    public static int EmbeddedPtclSolicitaEstado1(byte[] buffer, int direccion){
        int longitud=6;
        buffer[0]=0x02;
        buffer[1]=(byte)(longitud);
        buffer[2]=(byte)direccion;
        buffer[3]=(byte)b_ext_solicita_estado;
        buffer[4]=(byte)crcEasyFuel1(buffer);
        buffer[5]=0x03;
        return longitud;
    }

    public static int EmbeddedPtclCantidadTransacciones(byte[] buffer, int direccion){
        int longitud=7;
        buffer[0]=0x02;
        buffer[1]=(byte)(longitud>>0);
        buffer[2]=(byte)(longitud>>8);
        buffer[3]=(byte)direccion;
        buffer[4]=(byte)b_ext_leer_num_trans;
       // buffer[5]=(byte)crcEasyFuel2(buffer);
        buffer[6]=0x03;
        return longitud;
    }
    //NUEVAS TRAMAS 26082020
    //TRAMA DE CONFIGURACION

    public static int aceptarTramaConfiguracion(byte[] buffer, int direccion, int opcCodePrincipal, int opcCodeSecundario){
        int longitud=11;
        buffer[0]=0x02;
        buffer[1]=0x0B;
        buffer[2]=0x00;
        buffer[3]=(byte)direccion;
        buffer[4]=(byte)opcCodePrincipal; //opcode principal
        buffer[5]=(byte)opcCodeSecundario;
        buffer[6]=0x00;
        buffer[7]=0x00;
        buffer[8]=0x01;
        buffer[9]=(byte)crcEasyFuel2(buffer);
        buffer[10]=0x03;
        return longitud;
    }

    //TRAMA DE ESTADOS ADICIONALES

    public static int ackBluetooth(byte[] buffer, int direccion, int opcCodepRincipal, int opcCodeSecundario, int indiceBomba, int flagError, int codigoError){
        int longitud=12;
        buffer[0]=0x02;
        buffer[1]=0x0C;
        buffer[2]=0x00;
        buffer[3]=(byte)direccion;
        buffer[4]=(byte)opcCodepRincipal; //opcode principal
        buffer[5]=(byte)opcCodeSecundario;
        buffer[6]=(byte)indiceBomba;
        buffer[7]=0x00;
        buffer[8]=(byte)flagError;
        buffer[9]=(byte)codigoError;
        buffer[10]=(byte)crcEasyFuel2(buffer);
        buffer[11]=0x03;
        return longitud;
    }
}
