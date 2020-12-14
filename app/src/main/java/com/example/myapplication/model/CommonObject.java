package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonObject {
    @SerializedName("BUYSELLAD")
    public List<BuySellAd> buySellAdList;

    @SerializedName("PRODUCTS")
    public List<Products> productsList;

    // For Cart's Items
    @SerializedName("ITEMS")
    public List<CartItems> cartItemsList;
    @SerializedName("COUNT")
    public int COUNT;
    @SerializedName("CART_TOTAL")
    public String CART_TOTAL;
}
