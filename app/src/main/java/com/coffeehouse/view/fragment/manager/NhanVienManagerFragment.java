package com.coffeehouse.view.fragment.manager;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SearchView;

import com.coffeehouse.R;
import com.coffeehouse.adapter.NhanVienManagerAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.User;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.MyPermission;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.AddNhanVienDialog;
import com.coffeehouse.view.fragment.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class NhanVienManagerFragment extends BaseFragment implements NhanVienManagerAdapter.OnClickUpdateUser {

    public static final int SELECT_PHOTO = 1;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private NhanVienManagerAdapter nhanVienManagerAdapter;

    private Animation animationZoom;
    private MainView mainView;
    private AddNhanVienDialog.OnPickImageResult onPickImageResult;


    public NhanVienManagerFragment(MainView mainView) {
        super(R.layout.fragment_nhan_vien_manager);
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
                    recyclerView.setAdapter(nhanVienManagerAdapter = new NhanVienManagerAdapter(response.body().getContent(), NhanVienManagerFragment.this));
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

        btnAdd.setOnClickListener(view1 -> onClickAddUser());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void onClickAddUser() {
        AddNhanVienDialog addNhanVienDialog = new AddNhanVienDialog(getContext());
        addNhanVienDialog.setPickImageRequest(onResult -> {
            this.onPickImageResult = onResult;
            if (MyPermission.requirePermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MyPermission.PERMISSION_ACCESS_LIBRARY)) {
                startPickImage();
            }
        });
        addNhanVienDialog.setOnClickOkListener(user -> {
            mainView.showLoading();
            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .addEmployee(ResfulApi.createJsonRequestBody(user))
                    .enqueue(new Callback<ResponseData<User>>() {
                        @Override
                        public void onResponse(Call<ResponseData<User>> call, Response<ResponseData<User>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent() != null) {
                                mainView.showMessage("Thêm mới thành công!");
                                getEmployeeList();
                            } else {
                                mainView.showMessage(response.body() != null ? response.body().getMessage() : "Error!");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<User>> call, Throwable t) {
                            mainView.hideLoading();
                            mainView.showMessage(t.getMessage());
                        }
                    });
        });
        addNhanVienDialog.show();
    }

    @Override
    public void onClick(User user) {
        AddNhanVienDialog updateNhanVienDialog = new AddNhanVienDialog(getContext(), user);
        updateNhanVienDialog.setPickImageRequest(onResult -> {
            this.onPickImageResult = onResult;
            if (MyPermission.requirePermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MyPermission.PERMISSION_ACCESS_LIBRARY)) {
                startPickImage();
            }
        });
        updateNhanVienDialog.setOnClickOkListener(userUpdate -> {
            mainView.showLoading();
            ResfulApi.getInstance().getService(TheCoffeeService.class)
                    .updateEmployee(ResfulApi.createJsonRequestBody(userUpdate))
                    .enqueue(new Callback<ResponseData<String>>() {
                        @Override
                        public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                            mainView.hideLoading();
                            if (response.body() != null && response.body().getContent().equals("Successful")) {
                                mainView.showMessage("Cập nhật thành công!");
                                getEmployeeList();
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
        updateNhanVienDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            String imagePath = null;

            if (uri != null) {
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri,
                        filePath, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    cursor.close();
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    onPickImageResult.onResult(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MyPermission.PERMISSION_ACCESS_LIBRARY:
                if (MyPermission.onRequestResult(getContext(), grantResults,
                        R.string.ban_can_cap_quyen_de_truy_cap_thu_vien_anh)) {
                    startPickImage();
                }
                break;
            default:
        }
    }

    private void startPickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, ""), SELECT_PHOTO);
    }
}
