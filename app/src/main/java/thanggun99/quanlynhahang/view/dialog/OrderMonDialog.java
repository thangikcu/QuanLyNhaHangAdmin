package thanggun99.quanlynhahang.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import thanggun99.quanlynhahang.R;
import thanggun99.quanlynhahang.model.entity.Mon;
import thanggun99.quanlynhahang.presenter.PhucVuPresenter;

/**
 * Created by Thanggun99 on 22/11/2016.
 */

public class OrderMonDialog extends BaseDialog {
    @BindView(R.id.tv_ten_mon)
    TextView tvTenMon;
    @BindView(R.id.iv_mon)
    ImageView ivMon;
    @BindView(R.id.btn_tru)
    ImageButton btnTru;
    @BindView(R.id.edt_so_luong)
    EditText edtSoLuong;
    @BindView(R.id.tv_don_vi_tinh)
    TextView tvDonViTinh;
    @BindView(R.id.btn_cong)
    ImageButton btnCong;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tv_point_rating)
    TextView tvPointRating;

    private PhucVuPresenter phucVuPresenter;


    public OrderMonDialog(Context context, PhucVuPresenter phucVuPresenter) {
        super(context, R.layout.dialog_order_mon);

        this.phucVuPresenter = phucVuPresenter;


        tvDonViTinh.setMovementMethod(new ScrollingMovementMethod());

        tvTenMon.setMovementMethod(new ScrollingMovementMethod());

        btnCong.setOnClickListener(this);
        btnTru.setOnClickListener(this);
    }

    public void setContent(String tenBan, Mon mon) {
        ratingBar.setRating(mon.getRating() / mon.getPersonRating());
        tvPointRating.setText(mon.getRatingPoint());
        edtSoLuong.setText("1");
        tvTitle.setText(tenBan);
        tvTenMon.setText(mon.getTenMon());
        tvDonViTinh.setText(mon.getDonViTinh());
        Glide.with(getContext())
                .load(mon.getHinhAnh())
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_food)
                .into(ivMon);
        show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_ok:
                if (!TextUtils.isEmpty(edtSoLuong.getText())) {
                    try {
                        phucVuPresenter.orderMon(Integer.parseInt(edtSoLuong.getText().toString()));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    dismiss();
                }
                break;
            case R.id.btn_cong:
                int sl = 1;
                if (!edtSoLuong.getText().toString().isEmpty()) {
                    try {
                        sl = Integer.parseInt(edtSoLuong.getText().toString());
                        sl++;

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
                edtSoLuong.setText(sl + "");
                break;
            case R.id.btn_tru:
                int sl2 = 1;
                if (!edtSoLuong.getText().toString().isEmpty()) {
                    sl2 = Integer.parseInt(edtSoLuong.getText().toString());
                    if (sl2 > 1) sl2--;
                }
                edtSoLuong.setText(sl2 + "");
                break;
            default:
                break;
        }
    }

}
