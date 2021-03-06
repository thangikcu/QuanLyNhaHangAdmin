package com.coffeehouse.view.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.ChangePasswordDialog;
import com.coffeehouse.view.dialog.NotifiDialog;
import com.coffeehouse.view.fragment.BaseFragment;
import com.coffeehouse.view.fragment.ChamCongForQuanLyFragment;
import com.coffeehouse.view.fragment.ChamCongNhanVienFragment;
import com.coffeehouse.view.fragment.DepotFragment;
import com.coffeehouse.view.fragment.ManagerFragment;
import com.coffeehouse.view.fragment.PhucVuFragment;
import com.coffeehouse.view.fragment.SettingFragment;
import com.coffeehouse.view.fragment.ThongKeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainView {

    @BindView(R.id.btn_phuc_vu)
    Button btnPhucVu;
    @BindView(R.id.btn_cham_cong)
    Button btnChamCong;
    @BindView(R.id.btn_manager)
    Button btnManager;
    @BindView(R.id.btn_thong_ke)
    Button btnThongKe;
    @BindView(R.id.btn_depot)
    Button btnDepot;
    @BindView(R.id.btn_setting)
    Button btnSetting;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.layout_user_info)
    View layoutUserInfo;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    private TextView tvNumber;
    private Button btnSelected;

    private PopupMenu popupMenu;

    private PhucVuFragment phucVuFragment;
    private SettingFragment settingFragment;
    private ChamCongNhanVienFragment chamCongNhanVienFragment;
    private ChamCongForQuanLyFragment chamCongForQuanLyFragment;
    private ManagerFragment managerFragment;
    private ThongKeFragment thongKeFragment;
    private DepotFragment depotFragment;
    private Fragment fragmentIsShow;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        showLoading();
        initComponents();
        setEvents();

        getWindow().getDecorView().postDelayed(this::showPhucVu, 500);
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.clear();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
        if (outState != null) {
            outState.clear();
        }
    }

    public void initComponents() {
        btnSelected = new Button(this);

        popupMenu = new PopupMenu(this, layoutUserInfo);
        popupMenu.getMenuInflater().inflate(R.menu.account_menu, popupMenu.getMenu());
    }

    public void setEvents() {
        setupUserInstance();

        layoutUserInfo.setOnClickListener(this);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_change_pass:
                        showChangePasswordDialog();
                        return true;
                    case R.id.btn_logout:
                        AppInstance.clearLoginSection();
                        showLogin();
                        return true;
                    default:
                        return false;
                }
            }
        });

        btnPhucVu.setOnClickListener(this);
        btnManager.setOnClickListener(this);
        btnThongKe.setOnClickListener(this);
        btnDepot.setOnClickListener(this);
        btnChamCong.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    private void setupUserInstance() {
        Glide.with(this)
                .load(AppInstance.getLoginUser().getImageToShow())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(ivAvatar);

        tvName.setText(AppInstance.getLoginUser().getFullName());
        tvRule.setText(AppInstance.getLoginUser().getRule());

        if (AppInstance.getLoginUser().isAdmin()) {
            btnManager.setVisibility(View.VISIBLE);
            btnThongKe.setVisibility(View.VISIBLE);
            btnDepot.setVisibility(View.VISIBLE);
        } else {
            btnManager.setVisibility(View.GONE);
            btnThongKe.setVisibility(View.GONE);
            btnDepot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phuc_vu:
                if (phucVuFragment == null) phucVuFragment = new PhucVuFragment(this);
                fillFrame(phucVuFragment, btnPhucVu);
                break;
            case R.id.btn_manager:
                if (managerFragment == null) managerFragment = new ManagerFragment(this);
                fillFrame(managerFragment, btnManager);
                break;
            case R.id.btn_thong_ke:
                if (thongKeFragment == null) {
                    thongKeFragment = new ThongKeFragment(this);
                }
                fillFrame(thongKeFragment, btnThongKe);
                break;
            case R.id.btn_depot:
                if (depotFragment == null) {
                    depotFragment = new DepotFragment(this);
                }
                fillFrame(depotFragment, btnDepot);
                break;
            case R.id.btn_cham_cong:
                if (AppInstance.getLoginUser().isAdmin()) {
                    if (chamCongForQuanLyFragment == null) {
                        chamCongForQuanLyFragment = new ChamCongForQuanLyFragment(this);
                    }
                    fillFrame(chamCongForQuanLyFragment, btnChamCong);
                } else {
                    if (chamCongNhanVienFragment == null) {
                        chamCongNhanVienFragment = new ChamCongNhanVienFragment(this);
                    }
                    fillFrame(chamCongNhanVienFragment, btnChamCong);
                }
                break;
            case R.id.btn_setting:
                if (settingFragment == null) settingFragment = new SettingFragment();
                fillFrame(settingFragment, btnSetting);
                break;
            case R.id.layout_user_info:
                popupMenu.show();
                break;
            default:
                break;
        }
    }

    public void clearFrames() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments.size() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null) {

                    Utils.showLog("xoa " + fragment.getClass().getName());
                    transaction.remove(fragment);
                }
            }
            transaction.commit();
        }
        btnSelected.setSelected(false);
        fragmentIsShow = null;
        phucVuFragment = null;
        chamCongNhanVienFragment = null;
        managerFragment = null;
        thongKeFragment = null;
    }


    public void showChangePasswordDialog() {
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(this, this);
        changePasswordDialog.show();
    }

    public void showLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void showPhucVu() {
        if (phucVuFragment == null) {
            phucVuFragment = new PhucVuFragment(this);
        }
        fillFrame(phucVuFragment, btnPhucVu);
    }

    private void fillFrame(final BaseFragment fragment, Button button) {
        if (button.isSelected()) return;

        btnSelected.setSelected(false);
        button.setSelected(true);
        btnSelected = button;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragmentIsShow != null && fragmentIsShow.isVisible()) {
            transaction.hide(fragmentIsShow);
            fragmentIsShow.onPause();
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
            fragment.onResume();
            fragment.loadData();
        } else {
            transaction.add(R.id.frame, fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentIsShow = fragment;
    }

    @Override
    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Utils.getStringByRes(R.string.loading));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void showMessage(String message) {
        new NotifiDialog(this).notify(message);
    }

}
