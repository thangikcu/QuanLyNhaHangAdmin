package com.coffeehouse.view.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.adapter.WorkingReportAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.User;
import com.coffeehouse.model.entity.WorkingReport;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.MyPermission;
import com.coffeehouse.util.Utils;
import com.evrencoskun.tableview.TableView;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ChamCongNhanVienFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.btn_check_in)
    Button btnCheckIn;
    @BindView(R.id.btn_check_out)
    Button btnCheckOut;
    @BindView(R.id.spn_month)
    Spinner spnMonth;
    @BindView(R.id.spn_year)
    Spinner spnYear;
    @BindView(R.id.btn_view)
    Button btnView;
    @BindView(R.id.table_view)
    TableView tableView;
    @BindView(R.id.tv_salary)
    TextView tvSalary;
    @BindView(R.id.tv_so_cong)
    TextView tvSoCong;
    @BindView(R.id.tv_so_gio)
    TextView tvSoGio;
    @BindView(R.id.tv_nhan_vien)
    TextView tvNhanVien;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    private MainView mainView;
    private boolean viewFromNhanVien = true;
    private User user = AppInstance.getLoginUser();
    private OnClickBackListener onClickBackListener;
    private boolean isCheckIn;
    private String titleScanQRCode;
    private WorkingReportAdapter workingReportAdapter;
    private List<String> listMonth;
    private List<String> listYear;

    @SuppressLint("ValidFragment")
    public ChamCongNhanVienFragment(MainView mainView) {
        super(R.layout.fragment_cham_cong_nhan_vien);
        this.mainView = mainView;
    }

    @SuppressLint("ValidFragment")
    public ChamCongNhanVienFragment(MainView mainView, User user, OnClickBackListener onClickBackListener) {
        this(mainView);
        viewFromNhanVien = false;
        this.user = user;
        this.onClickBackListener = onClickBackListener;
    }

    private void getWorkingReport() {
        mainView.showLoading();
        int month = Integer.parseInt(listMonth.get(spnMonth.getSelectedItemPosition()));
        int year = Integer.parseInt(listYear.get(spnYear.getSelectedItemPosition()));

        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getWorkingTimeReport(user.getID(),
                        month + "/" + year)
                .enqueue(new Callback<ResponseData<User>>() {
                    @Override
                    public void onResponse(Call<ResponseData<User>> call, Response<ResponseData<User>> response) {
                        mainView.hideLoading();
                        if (response.body() != null && response.body().getContent() != null) {
                            User userWorking = response.body().getContent();
                            List<WorkingReport> listWorkingReport = userWorking.getListWorkingReport();
                            workingReportAdapter.setData(listWorkingReport);

                            int soCong = 0;
                            float soGio = 0;

                            if (!CollectionUtils.isEmpty(listWorkingReport)) {
                                for (int i = 0; i < listWorkingReport.size(); i++) {
                                    WorkingReport workingReport = listWorkingReport.get(i);

                                    if (workingReport.isValidate()) {
                                        ++soCong;
                                        soGio += Utils.getDifferenceTime(workingReport.getStartDate(),
                                                workingReport.getEndDate());
                                    }
                                }
                            }

                            tvSalary.setText(Utils.formatMoneyToVnd(userWorking.getSalaryPerMonth()));
                            tvSoCong.setText(soCong + " Công");
                            tvSoGio.setText(soGio + " Giờ");
                        } else {
                            workingReportAdapter.setData(null);
                            tvSoCong.setText("_");
                            tvSoGio.setText("_");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<User>> call, Throwable t) {
                        mainView.hideLoading();
                        mainView.showMessage("Error!");
                        workingReportAdapter.setData(null);
                        tvSoCong.setText("_");
                        tvSoGio.setText("_");
                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainView.showLoading();

        setCurrentMonth();
        view.postDelayed(this::getWorkingReport, 500);
    }

    @Override
    public void setEvents() {
        btnCheckIn.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);
        btnView.setOnClickListener(this);

        workingReportAdapter = new WorkingReportAdapter(getContext());
        tableView.setAdapter(workingReportAdapter);
        workingReportAdapter.setData(null);


        btnBack.setVisibility(viewFromNhanVien ? View.GONE : View.VISIBLE);
        tvNhanVien.setVisibility(viewFromNhanVien ? View.GONE : View.VISIBLE);
        btnCheckIn.setVisibility(viewFromNhanVien ? View.VISIBLE : View.GONE);
        btnCheckOut.setVisibility(viewFromNhanVien ? View.VISIBLE : View.GONE);

        tvNhanVien.setText(String.format("Nhân viên: %s", user.getFullName()));
    }

    @Override
    public void initComponents() {
        listMonth = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            listMonth.add(i + "");
        }

        ArrayAdapter monthAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                listMonth);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonth.setAdapter(monthAdapter);


        listYear = new ArrayList<>();

        int startYear = 2017;
        int endYear = 2024;
        for (int i = startYear; i <= endYear; i++) {
            listYear.add(i + "");
        }

        ArrayAdapter yearAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                listYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(yearAdapter);
    }

    private void decryptQRcode(String qrcodeData) {
        mainView.showLoading();

        ArrayMap<String, Object> requestBody = new ArrayMap<>();

        requestBody.put("employeeId", user.getID());
        requestBody.put("isCheckIn", isCheckIn);
        requestBody.put("qrCode", qrcodeData);

        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .decryptQrcode(ResfulApi.createJsonRequestBody(requestBody))
                .enqueue(new Callback<ResponseData<String>>() {
                    @Override
                    public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                        mainView.hideLoading();
                        if (response.body() != null && response.body().getContent().equals("Successful")) {
                            mainView.showMessage("Hoàn thành!");
                            setCurrentMonth();
                            getWorkingReport();
                        } else {
                            mainView.showMessage(response.body() != null ? response.body().getContent() : "Error!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                        mainView.hideLoading();
                        mainView.showMessage("Error!");
                    }
                });
    }

    private void setCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        spnMonth.setSelection(listMonth.indexOf((calendar.get(Calendar.MONTH) + 1) + ""));
        spnYear.setSelection(listYear.indexOf(calendar.get(Calendar.YEAR) + ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_view:
                getWorkingReport();
                break;
            case R.id.btn_check_in:
                startScanQRCode(true);
                break;
            case R.id.btn_check_out:
                startScanQRCode(false);
                break;
        }
    }

    private void startScanQRCode(boolean checkIn) {
        this.isCheckIn = checkIn;
        this.titleScanQRCode = checkIn ? "CheckIn" : "CheckOut";
        if (MyPermission.requirePermission(this, new String[]{Manifest.permission.CAMERA},
                MyPermission.PERMISSION_USE_CAMERA)) {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt(titleScanQRCode);
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        }
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onClickBackListener.onClickBack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                decryptQRcode(result.getContents());
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
                    startScanQRCode(isCheckIn);
                }
                break;
            default:
        }
    }

    public interface OnClickBackListener {
        void onClickBack();
    }
}
