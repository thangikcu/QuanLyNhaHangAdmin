package com.coffeehouse.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.coffeehouse.R;

import butterknife.BindView;


public class NotifiDialog extends BaseDialog {
    @BindView(R.id.tv_notifi)
    TextView tvNotifi;

    public NotifiDialog(@NonNull Context context) {
        super(context, R.layout.dialog_notifi);
        setCancelable(false);
    }

    public void notify(String message) {
        tvNotifi.setText(message);
        show();
    }

    public void notify(int residMessage) {
        tvNotifi.setText(residMessage);
        show();
    }
}
