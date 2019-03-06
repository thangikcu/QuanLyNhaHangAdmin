package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.Drink;

import butterknife.BindView;

/**
 * Created by Thanggun99 on 22/11/2016.
 */

public class OrderDialog extends BaseDialog {
    @BindView(R.id.tv_ten_mon)
    TextView tvTenMon;
    @BindView(R.id.iv_mon)
    ImageView ivMon;
    @BindView(R.id.btn_tru)
    ImageButton btnTru;
    @BindView(R.id.edt_so_luong)
    EditText edtSoLuong;
    @BindView(R.id.btn_cong)
    ImageButton btnCong;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.tv_point_rating)
    TextView tvPointRating;
    @BindView(R.id.tv_description)
    TextView tvDescription;

    private OnClickOk onClickOk;

    public OrderDialog(Context context, String deskName, Drink drink) {
        super(context, R.layout.dialog_order_mon);

        tvTenMon.setMovementMethod(new ScrollingMovementMethod());

        btnCong.setOnClickListener(this);
        btnTru.setOnClickListener(this);

        ratingBar.setRating(4.5f);
        tvPointRating.setText("50");
        edtSoLuong.setText("1");
        tvTitle.setText(deskName);
        tvTenMon.setText(drink.getName());
        tvDescription.setText(drink.getDescription());
//        Glide.with(getContext())
//                .load(drink.getHinhAnh())
//                .placeholder(R.drawable.ic_food)
//                .error(R.drawable.ic_food)
//                .into(ivMon);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_ok:
                if (!TextUtils.isEmpty(edtSoLuong.getText())) {
                    dismiss();
                    onClickOk.OnClick(Integer.parseInt(edtSoLuong.getText().toString().trim()));
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

    public void setOnClickOk(OnClickOk onClickOk) {
        this.onClickOk = onClickOk;
    }

    public interface OnClickOk {
        void OnClick(int count);
    }

}
