package com.coffeehouse.view.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coffeehouse.R;
import com.coffeehouse.model.entity.Drink;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.util.Utils;

import java.util.List;

import butterknife.BindView;

public class ThemMonDialog extends BaseDialog {
    @BindView(R.id.btn_chon_hinh)
    TextView btnChonHinh;
    @BindView(R.id.iv_hinh_anh)
    ImageView ivHinhAnh;
    @BindView(R.id.edt_ten_mon)
    EditText edtTenMon;
    @BindView(R.id.edt_don_gia)
    EditText edtDonGia;
    private final List<DrinkType> listDrinkType;
    @BindView(R.id.spn_nhom_mon)
    Spinner spnNhomMon;
    @BindView(R.id.ckb_hien_thi)
    CheckBox ckbHienThi;
    @BindView(R.id.edt_description)
    EditText edtDescription;

    private ArrayAdapter<String> nhomMonAdapter;
    private byte[] hinhAnhByte;
    private PickImageRequest pickImageRequest;
    private OnClickOkListener onClickOkListener;

    public ThemMonDialog(Context context, List<DrinkType> listDrinkType) {
        super(context, R.layout.dialog_them_mon);
        setCancelable(true);

        ckbHienThi.setVisibility(View.GONE);

        this.listDrinkType = listDrinkType;

        nhomMonAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listDrinkType);

        nhomMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNhomMon.setAdapter(nhomMonAdapter);

        btnChonHinh.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (checkForm()) {
                int hienThi = 0;
                if (ckbHienThi.isChecked()) {
                    hienThi = 1;
                }

                Drink drink = new Drink();
                drink.setImage(Utils.getStringImage(hinhAnhByte));
                drink.setName(edtTenMon.getText().toString().trim());
                drink.setDescription(edtDescription.getText().toString().trim());
                drink.setPrice(Integer.parseInt(edtDonGia.getText().toString().trim()));
                drink.setBeverageId(listDrinkType.get(spnNhomMon.getSelectedItemPosition()).getID());

                dismiss();
                onClickOkListener.onClickOk(drink);
            }
        } else if (v.getId() == R.id.btn_chon_hinh) {
            pickImageRequest.pickImage(this::showImage);
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtDonGia.getText())) {
            cancel = true;
            focusView = edtDonGia;
            edtDonGia.setError(Utils.getStringByRes(R.string.nhap_so_luong));
        }
        if (TextUtils.isEmpty(edtDescription.getText())) {
            cancel = true;
            focusView = edtDescription;
            edtDescription.setError(Utils.getStringByRes(R.string.nhap_don_vi_tinh));
        }
        if (TextUtils.isEmpty(edtTenMon.getText())) {
            cancel = true;
            focusView = edtTenMon;
            edtTenMon.setError(Utils.getStringByRes(R.string.nhap_ten_mon));
        }
        if (hinhAnhByte == null) {
            cancel = true;
            focusView = btnChonHinh;
            btnChonHinh.setError(Utils.getStringByRes(R.string.chon_hinh));
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    private void showImage(Bitmap bitmap) {
        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        if (bitmap != null) {
            hinhAnhByte = Utils.getByteImage(bitmap);

            Glide.with(getContext())
                    .load(hinhAnhByte)
                    .into(ivHinhAnh);
        }

    }

    public void clear() {
        ivHinhAnh.setImageBitmap(null);
        hinhAnhByte = null;
        edtDescription.setText(null);
        edtTenMon.setText(null);
        edtDonGia.setText(null);

        edtDonGia.setError(null);
        edtTenMon.setError(null);
        btnChonHinh.setError(null);
        edtDescription.setError(null);

    }

    public void fillContent(Drink mon) {
        //hinhAnhByte = mon.getHinhAnh();
        Glide.with(getContext())
                .load(hinhAnhByte)
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_food)
                .into(ivHinhAnh);
        edtTenMon.setText(mon.getName());
        edtDescription.setText(mon.getDescription());
        edtDonGia.setText(mon.getPrice() + "");

        for (DrinkType nhomMon : listDrinkType) {
            if (nhomMon.getID() == mon.getBeverageId()) {
                spnNhomMon.setSelection(listDrinkType.indexOf(nhomMon));
            }
        }
//
//        if (mon.getHienThi() == 0) {
//            ckbHienThi.setChecked(false);
//        } else {
//            ckbHienThi.setChecked(true);
//        }
    }

    public void setPickImageRequest(PickImageRequest pickImageRequest) {
        this.pickImageRequest = pickImageRequest;
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk(Drink drink);
    }

    public interface PickImageRequest {
        void pickImage(OnPickImageResult onResult);
    }

    public interface OnPickImageResult {
        void onResult(Bitmap result);
    }
}
