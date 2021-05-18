package com.example.tcpsocketclient.Entities;

public class MigrationEntity {
    private int IdSqlLiteMigration;
    private String MigrationStartDate;
    private String MigrationEndDate;
    private String MigrationDescription;
    private String RegistrationStatus;
    private String RegistrationUser;
    private int MigrationScene;

    public MigrationEntity() {
    }

    public int getIdSqlLiteMigration() {
        return IdSqlLiteMigration;
    }

    public void setIdSqlLiteMigration(int idSqlLiteMigration) {
        IdSqlLiteMigration = idSqlLiteMigration;
    }

    public String getMigrationStartDate() {
        return MigrationStartDate;
    }

    public void setMigrationStartDate(String migrationStartDate) {
        MigrationStartDate = migrationStartDate;
    }

    public String getMigrationEndDate() {
        return MigrationEndDate;
    }

    public void setMigrationEndDate(String migrationEndDate) {
        MigrationEndDate = migrationEndDate;
    }

    public String getMigrationDescription() {
        return MigrationDescription;
    }

    public void setMigrationDescription(String migrationDescription) {
        MigrationDescription = migrationDescription;
    }

    public String getRegistrationStatus() {
        return RegistrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        RegistrationStatus = registrationStatus;
    }

    public String getRegistrationUser() {
        return RegistrationUser;
    }

    public void setRegistrationUser(String registrationUser) {
        RegistrationUser = registrationUser;
    }

    public int getMigrationScene() {
        return MigrationScene;
    }

    public void setMigrationScene(int migrationScene) {
        MigrationScene = migrationScene;
    }
}
