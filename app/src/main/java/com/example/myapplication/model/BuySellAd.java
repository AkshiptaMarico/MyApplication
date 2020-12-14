package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class BuySellAd {

    @SerializedName("ID")
    public int ID;
    @SerializedName("USER_ID")
    public int USER_ID;
    @SerializedName("MANUFACTURER")
    public String MANUFACTURER;
    @SerializedName("MODEL")
    public String MODEL;
    @SerializedName("YEAR")
    public int YEAR;
    @SerializedName("IMAGE")
    public String IMAGE;
    @SerializedName("ASKING_PRICE")
    public int ASKING_PRICE;
    @SerializedName("KMS_DRIVEN")
    public int KMS_DRIVEN;
    @SerializedName("IS_NEGOTIABLE")
    public int IS_NEGOTIABLE;
    @SerializedName("TITLE")
    public String TITLE;
    @SerializedName("STATUS")
    public String STATUS;
    @SerializedName("LOCATION")
    public String LOCATION;
    @SerializedName("LATITUDE")
    public String LATITUDE;
    @SerializedName("LONGITUDE")
    public String LONGITUDE;
    @SerializedName("REG_NO")
    public String REG_NO;
    @SerializedName("MOBILE")
    public String MOBILE;
    @SerializedName("NO_OF_VIEWS")
    public String NO_OF_VIEWS;
    @SerializedName("DESCRIPTION")
    public String DESCRIPTION;
    @SerializedName("CREATED_DATE")
    public String CREATED_DATE;
    @SerializedName("UPDATED_DATE")
    public String UPDATED_DATE;
}
/*
"BUYSELLAD": [
            {
                "ID": 4,
                "USER_ID": 1,
                "MANUFACTURER": "HONDA",
                "MODEL": "HF DAWN",
                "YEAR": 2020,
                "IMAGE": "abc.jpg",
                "ASKING_PRICE": 20000,
                "KMS_DRIVEN": 4000,
                "IS_NEGOTIABLE": 0,
                "TITLE": "Bajaj Avenger 220R",
                "STATUS": "UNDER REVIEW",
                "CREATED_DATE": "2020-10-03T20:27:39.000000Z",
                "UPDATED_DATE": "2020-10-03T20:27:39.000000Z"
            }
        ]
        "ID": 1,
                "USER_ID": 1,
                "MANUFACTURER": "HERO MOTOCORP",
                "MODEL": "SPLENDOR+ BS6",
                "YEAR": 2020,
                "IMAGE": "1607265496098.png",
                "ASKING_PRICE": 20000,
                "KMS_DRIVEN": 4000,
                "IS_NEGOTIABLE": 0,
                "TITLE": "Bajaj Avenger 220R",
                "STATUS": "UNDER REVIEW",
                "LOCATION": null,
                "LATITUDE": null,
                "LONGITUDE": null,
                "REG_NO": null,
                "MOBILE": 8655571666,
                "NO_OF_VIEWS": null,
                "DESCRIPTION": "Good To use",
                "CREATED_DATE": "2020-10-03T20:10:08.000000Z",
                "UPDATED_DATE": "2020-10-03T20:10:08.000000Z"
 */
