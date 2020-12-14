package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Products {

    @SerializedName("ID")
    public int ID;
    @SerializedName("NAME")
    public String NAME;
    @SerializedName("PRICE")
    public String PRICE;
    @SerializedName("CATEGORY_ID")
    public int CATEGORY_ID;
    @SerializedName("CATEGORY")
    public String CATEGORY;
    @SerializedName("QUANTITY")
    public int QUANTITY;
    @SerializedName("IS_DELETED")
    public int IS_DELETED;
    @SerializedName("CREATED_DATE")
    public String CREATED_DATE;
    @SerializedName("UPDATED_DATE")
    public String UPDATED_DATE;
//    @SerializedName("PROD_ATTR")
//    public String PROD_ATTR;
}
/*
                "ID": 1,
                "NAME": "Fuse Pump",
                "PRICE": "300.00",
                "CATEGORY_ID": 1,
                "CATEGORY": "BODY PARTS",
                "QUANTITY": 10,
                "IS_DELETED": 0,
                "CREATED_DATE": "2020-10-03T20:10:08.000000Z",
                "UPDATED_DATE": "2020-10-03T20:10:08.000000Z",
                "PROD_ATTR": []
 */
