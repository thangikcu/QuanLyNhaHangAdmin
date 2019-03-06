package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.coffeehouse.R;
import com.coffeehouse.view.fragment.manager.BanManagerFragment;
import com.coffeehouse.view.fragment.manager.LoaiMonManagerFragment;
import com.coffeehouse.view.fragment.manager.MonManagerFragment;
import com.coffeehouse.view.fragment.manager.NhanVienManagerFragment;

@SuppressLint("ValidFragment")
public class ManagerFragment extends BaseFragment implements View.OnClickListener {
    private Button btnSelected, btnQlTinTuc, btnQlBan, btnQlMon, btnQlLoaiMon, btnQlNhanVien, btnQlKhachHang;

    private Fragment fragmentIsShow;
    private MonManagerFragment monManagerFragment;
    private BanManagerFragment banManagerFragment;
    private NhanVienManagerFragment nhanVienManagerFragment;
    private LoaiMonManagerFragment loaiMonManagerFragment;

    public ManagerFragment() {
        super(R.layout.fragment_manager);
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

            default:
                break;
        }
    }

    private void fillFrame(final Fragment fragment, Button button) {
        if (button.isSelected()) return;

        btnSelected.setSelected(false);
        button.setSelected(true);
        btnSelected = button;

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (fragmentIsShow != null && fragmentIsShow.isVisible()) {

            transaction.hide(fragmentIsShow);
            fragmentIsShow.onPause();
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.manager_frame, fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentIsShow = fragment;
    }

}
