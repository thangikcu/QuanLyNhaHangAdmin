package com.coffeehouse.util;

import android.os.SystemClock;
import android.view.View;

public abstract class AvoidDoubleClick implements View.OnClickListener {
    private long mLastClickTime;

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        onClicked(view);
    }

    protected abstract void onClicked(View view);
}
