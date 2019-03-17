package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import com.coffeehouse.R;
import com.coffeehouse.adapter.NhanVienManagerAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.User;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.AddNhanVienDialog;
import com.coffeehouse.view.dialog.QRCodeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ChamCongForQuanLyFragment extends BaseFragment implements NhanVienManagerAdapter.OnClickUpdateUser {

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private NhanVienManagerAdapter nhanVienManagerAdapter;

    private Animation animationZoom;
    private MainView mainView;
    private AddNhanVienDialog.OnPickImageResult onPickImageResult;


    public ChamCongForQuanLyFragment(MainView mainView) {
        super(R.layout.fragment_cham_cong_for_quan_ly);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.postDelayed(this::getEmployeeList, 500);
    }

    private void getEmployeeList() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListEmployee().enqueue(new Callback<ResponseData<List<User>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<User>>> call, Response<ResponseData<List<User>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    nhanVienManagerAdapter = new NhanVienManagerAdapter(response.body().getContent(),
                            ChamCongForQuanLyFragment.this);
                    nhanVienManagerAdapter.setDisableUpdate(true);

                    recyclerView.setAdapter(nhanVienManagerAdapter);
                } else {
                    mainView.showMessage("Error!");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<User>>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void initComponents() {
        animationZoom = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
    }

    @Override
    public void setEvents() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                edtTimKiem.onActionViewCollapsed();
                searchView.clearFocus();

                ArrayList<User> result = new ArrayList<>();

                if (nhanVienManagerAdapter != null && nhanVienManagerAdapter.getAllNhanVienList() != null) {
                    for (User user : nhanVienManagerAdapter.getAllNhanVienList()) {
                        if (Utils.removeAccent(user.getFullName().trim().toLowerCase())
                                .contains(Utils.removeAccent(query.trim().toLowerCase()))) {
                            result.add(user);
                        }
                    }
                }

                if (nhanVienManagerAdapter != null) {
                    nhanVienManagerAdapter.changeData(result);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyWord) {
                if (nhanVienManagerAdapter != null && TextUtils.isEmpty(keyWord)) {
                    nhanVienManagerAdapter.showAllData();
                }
                return true;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(User user) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        ChamCongNhanVienFragment chamCongNhanVienFragment = null;

        ChamCongNhanVienFragment finalChamCongNhanVienFragment = chamCongNhanVienFragment;
        chamCongNhanVienFragment = new ChamCongNhanVienFragment(mainView,
                user, () -> {
//            getChildFragmentManager().beginTransaction()
//                    .remove(finalChamCongNhanVienFragment)
//                    .commitAllowingStateLoss();
            getChildFragmentManager().popBackStack();
        });
        fragmentTransaction.add(R.id.container, chamCongNhanVienFragment, "cham_cong_detail")
                .commitAllowingStateLoss();
        fragmentTransaction.addToBackStack("cham_cong_detail");
    }

    @OnClick(R.id.btn_generate_qrcode)
    public void onViewClicked() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .generateQrcode().enqueue(new Callback<ResponseData<String>>() {
            @Override
            public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                mainView.hideLoading();
                if (response.body() != null && response.body().getContent() != null) {
                    new QRCodeDialog(getContext(), response.body().getContent())
                            .show();
                } else {
                    mainView.showMessage("Error!");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }
}
