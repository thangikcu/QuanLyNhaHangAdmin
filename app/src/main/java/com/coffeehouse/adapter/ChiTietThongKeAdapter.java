package com.coffeehouse.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.TurnOverDetail;
import com.coffeehouse.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class ChiTietThongKeAdapter extends BaseLoadMoreRecyclerViewAdapter<TurnOverDetail, ChiTietThongKeAdapter.ChiTietThongKeViewHolder> {

    public ChiTietThongKeAdapter(List<TurnOverDetail> listItem, OnLoadMoreListener onLoadMore, int numberItemPerPage) {
        super(listItem, onLoadMore, numberItemPerPage);
    }

    @Override
    protected ChiTietThongKeViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new ChiTietThongKeViewHolder(inflateView(parent, R.layout.item_chi_tiet_thong_ke));
    }

    @Override
    protected void onBindView(ChiTietThongKeViewHolder viewHolder, TurnOverDetail item) {
        viewHolder.bindData(item);
    }

    public static class ChiTietThongKeViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_ma_hoa_don)
        TextView tvMaHoaDon;
        @BindView(R.id.tv_thoi_gian)
        TextView tvThoiGian;
        @BindView(R.id.tv_nhan_vien)
        TextView tvNhanVien;
        @BindView(R.id.tv_thanh_tien)
        TextView tvThanhTien;

        public ChiTietThongKeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @SuppressLint("SimpleDateFormat")
        public void bindData(TurnOverDetail data) {
            tvMaHoaDon.setText(data.getOrderId());
            tvThoiGian.setText(new SimpleDateFormat("dd/MM/yyy HH:mm:ss").format(data.getCreateDate()));
            tvNhanVien.setText(data.getCreateName());
            tvThanhTien.setText(Utils.formatMoneyToVnd(data.getTotalPrice()));
        }

    }

}
