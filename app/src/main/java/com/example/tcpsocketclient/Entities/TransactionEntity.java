package com.example.tcpsocketclient.Entities;

import java.io.Serializable;

public class TransactionEntity implements Serializable {

    public int idSqlite;
    public int idTransaction;

    public int idHardware;

    public int idBomba;
    public int numeroBomba;
    public String nombreManguera;

    //public String estadoActual;
    public int estadoActual;
    public int cantidadDecimales;

    public int idProducto;
    public String nombreProducto;

    public String numeroTransaccion;

    //public String tipoTransaccion;
    public int tipoTransaccion;

    public String fechaInicio;
    public String horaInicio;
    public String fechaFin;
    public String horaFin;
    public String volumen;
    public String temperatura;

    public String idVehiculo;

    //public String tipoVehiculo;
    public int tipoVehiculo;

    public String placa;


    public String kilometraje;
    public String horometro;
    //public Double kilometraje;
    //public Double horometro;


    //public String turno;
    public int turno;

    //public String numeroTanque;
    public int numeroTanque;


    public String idConductor;
    public String idOperador;


    public String latitud;
    public String longitud;
    //public String tipoErrorPreseteo;
    //public String volumenAutorizado;
    //public String volumenAceptado;
    //public String codigoCliente;
    public int tipoErrorPreseteo;
    public int volumenAutorizado;
    public int volumenAceptado;
    public int codigoCliente;

    //public String codigoArea;
    //public String tipoTag;
    //public String tipoCierre;
    public int codigoArea;
    public int tipoTag;
    public int tipoCierre;

    public String crc;
    public String comentario;

    public String idUsuarioRegistro;
    public String estadoRegistro;
    public String estadoMigracion;
    public int escenario;

    public String contometroInicial;
    public String contometroFinal;

    public String imageUri;
    public String imageDecodeString;

    public TransactionEntity() {
    }

    public int getIdSqlite() {
        return idSqlite;
    }

    public void setIdSqlite(int idSqlite) {
        this.idSqlite = idSqlite;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public int getIdHardware() {
        return idHardware;
    }

    public void setIdHardware(int idHardware) {
        this.idHardware = idHardware;
    }


    public int getIdBomba() {
        return idBomba;
    }

    public void setIdBomba(int idBomba) {
        this.idBomba = idBomba;
    }

    public int getNumeroBomba() {
        return numeroBomba;
    }

    public void setNumeroBomba(int numeroBomba) {
        this.numeroBomba = numeroBomba;
    }

    public String getNombreManguera() {
        return nombreManguera;
    }

    public void setNombreManguera(String nombreManguera) {
        this.nombreManguera = nombreManguera;
    }

    public int getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(int estadoActual) {
        this.estadoActual = estadoActual;
    }

    public int getCantidadDecimales() {
        return cantidadDecimales;
    }

    public void setCantidadDecimales(int cantidadDecimales) {
        this.cantidadDecimales = cantidadDecimales;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public int getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(int tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(int tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getHorometro() {
        return horometro;
    }

    public void setHorometro(String horometro) {
        this.horometro = horometro;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getNumeroTanque() {
        return numeroTanque;
    }

    public void setNumeroTanque(int numeroTanque) {
        this.numeroTanque = numeroTanque;
    }

    public String getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
    }

    public String getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(String idOperador) {
        this.idOperador = idOperador;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getTipoErrorPreseteo() {
        return tipoErrorPreseteo;
    }

    public void setTipoErrorPreseteo(int tipoErrorPreseteo) {
        this.tipoErrorPreseteo = tipoErrorPreseteo;
    }

    public int getVolumenAutorizado() {
        return volumenAutorizado;
    }

    public void setVolumenAutorizado(int volumenAutorizado) {
        this.volumenAutorizado = volumenAutorizado;
    }

    public int getVolumenAceptado() {
        return volumenAceptado;
    }

    public void setVolumenAceptado(int volumenAceptado) {
        this.volumenAceptado = volumenAceptado;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getTipoTag() {
        return tipoTag;
    }

    public void setTipoTag(int tipoTag) {
        this.tipoTag = tipoTag;
    }

    public int getTipoCierre() {
        return tipoCierre;
    }

    public void setTipoCierre(int tipoCierre) {
        this.tipoCierre = tipoCierre;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdUsuarioRegistro() {
        return idUsuarioRegistro;
    }

    public void setIdUsuarioRegistro(String idUsuarioRegistro) {
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public String getEstadoMigracion() {
        return estadoMigracion;
    }

    public void setEstadoMigracion(String estadoMigracion) {
        this.estadoMigracion = estadoMigracion;
    }

    public int getEscenario() {
        return escenario;
    }

    public void setEscenario(int escenario) {
        this.escenario = escenario;
    }

    public String getContometroInicial() {
        return contometroInicial;
    }

    public void setContometroInicial(String contometroInicial) {
        this.contometroInicial = contometroInicial;
    }

    public String getContometroFinal() {
        return contometroFinal;
    }

    public void setContometroFinal(String contometroFinal) {
        this.contometroFinal = contometroFinal;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageDecodeString() {
        return imageDecodeString;
    }

    public void setImageDecodeString(String imageDecodeString) {
        this.imageDecodeString = imageDecodeString;
    }
}
