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
import com.coffeehouse.adapter.DeskManagerAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.Desk;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.AddDeskDialog;
import com.coffeehouse.view.dialog.ConfirmDialog;
import com.coffeehouse.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class DeskManagerFragment extends BaseFragment implements DeskManagerAdapter.OnClickDesk {
    private Button btnThemMoi;
    private RecyclerView recyclerView;
    private MainView mainView;
    private DeskManagerAdapter adapter;
    private SearchView edtTimKiem;


    public DeskManagerFragment(MainView mainView) {
        super(R.layout.fragment_ban_manager);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.postDelayed(this::getDeskList, 500);
    }

    private void getDeskList() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListDesk().enqueue(new Callback<ResponseData<List<Desk>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Desk>>> call, Response<ResponseData<List<Desk>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    adapter = new DeskManagerAdapter(response.body().getContent(),
                            DeskManagerFragment.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    mainView.showMessage("Error!");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Desk>>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void findViews(View view) {
        recyclerView = view.findViewById(R.id.list_ban);
        edtTimKiem = view.findViewById(R.id.edt_tim_kiem_ban);
        btnThemMoi = view.findViewById(R.id.btn_them_ban);
    }

    @Override
    public void initComponents() {


    }

    @Override
    public void setEvents() {

        edtTimKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                edtTimKiem.onActionViewCollapsed();
                edtTimKiem.clearFocus();

                ArrayList<Desk> result = new ArrayList<>();

                if (adapter != null && adapter.getAllDeskList() != null) {
                    for (Desk desk : adapter.getAllDeskList()) {
                        if (Utils.removeAccent(desk.getId().trim().toLowerCase())
                                .contains(Utils.removeAccent(query.trim().toLowerCase()))) {
                            result.add(desk);
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

                if (TextUtils.isEmpty(keyWord)) {
                    adapter.showAllData();
                }

                return true;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnThemMoi.setOnClickListener(view1 -> onClickAddNew());
    }

    public void onClickAddNew() {

        AddDeskDialog addDeskDialog = new AddDeskDialog(getContext());

        addDeskDialog.setOnClickOkListener(desk -> {
            mainView.showLoading();
            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .addDesk(desk.getId())
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                mainView.showMessage("Thêm mới thành công!");
                                getDeskList();
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
        addDeskDialog.show();
    }

    @Override
    public void onClickDelete(Desk desk) {
        ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
        confirmDialog.setContent("Xác nhận", "xóa " + desk.getId() + "?");
        confirmDialog.setOnClickOkListener(() -> {
            confirmDialog.dismiss();
            mainView.showLoading();
            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .deleteDesk(desk.getId())
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                mainView.showMessage("Xóa bàn thành công!");
                                getDeskList();
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
        confirmDialog.show();
    }
}
