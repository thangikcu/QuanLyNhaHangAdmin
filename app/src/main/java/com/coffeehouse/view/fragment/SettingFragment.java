package com.coffeehouse.view.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Switch;

import com.coffeehouse.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.dark_mode)
    Switch darkMode;

    public SettingFragment() {
        super(R.layout.fragment_setting);
    }

    @Override
    public void findViews(View view) {

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {
        darkMode.setOnCheckedChangeListener((compoundButton, checked) -> {
            getActivity().recreate();
        });
    }
}
