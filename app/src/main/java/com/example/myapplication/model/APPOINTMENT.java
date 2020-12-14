package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APPOINTMENT {
    @SerializedName("ID")
    public int iD;
    @SerializedName("CUSTOMER_NAME")
    public String CUSTOMER_NAME;
    @SerializedName("GARAGE_NAME")
    public String GARAGE_NAME;
    @SerializedName("DESCRIPTION")
    public String DESCRIPTION;
    @SerializedName("VEHICLE_NUMBER")
    public String VEHICLE_NUMBER;
    @SerializedName("STATUS")
    public String STATUS;
    @SerializedName("TIME")
    public String TIME;
    @SerializedName("IS_PAYMENT_COMPLETED")
    public int IS_PAYMENT_COMPLETED;
    @SerializedName("IS_DELIVERED")
    public int IS_DELIVERED;
    @SerializedName("RATING")
    public Object RATING;
    @SerializedName("BILL_UPLOAD")
    public Object BILL_UPLOAD;
    @SerializedName("CREATED_DATE")
    public String CREATED_DATE;
    @SerializedName("UPDATED_DATE")
    public String UPDATED_DATE;

    @SerializedName("APPOINTMENTS")
    public List<APPOINTMENT> appointmentList;

}
