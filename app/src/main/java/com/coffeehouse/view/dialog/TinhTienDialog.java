package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.Bill;
import com.coffeehouse.util.Utils;

import butterknife.BindView;

/**
 * Created by Thanggun99 on 12/12/2016.
 */

public class TinhTienDialog extends BaseDialog {

    private final OnClickOk onClickOk;
    @BindView(R.id.tv_tong_tien)
    TextView tvTongTien;
    @BindView(R.id.edt_tien_khach_dua)
    SearchView edtTienKhachDua;
    @BindView(R.id.tv_tien_tra_lai)
    TextView tvTienTraLai;

    private long tongtien;

    public TinhTienDialog(Context context, Bill bill, OnClickOk onClickOk) {
        super(context, R.layout.dialog_tinh_tien);
        this.onClickOk = onClickOk;

        edtTienKhachDua.setIconifiedByDefault(false);
        edtTienKhachDua.setIconified(false);
        int icSearchID = context.getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView imageView = edtTienKhachDua.findViewById(icSearchID);
        imageView.setVisibility(View.GONE);
        edtTienKhachDua.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && newText.length() < 10) {
                    try {
                        int tienKhachDua = Integer.parseInt(newText);

                        if (tienKhachDua > tongtien) {
                            tvTienTraLai.setText(Utils.formatMoney(tienKhachDua - tongtien));
                            return true;
                        } else tvTienTraLai.setText("");

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                } else tvTienTraLai.setText("");
                return false;
            }
        });

        setContent(bill);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            dismiss();
            onClickOk.onClick();
        }
    }

    public void setContent(Bill bill) {
        this.tongtien = bill.getTotalPrice();
        edtTienKhachDua.setQuery("", false);
        tvTitle.setText("Bàn " + bill.getDeskId());
//        tvTienMon.setText(Utils.formatMoney(bill.getTotalPrice()));
//        tvSoLuong.setText("(" + bill.getOrderDetails().size() + ")");
        tvTongTien.setText(Utils.formatMoney(bill.getTotalPrice()));
    }

    public interface OnClickOk {
        void onClick();
    }

}
