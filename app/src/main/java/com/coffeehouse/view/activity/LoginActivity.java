package com.coffeehouse.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.coffeehouse.AppInstance;
import com.coffeehouse.BuildConfig;
import com.coffeehouse.R;
import com.coffeehouse.model.LoginTask;
import com.coffeehouse.model.entity.Admin;
import com.coffeehouse.model.entity.User;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.NotifiDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.ckb_ghi_nho)
    CheckBox ckbGhiNho;
    @BindView(R.id.tv_error_login)
    TextView tvErrorLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private ProgressDialog progressDialog;
    private NotifiDialog notifiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        notifiDialog = new NotifiDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(Utils.getStringByRes(R.string.loading));

        btnLogin.setOnClickListener(this);

        if (BuildConfig.DEBUG) {
            edtUsername.setText("tran20190304");
            edtPassword.setText("chienthan123");
        }
    }

    public void onStartTask() {
        progressDialog.show();
    }

    public void onFinishTask() {
        progressDialog.dismiss();
    }

    private boolean checkConnect() {
        if (Utils.isConnectingToInternet()) {
            return true;
        } else {
            notifiDialog.notify(Utils.getStringByRes(R.string.kiem_tra_ket_noi_mang));
            return false;
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtPassword.getText()) || edtPassword.getText().length() < 6) {
            edtPassword.setError(Utils.getStringByRes(R.string.nhap_mat_khau));
            focusView = edtPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError(Utils.getStringByRes(R.string.nhap_ten_dang_nhap));
            focusView = edtUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            if (checkForm()) {
                Admin admin = new Admin();
                admin.setTenDangNhap(edtUsername.getText().toString().trim());
                admin.setMatKhau(edtPassword.getText().toString().trim());
                admin.setKieuDangNhap(LoginTask.NOT_AUTO);
                admin.setGhiNho(ckbGhiNho.isChecked());

                if (checkConnect()) {

                    onStartTask();

                    ArrayMap<String, Object> request = new ArrayMap<>();
                    request.put("userName", admin.getTenDangNhap());
                    request.put("password", admin.getMatKhau());


                    ResfulApi.getInstance().getService(TheCoffeeService.class)
                            .login(ResfulApi.createJsonRequestBody(request))
                            .enqueue(new Callback<ResponseData<User>>() {
                                @Override
                                public void onResponse(Call<ResponseData<User>> call, Response<ResponseData<User>> response) {

                                    User loginUser = response.body() != null ? response.body().getContent() : null;

                                    if (loginUser == null) {
                                        onFinishTask();
                                        new NotifiDialog(LoginActivity.this)
                                                .notify("Incorrect username or password!");
                                    } else {
                                        loginUser.setPassword(admin.getMatKhau());
                                        AppInstance.saveLoginUser(loginUser, admin.isGhiNho());
                                        LoginActivity.this.finish();
                                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        onFinishTask();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseData<User>> call, Throwable t) {
                                    onFinishTask();

                                    new NotifiDialog(LoginActivity.this).notify(t.getMessage());
                                }
                            });
                }
            }
        }
    }
}

