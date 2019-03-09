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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import com.coffeehouse.R;
import com.coffeehouse.adapter.MonManagerAdapter;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.Drink;
import com.coffeehouse.model.entity.DrinkType;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.MyPermission;
import com.coffeehouse.util.Utils;
import com.coffeehouse.view.dialog.ConfirmDialog;
import com.coffeehouse.view.dialog.ThemMonDialog;
import com.coffeehouse.view.fragment.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class MonManagerFragment extends BaseFragment implements View.OnClickListener, MonManagerAdapter.OnClickDrink {
    public static final int SELECT_PHOTO = 1;

    private Button btnThemMoi;
    private SearchView edtTimKiemMon;
    private RecyclerView monRecyclerView;
    private MonManagerAdapter monManagerAdapter;
    private ThemMonDialog themMonDialog;

    private Spinner spnNhomMon;
    private List<DrinkType> nhomMonList;
    private ArrayAdapter<String> nhomMonAdapter;
    private Animation animationZoom;
    private MainView mainView;
    private ArrayList<Drink> drinkList;
    private ThemMonDialog.OnPickImageResult onPickImageResult;


    public MonManagerFragment(MainView mainView) {
        super(R.layout.fragment_mon_manager);
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
                if (response.body() != null) {
                    nhomMonList = response.body().getContent();

                    ArrayList<DrinkType> drinkTypes = new ArrayList<>();
                    DrinkType element = new DrinkType();
                    element.setName("All");
                    drinkTypes.add(0, element);
                    drinkTypes.addAll(nhomMonList);

                    nhomMonAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, drinkTypes);

                    nhomMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnNhomMon.setAdapter(nhomMonAdapter);

                    getAllDrink();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<DrinkType>>> call, Throwable t) {
                mainView.hideLoading();
                mainView.showMessage(t.getMessage());
            }
        });
    }

    private void getAllDrink() {
        mainView.showLoading();
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getListDrink().enqueue(new Callback<ResponseData<List<DrinkType>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<DrinkType>>> call, Response<ResponseData<List<DrinkType>>> response) {
                mainView.hideLoading();
                if (response.body() != null) {
                    List<DrinkType> drinkTypeList = response.body().getContent();

                    drinkList = new ArrayList<>();
                    drinkTypeList.forEach(drinkType -> drinkList.addAll(drinkType.getListDrinks()));

                    monManagerAdapter = new MonManagerAdapter(drinkList, MonManagerFragment.this);
                    monRecyclerView.setAdapter(monManagerAdapter);
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
        spnNhomMon = view.findViewById(R.id.spn_nhom_mon);
        edtTimKiemMon = view.findViewById(R.id.edt_tim_kiem_mon);
        monRecyclerView = view.findViewById(R.id.list_mon);
        btnThemMoi = view.findViewById(R.id.btn_them_thuc_don);
    }

    @Override
    public void initComponents() {
        animationZoom = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
    }

    private List<Drink> getListDrinkByDrinkTypeId(long drinkTypeId) {
        List<Drink> drinks = new ArrayList<>();

        drinkList.forEach(drink -> {
            if (drink.getBeverageId() == drinkTypeId) {
                drinks.add(drink);
            }
        });

        return drinks;
    }

    @Override
    public void setEvents() {
        spnNhomMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monRecyclerView.startAnimation(animationZoom);

                if (monManagerAdapter != null) {
                    if (position == 0) {
                        monManagerAdapter.changeData(drinkList);
                    } else {
                        monManagerAdapter.changeData(getListDrinkByDrinkTypeId(nhomMonList.get(position - 1).getID()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

                if (monManagerAdapter != null) {
                    monManagerAdapter.changeData(monTimKiem);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyWord) {
                return true;
            }
        });

        btnThemMoi.setOnClickListener(this);

        monRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
/*


            if(imagePath == null && uri != null){
                imagePath = uri.toString();
            }

            onPickImageResult.onResult(imagePath);*/
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_them_thuc_don) {
            ThemMonDialog themMonDialog = new ThemMonDialog(getContext(), nhomMonList);
            themMonDialog.setPickImageRequest(onResult -> {
                this.onPickImageResult = onResult;
                if (MyPermission.requirePermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MyPermission.PERMISSION_ACCESS_LIBRARY)) {
                    startPickImage();
                }
            });
            themMonDialog.setOnClickOkListener(drink -> {
                mainView.showLoading();
                ResfulApi.getInstance().getService(TheCoffeeService.class)
                        .addDrink(ResfulApi.createJsonRequestBody(drink))
                        .enqueue(new Callback<ResponseData<String>>() {
                            @Override
                            public void onResponse(Call<ResponseData<String>> call, Response<ResponseData<String>> response) {
                                mainView.hideLoading();
                                if (response.body() != null && response.body().getContent().equals("Successful")) {
                                    mainView.showMessage("Thêm món mới thành công!");
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
            themMonDialog.show();
        }
    }

    private void startPickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, ""), SELECT_PHOTO);
    }

    @Override
    public void onClickDelete(Drink drink) {
        ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
        confirmDialog.setContent(Utils.getStringByRes(R.string.xac_nhan),
                Utils.getStringByRes(R.string.xac_nhan_xoa_mon) + " " + drink.getName() + "?");

        confirmDialog.setOnClickOkListener(new ConfirmDialog.OnClickOkListener() {
            @Override
            public void onClickOk() {
                confirmDialog.dismiss();
                // TODO: 09/03/2019 remove drink
            }
        });
    }

    @Override
    public void onClickUpdate(Drink drink) {
/*
        ThemMonDialog themMonDialog = new ThemMonDialog(getContext());
        themMonDialog.show();
*/

    }
}
