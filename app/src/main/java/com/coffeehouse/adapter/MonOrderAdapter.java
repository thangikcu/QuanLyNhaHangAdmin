package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coffeehouse.R;
import com.coffeehouse.model.entity.OrderDetail;
import com.coffeehouse.util.Utils;

import java.util.List;


/**
 * Created by Thanggun99 on 19/11/2016.
 */

public class MonOrderAdapter extends RecyclerView.Adapter<MonOrderAdapter.ViewHolder> {
    private List<OrderDetail> orderDetailList;
    private OnClickDrinkOrder onClickDrinkOrder;

    public MonOrderAdapter(OnClickDrinkOrder onClickDrinkOrder) {
        this.onClickDrinkOrder = onClickDrinkOrder;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mon_order, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        OrderDetail monOrder = orderDetailList.get(position);

        holder.tvTenMon.setText(monOrder.getDrinkName());
        holder.tvSoLuong.setText(monOrder.getCount() + "");
        holder.tvThanhTien.setText(Utils.formatMoney(monOrder.getPrice() * monOrder.getCount()));
        holder.ratingBar.setRating(4.5f);
        holder.tvRatingPoint.setText(50 + "");

        Glide.with(holder.itemView.getContext())
                .load(monOrder.getDrinkImageData())
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_food)
                .into(holder.ivMon);

    }

    @Override
    public int getItemCount() {
        if (orderDetailList == null) return 0;
        return orderDetailList.size();
    }

    public void changeData(List<OrderDetail> data) {
        orderDetailList = data;
        notifyDataSetChanged();
    }

    public interface OnClickDrinkOrder {
        void onClick(OrderDetail orderDetail);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenMon;
        TextView tvThanhTien;
        TextView tvSoLuong;
        ImageView ivMon;
        ImageButton btnDelete;
        RatingBar ratingBar;
        TextView tvRatingPoint;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvRatingPoint = itemView.findViewById(R.id.tv_point_rating);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvThanhTien = itemView.findViewById(R.id.tv_don_gia);
            tvSoLuong = itemView.findViewById(R.id.tv_so_luong);
            ivMon = itemView.findViewById(R.id.iv_mon);
            btnDelete = itemView.findViewById(R.id.btn_delete_mon_order);
            btnDelete.setVisibility(View.GONE);
            tvTenMon = itemView.findViewById(R.id.tv_ten_mon);

            tvTenMon.setMovementMethod(new ScrollingMovementMethod());
            tvTenMon.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onClickDrinkOrder.onClick(orderDetailList.get(getAdapterPosition()));
        }
    }

}
