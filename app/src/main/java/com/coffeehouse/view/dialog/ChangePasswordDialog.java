package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import com.coffeehouse.R;
import com.coffeehouse.presenter.MainPresenter;
import com.coffeehouse.util.Utils;


/**
 * Created by Thanggun99 on 28/02/2017.
 */

public class ChangePasswordDialog extends BaseDialog implements MainPresenter.ChangepasswordView {
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_re_password)
    EditText edtRePassword;


    public ChangePasswordDialog(Context context) {
        super(context, R.layout.dialog_change_password);
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (checkForm()) {

            }
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtRePassword.getText()) || edtNewPassword.getText().length() < 6) {
            cancel = true;
            focusView = edtRePassword;
            edtRePassword.setError(Utils.getStringByRes(R.string.nhap_lai_mat_khau));
        }
        if (TextUtils.isEmpty(edtNewPassword.getText()) || edtNewPassword.getText().length() < 6) {
            cancel = true;
            focusView = edtNewPassword;
            edtNewPassword.setError(Utils.getStringByRes(R.string.nhap_mat_khau_moi));
        }
        if (TextUtils.isEmpty(edtPassword.getText()) || edtPassword.getText().length() < 6) {
            cancel = true;
            focusView = edtPassword;
            edtPassword.setError(Utils.getStringByRes(R.string.nhap_mat_khau));
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        if (!edtRePassword.getText().toString().trim().equals(edtNewPassword.getText().toString().trim())) {
            edtRePassword.setError(Utils.getStringByRes(R.string.mat_khau_khong_khop));
            return false;
        }
        return true;
    }

    @Override
    public void showOnsuccess() {
        Utils.notifiOnDialog(Utils.getStringByRes(R.string.thay_doi_mat_khau_thanh_cong));
        dismiss();
    }

    @Override
    public void showPasswordWrong() {
        edtPassword.setError(Utils.getStringByRes(R.string.mat_khau_khong_dung));
        edtPassword.requestFocus();
        edtNewPassword.setText("");
        edtRePassword.setText("");
    }

    @Override
    public void showOnFail() {
        Utils.notifiOnDialog(Utils.getStringByRes(R.string.thay_doi_mat_khau_that_bai));
        edtPassword.setText("");
        edtNewPassword.setText("");
        edtRePassword.setText("");
    }
}
