package com.coffeehouse.view.fragment.manager;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.coffeehouse.R;
import com.coffeehouse.adapter.DrinkTypeManagerAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.AddDrinkTypeDialog;
import com.coffeehouse.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class DrinkTypeManagerFragment extends BaseFragment implements DrinkTypeManagerAdapter.OnClickDrinkType {
    private Button btnThemMoi;
    private RecyclerView recyclerView;
    private MainView mainView;
    private DrinkTypeManagerAdapter adapter;
    private SearchView edtTimKiemMon;


    public DrinkTypeManagerFragment(MainView mainView) {
        super(R.layout.fragment_loai_mon_manager);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.postDelayed(this::getDrinkTypeList, 500);
    }

    private void getDrinkTypeList() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getAllDrinkType().enqueue(new Callback<ResponseData<List<DrinkType>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<DrinkType>>> call, Response<ResponseData<List<DrinkType>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    adapter = new DrinkTypeManagerAdapter(response.body().getContent(),
                            DrinkTypeManagerFragment.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    mainView.showMessage("Error!");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<DrinkType>>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void findViews(View view) {
        recyclerView = view.findViewById(R.id.list_loai_mon);
        edtTimKiemMon = view.findViewById(R.id.edt_tim_kiem_loai_mon);
        btnThemMoi = view.findViewById(R.id.btn_them_loai_mon);
    }

    @Override
    public void initComponents() {


    }

    @Override
    public void setEvents() {

        edtTimKiemMon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                edtTimKiemMon.onActionViewCollapsed();
                edtTimKiemMon.clearFocus();

                ArrayList<DrinkType> result = new ArrayList<>();

                if (adapter != null && adapter.getAllDrinkTypeList() != null) {
                    for (DrinkType drinkType : adapter.getAllDrinkTypeList()) {
                        if (Utils.removeAccent(drinkType.getName().trim().toLowerCase())
                                .contains(Utils.removeAccent(query.trim().toLowerCase()))) {
                            result.add(drinkType);
                        }
                    }
                }

                if (adapter != null) {
                    adapter.changeData(result);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyWord) {

                if (adapter != null && TextUtils.isEmpty(keyWord)) {
                    adapter.showAllData();
                }

                return true;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnThemMoi.setOnClickListener(view1 -> onClickAddNew());
    }

    public void onClickAddNew() {

        AddDrinkTypeDialog addDrinkTypeDialog = new AddDrinkTypeDialog(getContext());

        addDrinkTypeDialog.setOnClickOkListener(drinkType -> {
            mainView.showLoading();
            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .addDrinkType(ResfulApi.createJsonRequestBody(drinkType))
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                mainView.showMessage("Thêm mới thành công!");
                                getDrinkTypeList();
                            } else {
                                mainView.showMessage(response.body() != null ? response.body().getMessage() : "Error!");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                            mainView.hideLoading();
                            mainView.showMessage(t.getMessage());
                        }
                    });
        });
        addDrinkTypeDialog.show();
    }

    @Override
    public void onClickDelete(DrinkType drinkType) {

    }

    @Override
    public void onClickUpdate(DrinkType drinkType) {
        AddDrinkTypeDialog addDrinkTypeDialog = new AddDrinkTypeDialog(getContext(), drinkType);
        addDrinkTypeDialog.setOnClickOkListener(drinkTypeUpdate -> {
            mainView.showLoading();
            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .updateDrinkType(ResfulApi.createJsonRequestBody(drinkTypeUpdate))
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                mainView.showMessage("Cập nhật thành công!");
                                getDrinkTypeList();
                            } else {
                                mainView.showMessage(response.body() != null ? response.body().getMessage() : "Error!");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                            mainView.hideLoading();
                            mainView.showMessage(t.getMessage());
                        }
                    });
        });
        addDrinkTypeDialog.show();
    }
}
