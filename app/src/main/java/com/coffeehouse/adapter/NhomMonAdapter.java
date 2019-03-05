package com.coffeehouse.adapter;

import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.DrinkType;

/**
 * Created by Thanggun99 on 19/11/2016.
 */

public class NhomMonAdapter extends BaseAdapter {
    private List<DrinkType> nhomMonList;
    private OnClickDrinkType onClickDrinkType;

    public NhomMonAdapter(List<DrinkType> nhomMonList, OnClickDrinkType onClickDrinkType) {
        this.nhomMonList = nhomMonList;
        this.onClickDrinkType = onClickDrinkType;
    }

    @Override
    public int getCount() {
        if (nhomMonList == null) return 0;
        return nhomMonList.size();
    }

    @Override
    public DrinkType getItem(int position) {
        return nhomMonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhom_mon, parent, false);

            viewHolder.tvTenNhom = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTenNhom.setMovementMethod(new ScrollingMovementMethod());
        viewHolder.tvTenNhom.setOnClickListener(v -> {
            onClickDrinkType.onClick(nhomMonList.get(position));
        });
        viewHolder.tvTenNhom.setText(nhomMonList.get(position).getName());
//        viewHolder.tvTenNhom.setBackgroundColor(nhomMonList.get(position).getMauSac());
        return convertView;
    }

    class ViewHolder {
        TextView tvTenNhom;
    }

    public interface OnClickDrinkType {
        void onClick(DrinkType drinkType);
    }
}
