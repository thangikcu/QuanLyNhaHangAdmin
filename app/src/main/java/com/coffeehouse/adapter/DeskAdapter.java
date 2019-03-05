package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.Desk;

import java.util.List;

/**
 * Created by Thanggun99 on 19/11/2016.
 */

public class DeskAdapter extends RecyclerView.Adapter<DeskAdapter.ViewHolder> {
    private List<Desk> banList;
    private OnDeskClick onDeskClick;
    private Desk banSelected;

    public DeskAdapter(List<Desk> deskList, OnDeskClick onDeskClick) {
        this.banList = deskList;
        this.onDeskClick = onDeskClick;
        if (banList != null && banList.size() > 0) {

            banSelected = banList.get(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ban, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Desk ban = banList.get(position);

        holder.tvBan.setText(ban.getTenBan());

        holder.tvBan.setBackgroundResource(ban.getIdResBgBan());

        if (ban.getSelected()) {
            holder.tvBan.setSelected(true);
        } else {
            holder.tvBan.setSelected(false);
        }

    }

    public void updateBan(Desk ban) {
        notifyItemChanged(banList.indexOf(ban));
    }

    @Override
    public int getItemCount() {
        if (banList == null) return 0;
        return banList.size();
    }

    public void changeSelectBan(Desk ban) {
        banSelected.setSelected(false);
        notifyItemChanged(banList.indexOf(banSelected));

        ban.setSelected(true);
        notifyItemChanged(banList.indexOf(ban));

        banSelected = ban;

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBan;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvBan = (TextView) itemView.findViewById(R.id.tv_title);
            tvBan.setMovementMethod(new ScrollingMovementMethod());
            tvBan.setOnClickListener(v -> {
                Desk ban = banList.get(getAdapterPosition());
                if (ban.getSelected()) {

                    return;
                }

                banSelected.setSelected(false);
                notifyItemChanged(banList.indexOf(banSelected));

                ban.setSelected(true);
                notifyItemChanged(getAdapterPosition());

                banSelected = ban;

                onDeskClick.onClick(ban);
            });
        }
    }

    public interface OnDeskClick {
        void onClick(Desk desk);
    }
}
