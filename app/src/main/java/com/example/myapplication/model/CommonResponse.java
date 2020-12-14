package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class CommonResponse {
    @SerializedName("STATUS")
    public String STATUS;
    @SerializedName("REASON")
    public String REASON;
    @SerializedName("DATA")
    public CommonObject DATA;
}
/*
{
    "STATUS": "SUCCESS",
    "REASON": null,
    "DATA": {
        "BUYSELLAD": [
 */
