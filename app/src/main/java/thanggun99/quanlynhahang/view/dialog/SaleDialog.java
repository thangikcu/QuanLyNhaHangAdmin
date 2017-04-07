package thanggun99.quanlynhahang.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import thanggun99.quanlynhahang.R;
import thanggun99.quanlynhahang.model.entity.HoaDon;
import thanggun99.quanlynhahang.presenter.PhucVuPresenter;

/**
 * Created by Thanggun99 on 10/12/2016.
 */

public class SaleDialog extends BaseDialog {
    @BindView(R.id.edt_sale)
    EditText edtSale;
    private int giamGia;
    private PhucVuPresenter phucVuPresenter;


    public SaleDialog(Context context, PhucVuPresenter phucVuPresenter) {
        super(context, R.layout.dialog_sale);

        this.phucVuPresenter = phucVuPresenter;

    }

    public void setContent(HoaDon hoaDon) {
        if (hoaDon.getMonOrderList().size() > 0) {
            tvTitle.setText(hoaDon.getBan().getTenBan());
            if (hoaDon.getGiamGia() > 0) {
                edtSale.setText(hoaDon.getGiamGia() + "");
                giamGia = hoaDon.getGiamGia();
            } else {
                giamGia = 0;
                edtSale.setText(null);
            }
            show();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (!TextUtils.isEmpty(edtSale.getText())
                    && Integer.parseInt(edtSale.getText().toString()) <= 100
                    && Integer.parseInt(edtSale.getText().toString()) != giamGia) {

                phucVuPresenter.saleHoaDon(Integer.parseInt(edtSale.getText().toString()));
                dismiss();
            }
        }
    }
}
