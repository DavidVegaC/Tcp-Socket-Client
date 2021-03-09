package com.example.tcpsocketclient.Entities;

import java.io.Serializable;

public class PersonEntity implements Serializable {

    public int IdPersonSqlLite;
    public int IdPerson;
    public String DocumentNumber;
    public String PersonName;
    public String FirstLastName;
    public String SecondLastName;
    public String Photocheck;

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

    public String getPathFileBase64() {
        return PathFileBase64;
    }

    public void setPathFileBase64(String pathFileBase64) {
        PathFileBase64 = pathFileBase64;
    }

    public String PathFileBase64;



    private String RegistrationStatus;

    public PersonEntity(int idPerson, String personName, String firstLastName, String secondLastName, String photocheck, String documentNumber, String registrationStatus, String pathFileBase64) {
        IdPerson = idPerson;
        DocumentNumber = documentNumber;
        PersonName = personName;
        FirstLastName = firstLastName;
        SecondLastName = secondLastName;
        Photocheck = photocheck;
        RegistrationStatus = registrationStatus;
        PathFileBase64 = pathFileBase64;
    }

    public PersonEntity(int idPersonSqlLite, int idPerson, String personName, String firstLastName, String secondLastName, String photocheck, String documentNumber, String registrationStatus, String pathFileBase64) {
        IdPersonSqlLite = idPersonSqlLite;
        IdPerson = idPerson;
        DocumentNumber = documentNumber;
        PersonName = personName;
        FirstLastName = firstLastName;
        SecondLastName = secondLastName;
        Photocheck = photocheck;
        RegistrationStatus = registrationStatus;
        PathFileBase64 = pathFileBase64;
    }

    public PersonEntity() {
    }

    public int getIdPersonSqlLite() {
        return IdPersonSqlLite;
    }

    public void setIdPersonSqlLite(int idPersonSqlLite) {
        IdPersonSqlLite = idPersonSqlLite;
    }

    public int getIdPerson() {
        return IdPerson;
    }

    public void setIdPerson(int idPerson) {
        IdPerson = idPerson;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
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

    public String getPhotocheck() {
        return Photocheck;
    }

    public void setPhotocheck(String photocheck) {
        Photocheck = photocheck;
    }

    public String getRegistrationStatus() {
        return RegistrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        RegistrationStatus = registrationStatus;
    }
}
