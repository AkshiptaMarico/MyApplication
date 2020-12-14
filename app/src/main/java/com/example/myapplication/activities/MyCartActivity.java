package com.example.myapplication.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterCartItems;
import com.example.myapplication.model.CartItems;
import com.example.myapplication.model.CommonResponse;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCartActivity extends AppCompatActivity {

    private RecyclerView rvCartItemList;
    private Button btnCartPayNow;
    private TextView tvCartItemCount, tvCartTotalValue;
    private AlertDialog alertDialog;

    private List<CartItems> cartItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_my_cart);

            toSetActionBar();
            init();
            if (cartItemsList == null) {
                rvCartItemList.setBackgroundColor(getResources().getColor(R.color.white));
                toFetchData();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            btnCartPayNow = findViewById(R.id.btnCartPayNow);

            tvCartItemCount = findViewById(R.id.tvCartItemCount);
            tvCartTotalValue = findViewById(R.id.tvCartTotalValue);

            rvCartItemList = findViewById(R.id.rvCartItemList);
            rvCartItemList.setHasFixedSize(true);
            rvCartItemList.setLayoutManager(new LinearLayoutManager(MyCartActivity.this));
            rvCartItemList.hasFixedSize();

        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void toFetchData() {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            Utility.showLoadingDialog(MyCartActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(MyCartActivity.this).addToRequestQueue(
                    RequestBuilder.toFetchCart(
                            header,
                            new Gson().toJson(header),
                            new Response.Listener<CommonResponse>() {
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(MyCartActivity.this.getResources().getString(R.string.str_success_uppercase))) {

                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ?
                                                            response.REASON : MyCartActivity.this.getResources().getString(R.string.str_invalid_data),
                                                    MyCartActivity.this);

                                        } else {
                                            tvCartItemCount.setText(String.format(getResources().getString(R.string.str_n_items_in_your_cart), response.DATA != null ? response.DATA.COUNT : 0));
                                            tvCartTotalValue.setText(getString(R.string.str_rs) + " " + (response.DATA != null ? response.DATA.CART_TOTAL : "0"));
                                            if (response.DATA != null) {
                                                cartItemsList = response.DATA.cartItemsList;
                                                if (cartItemsList != null) {
//                                                    rvCartItemList.setBackgroundColor(getResources().getColor(R.color.light_blue));
                                                    rvCartItemList.setAdapter(new AdapterCartItems(MyCartActivity.this, cartItemsList, false));
                                                }
                                            } else {
                                                Utility.toCallToast(getString(R.string.str_no_information_found), MyCartActivity.this);
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError: " + e.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();

                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data), MyCartActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    public void toCallIncreaseItemQuantityApi(int position) {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            header.put("ITEM_ID", cartItemsList.get(position).ID + "");
            Utility.showLoadingDialog(MyCartActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(MyCartActivity.this).addToRequestQueue(
                    RequestBuilder.toIncreaseCartItemQuantity(
                            header,
                            new Gson().toJson(header),
                            new Response.Listener<CommonResponse>() {
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(MyCartActivity.this.getResources().getString(R.string.str_success_uppercase))) {

                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ?
                                                            response.REASON : MyCartActivity.this.getResources().getString(R.string.str_invalid_data),
                                                    MyCartActivity.this);
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError: " + e.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                    toFetchData();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();
                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                    toFetchData();
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data), MyCartActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    public void toCallDecreaseItemQuantityApi(int position) {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            header.put("ITEM_ID", cartItemsList.get(position).ID + "");
            Utility.showLoadingDialog(MyCartActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(MyCartActivity.this).addToRequestQueue(
                    RequestBuilder.toDecreaseCartItemQuantity(
                            header,
                            new Gson().toJson(header),
                            new Response.Listener<CommonResponse>() {
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(MyCartActivity.this.getResources().getString(R.string.str_success_uppercase))) {

                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ?
                                                            response.REASON : MyCartActivity.this.getResources().getString(R.string.str_invalid_data),
                                                    MyCartActivity.this);
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError: " + e.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                    toFetchData();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();

                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                    toFetchData();
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data), MyCartActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    public void toCallRemoveItemFromCartApi(int position) {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            header.put("ITEM_ID", cartItemsList.get(position).ID + "");
            Utility.showLoadingDialog(MyCartActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(MyCartActivity.this).addToRequestQueue(
                    RequestBuilder.toDeleteCartItem(
                            header,
                            new Gson().toJson(header),
                            new Response.Listener<CommonResponse>() {
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(MyCartActivity.this.getResources().getString(R.string.str_success_uppercase))) {

                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ?
                                                            response.REASON : MyCartActivity.this.getResources().getString(R.string.str_invalid_data),
                                                    MyCartActivity.this);
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError: " + e.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                    toFetchData();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), MyCartActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();

                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                    toFetchData();
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(MyCartActivity.this.getResources().getString(R.string.str_invalid_data), MyCartActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    public void toConfirmDeleteItemFromCart(final int position) {
        try {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_alert, null);
            dialogBuilder.setView(dialogView);

            TextView tvMsg = dialogView.findViewById(R.id.tvMsg);
            TextView tvNo = dialogView.findViewById(R.id.tvNo);
            TextView tvYes = dialogView.findViewById(R.id.tvYes);

            tvMsg.setText(R.string.str_msg_do_you_want_to_delete_this_item_);
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCallRemoveItemFromCartApi(position);
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            });
            if (alertDialog == null) {
                alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        } catch (Exception | Error e) {
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
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pale_lilac)));
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }
}
