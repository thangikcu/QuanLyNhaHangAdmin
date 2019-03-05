package com.coffeehouse.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.coffeehouse.R;

/**
 * Created by Thanggun99 on 12/12/2016.
 */

public abstract class BaseDialog extends Dialog implements View.OnClickListener{
    @Nullable
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Nullable
    @BindView(R.id.btn_ok)
    Button btnOk;
    @Nullable
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    public BaseDialog(Context context, int layoutResID) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutResID);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (btnOk != null) {

            btnOk.setOnClickListener(this);
        }
        if (btnCancel != null) {

            btnCancel.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) dismiss();
    }
}
