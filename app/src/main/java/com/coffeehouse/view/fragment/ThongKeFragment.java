package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.coffeehouse.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
    @BindView(R.id.graph)
    GraphView graph;

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
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }



}
