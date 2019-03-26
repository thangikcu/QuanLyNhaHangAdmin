package com.coffeehouse.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.GoodReceiptDetail;
import com.coffeehouse.util.Utils;

import java.util.List;

import butterknife.BindView;

public class ReceiptDetailAdapter extends BaseRecyclerViewAdapter<GoodReceiptDetail, ReceiptDetailAdapter.ViewHolder> {

    public ReceiptDetailAdapter(List<GoodReceiptDetail> listItem) {
        super(listItem);
    }

    @Override
    protected ViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateView(parent, R.layout.item_receipt_detail));
    }

    @Override
    protected void onBindView(ViewHolder viewHolder, GoodReceiptDetail item) {
        viewHolder.bindData(item);
    }

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(GoodReceiptDetail receiptDetail) {
            tvName.setText(receiptDetail.getName());
            tvAmount.setText(receiptDetail.getAmount());
            tvPrice.setText(Utils.formatMoneyToVnd(receiptDetail.getPrice()));
        }
    }
}
