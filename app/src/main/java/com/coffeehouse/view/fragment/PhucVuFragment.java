package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.adapter.DeskAdapter;
import com.coffeehouse.adapter.DrinkAdapter;
import com.coffeehouse.adapter.MonOrderAdapter;
import com.coffeehouse.adapter.NhomMonAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.Ban;
import com.coffeehouse.model.entity.Bill;
import com.coffeehouse.model.entity.DatBan;
import com.coffeehouse.model.entity.Desk;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.model.entity.HoaDon;
import com.coffeehouse.model.entity.Mon;
import com.coffeehouse.model.entity.MonOrder;
import com.coffeehouse.model.entity.NhomMon;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.ConfirmDialog;
import com.coffeehouse.view.dialog.OrderMonDialog;
import com.coffeehouse.view.dialog.SaleDialog;
import com.coffeehouse.view.dialog.ThongTinDatBanDialog;
import com.coffeehouse.view.dialog.TinhTienDialog;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.coffeehouse.R.string.error;

@SuppressLint("ValidFragment")
public class PhucVuFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.list_ban)
    RecyclerView listBan;
    @BindView(R.id.btn_thuc_don)
    ImageButton btnThucDon;
    @BindView(R.id.tv_trang_thai)
    TextView tvTrangThai;
    @BindView(R.id.tv_ten_loai)
    TextView tvTenLoai;
    @BindView(R.id.list_mon_order)
    RecyclerView listMonOrder;
    @BindView(R.id.tv_tong_tien)
    TextView tvTongTien;
    @BindView(R.id.btn_sale)
    Button btnSale;
    @BindView(R.id.container_money_and_sale)
    TableRow containerMoneyAndSale;
    @BindView(R.id.btn_tinh_tien)
    Button btnTinhTien;
    @BindView(R.id.tv_ten_ban)
    TextView tvTenBan;
    @BindView(R.id.edt_tim_kiem_mon)
    SearchView edtTimKiemMon;
    @BindView(R.id.tbr)
    TableRow tbr;
    @BindView(R.id.list_mon)
    RecyclerView listMon;
    @BindView(R.id.list_nhom_mon)
    ListView listNhomMon;
    @BindView(R.id.layout_thuc_don)
    LinearLayout layoutThucDon;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private PopupMenu popupMenu;

    //adapter
    private DeskAdapter deskAdapter;
    private MonOrderAdapter monOrderAdapter;
    private DrinkAdapter drinkAdapter;
    private NhomMonAdapter nhomMonAdapter;

    //dialog
    private ConfirmDialog confirmDialog;
    private ThongTinDatBanDialog thongTinDatBanDialog;
    private OrderMonDialog orderMonDialog;
    private TinhTienDialog tinhTienDialog;
    private SaleDialog saleDialog;

    //animationAlpha
    private Animation animationAlpha;
    private Animation animationZoom;
    private Animation animationBounce;

    private Handler handler;
    private Runnable runnable;
    private MainView mainView;


    public PhucVuFragment(MainView mainView) {
        super(R.layout.fragment_phuc_vu);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListDesk();
        getMenu();
    }

    private void getMenu() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListDrink().enqueue(new Callback<ResponseData<List<DrinkType>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<DrinkType>>> call, Response<ResponseData<List<DrinkType>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    listNhomMon.setAdapter(nhomMonAdapter = new NhomMonAdapter(response.body().getContent(), drinkType -> {
                        showListDrink(drinkType);
                    }));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<DrinkType>>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }

    private void showListDrink(DrinkType drinkType) {
        if (drinkAdapter == null) {
            listMon.setAdapter(drinkAdapter = new DrinkAdapter(PhucVuFragment.this.getContext(),
                    drinkType.getListDrinks(), drink -> {

                Toast.makeText(getContext(), drink.getName(), Toast.LENGTH_SHORT).show();
            }));
        } else {
            drinkAdapter.changeData(drinkType.getListDrinks());
        }
    }


    private void getListDesk() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListDesk().enqueue(new Callback<ResponseData<List<Desk>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Desk>>> call, Response<ResponseData<List<Desk>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    listBan.setAdapter(deskAdapter = new DeskAdapter(response.body().getContent(), desk -> {
                        getDeskInfo(desk);
                    }));
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Desk>>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }

    private void getDeskInfo(Desk desk) {

        if (desk.isServing()) {
            mainView.showLoading();

            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .getOrderDetail(desk.getId()).enqueue(new Callback<ResponseData<Bill>>() {
                @Override
                public void onResponse(Call<ResponseData<Bill>> call, Response<ResponseData<Bill>> response) {
                    mainView.hideLoading();
                    // TODO: 05/03/2019 show ban phuc vu
                }

                @Override
                public void onFailure(Call<ResponseData<Bill>> call, Throwable t) {
                    mainView.hideLoading();
                    mainView.showMessage(t.getMessage());
                }
            });
        } else {
            // TODO: 05/03/2019 hien thi ban trong
        }
    }

    @Override
    public void onDestroy() {
        if (confirmDialog != null) confirmDialog.cancel();
        if (orderMonDialog != null) orderMonDialog.cancel();
        if (saleDialog != null) saleDialog.cancel();
        if (tinhTienDialog != null) tinhTienDialog.cancel();
        if (thongTinDatBanDialog != null) thongTinDatBanDialog.cancel();

        super.onDestroy();
    }

    @Override
    public void findViews(View view) {

    }

    @Override
    public void initComponents() {
        handler = new Handler();
        //initDialogs
        tinhTienDialog = new TinhTienDialog(getContext());
        thongTinDatBanDialog = new ThongTinDatBanDialog(getContext());
        confirmDialog = new ConfirmDialog(getContext());
        orderMonDialog = new OrderMonDialog(getContext());
        saleDialog = new SaleDialog(getContext());

        //initAnimation
        animationAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        animationZoom = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
        animationBounce = AnimationUtils.loadAnimation(AppInstance.getContext(), R.anim.bounce);

    }

    @Override
    public void setEvents() {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) layoutThucDon.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / 2;
        layoutThucDon.setLayoutParams(params);

        listBan.setLayoutManager(new GridLayoutManager(getContext(), 4));


        listMon.setLayoutManager(new LinearLayoutManager(getContext()));

        listMonOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        tvTenLoai.setMovementMethod(new ScrollingMovementMethod());
        tvTenBan.setMovementMethod(new ScrollingMovementMethod());

        btnThucDon.setOnClickListener(this);
        btnSale.setOnClickListener(this);
        btnTinhTien.setOnClickListener(this);

        edtTimKiemMon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                edtTimKiemMon.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                handler.removeCallbacks(runnable);

                runnable = new Runnable() {
                    @Override
                    public void run() {
// TODO: 05/03/2019 tim trong menu
                    }
                };
                handler.postDelayed(runnable, 200);

                return true;
            }
        });

        edtTimKiemMon.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvTenLoai.clearAnimation();
                    tvTenLoai.setVisibility(GONE);

                } else {
                    edtTimKiemMon.onActionViewCollapsed();
                    tvTenLoai.setText(getResources().getString(R.string.thuc_don));
                    tvTenLoai.setVisibility(VISIBLE);
                }
            }
        });

        tvTenBan.setOnClickListener(this);
        popupMenu = new PopupMenu(getContext(), tvTenBan);
        popupMenu.getMenuInflater().inflate(R.menu.ban_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_huy_ban:
                        return true;
                    case R.id.btn_info_dat_ban:
                        return true;
                    case R.id.btn_update_dat_ban:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public void clearTimKiem() {
        if (edtTimKiemMon.hasFocus()) edtTimKiemMon.onActionViewCollapsed();
    }

    public void showError(String message) {
        Utils.notifiOnDialog(message);
    }

    public void showThongTinDatBanDialog(DatBan datBan) {
        thongTinDatBanDialog.setContent(datBan);
    }

    public void showSnackbar(Boolean isSuccess, String message) {
        if (!TextUtils.isEmpty(message)) {
            if (!isSuccess) {
                message = Utils.getStringByRes(error);
            }

            final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View viewSnackbar = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewSnackbar.getLayoutParams();
            params.width = AppInstance.getContext().getResources().getDisplayMetrics().widthPixels / 2;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tinh_tien:
                break;
            case R.id.tv_ten_ban:
                if (!tvTrangThai.getText().equals(Utils.getStringByRes(R.string.trong)))
                    popupMenu.show();
                break;
            case R.id.btn_sale:
                break;
            case R.id.btn_thuc_don:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            default:
                break;
        }
    }

    public void closeThucDonLayout() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    public void showTinhTienDialog(HoaDon hoaDon) {
        tinhTienDialog.setContent(hoaDon);
    }

    public void showSaleDialog(HoaDon hoaDon) {
        saleDialog.setContent(hoaDon);
    }

    public void showTongTien(int tongTien) {
        tvTongTien.setText(Utils.formatMoney(tongTien));
        tvTongTien.startAnimation(animationAlpha);

    }

    public void showGiamGia(int giamGia) {
        if (giamGia > 0) {

            btnSale.setText(giamGia + "%");
        } else {
            btnSale.setText(Utils.getStringByRes(R.string.sale));
        }
        btnSale.startAnimation(animationAlpha);
    }

    public void showConfirmDialog(String tenBan, String tenMon) {
        confirmDialog.setContent(tenBan, String.format(Utils.getStringByRes(R.string.huy_order), tenMon));
        confirmDialog.setOnClickOkListener(new ConfirmDialog.OnClickOkListener() {
            @Override
            public void onClickOk() {
                confirmDialog.dismiss();
            }
        });
    }

    public void showOrderMonDialog(String tenBan, Mon mon) {
        orderMonDialog.setContent(tenBan, mon);
    }

    public void notifyAddListMonOrder() {
        listMonOrder.scrollToPosition(0);
        monOrderAdapter.notifyItemInserted(0);

    }

    public void notifyChangeListMonOrder() {
        monOrderAdapter.notifyDataSetChanged();
    }

    public void notifyRemoveListMonOrder() {
        monOrderAdapter.deleteMonOrder();
    }

    public void notifyUpDateListMonOrder(MonOrder currentMonOrder) {
        listMonOrder.scrollToPosition(monOrderAdapter.getPositionOf(currentMonOrder));
        monOrderAdapter.updateMonOrder(currentMonOrder);

    }

    public void showNhomMon(NhomMon nhomMon) {
        tvTenLoai.setText(nhomMon.getTenLoai());
        tvTenLoai.startAnimation(animationAlpha);

        tbr.setBackgroundColor(nhomMon.getMauSac());
    }

    public void showBan(Ban ban) {

        tvTenBan.setText(ban.getTenBan());
        tvTrangThai.setText(ban.getStringTrangThai());

        tvTenBan.startAnimation(animationZoom);
        tvTrangThai.startAnimation(animationAlpha);
    }

    public void showBanPhucVu(HoaDon hoaDon) {
        showBan(hoaDon.getBan());
        showTongTien(hoaDon.getTongTien());
        showGiamGia(hoaDon.getGiamGia());

        monOrderAdapter.changeData(hoaDon.getMonOrderList());

        popupMenu.getMenu().findItem(R.id.btn_update_dat_ban).setVisible(false);
        if (hoaDon.getDatBan() != null)
            popupMenu.getMenu().findItem(R.id.btn_info_dat_ban).setVisible(true);
        else popupMenu.getMenu().findItem(R.id.btn_info_dat_ban).setVisible(false);
    }

}
