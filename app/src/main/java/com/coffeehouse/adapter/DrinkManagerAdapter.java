package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.Drink;
import com.coffeehouse.util.Utils;

import java.util.List;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class DrinkManagerAdapter extends RecyclerView.Adapter<DrinkManagerAdapter.ViewHolder> {

    private List<Drink> allDrinkList;
    private List<Drink> drinkList;
    private OnClickDrink onClickDrink;
    private int currentPosition;

    public DrinkManagerAdapter(List<Drink> allDrinkList, OnClickDrink onClickDrink) {
        this.allDrinkList = allDrinkList;
        drinkList = allDrinkList;
        this.onClickDrink = onClickDrink;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mon_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drink mon = drinkList.get(position);

        holder.tvTenMon.setText(mon.getName());
        holder.tvDonGia.setText(Utils.formatMoney(mon.getPrice()));
        holder.tvRatingPoint.setText(50 + "");
        holder.ratingBar.setRating(4.5f);
/*
        Glide.with(context)
                .load(mon.getHinhAnh())
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_food)
                .into(holder.ivHinhAnh);*/

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getItemCount() {
        if (drinkList != null) {
            return drinkList.size();
        }
        return 0;
    }

    public void notifyItemRemoved(Drink mon) {
        if (mon != null) {

            notifyItemRemoved(drinkList.indexOf(mon));
        } else {

            notifyItemRemoved(currentPosition);
        }
    }

    public void notifyItemChanged(Drink mon) {
        if (mon != null) {
            notifyItemChanged(drinkList.indexOf(mon));
        } else {

            notifyItemChanged(currentPosition);
        }
    }


    public void setDatas(List<Drink> datas) {
        this.drinkList = datas;
    }

    public void showAllData() {
        this.drinkList = allDrinkList;
        notifyDataSetChanged();
    }

    public void changeData(List<Drink> monList) {
        this.drinkList = monList;
        notifyDataSetChanged();
    }

    public void removeMon() {
        notifyItemRemoved(currentPosition);
    }

    public interface OnClickDrink {
        void onClickDelete(Drink drink);

        void onClickUpdate(Drink drink);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDonGia;
        TextView tvTenMon;
        ImageView ivHinhAnh;
        TextView tvRatingPoint;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingPoint = itemView.findViewById(R.id.tv_point_rating);

            ivHinhAnh = itemView.findViewById(R.id.iv_mon);

            tvDonGia = itemView.findViewById(R.id.tv_don_gia);
            tvTenMon = itemView.findViewById(R.id.tv_ten_mon);

            itemView.findViewById(R.id.btn_update).setOnClickListener(this);
//            itemView.findViewById(R.id.btn_delete).setOnClickListener(this);
            itemView.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.btn_delete:
                    onClickDrink.onClickDelete(drinkList.get(getAdapterPosition()));
                    break;
                case R.id.btn_update:
                    onClickDrink.onClickUpdate(drinkList.get(getAdapterPosition()));
                    break;
                default:
                    break;
            }

        }
    }

}
