package com.example.tcpsocketclient.Entities;

import java.io.Serializable;

public class UserEntity implements Serializable {

    private int idSqlLite;
    private int IdUser;

    public int getIdPerson() {
        return IdPerson;
    }

    public void setIdPerson(int idPerson) {
        IdPerson = idPerson;
    }

    private int IdPerson;
    private String PersonName;
    private String FirstLastName;
    private String SecondLastName;
    public String Photocheck;
    private String UUser;
    private String UPassword;
    private String RegistrationStatus;
    public String getValorConsulta() {
        return ValorConsulta;
    }

    public void setValorConsulta(String valorConsulta) {
        ValorConsulta = valorConsulta;
    }

    public String getMensajeConsulta() {
        return MensajeConsulta;
    }

    public void setMensajeConsulta(String mensajeConsulta) {
        MensajeConsulta = mensajeConsulta;
    }

    public String ValorConsulta;
    public String MensajeConsulta;
    public UserEntity() {
    }

    public UserEntity(int idUser, int idPerson, String personName, String photocheck, String firstLastName, String secondLastName, String UUser, String UPassword, String registrationStatus) {
        IdUser = idUser;
        IdPerson = idPerson;
        PersonName = personName;
        Photocheck = photocheck;
        FirstLastName = firstLastName;
        SecondLastName = secondLastName;
        this.UUser = UUser;
        this.UPassword = UPassword;
        RegistrationStatus = registrationStatus;
    }

    public UserEntity(int idSqlLite, int idUser, int idPerson, String personName, String photocheck, String firstLastName, String secondLastName, String UUser, String UPassword, String registrationStatus) {
        this.idSqlLite = idSqlLite;
        IdUser = idUser;
        IdPerson = idPerson;
        PersonName = personName;
        Photocheck = photocheck;
        FirstLastName = firstLastName;
        SecondLastName = secondLastName;
        this.UUser = UUser;
        this.UPassword = UPassword;
        RegistrationStatus = registrationStatus;
    }

    public int getIdSqlLite() {
        return idSqlLite;
    }

    public void setIdSqlLite(int idSqlLite) {
        this.idSqlLite = idSqlLite;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getPhotocheck() {
        return Photocheck;
    }

    public void setPhotocheck(String photocheck) {
        Photocheck = photocheck;
    }

    public String getFirstLastName() {
        return FirstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        FirstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return SecondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        SecondLastName = secondLastName;
    }

    public String getUUser() {
        return UUser;
    }

    public void setUUser(String UUser) {
        this.UUser = UUser;
    }

    public String getUPassword() {
        return UPassword;
    }

    public void setUPassword(String UPassword) {
        this.UPassword = UPassword;
    }

    public String getRegistrationStatus() {
        return RegistrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        RegistrationStatus = registrationStatus;
    }
}
