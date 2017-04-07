package thanggun99.quanlynhahang.view.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import thanggun99.quanlynhahang.R;


public class ConfirmDialog extends BaseDialog {
    @BindView(R.id.tv_message)
    TextView tvMessage;

    private OnClickOkListener onClickOkListener;

    public ConfirmDialog(Context context) {
        super(context, R.layout.dialog_confirm);

        tvMessage.setMovementMethod(new ScrollingMovementMethod());
    }

    public void setContent(String title, String message) {
        tvTitle.setText(title);
        tvMessage.setText(message);
        show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            onClickOkListener.onClickOk();
        }
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk();
    }
}
