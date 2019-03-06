package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coffeehouse.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ThongKeFragment extends BaseFragment {

    @BindView(R.id.tv_so_luong_hoa_don_chua_tinh_tien)
    TextView tvSoLuongHoaDonChuaTinhTien;
    @BindView(R.id.tv_so_luong_hoa_don_tinh_tien)
    TextView tvSoLuongHoaDonTinhTien;
    @BindView(R.id.tv_tong_tien)
    TextView tvTongTien;
    @BindView(R.id.tv_so_luong_ban_dat_truoc)
    TextView tvSoLuongBanDatTruoc;
    @BindView(R.id.tv_so_luong_khach_hang)
    TextView tvSoLuongKhachHang;
    @BindView(R.id.tv_so_luong_nhom_mon)
    TextView tvSoLuongNhomMon;
    @BindView(R.id.tv_so_luong_mon)
    TextView tvSoLuongMon;
    @BindView(R.id.layout_thong_tin_dat_ban)
    LinearLayout layoutThongTinDatBan;

    public ThongKeFragment() {
        super(R.layout.fragment_thong_ke);

    }

    @Override
    public void findViews(View view) {
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {
        showInfo();
    }

    public void showInfo() {
     /*   tvSoLuongHoaDonChuaTinhTien.setText(database.getHoaDonChuaTinhTienList().size() + " hoá đơn");
        tvSoLuongHoaDonTinhTien.setText(hoaDonList.size() + " hoá đơn");
        tvSoLuongKhachHang.setText(database.getKhachHangList().size() + " khách hàng");
        tvSoLuongMon.setText(database.getMonList().size() + " món");
        tvSoLuongNhomMon.setText(database.getNhomMonList().size() + " nhóm");
        tvSoLuongDatBan.setText((database.getDatBanChuaSetBanList().size() + database.getDatBanChuaTinhTienList().size()) + " đặt trước");*/

//        tvTongTien.setText(Utils.formatMoney(tongTien));
    }


}
