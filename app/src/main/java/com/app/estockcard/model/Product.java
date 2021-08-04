package com.app.estockcard.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject {

    @PrimaryKey
    private int id;

    //Category means is group type of goods example : 1. Drinks, 2. Snacks
    private int categoryId=-1;

    private String name;
    private String dateCreated;
    private String barcode;
    private String description;
    private int sellingPrice;
    private int purchasePrice;
    private int availableStock;
    private int minimumStock;
    private byte[] photo;

    //There is two types : 1. Group, 2. Item
    private int type;
    @Ignore
    public static final int CategoryType =0;
    @Ignore
    public static final int ProductType =1;

    @Ignore
    public static final int NOT_ADD_QUANTITY=21;
    @Ignore
    public static final int ADD_QUANTITY=22;

    @Ignore
    private int quantityState;

    private int countOrdered;

    public void setQuantityState(int quantityState) {
        this.quantityState = quantityState;
    }

    public int getQuantityState() {
        return quantityState;
    }



    public void setCountOrdered(int countOrdered) {
        this.countOrdered = countOrdered;
    }

    public int getCountOrdered() {
        return countOrdered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int  getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
