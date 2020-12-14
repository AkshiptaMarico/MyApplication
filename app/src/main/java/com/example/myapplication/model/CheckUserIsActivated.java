package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class CheckUserIsActivated {

    @SerializedName("IS_ACTIVATED")
    public boolean IS_ACTIVATED;
    @SerializedName("GARAGE_PROFILE")
    public GARAGEPROFILE GARAGE_PROFILE;
    @SerializedName("DASHBOARD_DETAIL")
    public DASHBOARDDETAIL DASHBOARD_DETAIL;
}




