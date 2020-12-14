package com.example.myapplication.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterBuySellBike1;
import com.example.myapplication.model.BuySellAd;
import com.example.myapplication.model.CommonResponse;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<BuySellAd> buySellAdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        toSetActionBar();

        findViewById(R.id.fab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAdsActivity.this, CreateNewAdsActivity.class));
            }
        });

        rv = findViewById(R.id.rvList2);
        rv.setLayoutManager(new LinearLayoutManager(MyAdsActivity.this));
        rv.hasFixedSize();
        if (buySellAdList != null) {
            rv.setBackgroundColor(getResources().getColor(R.color.light_blue));
            rv.setAdapter(new AdapterBuySellBike1(MyAdsActivity.this, buySellAdList, true));
        }
        if (buySellAdList == null) {
            rv.setBackgroundColor(getResources().getColor(R.color.white));
            toFetchData();
        }
    }

    private void toFetchData() {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            Utility.showLoadingDialog(MyAdsActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(MyAdsActivity.this).addToRequestQueue(
                    RequestBuilder.toFetchVehiclesList(
                            header,
                            header.toString(),
                            new Response.Listener<CommonResponse>() {
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(MyAdsActivity.this.getResources().getString(R.string.str_success_uppercase))) {

                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ?
                                                            response.REASON : MyAdsActivity.this.getResources().getString(R.string.str_invalid_data),
                                                    MyAdsActivity.this);

                                        } else {
                                            if (response.DATA != null) {
                                                buySellAdList = response.DATA.buySellAdList;
                                                if (buySellAdList != null) {
                                                    rv.setBackgroundColor(getResources().getColor(R.color.light_blue));
                                                    rv.setAdapter(new AdapterBuySellBike1(MyAdsActivity.this, buySellAdList, false));
                                                }
                                            } else {
                                                Utility.toCallToast(getString(R.string.str_no_information_found), MyAdsActivity.this);
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(MyAdsActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError: " + e.getMessage(), MyAdsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(MyAdsActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), MyAdsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();

                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(MyAdsActivity.this.getResources().getString(R.string.str_invalid_data), MyAdsActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toSetActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pale_lilac)));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }
}
