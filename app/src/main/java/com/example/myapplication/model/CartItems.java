package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class CartItems {
    @SerializedName("ID")
    public int ID;
    @SerializedName("USER_ID")
    public int USER_ID;
    @SerializedName("PRODUCT_ID") public int PRODUCT_ID;
    @SerializedName("PRODUCT_NAME") public String PRODUCT_NAME;
    @SerializedName("IS_DELETED")
    public int IS_DELETED;
    @SerializedName("QUANTITY")
    public int QUANTITY;
    @SerializedName("SELLING_PRICE")
    public String SELLING_PRICE;
    @SerializedName("FINAL_PRICE")
    public String FINAL_PRICE;
    @SerializedName("CREATED_DATE")
    public String CREATED_DATE;
    @SerializedName("UPDATED_DATE")
    public String UPDATED_DATE;
}
/*{
"ID": 2,
"USER_ID": 1,
"PRODUCT_ID": 2,
"IS_DELETED": 0,
"QUANTITY": 3,
"SELLING_PRICE": "250",
"FINAL_PRICE": "250",
"CREATED_DATE": "2020-11-29T09:15:18.000000Z",
"UPDATED_DATE": "2020-11-29T09:18:55.000000Z",
"PRODUCT_NAME": "Gaze"}, */
