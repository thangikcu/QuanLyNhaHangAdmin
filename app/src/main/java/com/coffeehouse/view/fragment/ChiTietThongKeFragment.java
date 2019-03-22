package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.adapter.ChiTietThongKeAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.TurnOverDetail;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.AddNhanVienDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ChiTietThongKeFragment extends BaseFragment {

    private final String turnOverStatus;
    private final String time;
    private final OnClickBackListener onClickBackListener;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_doanh_thu)
    TextView tvDoanhThu;
    private ChiTietThongKeAdapter chiTietThongKeAdapter;
    private Animation animationZoom;
    private MainView mainView;
    private long tongDoanhThu;
    private AddNhanVienDialog.OnPickImageResult onPickImageResult;


    public ChiTietThongKeFragment(MainView mainView, long tongDoanhThu, String turnOverStatus, String time, OnClickBackListener onClickBackListener) {
        super(R.layout.fragment_chi_tiet_thong_ke);
        this.mainView = mainView;
        this.tongDoanhThu = tongDoanhThu;
        this.turnOverStatus = turnOverStatus;
        this.time = time;
        this.onClickBackListener = onClickBackListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.postDelayed(() -> getChiTietThongKe(0, true), 500);
    }

    private void getChiTietThongKe(int pageIndex, boolean showLoading) {
        if (showLoading) {
            mainView.showLoading();
        }
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getTurnOverDetail(time, turnOverStatus, pageIndex)
                .enqueue(new Callback<ResponseData<List<TurnOverDetail>>>() {
                    @Override
                    public void onResponse(Call<ResponseData<List<TurnOverDetail>>> call,
                                           Response<ResponseData<List<TurnOverDetail>>> response) {
                        mainView.hideLoading();
                        if (response.body() != null && response.body().getContent() != null) {
                            List<TurnOverDetail> turnOverDetailList = response.body().getContent();

                            if (chiTietThongKeAdapter == null) {
                                chiTietThongKeAdapter = new ChiTietThongKeAdapter(turnOverDetailList,
                                        pageIndexToLoadMore -> getChiTietThongKe(pageIndexToLoadMore, false),
                                        10);
                                recyclerView.setAdapter(chiTietThongKeAdapter);
                            } else {
                                if (turnOverDetailList.size() < chiTietThongKeAdapter.getNumberItemPerPage()) {
                                    chiTietThongKeAdapter.setRemaingData(false);
                                }
                                chiTietThongKeAdapter.setLoading(false);
                                chiTietThongKeAdapter.addItem(turnOverDetailList);
                            }
                        } else {
                            mainView.showMessage("Error!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<List<TurnOverDetail>>> call, Throwable t) {
                        mainView.hideLoading();
                        if (chiTietThongKeAdapter != null) {
                            chiTietThongKeAdapter.setLoading(false);
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

        tvTitle.setText(String.format("Danh sách hóa đơn trong %1s %2s",
                turnOverStatus.equals("YEAR") ? "năm" : "tháng", time));
        tvDoanhThu.setText("Doanh thu: " + Utils.formatMoneyToVnd(tongDoanhThu));
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onClickBackListener.onClickBack();
    }

    public interface OnClickBackListener {
        void onClickBack();
    }
}
