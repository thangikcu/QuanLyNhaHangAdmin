package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.DrinkType;

import java.util.List;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class DrinkTypeManagerAdapter extends RecyclerView.Adapter<DrinkTypeManagerAdapter.ViewHolder> {

    private List<DrinkType> allDrinkTypeList;
    private List<DrinkType> drinkTypeList;
    private OnClickDrinkType onClickDrinkType;

    public DrinkTypeManagerAdapter(List<DrinkType> allDrinkTypeList, OnClickDrinkType onClickDrinkType) {
        this.allDrinkTypeList = allDrinkTypeList;
        drinkTypeList = allDrinkTypeList;
        this.onClickDrinkType = onClickDrinkType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mon_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DrinkType drinkType = drinkTypeList.get(position);

        holder.tvDrinkTypeName.setText(drinkType.getName());
    }

    @Override
    public int getItemCount() {
        if (drinkTypeList != null) {
            return drinkTypeList.size();
        }
        return 0;
    }

    public List<DrinkType> getAllDrinkTypeList() {
        return allDrinkTypeList;
    }

    public void setDatas(List<DrinkType> datas) {
        this.drinkTypeList = datas;
    }

    public void showAllData() {
        this.drinkTypeList = allDrinkTypeList;
        notifyDataSetChanged();
    }

    public void changeData(List<DrinkType> drinkTypeList) {
        this.drinkTypeList = drinkTypeList;
        notifyDataSetChanged();
    }

    public interface OnClickDrinkType {
        void onClickDelete(DrinkType drinkType);

        void onClickUpdate(DrinkType drinkType);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDrinkTypeName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDrinkTypeName = itemView.findViewById(R.id.tv_ten_mon);

            itemView.findViewById(R.id.btn_update).setOnClickListener(this);
            itemView.findViewById(R.id.btn_delete).setOnClickListener(this);
            itemView.findViewById(R.id.iv_mon).setVisibility(View.GONE);
            itemView.findViewById(R.id.ratingBar).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.tv_point_rating).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.tv_don_gia).setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_delete:
                    onClickDrinkType.onClickDelete(drinkTypeList.get(getAdapterPosition()));
                    break;
                case R.id.btn_update:
                    onClickDrinkType.onClickUpdate(drinkTypeList.get(getAdapterPosition()));
                    break;
                default:
                    break;
            }

        }
    }

}
