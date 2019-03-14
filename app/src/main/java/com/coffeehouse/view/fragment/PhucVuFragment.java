package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.adapter.DeskAdapter;
import com.coffeehouse.adapter.DrinkAdapter;
import com.coffeehouse.adapter.MonOrderAdapter;
import com.coffeehouse.adapter.NhomMonAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.Bill;
import com.coffeehouse.model.entity.Desk;
import com.coffeehouse.model.entity.Drink;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.model.entity.OrderDetail;
import com.coffeehouse.model.entity.OrderRequest;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.OrderDialog;
import com.coffeehouse.view.dialog.TinhTienDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    @BindView(R.id.tv_gio_den)
    TextView tvGioDen;
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
    @BindView(R.id.ln_thong_tin_ban)
    View layoutBanPhucVu;

    //adapter
    private DeskAdapter deskAdapter;
    private MonOrderAdapter monOrderAdapter;
    private DrinkAdapter drinkAdapter;
    private NhomMonAdapter nhomMonAdapter;

    //animationAlpha
    private Animation animationAlpha;
    private Animation animationZoom;
    private Animation animationBounce;

    private MainView mainView;
    private Bill currentBill;
    private Desk currentDesk;
    private List<Drink> drinkList;


    public PhucVuFragment(MainView mainView) {
        super(R.layout.fragment_phuc_vu);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    @Override
    public void loadData() {
        view.postDelayed(() -> {
            getListDesk();
            getMenu();
        }, 500);
    }

    private void getMenu() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListDrink().enqueue(new Callback<ResponseData<List<DrinkType>>>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseData<List<DrinkType>>> call, Response<ResponseData<List<DrinkType>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    List<DrinkType> drinkTypeList = response.body().getContent();

                    drinkList = new ArrayList<>();
                    drinkTypeList.forEach(drinkType -> drinkList.addAll(drinkType.getListDrinks()));

                    listNhomMon.setAdapter(nhomMonAdapter = new NhomMonAdapter(drinkTypeList, drinkType -> {
                        showListDrink(drinkType);
                    }));

                    showListDrink(drinkTypeList.size() > 0 ? drinkTypeList.get(0) : null);
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
        if (drinkType != null) {
            tvTenLoai.setText(drinkType.getName());
            tvTenLoai.startAnimation(animationAlpha);

//          tbr.setBackgroundColor(drinkType.getMauSac());
            drinkAdapter.changeData(drinkType.getListDrinks());
        }
    }

    private void OrderDrink(Drink drink, int count) {
        mainView.showLoading();

        if (currentBill != null && currentDesk != null && currentDesk.isServing()) {

            boolean drinkExist = false;

            for (int i = 0; i < currentBill.getOrderDetails().size(); i++) {
                OrderDetail orderDetail = currentBill.getOrderDetails().get(i);
                if (orderDetail.getDrinkId() == drink.getID()) {
                    drinkExist = true;
                    orderDetail.setCount(orderDetail.getCount() + count);
                    break;
                }
            }

            if (!drinkExist) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setCount(count);
                orderDetail.setDrinkId(drink.getID());
                orderDetail.setDrinkName(drink.getName());
                orderDetail.setPrice(drink.getPrice());
                currentBill.getOrderDetails().add(orderDetail);
            }

            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .updateBill(ResfulApi.createJsonRequestBody(currentBill))
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                getDeskInfo(currentDesk);
                                showSnackbar(currentDesk.getId() + ": + " + count + drink.getName());
                            } else {
                                mainView.showMessage(response.body() != null ? response.body().getMessage() : "Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                            mainView.hideLoading();
                            mainView.showMessage(t.getMessage());
                        }
                    });
        } else {

            OrderRequest orderRequest = new OrderRequest();

            orderRequest.setDeskId(currentDesk.getId());
            orderRequest.setEmployeeId(AppInstance.getLoginUser().getID());

            ArrayList<OrderRequest.DrinkId> drinkIds = new ArrayList<>();
            OrderRequest.DrinkId drinkId = new OrderRequest.DrinkId();
            drinkId.setDrinkId(drink.getID());
            drinkId.setCount(count);
            drinkIds.add(drinkId);
            orderRequest.setDrinkIds(drinkIds);

            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .createBill(ResfulApi.createJsonRequestBody(orderRequest))
                    .enqueue(new Callback<ResponseData<Bill>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Bill>> call, Response<ResponseData<Bill>> response) {
                            mainView.hideLoading();
                            if (response.body() != null) {
                                currentDesk.setServing(true);
                                deskAdapter.updateBan(currentDesk);
                                showDeskInfo(currentDesk);
                                showBanPhucVu(response.body().getContent());
                                showSnackbar("+Hóa đơn mới " + currentDesk.getId());
                            } else {
                                mainView.showMessage(response.body() != null ? response.body().getMessage() : "Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Bill>> call, Throwable t) {
                            mainView.hideLoading();
                            mainView.showMessage(t.getMessage());
                        }
                    });
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
                    List<Desk> deskList = response.body().getContent();

                    if (deskList.size() > 0) {
                        deskList.get(0).setSelected(true);
                        getDeskInfo(deskList.get(0));
                    }

                    listBan.setAdapter(deskAdapter = new DeskAdapter(deskList, desk -> {
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
        showDeskInfo(desk);

        if (desk.isServing()) {
            mainView.showLoading();

            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .getOrderDetail(desk.getId()).enqueue(new Callback<ResponseData<Bill>>() {
                @Override
                public void onResponse(Call<ResponseData<Bill>> call, Response<ResponseData<Bill>> response) {
                    mainView.hideLoading();
                    if (response.body() != null) {
                        showBanPhucVu(response.body().getContent());
                    } else {
                        mainView.showMessage("Error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseData<Bill>> call, Throwable t) {
                    mainView.hideLoading();
                    mainView.showMessage(t.getMessage());
                }
            });
        } else {
            currentBill = null;
        }
    }

    private void showDeskInfo(Desk desk) {
        currentDesk = desk;
        tvTenBan.setText(desk.getId());
        tvTrangThai.setText(desk.getStringTrangThai());

        tvTenBan.startAnimation(animationZoom);
        tvTrangThai.startAnimation(animationAlpha);

        layoutBanPhucVu.setVisibility(desk.isServing() ? VISIBLE : GONE);
    }

    public void showBanPhucVu(Bill bill) {
        this.currentBill = bill;

        tvTongTien.setText(Utils.formatMoney(bill.getTotalPrice()));
        tvTongTien.startAnimation(animationAlpha);
        tvGioDen.setText(bill.getCreateDate());

        if (drinkList != null) {
            bill.getOrderDetails().forEach(drinkOrder -> {
                for (int i = 0; i < drinkList.size(); i++) {
                    if (drinkOrder.getDrinkId() == drinkList.get(i).getID()) {
                        drinkOrder.setDrinkImageData(drinkList.get(i).getImageToShow());
                        break;
                    }
                }
            });
        }

        monOrderAdapter.changeData(bill.getOrderDetails());
    }

    @Override
    public void findViews(View view) {

    }

    @Override
    public void initComponents() {
        //initDialogs
        //tinhTienDialog = new TinhTienDialog();

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
        layoutBanPhucVu.setVisibility(GONE);

        listBan.setLayoutManager(new GridLayoutManager(getContext(), 4));

        listMon.setLayoutManager(new LinearLayoutManager(getContext()));
        listMon.setAdapter(drinkAdapter = new DrinkAdapter(drink -> {
            OrderDialog orderDialog = new OrderDialog(getContext(), currentDesk.getId(),
                    drink);
            orderDialog.setOnClickOk(count -> {
                OrderDrink(drink, count);
            });
            orderDialog.show();
        }));

        listMonOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        listMonOrder.setAdapter(monOrderAdapter = new MonOrderAdapter(this::OnClickMonOrder));

        tvTenLoai.setMovementMethod(new ScrollingMovementMethod());
        tvTenBan.setMovementMethod(new ScrollingMovementMethod());

        btnThucDon.setOnClickListener(this);
        btnSale.setOnClickListener(this);
        btnTinhTien.setOnClickListener(this);

        edtTimKiemMon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                edtTimKiemMon.onActionViewCollapsed();

                ArrayList<Drink> monTimKiem = new ArrayList<>();

                if (drinkList != null) {
                    for (Drink mon : drinkList) {
                        if (Utils.removeAccent(mon.getName().trim().toLowerCase())
                                .contains(Utils.removeAccent(query.trim().toLowerCase()))) {
                            monTimKiem.add(mon);
                        }
                    }
                }

                listMon.startAnimation(animationAlpha);
                drinkAdapter.changeData(monTimKiem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return true;
            }
        });

        edtTimKiemMon.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvTenLoai.clearAnimation();
                tvTenLoai.setVisibility(GONE);

            } else {
                edtTimKiemMon.onActionViewCollapsed();
                tvTenLoai.setText(getResources().getString(R.string.thuc_don));
                tvTenLoai.setVisibility(VISIBLE);
            }
        });

        tvTenBan.setOnClickListener(this);
    }

    private void OnClickMonOrder(OrderDetail orderDetail) {
        AtomicReference<Drink> drink = new AtomicReference<>();

        if (drinkList != null) {
            drinkList.forEach(d -> {
                if (d.getID() == orderDetail.getDrinkId()) {
                    drink.set(d);
                }
            });
        }

        if (drink.get() != null) {
            OrderDialog orderDialog = new OrderDialog(getContext(), currentDesk.getId(),
                    drink.get());
            orderDialog.setOnClickOk(count -> {
                OrderDrink(drink.get(), count);
            });
            orderDialog.show();
        }
    }

    public void showSnackbar(String message) {
        if (!TextUtils.isEmpty(message)) {
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
                payment();
                break;
            case R.id.tv_ten_ban:
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

    private void payment() {
        if (currentBill != null) {
            new TinhTienDialog(getContext(), currentBill, () -> {

                mainView.showLoading();

                ResfulApi.getInstance().getService(TheCoffeeService.class)
                        .payment(currentBill.getID())
                        .enqueue(new Callback<ResponseData<String>>() {
                            @Override
                            public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                                mainView.hideLoading();
                                if (response.body() != null && response.body().getContent().equals("Successful")) {
                                    currentDesk.setServing(false);
                                    deskAdapter.updateBan(currentDesk);
                                    showDeskInfo(currentDesk);
                                    showSnackbar(currentDesk.getId() + " thanh toán +" + Utils.formatMoney(currentBill.getTotalPrice()));
                                    currentBill = null;
                                } else {
                                    mainView.showMessage(response.body() != null ? response.body().getMessage() : "Error");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                                mainView.hideLoading();
                                mainView.showMessage(t.getMessage());
                            }
                        });
            }).show();
        }
    }

    public void closeThucDonLayout() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }
}
