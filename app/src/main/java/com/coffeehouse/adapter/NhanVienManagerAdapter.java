package com.coffeehouse.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coffeehouse.R;
import com.coffeehouse.model.entity.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thanggun99 on 11/03/2017.
 */

public class NhanVienManagerAdapter extends RecyclerView.Adapter<NhanVienManagerAdapter.ViewHolder> {

    private List<User> allNhanVienList;
    private List<User> nhanVienList;
    private OnClickUpdateUser onClickUpdateUser;
    private boolean disableUpdate = false;

    public NhanVienManagerAdapter(List<User> allNhanVienList, OnClickUpdateUser onClickUpdateUser) {
        this.allNhanVienList = allNhanVienList;
        nhanVienList = allNhanVienList;
        this.onClickUpdateUser = onClickUpdateUser;
    }

    public void setDisableUpdate(boolean disableUpdate) {
        this.disableUpdate = disableUpdate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhan_vien_manager, parent, false), disableUpdate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = nhanVienList.get(position);
        holder.bindData(user, onClickUpdateUser);
    }

    public List<User> getAllNhanVienList() {
        return allNhanVienList;
    }

    @Override
    public int getItemCount() {
        if (nhanVienList != null) {
            return nhanVienList.size();
        }
        return 0;
    }

    public void setDatas(List<User> datas) {
        this.nhanVienList = datas;
    }

    public void showAllData() {
        this.nhanVienList = allNhanVienList;
        notifyDataSetChanged();
    }

    public void changeData(List<User> userList) {
        this.nhanVienList = userList;
        notifyDataSetChanged();
    }

    public interface OnClickUpdateUser {
        void onClick(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_rule)
        TextView tvRule;
        @BindView(R.id.tv_so_dien_thoai)
        TextView tvSoDienThoai;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.btn_update)
        View btnUpdate;
        private boolean disableUpdate;

        public ViewHolder(View itemView, boolean disableUpdate) {
            super(itemView);
            this.disableUpdate = disableUpdate;
            ButterKnife.bind(this, itemView);

            btnUpdate.setVisibility(disableUpdate ? View.GONE : View.VISIBLE);
        }

        public void bindData(User user, OnClickUpdateUser onClickUpdateUser) {
            Glide.with(itemView.getContext())
                    .load(user.getImageToShow())
                    .placeholder(R.drawable.ic_food)
                    .error(R.drawable.ic_food)
                    .into(ivAvatar);

            tvName.setText(user.getFullName());
            tvRule.setText(user.getRule());
            tvSoDienThoai.setText(user.getPhoneNumber());
            tvStatus.setText(user.getStatus());


            if (disableUpdate) {
                itemView.setOnClickListener(view -> onClickUpdateUser.onClick(user));
            } else {
                btnUpdate.setOnClickListener(view -> onClickUpdateUser.onClick(user));
            }
        }
    }

}
