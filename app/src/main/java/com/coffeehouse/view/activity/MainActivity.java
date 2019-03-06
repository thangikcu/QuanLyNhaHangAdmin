package com.coffeehouse.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.ChangePasswordDialog;
import com.coffeehouse.view.dialog.ErrorDialog;
import com.coffeehouse.view.dialog.NotifiDialog;
import com.coffeehouse.view.fragment.ChamCongFragment;
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
    @BindView(R.id.btn_setting)
    Button btnSetting;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.btn_drop_down)
    ImageButton btnDropDown;

    private TextView tvNumber;
    private Button btnSelected;

    private PopupMenu popupMenu;

    private PhucVuFragment phucVuFragment;
    private SettingFragment settingFragment;
    private ChamCongFragment chamCongFragment;
    private ManagerFragment managerFragment;
    private ThongKeFragment thongKeFragment;
    private Fragment fragmentIsShow;

    private ProgressDialog progressDialog;
    private ErrorDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initComponents();
        setEvents();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onDestroy();
    }

    public void initComponents() {
        btnSelected = new Button(this);

        popupMenu = new PopupMenu(this, btnDropDown);
        popupMenu.getMenuInflater().inflate(R.menu.account_menu, popupMenu.getMenu());

        errorDialog = new ErrorDialog(this);

        phucVuFragment = new PhucVuFragment(this);

        showPhucVu();
    }

    public void setEvents() {
        setAdmin();

        btnDropDown.setOnClickListener(this);
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
        btnChamCong.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    private void setAdmin() {
        tvName.setText(AppInstance.getLoginUser().getFullName());
        tvRule.setText(AppInstance.getLoginUser().getRule());

        if (AppInstance.getLoginUser().isAdmin()) {
            btnManager.setVisibility(View.VISIBLE);
            btnThongKe.setVisibility(View.VISIBLE);
            btnChamCong.setVisibility(View.GONE);
        } else {
            btnManager.setVisibility(View.GONE);
            btnThongKe.setVisibility(View.GONE);
            btnChamCong.setVisibility(View.VISIBLE);
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
                if (managerFragment == null) managerFragment = new ManagerFragment();
                fillFrame(managerFragment, btnManager);
                break;
            case R.id.btn_thong_ke:
                if (thongKeFragment == null) {

                    thongKeFragment = new ThongKeFragment();
                } else {
                    thongKeFragment.showInfo();
                }
                fillFrame(thongKeFragment, btnThongKe);
                break;
            case R.id.btn_cham_cong:
                if (chamCongFragment == null) chamCongFragment = new ChamCongFragment();
                fillFrame(chamCongFragment, btnChamCong);
                break;
            case R.id.btn_setting:
                if (settingFragment == null) settingFragment = new SettingFragment();
                fillFrame(settingFragment, btnSetting);
                break;
            case R.id.btn_drop_down:
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
        chamCongFragment = null;
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
        if (phucVuFragment == null) phucVuFragment = new PhucVuFragment(this);
        fillFrame(phucVuFragment, btnPhucVu);
    }

    private void fillFrame(final Fragment fragment, Button button) {
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
        } else {
            transaction.add(R.id.frame, fragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentIsShow = fragment;
        fragmentIsShow.onResume();

    }

    public void showGetDatasFailDialog() {
        errorDialog.show();
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
        new NotifiDialog(this).notifi(message);
    }

}
