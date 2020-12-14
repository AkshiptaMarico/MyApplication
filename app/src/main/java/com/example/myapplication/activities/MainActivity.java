package com.example.myapplication.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.SellOrBuyBikeFragment;
import com.example.myapplication.fragments.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.getMenu().findItem(R.id.mi_home).setChecked(true);
        openFragment(HomeFragment.newInstance());
        setTitleMenuItems(getResources().getString(R.string.str_title_home), false, false, false, false);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.mi_home:
                            openFragment(HomeFragment.newInstance());
                            setTitleMenuItems(getResources().getString(R.string.str_title_home), false, false, false, false);
                            return true;
                        case R.id.mi_shop:
                            openFragment(ShopFragment.newInstance());
                            setTitleMenuItems(getResources().getString(R.string.str_title_shop), true, true, true, false);
                            return true;
                        case R.id.mi_sell_or_buy:
                            openFragment(SellOrBuyBikeFragment.newInstance());
                            setTitleMenuItems(getResources().getString(R.string.str_title_sell_or_buy), true, false, false, true);
                            return true;
                        default:
                            setTitleMenuItems(getResources().getString(R.string.str_title_home), false, false, false, false);
                            return true;
                    }
                }
            };


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        myMenu = menu;

        switch (bottomNavigation.getSelectedItemId()) {
            case R.id.mi_home:
                setTitleMenuItems(getResources().getString(R.string.str_title_home), false, false, false, false);
                break;
            case R.id.mi_shop:
                setTitleMenuItems(getResources().getString(R.string.str_title_shop), true, true, true, false);
                break;
            case R.id.mi_sell_or_buy:
                setTitleMenuItems(getResources().getString(R.string.str_title_sell_or_buy), true, false, false, true);
                break;
            default:
                setTitleMenuItems(getResources().getString(R.string.str_title_home), false, false, false, false);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.mi_my_ads:
                startActivity(new Intent(MainActivity.this, MyAdsActivity.class));
                break;
            case R.id.mi_cart:
                startActivity(new Intent(MainActivity.this, MyCartActivity.class));
                break;
            case R.id.mi_filter:
//                startActivity(new Intent(MainActivity.this, CreateNewAdsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTitleMenuItems(String title, boolean isBackArrow, boolean isFilter, boolean isCart, boolean isMyAds) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>" + title + "</font>"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(isBackArrow);
            getSupportActionBar().setDisplayShowHomeEnabled(isBackArrow);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        if (myMenu != null) {
            myMenu.findItem(R.id.mi_filter).setVisible(isFilter);
            myMenu.findItem(R.id.mi_cart).setVisible(isCart);
            myMenu.findItem(R.id.mi_my_ads).setVisible(isMyAds);
        }
    }
}
