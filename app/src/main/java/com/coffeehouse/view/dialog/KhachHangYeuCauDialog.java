package com.coffeehouse.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import com.coffeehouse.R;
import com.coffeehouse.adapter.MonKhachHangYeuCauAdapter;
import com.coffeehouse.adapter.YeuCauAdapter;
import com.coffeehouse.model.entity.Ban;
import com.coffeehouse.model.entity.YeuCau;
import com.coffeehouse.presenter.PhucVuPresenter;


public class KhachHangYeuCauDialog extends BaseDialog implements PhucVuPresenter.YeuCauView {

    @BindView(R.id.list_yeu_cau)
    RecyclerView listYeuCau;
    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.layout_yeu_cau)
    LinearLayout layoutYeuCau;
    @BindView(R.id.spn_ban)
    Spinner spnBan;
    @BindView(R.id.tv_ten_ban)
    TextView tvTenBan;
    @BindView(R.id.tv_ten_khach_hang)
    TextView tvTenKhachHang;
    @BindView(R.id.tv_so_dien_thoai)
    TextView tvSoDienThoai;
    @BindView(R.id.list_mon_order)
    RecyclerView listMonOrder;
    @BindView(R.id.layout_chi_tiet_yeu_cau)
    LinearLayout layoutChiTietYeuCau;

    private PhucVuPresenter phucVuPresenter;

    private YeuCauAdapter yeuCauAdapter;

    private MonKhachHangYeuCauAdapter monKhachHangYeuCauAdapter;
    private ArrayList<Ban> banList;
    private ArrayAdapter<String> banAdapter;
    private Ban ban;

    @SuppressLint("NewApi")
    public KhachHangYeuCauDialog(Context context, PhucVuPresenter phucVuPresenter) {
        super(context, R.layout.dialog_khach_hang_yeu_cau);
        this.phucVuPresenter = phucVuPresenter;
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        setCancelable(true);

        layoutChiTietYeuCau.setVisibility(View.GONE);

        listMonOrder.setLayoutManager(new LinearLayoutManager(context));
        monKhachHangYeuCauAdapter = new MonKhachHangYeuCauAdapter(context);
        listMonOrder.setAdapter(monKhachHangYeuCauAdapter);

        banList = new ArrayList<>();
        banAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, banList);

        banAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBan.setAdapter(banAdapter);
        spnBan.setDropDownWidth(250);

        layoutYeuCau.setVisibility(View.VISIBLE);

        btnClose.setOnClickListener(this);

        listYeuCau.setLayoutManager(new LinearLayoutManager(context));

        yeuCauAdapter = new YeuCauAdapter(context, phucVuPresenter);
        listYeuCau.setAdapter(yeuCauAdapter);

        phucVuPresenter.setYeuCauView(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (!tvTenBan.isShown()) {
                    ban = banList.get(spnBan.getSelectedItemPosition());
                }
                phucVuPresenter.khachHangOrderMon(ban);
                dismiss();
                break;
            case R.id.btn_cancel:
                layoutChiTietYeuCau.setVisibility(View.GONE);
                layoutYeuCau.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void showChiTietYeuCau(YeuCau yeuCau) {

        monKhachHangYeuCauAdapter.changeData(yeuCau.getMonYeuCauList());

        tvTenKhachHang.setText(yeuCau.getKhachHang().getHoTen());
        tvSoDienThoai.setText(yeuCau.getKhachHang().getSoDienThoai());

        layoutYeuCau.setVisibility(View.GONE);
        layoutChiTietYeuCau.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBanList(ArrayList<Ban> banList) {
        this.banList.clear();
        for (Ban ban : banList) {
            if (ban.getTrangThai() == 0) {
                this.banList.add(ban);
            }
        }
        banAdapter.notifyDataSetChanged();

        tvTenBan.setVisibility(View.GONE);
        spnBan.setVisibility(View.VISIBLE);

    }

    @Override
    public void showBan(Ban ban) {
        this.ban = ban;
        tvTenBan.setText(ban.getTenBan());

        tvTenBan.setVisibility(View.VISIBLE);
        spnBan.setVisibility(View.INVISIBLE);
    }

    @Override
    public void notifyDatachange() {
        if (yeuCauAdapter != null) {

            yeuCauAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyYeuCauChange(YeuCau yeuCau) {
        if (yeuCauAdapter != null) {

            yeuCauAdapter.notifyItemChanged(yeuCau);
            layoutChiTietYeuCau.setVisibility(View.GONE);
            layoutYeuCau.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyRemoveYeuCau(YeuCau yeuCau) {
        if (yeuCauAdapter != null) {

            yeuCauAdapter.notifyItemRemoved(yeuCau);
            layoutChiTietYeuCau.setVisibility(View.GONE);
            layoutYeuCau.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void notifyAddYeuCau() {

        yeuCauAdapter.notifyItemInserted(0);
        layoutChiTietYeuCau.setVisibility(View.GONE);
        layoutYeuCau.setVisibility(View.VISIBLE);
    }
}
