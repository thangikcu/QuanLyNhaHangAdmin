package thanggun99.quanlynhahang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import thanggun99.quanlynhahang.R;
import thanggun99.quanlynhahang.model.entity.Mon;
import thanggun99.quanlynhahang.presenter.PhucVuPresenter;
import thanggun99.quanlynhahang.util.Utils;


/**
 * Created by Thanggun99 on 19/11/2016.
 */

public class MonAdapter extends RecyclerView.Adapter<MonAdapter.ViewHolder> {
    private ArrayList<Mon> monList;
    private PhucVuPresenter phucVuPresenter;
    private Context context;

    public MonAdapter(Context context, PhucVuPresenter phucVuPresenter) {
        this.context = context;
        this.phucVuPresenter = phucVuPresenter;
        this.monList = phucVuPresenter.getDatabase().getMonList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mon, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mon mon = monList.get(position);

        holder.tvTenMon.setText(mon.getTenMon());
        holder.tvTenMon.scrollTo(0, 0);
        holder.tvDonGia.setText(Utils.formatMoney(mon.getDonGia()) + "/" + mon.getDonViTinh());
        holder.ratingBar.setRating(mon.getRating() / mon.getPersonRating());
        holder.tvPointRating.setText(mon.getRatingPoint());
        Glide.with(context)
                .load(mon.getHinhAnh())
                .error(R.drawable.ic_food)
                .placeholder(R.drawable.ic_food)
                .into(holder.ivMon);
    }

    @Override
    public int getItemCount() {
        if (monList == null) return 0;
        return monList.size();
    }

    public void changeData(ArrayList<Mon> data) {
        monList = data;
        notifyDataSetChanged();
    }

    public Mon getItem(int position) {
        return monList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_mon)
        ImageView ivMon;
        @BindView(R.id.tv_ten_mon)
        TextView tvTenMon;
        @BindView(R.id.tv_don_gia)
        TextView tvDonGia;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tv_point_rating)
        TextView tvPointRating;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            tvTenMon.setMovementMethod(new ScrollingMovementMethod());
            tvTenMon.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            phucVuPresenter.onClickMon(getItem(getAdapterPosition()));
        }
    }
}
