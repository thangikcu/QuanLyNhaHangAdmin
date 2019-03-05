package com.coffeehouse.view.dialog;

import android.content.Context;
import android.view.View;

import com.coffeehouse.R;

/**
 * Created by Thanggun99 on 10/12/2016.
 */

public class ErrorDialog extends BaseDialog{

    public ErrorDialog(Context context) {
        super(context, R.layout.dialog_error);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
