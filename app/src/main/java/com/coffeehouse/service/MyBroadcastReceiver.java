package com.coffeehouse.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.DatBan;
import com.coffeehouse.model.entity.KhachHang;
import com.coffeehouse.model.entity.YeuCau;
import com.coffeehouse.presenter.MainPresenter;
import com.coffeehouse.presenter.PhucVuPresenter;
import com.coffeehouse.util.Utils;


/**
 * Created by Thanggun99 on 06/04/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private PhucVuPresenter phucVuPresenter;
    private MainPresenter mainPresenter;

    public MyBroadcastReceiver(PhucVuPresenter phucVuPresenter, MainPresenter mainPresenter) {
        this.phucVuPresenter = phucVuPresenter;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case ConnectChangeBroadcastReceiver.CONNECT_FAIL:
                if (mainPresenter != null) {

                    mainPresenter.showConnectToServerFailDialog();
                }
                break;
            case ConnectChangeBroadcastReceiver.CONNECT_AVAILABLE:
                if (mainPresenter != null) {

                    mainPresenter.reLoadDatas();
                }
                break;
            case MyFirebaseMessagingService.DAT_BAN_ACTION:
                if (phucVuPresenter != null) {
                    DatBan datBan = (DatBan) intent.getSerializableExtra(MyFirebaseMessagingService.DAT_BAN);

                    phucVuPresenter.datBanService(datBan);

                    Utils.showNotify(Utils.getStringByRes(R.string.khach_hang_dat_ban),
                            intent.getStringExtra(MyFirebaseMessagingService.TEN_KHACH_HANG));
                }
                break;
            case MyFirebaseMessagingService.HUY_DAT_BAN_ACTION:
                if (phucVuPresenter != null) {
                    phucVuPresenter.huyDatBanService(intent.getIntExtra(MyFirebaseMessagingService.MA_DAT_BAN, 0));

                    Utils.showNotify(Utils.getStringByRes(R.string.khach_hang_huy_dat_ban),
                            intent.getStringExtra(MyFirebaseMessagingService.TEN_KHACH_HANG));
                }
                break;
            case MyFirebaseMessagingService.UPDATE_DAT_BAN_ACTION:
                if (phucVuPresenter != null) {
                    DatBan datBanUpdate = (DatBan) intent.getSerializableExtra(MyFirebaseMessagingService.DAT_BAN);

                    phucVuPresenter.updateDatBanService(datBanUpdate);

                    Utils.showNotify(Utils.getStringByRes(R.string.update_dat_ban),
                            intent.getStringExtra(MyFirebaseMessagingService.TEN_KHACH_HANG));
                }
                break;
            case MyFirebaseMessagingService.KHACH_VAO_BAN_ACTION:
                if (phucVuPresenter != null) {
                    DatBan datBanVaoBan = (DatBan) intent.getSerializableExtra(MyFirebaseMessagingService.DAT_BAN);

                    phucVuPresenter.khachVaoBanService(datBanVaoBan);

                    Utils.showNotify(datBanVaoBan.getBan().getTenBan(), Utils.getStringByRes(R.string.khach_vao_ban));
                }
                break;
            case MyFirebaseMessagingService.KHACH_HANG_REGISTER_ACTION:
                if (phucVuPresenter != null) {
                    KhachHang khachHang = (KhachHang) intent.getSerializableExtra(MyFirebaseMessagingService.KHACH_HANG);

                    phucVuPresenter.addKhachHang(khachHang);

                    Utils.showLog(khachHang.getMaKhachHang() + khachHang.getHoTen() + khachHang.getDiaChi()
                            + khachHang.getSoDienThoai() + khachHang.getTenDangNhap()
                            + khachHang.getMatKhau() + khachHang.getMaToken());
                }
                break;
            case MyFirebaseMessagingService.KHACH_HANG_YEU_CAU_ACTION:
                if (phucVuPresenter != null) {
                    YeuCau yeuCau = (YeuCau) intent.getSerializableExtra(MyFirebaseMessagingService.YEU_CAU);

                    phucVuPresenter.khachHangYeuCauService(yeuCau);

                    Utils.showNotify(Utils.getStringByRes(R.string.khach_hang_yeu_cau),
                            intent.getStringExtra(MyFirebaseMessagingService.TEN_KHACH_HANG));
                }

                break;
            case MyFirebaseMessagingService.LOGOUT_ACTION:
                mainPresenter.otherLogin();
                break;
            default:
                break;
        }
    }
}
