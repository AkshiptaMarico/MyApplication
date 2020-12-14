package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class SaveNewAd {
    @SerializedName("TOKEN")
    public String TOKEN;
    @SerializedName("MANUFACTURER_ID")
    public String MANUFACTURER_ID;
    @SerializedName("MODEL_ID")
    public String MODEL_ID;
    @SerializedName("YEAR")
    public String YEAR;
    @SerializedName("IMAGE")
    public String IMAGE;
    @SerializedName("ASKING_PRICE")
    public String ASKING_PRICE;
    @SerializedName("KMS_DRIVEN")
    public String KMS_DRIVEN;
    @SerializedName("IS_NEGOTIABLE")
    public int IS_NEGOTIABLE;
    @SerializedName("DESCRIPTION")
    public String DESCRIPTION;
    @SerializedName("TITLE")
    public String TITLE;
}
/*
TOKEN:ABCD
MANUFACTURER_ID:2
MODEL_ID:3
YEAR:2020
IMAGE:abc.jpg
ASKING_PRICE:20000
KMS_DRIVEN:4000
IS_NEGOTIABLE:0
DESCRIPTION:Good To use
TITLE:Bajaj Avenger 220R
 */
