package com.coffeehouse.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.GoodReceipt;
import com.coffeehouse.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class ReceiptAdapter extends BaseLoadMoreRecyclerViewAdapter<GoodReceipt, ReceiptAdapter.ReceiptViewHolder> {

    public ReceiptAdapter(List<GoodReceipt> listItem, OnLoadMoreListener onLoadMore, int numberItemPerPage, OnItemClickListener onItemClickListener) {
        super(listItem, onLoadMore, numberItemPerPage, onItemClickListener);
    }

    @Override
    protected ReceiptViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new ReceiptViewHolder(inflateView(parent, R.layout.item_receipt));
    }

    @Override
    protected void onBindView(ReceiptViewHolder viewHolder, GoodReceipt item) {
        viewHolder.bindData(item);
    }

    public static class ReceiptViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_ma)
        TextView tvMa;
        @BindView(R.id.tv_thoi_gian)
        TextView tvThoiGian;
        @BindView(R.id.tv_nhan_vien)
        TextView tvNhanVien;
        @BindView(R.id.tv_tong_tien)
        TextView tvTongTien;


        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @SuppressLint("SimpleDateFormat")
        public void bindData(GoodReceipt data) {
            tvMa.setText(data.getId());
            tvThoiGian.setText(new SimpleDateFormat("dd/MM/yyy HH:mm:ss").format(data.getCreateDate()));
            tvNhanVien.setText(data.getEmployeeName());
            tvTongTien.setText(Utils.formatMoneyToVnd(data.getTotalPrice()));
        }

    }
}
