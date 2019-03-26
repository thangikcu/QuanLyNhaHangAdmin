package com.coffeehouse.view.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coffeehouse.R;
import com.coffeehouse.adapter.ReceiptDetailAdapter;
import com.coffeehouse.model.entity.GoodReceiptDetail;

import java.util.List;

import butterknife.BindView;

public class AddReceiptDialog extends BaseDialog {

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_unit)
    EditText edtAmount;
    @BindView(R.id.edt_don_gia)
    EditText edtDonGia;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ReceiptDetailAdapter receiptDetailAdapter;

    private OnClickOkListener onClickOkListener;

    public AddReceiptDialog(Context context) {
        super(context, R.layout.dialog_add_receipt);
        setCancelable(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(receiptDetailAdapter = new ReceiptDetailAdapter(null));
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_add) {
            if (checkForm()) {
                GoodReceiptDetail receiptDetail = new GoodReceiptDetail();

                receiptDetail.setName(edtName.getText().toString().trim());
                receiptDetail.setAmount(edtAmount.getText().toString().trim());
                receiptDetail.setPrice(Integer.parseInt(edtDonGia.getText().toString().trim()));

                receiptDetailAdapter.addItem(receiptDetail);

                edtName.setText("");
                edtAmount.setText("");
                edtDonGia.setText("");
            }
        } else if (v.getId() == R.id.btn_ok) {
            if (receiptDetailAdapter.getItemCount() > 0) {
                onClickOkListener.onClickOk(receiptDetailAdapter.getListItem());
                dismiss();
            }
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtDonGia.getText())) {
            cancel = true;
            focusView = edtDonGia;
            edtDonGia.setError("Nhập đơn giá");
        }
        if (TextUtils.isEmpty(edtAmount.getText())) {
            cancel = true;
            focusView = edtAmount;
            edtAmount.setError("Nhập đơn vị");
        }
        if (TextUtils.isEmpty(edtName.getText())) {
            cancel = true;
            focusView = edtName;
            edtName.setError("Nhập tên");
        }
        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    public interface OnClickOkListener {
        void onClickOk(List<GoodReceiptDetail> receiptDetailList);
    }
}
