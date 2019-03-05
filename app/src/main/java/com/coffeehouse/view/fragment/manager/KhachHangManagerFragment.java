package com.coffeehouse.view.fragment.manager;


import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.coffeehouse.R;
import com.coffeehouse.adapter.TinTucAdapter;
import com.coffeehouse.model.Database;
import com.coffeehouse.model.TinTucManager;
import com.coffeehouse.presenter.MainPresenter;
import com.coffeehouse.view.fragment.BaseFragment;

@SuppressLint("ValidFragment")
public class KhachHangManagerFragment extends BaseFragment {
    private Button btnThemMoi;
    private RecyclerView tinTucrecyclerView;
    private TinTucAdapter tinTucAdapter;
    private Database database;
    private MainPresenter mainPresenter;
    private TinTucManager tinTucManager;


    public KhachHangManagerFragment(MainPresenter mainPresenter) {
        super(R.layout.fragment_khach_hang_manager);
        this.database = mainPresenter.getDatabase();
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void findViews(View view) {
        tinTucrecyclerView = (RecyclerView) view.findViewById(R.id.list_nhan_vien);
        btnThemMoi = (Button) view.findViewById(R.id.btn_them_nhan_vien);
    }

    @Override
    public void initComponents() {


    }

    @Override
    public void setEvents() {
        tinTucrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}