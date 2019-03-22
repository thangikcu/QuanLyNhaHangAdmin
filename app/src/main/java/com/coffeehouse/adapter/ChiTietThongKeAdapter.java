package com.coffeehouse.adapter;

import android.view.ViewGroup;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.TurnOverDetail;

import java.util.List;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class ChiTietThongKeAdapter extends BaseLoadMoreRecyclerViewAdapter<TurnOverDetail, ChiTietThongKeViewHolder> {

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
}
