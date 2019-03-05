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
public class BanManagerFragment extends BaseFragment{
    private Button btnThemMoi;
    private RecyclerView tinTucrecyclerView;
    private TinTucAdapter tinTucAdapter;
    private Database database;
    private MainPresenter mainPresenter;
    private TinTucManager tinTucManager;


    public BanManagerFragment(MainPresenter mainPresenter) {
        super(R.layout.fragment_ban_manager);
        this.database = mainPresenter.getDatabase();
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void findViews(View view) {
        tinTucrecyclerView = (RecyclerView) view.findViewById(R.id.list_ban);
        btnThemMoi = (Button) view.findViewById(R.id.btn_them_ban);
    }

    @Override
    public void initComponents() {


    }

    @Override
    public void setEvents() {
        tinTucrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
