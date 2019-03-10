package com.coffeehouse.view.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coffeehouse.R;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.MyPermission;
import com.coffeehouse.view.dialog.QRCodeDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ChamCongFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.btn_check_in)
    Button btnCheckIn;
    @BindView(R.id.btn_check_out)
    Button btnCheckOut;
    @BindView(R.id.btn_generate_qrcode)
    Button btnGenerateQrcode;
    private MainView mainView;
    private String titleScanQRCode;

    @SuppressLint("ValidFragment")
    public ChamCongFragment(MainView mainView) {
        super(R.layout.fragment_cham_cong);
        this.mainView = mainView;
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {
        btnGenerateQrcode.setOnClickListener(this);
        btnCheckIn.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generate_qrcode:
                mainView.showLoading();
                ResfulApi.getInstance().getService(TheCoffeeService.class)
                        .generateQrcode().enqueue(new Callback<ResponseData<String>>() {
                    @Override
                    public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                        mainView.hideLoading();
                        if (response.body() != null && response.body().getContent() != null) {
                            new QRCodeDialog(getContext(), response.body().getContent())
                                    .show();
                        } else {
                            mainView.showMessage("Error!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                        mainView.hideLoading();
                        mainView.showMessage(t.getMessage());
                    }
                });
                break;
            case R.id.btn_check_in:
                startScanQRCode("CheckIn");
                break;
            case R.id.btn_check_out:
                startScanQRCode("CheckOut");
                break;
        }
    }

    private void startScanQRCode(String title) {
        this.titleScanQRCode = title;
        if (MyPermission.requirePermission(this, new String[]{Manifest.permission.CAMERA},
                MyPermission.PERMISSION_USE_CAMERA)) {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt(title);
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Scan fail!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MyPermission.PERMISSION_USE_CAMERA:
                if (MyPermission.onRequestResult(getContext(), grantResults,
                        R.string.ban_can_cap_quyen_de_su_dung_camera)) {
                    startScanQRCode(titleScanQRCode);
                }
                break;
            default:
        }
    }
}
