package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coffeehouse.R;
import com.coffeehouse.model.entity.Drink;
import com.coffeehouse.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Thanggun99 on 19/11/2016.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> {
    private List<Drink> drinkList;
    private OnClickDrink onClickDrink;

    public DrinkAdapter(OnClickDrink onClickDrink) {
        this.onClickDrink = onClickDrink;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drink, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drink mon = drinkList.get(position);

        holder.tvTenMon.setText(mon.getName());
        holder.tvTenMon.scrollTo(0, 0);
        holder.tvDonGia.setText(Utils.formatMoney(mon.getPrice()));
        holder.ratingBar.setRating(4.5f);
        holder.tvPointRating.setText("50");

        Glide.with(holder.itemView.getContext())
                .load(mon.getImageToShow())
                .error(R.drawable.ic_food)
                .placeholder(R.drawable.ic_food)
                .into(holder.ivMon);
    }

    @Override
    public int getItemCount() {
        if (drinkList == null) return 0;
        return drinkList.size();
    }

    public void changeData(List<Drink> data) {
        drinkList = data;
        notifyDataSetChanged();
    }

    public Drink getItem(int position) {
        return drinkList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_mon)
        ImageView ivMon;
        @BindView(R.id.tv_ten_mon)
        TextView tvTenMon;
        @BindView(R.id.tv_don_gia)
        TextView tvDonGia;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tv_point_rating)
        TextView tvPointRating;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            tvTenMon.setMovementMethod(new ScrollingMovementMethod());
            tvTenMon.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickDrink.onClick(drinkList.get(getAdapterPosition()));
        }
    }

    public interface OnClickDrink {
        void onClick(Drink drink);
    }

}
