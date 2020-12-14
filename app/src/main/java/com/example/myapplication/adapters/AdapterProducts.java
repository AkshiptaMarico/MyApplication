package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Products;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHolder> {

    private Context context;
    private List<Products> list;
    private boolean isTrue;

    public AdapterProducts(Context _context, List<Products> _list, boolean is) {
        context = _context;
        list = _list;
        isTrue = is;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterProducts.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_shop_item, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tvProductName.setText(list.get(position).NAME);
            holder.tvProductQty.setText(list.get(position).QUANTITY+"");
            holder.tvProductAmount.setText(context.getString(R.string.str_rs) + " " + list.get(position).PRICE);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivMinus, ivPlus;
        TextView tvProductName, tvProductQty, tvProductAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProductImage);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            tvProductAmount = itemView.findViewById(R.id.tvProductAmount);
        }
    }
}
