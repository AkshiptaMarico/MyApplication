package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.activities.OwnRequirementsActivity;
import com.example.myapplication.adapters.AdapterProducts;
import com.example.myapplication.model.BasicModel;
import com.example.myapplication.model.CommonResponse;
import com.example.myapplication.model.Products;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopFragment extends Fragment {

    private TabLayout tabLayout;
    private RecyclerView rv;
    private List<Products> productsList;

    public ShopFragment() {
    }

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_shop, container, false);
            toFetchCategoryData();

            rv = view.findViewById(R.id.rvShopProducts);
            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            rv.hasFixedSize();
            if (productsList != null) {
                rv.setBackgroundColor(getResources().getColor(R.color.light_blue));
                rv.setAdapter(new AdapterProducts(getActivity(), productsList, false));
            } else {
                rv.setBackgroundColor(getResources().getColor(R.color.white));
                toFetchProductData(0);
            }

            view.findViewById(R.id.fabEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), OwnRequirementsActivity.class));
                }
            });


            tabLayout = view.findViewById(R.id.tabs);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.transparent));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    try {
                        toFetchProductData(tab.getPosition());
                        selectedTab(tab, tab.getText().toString() != null ? tab.getText().toString() : "");
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    try {
                        unselectedTab(tab, tab.getText().toString() != null ? tab.getText().toString() : "");
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            return view;
        } catch (Exception | Error e) {
            e.printStackTrace();
            return null;
        }
    }

    private void toFetchCategoryData() {
        try {
            Utility.showLoadingDialog(getActivity(), "", " Please wait ... ");
            ApiRequest.getInstance(getActivity()).addToRequestQueue(
                    RequestBuilder.toFetchCategoryList(
                            new Response.Listener<BasicModel>() {
                                @Override
                                public void onResponse(BasicModel response) {
                                    try {
                                        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.category_pager_item).setText("All"));
                                        selectedTab(tabLayout.getTabAt(0), "ALL");
                                        if (response != null) {
                                            if (response.basicModelList != null) {
                                                for (int i = 0; i < response.basicModelList.size(); i++) {
                                                    tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.category_pager_item).setText(response.basicModelList.get(i).NAME));
                                                    unselectedTab(tabLayout.getTabAt(i + 1), response.basicModelList.get(i).NAME);
                                                }
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(getActivity().getResources().getString(R.string.str_invalid_data) + "\nError : " + error.getMessage(), getActivity());
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

    private void selectedTab(TabLayout.Tab tab, String str) {
        try {
            View view = tab.getCustomView();
            TextView selectedText = view.findViewById(R.id.tvName);
            Log.d("ll ", "tabjj : " + selectedText.getText());
            selectedText.setText(str);
            selectedText.setPadding(5, 5, 5, 5);
            selectedText.setTextSize(12);
            selectedText.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            selectedText.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_cornor_blue));
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void unselectedTab(TabLayout.Tab tab, String str) {
        try {
            View view = tab.getCustomView();
            TextView selectedText = view.findViewById(R.id.tvName);
            Log.d("ll ", "tabjj : " + selectedText.getText());
            selectedText.setText(str);
            selectedText.setPadding(5, 5, 5, 5);
            selectedText.setTextSize(12);
            selectedText.setTextColor(ContextCompat.getColor(getActivity(), R.color.darkish_blue));
            selectedText.setBackground(getContext().getResources().getDrawable(R.drawable.r));
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void toFetchProductData(int categoryId) {
        try {
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            if (categoryId > 0) {
                header.put("CATEGORY_ID", categoryId + "");
            }
            Utility.showLoadingDialog(getActivity(), "", " Please wait ... ");
            ApiRequest.getInstance(getActivity()).addToRequestQueue(
                    RequestBuilder.toFetchProductList(categoryId,
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
                                                productsList = response.DATA.productsList;
                                                if (productsList != null) {
                                                    rv.setBackgroundColor(getResources().getColor(R.color.light_blue));
                                                    rv.setAdapter(new AdapterProducts(getActivity(), productsList, false));
                                                } else {
                                                    rv.setBackgroundColor(getResources().getColor(R.color.white));
                                                    rv.setAdapter(null);
                                                    Utility.toCallToast(getString(R.string.str_no_information_found), getActivity());
                                                }
                                            } else {
                                                rv.setBackgroundColor(getResources().getColor(R.color.white));
                                                rv.setAdapter(null);
                                                Utility.toCallToast(getString(R.string.str_no_information_found), getActivity());
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        rv.setBackgroundColor(getResources().getColor(R.color.white));
                                        rv.setAdapter(null);
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
                                        rv.setBackgroundColor(getResources().getColor(R.color.white));
                                        rv.setAdapter(null);
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
            rv.setBackgroundColor(getResources().getColor(R.color.white));
            rv.setAdapter(null);
            Utility.toCallToast(getActivity().getResources().getString(R.string.str_invalid_data), getActivity());
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }
}
