package com.coffeehouse.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import com.coffeehouse.R;


public class NotifiDialog extends BaseDialog {
    @BindView(R.id.tv_notifi)
    TextView tvNotifi;

    public NotifiDialog(@NonNull Context context) {
        super(context, R.layout.dialog_notifi);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        setCancelable(false);

    }

    public void notifi(String message) {
        tvNotifi.setText(message);
        show();
    }
}
