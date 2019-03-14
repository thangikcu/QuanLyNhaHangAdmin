package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.Desk;

import java.util.List;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class DeskManagerAdapter extends RecyclerView.Adapter<DeskManagerAdapter.ViewHolder> {

    private List<Desk> allDeskList;
    private List<Desk> deskList;
    private OnClickDesk onClickDesk;

    public DeskManagerAdapter(List<Desk> allDeskList, OnClickDesk onClickDesk) {
        this.allDeskList = allDeskList;
        deskList = allDeskList;
        this.onClickDesk = onClickDesk;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mon_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Desk desk = deskList.get(position);

        holder.tvDeskName.setText(desk.getId());
    }

    @Override
    public int getItemCount() {
        if (deskList != null) {
            return deskList.size();
        }
        return 0;
    }

    public List<Desk> getAllDeskList() {
        return allDeskList;
    }

    public void setDatas(List<Desk> datas) {
        this.deskList = datas;
    }

    public void showAllData() {
        this.deskList = allDeskList;
        notifyDataSetChanged();
    }

    public void changeData(List<Desk> deskList) {
        this.deskList = deskList;
        notifyDataSetChanged();
    }

    public interface OnClickDesk {
        void onClickDelete(Desk desk);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDeskName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDeskName = itemView.findViewById(R.id.tv_ten_mon);

            itemView.findViewById(R.id.btn_delete).setOnClickListener(this);

            itemView.findViewById(R.id.iv_mon).setVisibility(View.GONE);
            itemView.findViewById(R.id.btn_update).setVisibility(View.GONE);
            itemView.findViewById(R.id.ratingBar).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.tv_point_rating).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.tv_don_gia).setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_delete:
                    onClickDesk.onClickDelete(deskList.get(getAdapterPosition()));
                    break;
                default:
                    break;
            }

        }
    }

}
