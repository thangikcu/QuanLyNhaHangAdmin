package com.coffeehouse.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.TurnOverDetail;
import com.coffeehouse.util.Utils;

import java.text.SimpleDateFormat;

import butterknife.BindView;

public class ChiTietThongKeViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder {
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
