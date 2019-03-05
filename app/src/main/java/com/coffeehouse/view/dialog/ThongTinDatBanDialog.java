package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.coffeehouse.R;
import com.coffeehouse.model.entity.DatBan;

/**
 * Created by Thanggun99 on 24/12/2016.
 */

public class ThongTinDatBanDialog extends BaseDialog {
    @BindView(R.id.tv_ten_khach_hang)
    TextView tvTenKhachHang;
    @BindView(R.id.tv_so_dien_thoai)
    TextView tvSoDienThoai;
    @BindView(R.id.tv_khoang_gio_den)
    TextView tvKhoangGioDen;
    @BindView(R.id.tv_yeu_cau)
    TextView tvYeuCau;

    public ThongTinDatBanDialog(Context context) {
        super(context, R.layout.dialog_thong_tin_dat_ban);

        tvYeuCau.setMovementMethod(new ScrollingMovementMethod());
    }

    public void setContent(DatBan datBan) {
        if (datBan != null) {
            tvTenKhachHang.setText(datBan.getTenKhachHang());
            tvSoDienThoai.setText(datBan.getSoDienThoai());
            tvKhoangGioDen.setText(datBan.getGioDen());
            tvYeuCau.setText(datBan.getYeuCau());
            show();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
