package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class GARAGEPROFILE{
    @SerializedName("ID")
    public int ID;
    @SerializedName("NAME")
    public String NAME;
    @SerializedName("CONTACT_NO")
    public Object CONTACT_NO;
    @SerializedName("ALTERNATE_CONTACT_NO")
    public Object ALTERNATE_CONTACT_NO;
    @SerializedName("ADDRESS")
    public String ADDRESS;
    @SerializedName("PAN")
    public String PAN;
    @SerializedName("PAN_UPLOAD")
    public String PAN_UPLOAD;
    @SerializedName("GUMASTHA")
    public String GUMASTHA;
    @SerializedName("GUMASTHA_UPLOAD")
    public String GUMASTHA_UPLOAD;
    @SerializedName("GSTIN")
    public String GSTIN;
    @SerializedName("GSTIN_UPLOAD")
    public String GSTIN_UPLOAD;
    @SerializedName("OWNER_NAME")
    public String OWNER_NAME;
    @SerializedName("OWNER_CONTACT")
    public long OWNER_CONTACT;
    @SerializedName("OWNER_ADDRESS")
    public String OWNER_ADDRESS;
    @SerializedName("OWNER_AADHAR_UPLOAD")
    public Object OWNER_AADHAR_UPLOAD;
    @SerializedName("OWNER_PAN_UPLOAD")
    public Object OWNER_PAN_UPLOAD;
    @SerializedName("DAILY_BANDWIDTH")
    public Object DAILY_BANDWIDTH;
    @SerializedName("PLAN_ID")
    public int PLAN_ID;
    @SerializedName("PLAN_EXPIRY_DATE")
    public String PLAN_EXPIRY_DATE;
}
