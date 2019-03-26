package com.coffeehouse.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.adapter.ReceiptDetailAdapter;
import com.coffeehouse.model.entity.GoodReceipt;
import com.coffeehouse.util.Utils;

import java.text.SimpleDateFormat;

import butterknife.BindView;

public class ViewReceiptDialog extends BaseDialog {

    @BindView(R.id.tv_ma)
    TextView tvMa;
    @BindView(R.id.tv_ngay_nhap)
    TextView tvNgayNhap;
    @BindView(R.id.tv_nhan_vien)
    TextView tvNhanVien;
    @BindView(R.id.tv_tong_tien)
    TextView tvTongTien;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @SuppressLint("SimpleDateFormat")
    public ViewReceiptDialog(Context context, GoodReceipt receipt) {
        super(context, R.layout.dialog_receipt_detail);
        setCancelable(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        tvMa.setText(receipt.getId());
        tvNgayNhap.setText(new SimpleDateFormat("dd/MM/yyy HH:mm:ss").format(receipt.getCreateDate()));
        tvNhanVien.setText(receipt.getEmployeeName());
        tvTongTien.setText(Utils.formatMoneyToVnd(receipt.getTotalPrice()));

        recyclerView.setAdapter(new ReceiptDetailAdapter(receipt.getGoodReceiptDetail()));
    }
}
