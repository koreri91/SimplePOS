package com.app.estockcard.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject {
    @PrimaryKey
    private String id;

    private String dateTime;
    private String operator;
    private int paymentMethod;
    //if credit set downpayment
    private String paymentLog;
    private double downPayment;
    private String dueDatePayment;
    //if credit get buyer from customer id
    private int buyerId;
    private double subtotal;
    private double discount;
    private double total;
    private double leftoverMoney;
    private int status;
    private String listBill;

    @Ignore
    public static final int CashPaymentMethod=31;
    @Ignore
    public static final int CreditPaymentMethod=21;

    @Ignore
    public static final int TRANSACTION_ALL=13;
    @Ignore
    public static final int TRANSACTION_CANCELLED=10;
    @Ignore
    public static final int TRANSACTION_CREDIT=11;
    @Ignore
    public static final int TRANSACTION_SUCCESS=12;

    @Ignore
    private int viewHolder;

    @Ignore
    public static final int viewHolderTitle=56;
    @Ignore
    public static final int viewHolderData=57;

    public int getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(int viewHolder) {
        this.viewHolder = viewHolder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentLog(String paymentLog) {
        this.paymentLog = paymentLog;
    }

    public String getPaymentLog() {
        return paymentLog;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLeftoverMoney(double changeMoney) {
        this.leftoverMoney = changeMoney;
    }

    public double getLeftoverMoney() {
        return leftoverMoney;
    }

    public void setDueDatePayment(String dueDatePayment) {
        this.dueDatePayment = dueDatePayment;
    }

    public String getDueDatePayment() {
        return dueDatePayment;
    }

    public void setListBill(String listBill) {
        this.listBill = listBill;
    }

    public String getListBill() {
        return listBill;
    }
}
