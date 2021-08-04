package com.app.estockcard.model;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class Shift extends RealmObject {

    private String groupID;
    private int orderInDay;
    private String dateWork;
    private int employeeID =-1;
    private String startHour;
    private String endHour;

    @Ignore
    private int drawableID;

    @Ignore
    private String name;
    @Ignore
    private int holderType;

    @Ignore
    private List<Shift> shifter;

    @Ignore
    public static final int ATTRIBUTE_ID = 1;
    @Ignore
    public static final int ATTRIBUTE_WORK_HOUR = 2;

    public List<Shift> getShifter() {
        return shifter;
    }

    public void setShifter(List<Shift> shifter) {
        this.shifter = shifter;
    }

    public int getHolderType() {
        return holderType;
    }

    public void setHolderType(int holderType) {
        this.holderType = holderType;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public int getOrderInDay() {
        return orderInDay;
    }

    public void setOrderInDay(int orderInDay) {
        this.orderInDay = orderInDay;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getDateWork() {
        return dateWork;
    }

    public void setDateWork(String dateWork) {
        this.dateWork = dateWork;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }
}
