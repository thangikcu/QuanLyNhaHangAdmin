package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.coffeehouse.R;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.view.fragment.manager.DeskManagerFragment;
import com.coffeehouse.view.fragment.manager.DrinkManagerFragment;
import com.coffeehouse.view.fragment.manager.DrinkTypeManagerFragment;
import com.coffeehouse.view.fragment.manager.NhanVienManagerFragment;

import java.util.Objects;

@SuppressLint("ValidFragment")
public class ManagerFragment extends BaseFragment implements View.OnClickListener {
    private Button btnSelected, btnQlTinTuc, btnQlBan, btnQlMon, btnQlLoaiMon, btnQlNhanVien, btnQlKhachHang;

    private BaseFragment fragmentIsShow;
    private DrinkManagerFragment drinkManagerFragment;
    private DeskManagerFragment deskManagerFragment;
    private NhanVienManagerFragment nhanVienManagerFragment;
    private DrinkTypeManagerFragment drinkTypeManagerFragment;
    private MainView mainView;

    public ManagerFragment(MainView mainView) {
        super(R.layout.fragment_manager);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainView.showLoading();
        view.postDelayed(() -> btnQlMon.performClick(), 500);
    }

    @Override
    public void loadData() {
        if (fragmentIsShow != null) {
            fragmentIsShow.loadData();
        }
    }

    @Override
    public void findViews(View view) {

        btnQlBan = view.findViewById(R.id.btn_ql_ban);
        btnQlLoaiMon = view.findViewById(R.id.btn_ql_loai_mon);
        btnQlNhanVien = view.findViewById(R.id.btn_ql_nhan_vien);
        btnQlMon = view.findViewById(R.id.btn_ql_thuc_don);
        btnQlTinTuc = view.findViewById(R.id.btn_ql_tin_tuc);
        btnQlKhachHang = view.findViewById(R.id.btn_ql_khach_hang);

        btnSelected = new Button(getContext());
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {
        btnQlMon.setOnClickListener(this);
        btnQlTinTuc.setOnClickListener(this);
        btnQlNhanVien.setOnClickListener(this);
        btnQlLoaiMon.setOnClickListener(this);
        btnQlBan.setOnClickListener(this);
        btnQlKhachHang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ql_thuc_don:
                if (drinkManagerFragment == null) {
                    drinkManagerFragment = new DrinkManagerFragment(mainView);
                }
                fillFrame(drinkManagerFragment, btnQlMon);
                break;
            case R.id.btn_ql_loai_mon:
                if (drinkTypeManagerFragment == null) {
                    drinkTypeManagerFragment = new DrinkTypeManagerFragment(mainView);
                }
                fillFrame(drinkTypeManagerFragment, btnQlLoaiMon);
                break;
            case R.id.btn_ql_ban:
                if (deskManagerFragment == null) {
                    deskManagerFragment = new DeskManagerFragment(mainView);
                }
                fillFrame(deskManagerFragment, btnQlBan);
                break;
            case R.id.btn_ql_nhan_vien:
                if (nhanVienManagerFragment == null) {
                    nhanVienManagerFragment = new NhanVienManagerFragment(mainView);
                }
                fillFrame(nhanVienManagerFragment, btnQlNhanVien);
                break;
            default:
                break;
        }
    }

    private void fillFrame(final BaseFragment fragment, Button button) {
        if (button.isSelected()) return;

        btnSelected.setSelected(false);
        button.setSelected(true);
        btnSelected = button;

        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();

        if (fragmentIsShow != null && fragmentIsShow.isVisible()) {

            transaction.hide(fragmentIsShow);
            fragmentIsShow.onPause();
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
            fragment.onResume();
            fragment.loadData();
        } else {
            transaction.add(R.id.manager_frame, fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentIsShow = fragment;
    }

}
