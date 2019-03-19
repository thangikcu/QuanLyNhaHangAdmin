package com.coffeehouse.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coffeehouse.R;
import com.coffeehouse.model.entity.WorkingReport;
import com.coffeehouse.util.Utils;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class WorkingReportAdapter extends AbstractTableAdapter<String, String, String> {
    public WorkingReportAdapter(Context context) {
        super(context);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @SuppressLint("SimpleDateFormat")
    public void setData(List<WorkingReport> listWorkingReport) {

        List<String> columnHeaderItems = new ArrayList<>();
        columnHeaderItems.add("Id");
        columnHeaderItems.add("Ngày");
        columnHeaderItems.add("Giờ đến");
        columnHeaderItems.add("Giờ về");
        columnHeaderItems.add("Thời gian làm việc(h)");

        List<String> rowHeaderItems = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        if (listWorkingReport != null) {
            for (int i = 0; i < listWorkingReport.size(); i++) {
                WorkingReport workingReport = listWorkingReport.get(i);

                rowHeaderItems.add((i + 1) + "");
                List<String> dataRow = new ArrayList<>();

                //Id
                dataRow.add(workingReport.getId() + "");
                //Ngày
                dataRow.add(new SimpleDateFormat("dd/MM/yyyy").format(workingReport.getStartDate()));
                //checkIn
                dataRow.add(new SimpleDateFormat("HH:mm:ss").format(workingReport.getStartDate()));

                if (workingReport.isValidate()) {
                    //checkOut
                    dataRow.add(new SimpleDateFormat("HH:mm:ss").format(workingReport.getEndDate()));

                    float differenceTime = Utils.getDifferenceTime(workingReport.getStartDate(), workingReport.getEndDate());
                    //Thời gian làm việc
                    dataRow.add(differenceTime + "");
                } else {
                    dataRow.add("");
                    dataRow.add("");
                }

                rows.add(dataRow);
            }
        }

        setAllItems(columnHeaderItems, rowHeaderItems, rows);
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        return new MyCellViewHolder(LayoutInflater.from(mContext).inflate(R.layout.my_cell_layout,
                parent, false));
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        MyCellViewHolder viewHolder = (MyCellViewHolder) holder;
        viewHolder.cell_textview.setText(cellItemModel.toString());

        viewHolder.cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewHolder.cell_textview.requestLayout();
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        return new MyColumnHeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout
                .table_view_column_header_layout, parent, false));
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        MyColumnHeaderViewHolder columnHeaderViewHolder = (MyColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.column_header_textview.setText(columnHeaderItemModel.toString());

        if (columnPosition == 3) {
            Resources r = holder.itemView.getContext().getResources();
            columnHeaderViewHolder.column_header_container.getLayoutParams().width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    72,
                    r.getDisplayMetrics()
            );
        } else {
            columnHeaderViewHolder.column_header_container.getLayoutParams().width = LinearLayout
                    .LayoutParams.WRAP_CONTENT;
        }
        columnHeaderViewHolder.column_header_textview.requestLayout();
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        return new MyRowHeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout
                .table_view_row_header_layout, parent, false));
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        MyRowHeaderViewHolder rowHeaderViewHolder = (MyRowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderItemModel.toString());
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.table_view_corner_layout, null, false);
    }

    class MyCellViewHolder extends AbstractViewHolder {

        final TextView cell_textview;
        final LinearLayout cell_container;

        MyCellViewHolder(View itemView) {
            super(itemView);
            cell_textview = itemView.findViewById(R.id.cell_data);
            cell_container = itemView.findViewById(R.id.cell_container);
        }
    }

    class MyColumnHeaderViewHolder extends AbstractViewHolder {

        final TextView column_header_textview;
        final View column_header_container;

        MyColumnHeaderViewHolder(View itemView) {
            super(itemView);
            column_header_container = itemView.findViewById(R.id.column_header_container);
            column_header_textview = itemView.findViewById(R.id.column_header_textView);
        }
    }

    class MyRowHeaderViewHolder extends AbstractViewHolder {

        final TextView row_header_textview;

        MyRowHeaderViewHolder(View itemView) {
            super(itemView);
            row_header_textview = itemView.findViewById(R.id.row_header_textview);
        }
    }
}
