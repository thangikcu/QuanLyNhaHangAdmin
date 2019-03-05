package com.coffeehouse.presenter;

import com.coffeehouse.R;
import com.coffeehouse.interfaces.ShowOnMain;
import com.coffeehouse.model.Database;
import com.coffeehouse.model.MainInteractor;
import com.coffeehouse.util.Utils;

/**
 * Created by Thanggun99 on 07/03/2017.
 */

public class MainPresenter implements MainInteractor.OnMainInteractorFinishListener {
    private MainView mainview;
    private ChangepasswordView changepasswordView;
    private MainInteractor mainInteractor;

    public MainPresenter(MainView mainview, Database database) {

        this.mainview = mainview;
        mainInteractor = new MainInteractor(this, database);
    }

    //check connection
    public boolean checkConnect() {
        if (Utils.isConnectingToInternet()) {
            return true;
        } else {
            mainview.showNotifyDialog(Utils.getStringByRes(R.string.kiem_tra_ket_noi_mang));
            return false;
        }
    }

    public void getDatas() {

        mainInteractor.getDatas();

    }

    @Override
    public void onFinishGetDatas() {
        mainview.updateFloatButton(getDatabase().getYeuCauList().size());
        mainview.setYeuCauList();
        mainview.showContent();
    }

    @Override
    public void onGetDatasFail() {
        mainview.showGetDatasFailDialog();
    }

    @Override
    public void onStartTask() {
        mainview.showProgress();
    }

    @Override
    public void onFinishTask() {
        mainview.hideProgress();
    }

    public void onClickChangePassword() {
        mainview.showChangePasswordDialog();
    }

    public void changePassWord(String password, String passwordNew) {
        if (checkConnect()) {
            mainInteractor.changePassword(password, passwordNew);
        }
    }

    @Override
    public void onChangePasswordSucess() {
        changepasswordView.showOnsuccess();
    }

    @Override
    public void onChangePasswordFail() {
        changepasswordView.showOnFail();
    }

    @Override
    public void passwordWrong() {
        changepasswordView.showPasswordWrong();
    }

    public void setChangepasswordView(ChangepasswordView changepasswordView) {
        this.changepasswordView = changepasswordView;
    }

    public void otherLogin() {
        if (getDatabase().getAdmin() != null) {

            mainview.showOtherLoginDialog();
            logout();
        }
    }

    public void logout() {
        mainInteractor.logout();
        mainview.removeFloatButton();
        mainview.showLogin();
    }

    public Database getDatabase() {
        return mainInteractor.getDatabase();
    }

    public void showConnectToServerFailDialog() {
        mainview.showNotifyDialog(Utils.getStringByRes(R.string.mat_ket_noi_toi_may_chu));
    }

    public void reLoadDatas() {
        mainview.clearFrames();
        mainInteractor.reloadDatas();
    }

    @Override
    public void onLoginFail() {
        logout();
    }

    @Override
    public void onOtherlogin() {
        otherLogin();
    }


    public interface MainView extends ShowOnMain {

        void showGetDatasFailDialog();

        void showContent();

        void showChangePasswordDialog();

        void showLogin();

        void setYeuCauList();

        void clearFrames();

        void showOtherLoginDialog();
    }


    public interface ChangepasswordView {

        void showOnsuccess();

        void showPasswordWrong();

        void showOnFail();

    }
}
