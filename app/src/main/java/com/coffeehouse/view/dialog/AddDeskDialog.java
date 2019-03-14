package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.Desk;

import butterknife.BindView;

public class AddDeskDialog extends BaseDialog {

    @BindView(R.id.edt_ten_ban)
    EditText edtTenBan;

    private OnClickOkListener onClickOkListener;

    public AddDeskDialog(Context context) {
        super(context, R.layout.dialog_add_desk);
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (checkForm()) {

                Desk desk = new Desk();
                desk.setId(edtTenBan.getText().toString().trim());

                dismiss();
                onClickOkListener.onClickOk(desk);
            }
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtTenBan.getText())) {
            cancel = true;
            focusView = edtTenBan;
            edtTenBan.setError("Nhập tên bàn");
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
        void onClickOk(Desk desk);
    }
}
