package com.app.estockcard.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Employee extends RealmObject {

    @PrimaryKey
    private int id;

    private byte[] photo;
    private String name;
    private String residenceNumber;
    private String placeBirthday;
    private String dateBirthday;
    private String gender;

    private String email;
    private String cellularNumber;
    private String whatsAppNumber;

    private String city;
    private String subDistrict;
    private String village;
    private String postalCode;
    private String address;

    //There is two types : 1. Group, 2. Item
    private int type;
    @Ignore
    public static final int EmployeeType =0;
    @Ignore
    public static final int CustomerType =1;


    @Ignore
    private int viewType;

    @Ignore
    private int drawableID;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResidenceNumber() {
        return residenceNumber;
    }

    public void setResidenceNumber(String residenceNumber) {
        this.residenceNumber = residenceNumber;
    }

    public String getPlaceBirthday() {
        return placeBirthday;
    }

    public void setPlaceBirthday(String placeBirthday) {
        this.placeBirthday = placeBirthday;
    }

    public String getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(String dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellularNumber() {
        return cellularNumber;
    }

    public void setCellularNumber(String cellularNumber) {
        this.cellularNumber = cellularNumber;
    }

    public String getWhatsAppNumber() {
        return whatsAppNumber;
    }

    public void setWhatsAppNumber(String whatsAppNumber) {
        this.whatsAppNumber = whatsAppNumber;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
