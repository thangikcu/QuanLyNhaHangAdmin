package com.coffeehouse.view.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.coffeehouse.AppInstance;
import com.coffeehouse.R;
import com.coffeehouse.interfaces.MainView;
import com.coffeehouse.model.entity.TurnOver;
import com.coffeehouse.restapi.ResfulApi;
import com.coffeehouse.restapi.ResponseData;
import com.coffeehouse.restapi.TheCoffeeService;
import com.coffeehouse.util.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ThongKeFragment extends BaseFragment implements OnDataPointTapListener {

    @BindView(R.id.spn_month)
    Spinner spnMonth;
    @BindView(R.id.spn_year)
    Spinner spnYear;
    @BindView(R.id.layout_month)
    View layoutMonth;
    @BindView(R.id.btn_view)
    Button btnView;
    @BindView(R.id.btn_view_detail)
    TextView btnViewDetail;
    @BindView(R.id.toggle_view_by)
    ToggleButton toggleViewBy;
    @BindView(R.id.graph)
    GraphView graph;
    private MainView mainView;
    private long tongDoanhThu;
    private ArrayList<String> listMonth;
    private ArrayList<String> listYear;

    public ThongKeFragment(MainView mainView) {
        super(R.layout.fragment_thong_ke);
        this.mainView = mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainView.showLoading();
        setCurrentMonth();
        view.postDelayed(this::loadData, 500);
    }

    @Override
    public void loadData() {
        mainView.showLoading();

        boolean viewByYear = toggleViewBy.isChecked();

        String turnOverStatus = viewByYear ? "YEAR" : "MONTH";

        int month = Integer.parseInt(listMonth.get(spnMonth.getSelectedItemPosition()));
        int year = Integer.parseInt(listYear.get(spnYear.getSelectedItemPosition()));

        String time = viewByYear ? (year + "") : (month + "/" + year);

        layoutMonth.setVisibility(viewByYear ? View.INVISIBLE : View.VISIBLE);

        graph.setTitle(String.format("Doanh thu trong %1s %2s", (viewByYear ? "năm" : "tháng"), time));
        graph.setTitleColor(Objects.requireNonNull(getContext()).getColor(R.color.colorPrimary));

        tongDoanhThu = 0;
        ResfulApi.getInstance().getService(TheCoffeeService.class)
                .getTurnOver(time, turnOverStatus)
                .enqueue(new Callback<ResponseData<List<TurnOver>>>() {
                    @Override
                    public void onResponse(Call<ResponseData<List<TurnOver>>> call,
                                           Response<ResponseData<List<TurnOver>>> response) {
                        mainView.hideLoading();
                        if (response.body() != null && response.body().getContent() != null) {
                            List<TurnOver> turnOverList = response.body().getContent();

                            List<DataPoint> dataPointList = new ArrayList<>();
                            List<DataPoint> dataPointList2 = new ArrayList<>();

                            for (int i = 0; i < turnOverList.size(); i++) {
                                TurnOver turnOver = turnOverList.get(i);
                                dataPointList.add(new DataPoint(turnOver.getName(), turnOver.getTurnOver()));
                                if (turnOver.getTurnOver() > 0) {
                                    dataPointList2.add(new DataPoint(turnOver.getName(), turnOver.getTurnOver()));
                                    tongDoanhThu += turnOver.getTurnOver();
                                }
                            }

                            DataPoint[] points = dataPointList.toArray(new DataPoint[0]);

                            LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(points);
                            lineGraphSeries.setDrawDataPoints(true);
                            lineGraphSeries.setDrawBackground(true);
                            lineGraphSeries.setAnimated(true);
                            lineGraphSeries.setOnDataPointTapListener(ThongKeFragment.this);

                            BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPointList2.toArray(new DataPoint[0]));
                            barGraphSeries.setDrawValuesOnTop(true);
                            barGraphSeries.setValuesOnTopColor(Color.BLACK);
                            barGraphSeries.setDataWidth(0.1f);
                            barGraphSeries.setColor(Color.TRANSPARENT);

                            graph.removeAllSeries();
//                            graph.setCursorMode(true);
                            graph.addSeries(lineGraphSeries);
                            graph.addSeries(barGraphSeries);

                            graph.getViewport().setScalable(true);
                            graph.getViewport().setScalableY(true);
                            graph.getViewport().calcCompleteRange();
                        } else {
                            graph.removeAllSeries();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<List<TurnOver>>> call, Throwable t) {
                        mainView.hideLoading();
                        mainView.showMessage("Error!");
                        graph.removeAllSeries();
                    }
                });
    }

    @Override
    public void onTap(Series series, DataPointInterface dataPoint) {
        boolean viewByYear = toggleViewBy.isChecked();
        String info = ((int) dataPoint.getX()) + ": " + Utils.formatMoneyToVnd(dataPoint.getY());
        showSnackbar((viewByYear ? "Tháng " : "Ngày ") + info);
    }

    public void showSnackbar(String message) {
        if (!TextUtils.isEmpty(message)) {
            final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View viewSnackbar = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewSnackbar.getLayoutParams();
            params.width = AppInstance.getContext().getResources().getDisplayMetrics().widthPixels / 2;
            viewSnackbar.setLayoutParams(params);
            viewSnackbar.setOnClickListener(v -> snackbar.dismiss());
            snackbar.show();
        }
    }

    @Override
    public void initComponents() {
        listMonth = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            listMonth.add(i + "");
        }

        ArrayAdapter monthAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                listMonth);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonth.setAdapter(monthAdapter);


        listYear = new ArrayList<>();

        int startYear = 2017;
        int endYear = 2024;
        for (int i = startYear; i <= endYear; i++) {
            listYear.add(i + "");
        }

        ArrayAdapter yearAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                listYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(yearAdapter);
    }

    private void setCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        spnMonth.setSelection(listMonth.indexOf((calendar.get(Calendar.MONTH) + 1) + ""));
        spnYear.setSelection(listYear.indexOf(calendar.get(Calendar.YEAR) + ""));
    }

    private void openViewDetail() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        boolean viewByYear = toggleViewBy.isChecked();
        String turnOverStatus = viewByYear ? "YEAR" : "MONTH";

        int month = Integer.parseInt(listMonth.get(spnMonth.getSelectedItemPosition()));
        int year = Integer.parseInt(listYear.get(spnYear.getSelectedItemPosition()));

        String time = viewByYear ? (year + "") : (month + "/" + year);

        fragmentTransaction.add(R.id.container, new ChiTietThongKeFragment(mainView, tongDoanhThu, turnOverStatus, time,
                () -> getChildFragmentManager().popBackStack()), "thong_ke_detail")
                .commitAllowingStateLoss();
        fragmentTransaction.addToBackStack("thong_ke_detail");
    }

    @Override
    public void setEvents() {
        toggleViewBy.setOnCheckedChangeListener((compoundButton, b) -> loadData());
        btnView.setOnClickListener(view1 -> loadData());
        btnViewDetail.setPaintFlags(btnViewDetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnViewDetail.setOnClickListener(view1 -> openViewDetail());
    }
}
