package thanggun99.quanlynhahang.view.fragment;

import android.annotation.SuppressLint;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import thanggun99.quanlynhahang.App;
import thanggun99.quanlynhahang.R;
import thanggun99.quanlynhahang.adapter.DatBanChuaSetBanAdapter;
import thanggun99.quanlynhahang.model.entity.Ban;
import thanggun99.quanlynhahang.model.entity.DatBan;
import thanggun99.quanlynhahang.presenter.PhucVuPresenter;
import thanggun99.quanlynhahang.util.TimePicker;
import thanggun99.quanlynhahang.util.Utils;
import thanggun99.quanlynhahang.view.dialog.ConfirmDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@SuppressLint("ValidFragment")
public class DatBanFragment extends BaseFragment implements View.OnClickListener, PhucVuPresenter.DatBanView, TimePicker.OnFinishPickTimeListener {

    @BindView(R.id.tv_ten_khach_hang)
    TextView tvTenKhachHang;
    @BindView(R.id.tv_so_dien_thoai)
    TextView tvSoDienThoai;
    @BindView(R.id.tv_khoang_gio_den)
    TextView tvKhoangGioDen;
    @BindView(R.id.tv_yeu_cau)
    TextView tvYeuCau;
    @BindView(R.id.layout_thong_tin_dat_ban)
    LinearLayout layoutThongTinDatBan;
    @BindView(R.id.spn_ban)
    Spinner spnBan;
    @BindView(R.id.layout_thong_tin_dat_ban_chua_set_ban)
    LinearLayout layoutThongTinDatBanChuaSetBan;
    @BindView(R.id.edt_ten_khach_hang)
    EditText edtTenKhachHang;
    @BindView(R.id.edt_so_dien_thoai)
    EditText edtSoDienThoai;
    @BindView(R.id.edt_gio_den)
    EditText edtGioDen;
    @BindView(R.id.edt_yeu_cau)
    EditText edtYeuCau;
    @BindView(R.id.layout_dat_ban)
    LinearLayout layoutDatBan;
    @BindView(R.id.btn_them_dat_ban)
    Button btnThemDatBan;
    @BindView(R.id.btn_vao_ban)
    Button btnVaoBan;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.edt_tim_kiem_dat_ban)
    SearchView edtTimKiemDatBan;
    @BindView(R.id.list_dat_ban)
    RecyclerView listDatBan;

    private PhucVuPresenter phucVuPresenter;
    private DatBanChuaSetBanAdapter datBanChuaSetBanAdapter;

    private ArrayList<Ban> banList;
    private ArrayAdapter<String> banAdapter;

    private TimePicker timePicker;
    private ConfirmDialog confirmDialog;

    private Animation animationZoom;

    public DatBanFragment(PhucVuPresenter phucVuPresenter) {
        super(R.layout.fragment_dat_ban);
        this.phucVuPresenter = phucVuPresenter;
        phucVuPresenter.setDatBanView(this);
    }

    @Override
    public void findViews(View view) {

    }

    @SuppressLint("NewApi")
    @Override
    public void setEvents() {
        showLayoutDatBan();

        listDatBan.setAdapter(datBanChuaSetBanAdapter);
        listDatBan.setLayoutManager(new LinearLayoutManager(getContext()));

        banAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBan.setAdapter(banAdapter);
        spnBan.setDropDownWidth(250);

        btnThemDatBan.setOnClickListener(this);
        btnVaoBan.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        tvYeuCau.setMovementMethod(new ScrollingMovementMethod());

        edtTimKiemDatBan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                edtTimKiemDatBan.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyWord) {
                phucVuPresenter.findDatBan(keyWord);
                return true;
            }
        });

        edtTimKiemDatBan.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    edtTimKiemDatBan.onActionViewCollapsed();
                    datBanChuaSetBanAdapter.showAllData();
                }
            }
        });
        edtGioDen.setOnClickListener(this);
        edtGioDen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if (!timePicker.isAdded()) {

                        timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
                    }
            }
        });
    }

    @Override
    public void initComponents() {
        timePicker = new TimePicker();
        timePicker.setOnFinishPickTimeListener(this);
        datBanChuaSetBanAdapter = new DatBanChuaSetBanAdapter(phucVuPresenter);

        banList = new ArrayList<>();

        banAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, banList);

        confirmDialog = new ConfirmDialog(getContext());

        //initAnimation
        animationZoom = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);

    }

    @Override
    public void showThongTinDatBanChuaSetBan(DatBan datBan, ArrayList<Ban> banList) {
        this.banList.clear();
        for (Ban ban : banList) {
            if (ban.getTrangThai() == 0) {
                this.banList.add(ban);
            }
        }
        banAdapter.notifyDataSetChanged();

        tvTenKhachHang.setText(datBan.getTenKhachHang());
        tvSoDienThoai.setText(datBan.getSoDienThoai());
        tvKhoangGioDen.setText(datBan.getGioDen());
        tvYeuCau.setText(datBan.getYeuCau());
        tvYeuCau.scrollTo(0, 0);

        showLayoutThongTinDatBan();
    }

    private void showLayoutThongTinDatBan() {
        btnVaoBan.setVisibility(VISIBLE);
        btnThemDatBan.setVisibility(GONE);

        btnCancel.setVisibility(VISIBLE);

        layoutDatBan.clearAnimation();

        layoutDatBan.setVisibility(GONE);

        layoutThongTinDatBanChuaSetBan.setVisibility(VISIBLE);

        layoutThongTinDatBanChuaSetBan.startAnimation(animationZoom);
    }

    private void showLayoutDatBan() {
        btnVaoBan.setVisibility(GONE);
        btnThemDatBan.setVisibility(VISIBLE);

        btnCancel.setVisibility(GONE);

        layoutThongTinDatBanChuaSetBan.clearAnimation();

        layoutThongTinDatBanChuaSetBan.setVisibility(GONE);

        layoutDatBan.setVisibility(VISIBLE);

        layoutDatBan.startAnimation(animationZoom);
    }

    @Override
    public void fillFormUpdateDatBanChuaSetBan(DatBan datBan) {
        showLayoutDatBan();

        btnCancel.setVisibility(VISIBLE);

        btnThemDatBan.setText(Utils.getStringByRes(R.string.cap_nhat));
        edtTenKhachHang.setText(datBan.getTenKhachHang());
        edtSoDienThoai.setText(datBan.getSoDienThoai());
        edtGioDen.setText(datBan.getGioDen());
        edtYeuCau.setText(datBan.getYeuCau());
    }

    @Override
    public void notifyAddListDatBanChuaSetBan() {
        listDatBan.scrollToPosition(0);
        datBanChuaSetBanAdapter.notifyItemInserted(0);
    }

    @Override
    public void notifyRemoveListDatBanChuaSetBan(DatBan datBan) {
        datBanChuaSetBanAdapter.notifyItemRemoved(datBan);
    }

    @Override
    public void notifyUpdateListDatBanChuaSetBan(DatBan datBan) {
        listDatBan.scrollToPosition(datBanChuaSetBanAdapter.getPositonOf(datBan));
        datBanChuaSetBanAdapter.notifyItemChanged(datBan);
    }

    @Override
    public void notifyChangeListDatBanChuaTinhTien(ArrayList<DatBan> datBanList) {
        datBanChuaSetBanAdapter.changeData(datBanList);
    }

    @Override
    public void showConfirmDialog() {
        confirmDialog.setContent(Utils.getStringByRes(R.string.xac_nhan),
                Utils.getStringByRes(R.string.ban_co_muon_huy_dat_ban));

        confirmDialog.setOnClickOkListener(new ConfirmDialog.OnClickOkListener() {
            @Override
            public void onClickOk() {
                phucVuPresenter.huyDatBanChuaSetBan();
                confirmDialog.dismiss();
            }
        });
    }

    private boolean chekForm() {
        boolean cancle = false;
        View focusView = null;

        if (TextUtils.isEmpty(edtGioDen.getText().toString()) || !edtGioDen.getText().toString().contains("-")) {
            edtGioDen.setError(getString(R.string.nhap_gio_den));
            focusView = edtGioDen;
            cancle = true;
        }
        if (TextUtils.isEmpty(edtSoDienThoai.getText().toString()) || edtSoDienThoai.length() < 9) {
            edtSoDienThoai.setError(getString(R.string.nhap_so_dien_thoai));
            focusView = edtSoDienThoai;
            cancle = true;
        }
        if (TextUtils.isEmpty(edtTenKhachHang.getText().toString())) {
            edtTenKhachHang.setError(getString(R.string.nhap_ten_khach_hang));
            focusView = edtTenKhachHang;
            cancle = true;
        }

        if (cancle) {
            focusView.requestFocus();
            return false;
        } else {

            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_them_dat_ban:
                if (chekForm()) {
                    DatBan datBan = new DatBan();
                    datBan.setTenKhachHang(edtTenKhachHang.getText().toString().trim());
                    datBan.setSoDienThoai(edtSoDienThoai.getText().toString().trim());
                    datBan.setGioDen(edtGioDen.getText().toString().trim());
                    datBan.setYeuCau(edtYeuCau.getText().toString().trim());
                    if (btnThemDatBan.getText().equals(Utils.getStringByRes(R.string.them_moi))) {

                        phucVuPresenter.onClickDatBanChuaSetBan(datBan);
                    } else {
                        phucVuPresenter.updateDatBanChuaSetBan(datBan);
                    }
                }
                break;
            case R.id.btn_vao_ban:
                phucVuPresenter.khachDatBanVaoBan(banList.get(spnBan.getSelectedItemPosition()));
                break;
            case R.id.btn_cancel:
                clearFormDatBan();
                break;
            case R.id.edt_gio_den:
                if (!timePicker.isAdded()) {

                    timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void clearFormDatBan() {
        showLayoutDatBan();

        btnThemDatBan.setText(Utils.getStringByRes(R.string.them_moi));

        edtTenKhachHang.clearFocus();
        edtSoDienThoai.clearFocus();
        edtGioDen.clearFocus();
        edtYeuCau.clearFocus();

        edtTenKhachHang.setError(null);
        edtSoDienThoai.setError(null);
        edtGioDen.setError(null);
        edtYeuCau.setError(null);

        edtTenKhachHang.setText(null);
        edtSoDienThoai.setText(null);
        edtGioDen.setText(null);
        edtYeuCau.setText(null);
    }

    @Override
    public void onFinishPickTime(String date) {
        edtGioDen.setText(date);
        edtGioDen.setError(null);
    }

    @Override
    public void showSnackbar(String message) {
        if (!TextUtils.isEmpty(message)) {

            final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View viewSnackbar = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewSnackbar.getLayoutParams();
            params.width = App.getContext().getResources().getDisplayMetrics().widthPixels / 2;
            viewSnackbar.setLayoutParams(params);
            viewSnackbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }
}
