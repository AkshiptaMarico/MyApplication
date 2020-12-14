package com.example.myapplication.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterBuySellBike;
import com.example.myapplication.model.BuySellAd;
import com.example.myapplication.model.CommonResponse;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellOrBuyBikeFragment extends Fragment {

    private RecyclerView rv;
    private List<BuySellAd> buySellAdList;

    public SellOrBuyBikeFragment() {
    }

    public static SellOrBuyBikeFragment newInstance() {
        return new SellOrBuyBikeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void toFetchData() {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            Utility.showLoadingDialog(getActivity(), "", " Please wait ... ");
            ApiRequest.getInstance(getActivity()).addToRequestQueue(
                    RequestBuilder.toFetchVehiclesList(
                            header,
                            header.toString(),
                            new Response.Listener<CommonResponse>() {
                                @Override
                                public void onResponse(CommonResponse response) {
                                    try {
                                        if (!response.STATUS.trim().toUpperCase().equals(getActivity().getResources().getString(R.string.str_success_uppercase))) {
                                            Utility.toCallToast((response.REASON != null && response.REASON.length() > 0) ? response.REASON : getActivity().getResources().getString(R.string.str_invalid_data), getActivity());
                                        } else {
                                            if (response.DATA != null) {
                                                buySellAdList = response.DATA.buySellAdList;
                                                if (buySellAdList != null) {
                                                    rv.setBackgroundColor(getResources().getColor(R.color.light_blue));
                                                    rv.setAdapter(new AdapterBuySellBike(getActivity(), buySellAdList, false));
                                                } else {
                                                    rv.setBackgroundColor(getResources().getColor(R.color.white));
                                                    rv.setAdapter(null);
                                                }
                                            } else {
                                                Utility.toCallToast(getString(R.string.str_no_information_found), getActivity());
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(getActivity().getResources().getString(R.string.str_invalid_data)
                                                + "\nError: " + e.getMessage(), getActivity());
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(getActivity().getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), getActivity());
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();

                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }));

        } catch (Exception | Error e) {
            Utility.toCallToast(getActivity().getResources().getString(R.string.str_invalid_data), getActivity());
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_sell_or_buy_bike, container, false);
            rv = view.findViewById(R.id.rvList1);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.hasFixedSize();
            if (buySellAdList != null) {
                rv.setBackgroundColor(getResources().getColor(R.color.light_blue));
                rv.setAdapter(new AdapterBuySellBike(getActivity(), buySellAdList, false));
            } else {
                rv.setBackgroundColor(getResources().getColor(R.color.white));
                toFetchData();
            }
            return view;
        } catch (Exception | Error e) {
            e.printStackTrace();
            return null;
        }
    }
}
