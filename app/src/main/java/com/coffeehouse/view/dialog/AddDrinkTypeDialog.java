package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.DrinkType;

import butterknife.BindView;

public class AddDrinkTypeDialog extends BaseDialog {

    @BindView(R.id.edt_ten_loai)
    EditText edtTenLoai;
    @BindView(R.id.edt_description)
    EditText edtDescription;

    private DrinkType drinkType;
    private OnClickOkListener onClickOkListener;

    public AddDrinkTypeDialog(Context context) {
        super(context, R.layout.dialog_add_drink_type);
        setCancelable(true);
    }

    public AddDrinkTypeDialog(Context context, DrinkType drinkType) {
        this(context);
        this.drinkType = drinkType;

        edtTenLoai.setText(drinkType.getName());
        edtDescription.setText(drinkType.getDescription());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (checkForm()) {

                DrinkType drinkType = new DrinkType();

                if (this.drinkType != null) {
                    drinkType.setID(this.drinkType.getID());
                }

                drinkType.setName(edtTenLoai.getText().toString().trim());
                drinkType.setDescription(edtDescription.getText().toString().trim());

                dismiss();
                onClickOkListener.onClickOk(drinkType);
            }
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtDescription.getText())) {
            cancel = true;
            focusView = edtDescription;
            edtDescription.setError("Nhập miêu tả");
        }
        if (TextUtils.isEmpty(edtTenLoai.getText())) {
            cancel = true;
            focusView = edtTenLoai;
            edtTenLoai.setError("Nhập tên loại");
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk(DrinkType drinkType);
    }
}
