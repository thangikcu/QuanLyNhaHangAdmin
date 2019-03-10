package com.coffeehouse.view.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.widget.ImageView;

import com.coffeehouse.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;

public class QRCodeDialog extends BaseDialog {

    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;

    public QRCodeDialog(Context context, String content) {
        super(context, R.layout.dialog_qrcode);
        setCancelable(true);

        try {
            ArrayMap<EncodeHintType, Object> option = new ArrayMap<>();
            option.put(EncodeHintType.MARGIN, 2);
            Bitmap bitmap = new BarcodeEncoder().encodeBitmap(content,
                    BarcodeFormat.QR_CODE, 800, 800, option);

            ivQrcode.setImageBitmap(bitmap);
        } catch (Exception ignored) {
        }
    }
}
