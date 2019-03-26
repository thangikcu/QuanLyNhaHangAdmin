package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.adapter.BaseRecyclerViewAdapter;
import com.coffeehouse.adapter.ReceiptAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.GoodReceipt;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.view.dialog.AddReceiptDialog;
import com.coffeehouse.view.dialog.ViewReceiptDialog;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class DepotFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private ReceiptAdapter receiptAdapter;
    private Animation animationZoom;
    private MainView mainView;


    public DepotFragment(MainView mainView) {
        super(R.layout.fragment_depot);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.postDelayed(() -> getDepot(0, true), 500);
    }

    private void getDepot(int pageIndex, boolean showLoading) {
        if (showLoading) {
            mainView.showLoading();
        }
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListReceipt(pageIndex)
                .enqueue(new Callback<ResponseData<List<GoodReceipt>>>() {
                    @Override
                    public void onResponse(Call<ResponseData<List<GoodReceipt>>> call,
                                           Response<ResponseData<List<GoodReceipt>>> response) {
                        mainView.hideLoading();
                        if (response.body() != null && response.body().getContent() != null) {
                            List<GoodReceipt> goodReceiptList = response.body().getContent();

                            if (receiptAdapter == null) {
                                receiptAdapter = new ReceiptAdapter(goodReceiptList,
                                        pageIndexToLoadMore -> getDepot(pageIndexToLoadMore, false),
                                        10, DepotFragment.this);
                                recyclerView.setAdapter(receiptAdapter);
                            } else {
                                if (goodReceiptList.size() < receiptAdapter.getNumberItemPerPage()) {
                                    receiptAdapter.setRemaingData(false);
                                }
                                receiptAdapter.setLoading(false);
                                receiptAdapter.addItem(goodReceiptList);
                            }
                        } else {
                            mainView.showMessage("Error!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<List<GoodReceipt>>> call, Throwable t) {
                        mainView.hideLoading();
                        if (receiptAdapter != null) {
                            receiptAdapter.setLoading(false);
                        }
                        mainView.showMessage("Error!");
                    }
                });
    }

    @Override
    public void initComponents() {
        animationZoom = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAdd.setOnClickListener(view1 -> onClickNhapHang());
    }

    private void onClickNhapHang() {
        AddReceiptDialog addReceiptDialog = new AddReceiptDialog(getContext());
        addReceiptDialog.setOnClickOkListener(receiptDetailList -> {
            mainView.showLoading();

            ArrayMap<String, Object> requestBody = new ArrayMap<>();

            requestBody.put("employeeId", AppInstance.getLoginUser().getID());
            requestBody.put("goodReceiptDtos", receiptDetailList);

            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .addReceipt(ResfulApi.createJsonRequestBody(requestBody))
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call,
                                               Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                if (receiptAdapter != null) {
                                    receiptAdapter.clear();
                                    receiptAdapter = null;
                                }
                                getDepot(0, true);
                            } else {
                                mainView.showMessage("Error!");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                            mainView.hideLoading();
                            mainView.showMessage("Error!");
                        }
                    });
        });
        addReceiptDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        new ViewReceiptDialog(getContext(), receiptAdapter.getItemAt(position))
                .show();
    }
}
