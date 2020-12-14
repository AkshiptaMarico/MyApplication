package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.model.BuySellAd;
import com.example.myapplication.model.CommonResponse;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellOrBuyBikeDetailsActivity extends AppCompatActivity {

    private TextView tvAmount, tvName, tvLocation, tvDescriptionHeader, tvDescription, tvYear, tvRegisterNo, tvKMDriven, tvAdID, tvReportAd;
    private ImageView ivBike;
    private Button btnCall;
    private String id, callNumber = "";

    private List<BuySellAd> buySellAdList;
    private BuySellAd item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_or_buy_bike_details);
        toSetActionBar();
        initViews();
        toFetchData();
    }

    private void initViews() {
        try {
            ivBike = findViewById(R.id.ivDetailsBike);
            btnCall = findViewById(R.id.btnDetailsCall);

            ivBike = findViewById(R.id.ivDetailsBike);
            tvAmount = findViewById(R.id.tvDetailsAmount);
            tvName = findViewById(R.id.tvDetailsName);
            tvLocation = findViewById(R.id.tvDetailsLocation);
            tvDescriptionHeader = findViewById(R.id.tvDetailsDescriptionHeading);
            tvDescription = findViewById(R.id.tvDetailsDescription);
            tvYear = findViewById(R.id.tvDetailsYear);
            tvRegisterNo = findViewById(R.id.tvDetailsRegisterNo);
            tvKMDriven = findViewById(R.id.tvDetailsKmDriven);
            tvAdID = findViewById(R.id.tvDetailsAdId);
            tvReportAd = findViewById(R.id.tvDetailsReportAd);

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callNumber)));
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
            });

            if (getIntent() != null && getIntent().hasExtra("ID")) {
                id = getIntent().getStringExtra("ID");
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void toFetchData() {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            Utility.showLoadingDialog(SellOrBuyBikeDetailsActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(SellOrBuyBikeDetailsActivity.this).addToRequestQueue(
                    RequestBuilder.toFetchVehiclesDetailById(
                            header,
                            "{" +
                                    "    \"TOKEN\": \"ABCD\"," +
                                    "    \"BUY_SELL_ID\": \"" + id + "\"" +
                                    "}",
                            new Response.Listener<CommonResponse>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(SellOrBuyBikeDetailsActivity.this.getResources().getString(R.string.str_success_uppercase))) {
                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ? response.REASON : SellOrBuyBikeDetailsActivity.this.getResources().getString(R.string.str_invalid_data), SellOrBuyBikeDetailsActivity.this);
                                        } else {
                                            if (response.DATA != null) {
                                                buySellAdList = response.DATA.buySellAdList;
                                                if (buySellAdList != null && buySellAdList.size() > 0) {
                                                    if (buySellAdList.get(0) != null) {
                                                        item = buySellAdList.get(0);
                                                        Picasso.get()
                                                                .load(RequestBuilder.IMAGE_BASE_URL + item.IMAGE)
                                                                .error(R.drawable.ic_lightblue_motorcycle)
                                                                .into(ivBike);
                                                        tvAmount.setText("" + getString(R.string.str_rs) + item.ASKING_PRICE);
                                                        tvName.setText("" + (item.TITLE != null ? item.TITLE : ""));
                                                        tvLocation.setText("" + (item.LOCATION != null ? item.LOCATION : ""));
                                                        tvDescription.setText("" + (item.DESCRIPTION != null ? item.DESCRIPTION : ""));
                                                        tvYear.setText("" + item.YEAR);
                                                        tvRegisterNo.setText("" + (item.REG_NO != null ? item.REG_NO : ""));
                                                        tvAdID.setText("" + item.ID);
                                                        callNumber = (item.MOBILE != null ? item.MOBILE : "");
                                                    }
                                                }
                                            } else {
                                                Utility.toCallToast(getString(R.string.str_no_information_found), SellOrBuyBikeDetailsActivity.this);
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(SellOrBuyBikeDetailsActivity.this.getResources().getString(R.string.str_invalid_data) + "\nError: " + e.getMessage(), SellOrBuyBikeDetailsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(SellOrBuyBikeDetailsActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), SellOrBuyBikeDetailsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();

                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(SellOrBuyBikeDetailsActivity.this.getResources().getString(R.string.str_invalid_data), SellOrBuyBikeDetailsActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSetActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }
}
