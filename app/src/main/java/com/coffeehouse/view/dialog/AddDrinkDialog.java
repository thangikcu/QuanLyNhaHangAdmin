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

public class AddDrinkDialog extends BaseDialog {
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

    private Drink drink;
    private ArrayAdapter<String> nhomMonAdapter;
    private byte[] hinhAnhByte;
    private PickImageRequest pickImageRequest;
    private OnClickOkListener onClickOkListener;

    public AddDrinkDialog(Context context, List<DrinkType> listDrinkType) {
        super(context, R.layout.dialog_add_drink);
        setCancelable(true);

        ckbHienThi.setVisibility(View.GONE);

        this.listDrinkType = listDrinkType;

        nhomMonAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listDrinkType);

        nhomMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNhomMon.setAdapter(nhomMonAdapter);

        btnChonHinh.setOnClickListener(this);

    }

    public AddDrinkDialog(Context context, Drink drink, List<DrinkType> listDrinkType) {
        this(context, listDrinkType);
        this.drink = drink;

        spnNhomMon.setEnabled(false);

        hinhAnhByte = drink.getImageToShow();

        Glide.with(getContext())
                .load(drink.getImageToShow())
                .placeholder(R.drawable.ic_food)
                .error(R.drawable.ic_food)
                .into(ivHinhAnh);

        edtTenMon.setText(drink.getName());
        edtDescription.setText(drink.getDescription());
        edtDonGia.setText(drink.getPrice() + "");

        for (DrinkType nhomMon : listDrinkType) {
            if (nhomMon.getID() == drink.getBeverageId()) {
                spnNhomMon.setSelection(listDrinkType.indexOf(nhomMon));
            }
        }
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

                if (this.drink != null) {
                    drink.setID(this.drink.getID());
                }

                drink.setDrinkImage(Utils.getStringImage(hinhAnhByte));
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
            edtDonGia.setError("Nhập đơn giá");
        }
        if (TextUtils.isEmpty(edtDescription.getText())) {
            cancel = true;
            focusView = edtDescription;
            edtDescription.setError("Nhập miêu tả");
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
