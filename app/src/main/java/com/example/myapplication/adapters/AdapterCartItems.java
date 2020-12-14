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
import com.example.myapplication.activities.MyCartActivity;
import com.example.myapplication.model.CartItems;

import java.util.List;

public class AdapterCartItems extends RecyclerView.Adapter<AdapterCartItems.ViewHolder> {

    private Context context;
    private List<CartItems> list;
    private boolean isTrue;

    public AdapterCartItems(Context _context, List<CartItems> _list, boolean is) {
        context = _context;
        list = _list;
        isTrue = is;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterCartItems.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tvItemName.setText(list.get(position).PRODUCT_NAME);
            holder.tvItemQty.setText(String.valueOf(list.get(position).QUANTITY));
            holder.tvItemAmount.setText(String.format("%s %s", context.getString(R.string.str_rs), list.get(position).FINAL_PRICE));
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemMinus, ivItemPlus;
        TextView tvItemName, tvItemQty, tvItemAmount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemMinus = itemView.findViewById(R.id.ivCartItemMinus);
            ivItemPlus = itemView.findViewById(R.id.ivCartItemPlus);
            tvItemName = itemView.findViewById(R.id.tvCartItemName);
            tvItemQty = itemView.findViewById(R.id.tvCartItemQuantity);
            tvItemAmount = itemView.findViewById(R.id.tvCartItemAmount);

            ivItemMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (list.get(getAdapterPosition()).QUANTITY == 1) {
                            ((MyCartActivity) context).toConfirmDeleteItemFromCart(getAdapterPosition());
                        } else {
                            ((MyCartActivity) context).toCallDecreaseItemQuantityApi(getAdapterPosition());
                        }
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
            });

            ivItemPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((MyCartActivity) context).toCallIncreaseItemQuantityApi(getAdapterPosition());
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
