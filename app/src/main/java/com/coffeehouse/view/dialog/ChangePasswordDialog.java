package com.coffeehouse.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.User;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.CoffeeStorage;
import com.coffeehouse.util.Utils;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Thanggun99 on 28/02/2017.
 */

public class ChangePasswordDialog extends BaseDialog {
    private MainView mainView;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_re_password)
    EditText edtRePassword;


    public ChangePasswordDialog(Context context, MainView mainView) {
        super(context, R.layout.dialog_change_password);
        this.mainView = mainView;
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (checkForm()) {
                User loginUser = AppInstance.getLoginUser();

                if (!loginUser.getPassword().equals(edtPassword.getText().toString().trim())) {
                    edtPassword.setError(Utils.getStringByRes(R.string.mat_khau_khong_dung));
                    edtPassword.requestFocus();
                    return;
                }

                mainView.showLoading();


                ArrayMap<String, Object> requestBody = new ArrayMap<>();

                requestBody.put("employeeId", loginUser.getID());
                requestBody.put("password", edtNewPassword.getText().toString().trim());
                requestBody.put("confirmPassword", edtRePassword.getText().toString().trim());


                ResfulApi.getInstance().getService(TheCoffeeService.class).changePassword(ResfulApi.createJsonRequestBody(requestBody)).enqueue(new Callback<ResponseData<String>>() {
                    @Override
                    public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                        mainView.hideLoading();
                        assert response.body() != null;
                        if (response.body().getContent().equals("Successful")) {
                            dismiss();
                            CoffeeStorage.getInstance().saveLoginUser(null);
                            loginUser.setPassword(edtNewPassword.getText().toString().trim());
                            mainView.showMessage(Utils.getStringByRes(R.string.thay_doi_mat_khau_thanh_cong));
                        } else {
                            mainView.showMessage(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                        mainView.hideLoading();
                        mainView.showMessage(t.getMessage());
                    }
                });
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

}
