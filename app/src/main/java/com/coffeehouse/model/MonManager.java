package com.coffeehouse.model;

import android.os.AsyncTask;

import com.coffeehouse.model.entity.Mon;
import com.coffeehouse.model.entity.NhomMon;
import com.coffeehouse.presenter.MainPresenter;
import com.coffeehouse.view.dialog.AddDrinkDialog;
import com.coffeehouse.view.fragment.manager.DrinkManagerFragment;

import java.util.ArrayList;

/**
 * Created by Thanggun99 on 23/03/2017.
 */

public class MonManager {

    private Database database;
    private MainPresenter mainPresenter;
    private MonManagerView monManagerView;
    private DrinkManagerFragment fragment;
    private Mon currentMon;
    private AddDrinkDialog addDrinkDialog;

    public MonManager(MainPresenter mainPresenter) {

        this.database = mainPresenter.getDatabase();
        this.mainPresenter = mainPresenter;
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMonManagerView(MonManagerView monManagerView) {
        this.monManagerView = monManagerView;
    }

    public ArrayList<Mon> findMon(String keyWord) {
        return database.getListMonByTenMon(keyWord);
    }

    public DrinkManagerFragment getFragment() {
        return fragment;
    }

    public void setFragment(DrinkManagerFragment fragment) {
        this.fragment = fragment;
    }

    public void addMon(final Mon mon) {
        class AddTinTucTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected void onPreExecute() {
                mainPresenter.onStartTask();
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                delay(500);

                return mon.addNew();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    database.addMon(mon);
                    monManagerView.onFinishAddMonSuccess();
                } else {
                    monManagerView.onAddMonFail();
                }
                mainPresenter.onFinishTask();
            }
        }
        if (mainPresenter.checkConnect()) {

            new AddTinTucTask().execute();
        }
    }

    public void deleteMon(final Mon mon) {

        class DeleteTinTucTask extends AsyncTask<Void, Void, Boolean> {

            @Override
            protected void onPreExecute() {
                mainPresenter.onStartTask();
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                delay(500);
                return mon.delete();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    database.deleteMon(mon);
                    monManagerView.onFinishDeleteMonSuccess();
                } else {
                    monManagerView.onDeleteMonFail();
                }
                mainPresenter.onFinishTask();
            }
        }
        if (mainPresenter.checkConnect()) {

            new DeleteTinTucTask().execute();
        }

    }

    public void updateMon(final Mon mon) {

        class UpdateTinTucTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected void onPreExecute() {
                mainPresenter.onStartTask();
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                delay(500);
                return currentMon.updateMon(mon);

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    monManagerView.onFinishUpdateMon();
                } else {
                    monManagerView.onUpdateMonFail();
                }
                mainPresenter.onFinishTask();
            }
        }
        if (mainPresenter.checkConnect()) {

            new UpdateTinTucTask().execute();
        }
    }

    public void setCurrentMon(Mon currentMon) {
        this.currentMon = currentMon;
    }

    public Mon getCurrentMon() {
        return currentMon;
    }

    public AddDrinkDialog getAddDrinkDialog() {
        return addDrinkDialog;
    }


    public void setAddDrinkDialog(AddDrinkDialog addDrinkDialog) {
        this.addDrinkDialog = addDrinkDialog;
    }

    public ArrayList<Mon> getMonList() {
        return database.getMonList();
    }

    public ArrayList<NhomMon> getNhomMonList() {
        return database.getNhomMonList();
    }

    public ArrayList<Mon> getMonByMaLoai(int maLoai) {
        return database.getListMonByMaLoai(maLoai);
    }

    public interface MonManagerView {

        void onFinishAddMonSuccess();

        void onAddMonFail();

        void onDeleteMonFail();

        void onFinishDeleteMonSuccess();

        void onFinishUpdateMon();

        void onUpdateMonFail();
    }

}
