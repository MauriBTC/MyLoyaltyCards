package com.example.myloyaltycards.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "cards")
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "client_id")
    private String clientID;
    @ColumnInfo(name = "company_name")
    private String companyName;
    @ColumnInfo(name = "owner_name")
    private String ownerName;
    @ColumnInfo(name = "owner_surname")
    private String ownerSurname;
    @ColumnInfo(name = "logo_path")
    private String logoPath;
    @ColumnInfo(name = "format_code")
    private int formatCode;
    private int color;

    public Card (String clientID, String companyName, String ownerName, String ownerSurname,
                 String logoPath, int formatCode, int color) {
        this.clientID = clientID;
        this.companyName = companyName;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.logoPath = logoPath;
        this.formatCode = formatCode;
        this.color = color;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname = ownerSurname;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(int formatCode) {
        this.formatCode = formatCode;
    }
}
