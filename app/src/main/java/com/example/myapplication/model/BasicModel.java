package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BasicModel {
    @SerializedName("ID")
    public int ID;
    @SerializedName("NAME")
    public String NAME;

    @SerializedName("master")
    public List<BasicModel> basicModelList;
}
/*
{
        "ID": 1,
        "NAME": "HERO MOTOCORP"
    },
 */
