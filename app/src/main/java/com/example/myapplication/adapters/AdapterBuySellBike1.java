package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.ProductDetailsActivity;
import com.example.myapplication.activities.SellOrBuyBikeDetailsActivity;
import com.example.myapplication.model.BuySellAd;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.String.format;

public class AdapterBuySellBike1 extends RecyclerView.Adapter<AdapterBuySellBike1.ViewHolder> {

    private Context context;
    private List<BuySellAd> list;
    private boolean isTrue;

    public AdapterBuySellBike1(Context _context, List<BuySellAd> _list, boolean is) {
        context = _context;
        list = _list;
        isTrue = is;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterBuySellBike1.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sell_or_buy_bike_item, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.ivEdit.setVisibility(isTrue ? View.VISIBLE : View.GONE);
            holder.tv1.setText(format("%s %d", context.getString(R.string.str_rs), list.get(position).ASKING_PRICE));
            holder.tv2.setText(list.get(position).TITLE);
            holder.tv3.setText(format("%d - %d", list.get(position).YEAR, list.get(position).KMS_DRIVEN));
            // https://cdn.zeplin.io/5ec7b0824e89162984860e4d/assets/6F322F51-CE12-48C8-8B8C-990A0CD874A6.png
            Picasso.get()
                    .load("https://cdn.zeplin.io/5ec7b0824e89162984860e4d/assets/6F322F51-CE12-48C8-8B8C-990A0CD874A6.png")
                    .into(holder.ivProduct);
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivEdit;
        TextView tv1, tv2, tv3;
        LinearLayout ll1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll1 = itemView.findViewById(R.id.ll1);
            ivProduct = itemView.findViewById(R.id.ivProduct1);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
//            ll1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ProductDetailsActivity.class);
//                    intent.putExtra("ID", list.get(getAdapterPosition()).ID);
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
