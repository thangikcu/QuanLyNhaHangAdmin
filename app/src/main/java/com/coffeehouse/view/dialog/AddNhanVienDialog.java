package com.coffeehouse.view.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coffeehouse.R;
import com.coffeehouse.model.entity.User;
import com.coffeehouse.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

public class AddNhanVienDialog extends BaseDialog implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.edt_fullname)
    EditText edtFullname;
    @BindView(R.id.edt_dob)
    EditText edtDob;
    @BindView(R.id.edt_so_dien_thoai)
    EditText edtSoDienThoai;
    @BindView(R.id.edt_salary)
    EditText edtSalary;
    @BindView(R.id.edt_work_hours)
    EditText edtWorkHours;
    @BindView(R.id.tv_chon_hinh)
    TextView tvChonHinh;
    @BindView(R.id.tv_age)
    TextView tvAge;
/*
    @BindView(R.id.spn_rule)
    Spinner spnRule;
*/

    //    private ArrayList<String> ruleList;
    private User user;
    private ArrayAdapter<String> nhomMonAdapter;
    private byte[] hinhAnhByte;
    private PickImageRequest pickImageRequest;
    private OnClickOkListener onClickOkListener;

    public AddNhanVienDialog(Context context) {
        super(context, R.layout.dialog_add_nhan_vien);
        setCancelable(true);

   /*     ruleList = new ArrayList<>();
        ruleList.add(User.EMPLOYEE);
        ruleList.add(User.MANAGER);

        nhomMonAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, ruleList);
        nhomMonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnRule.setAdapter(nhomMonAdapter);*/
    }

    public AddNhanVienDialog(Context context, User user) {
        this(context);
        this.user = user;

        hinhAnhByte = user.getImageToShow();

        //spnRule.setEnabled(false);

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(user.getDob());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            tvAge.setText("Tuổi: " + Period.between(LocalDate.of(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)),
                    LocalDate.now()).getYears());
        }

        Glide.with(getContext())
                .load(user.getImageToShow())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(ivAvatar);

        edtFullname.setText(user.getFullName());
        edtDob.setText(user.getDob());
        edtSoDienThoai.setText(user.getPhoneNumber());
        edtWorkHours.setText(user.getWorkingHours() + "");
        edtSalary.setText(user.getSalary() + "");

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_ok) {
            if (checkForm()) {
                User user = new User();

                if (this.user != null) {
                    user.setID(this.user.getID());
                } else {
                    user.setID(UUID.randomUUID().toString().substring(0, 19));
                }

                user.setDob(edtDob.getText().toString().trim());

                user.setAvatarData(Utils.getStringImage(hinhAnhByte));
                user.setFullName(edtFullname.getText().toString().trim());
                user.setPhoneNumber(edtSoDienThoai.getText().toString().trim());
                user.setWorkingHours(Integer.parseInt(edtWorkHours.getText().toString().trim()));
                user.setSalary(Integer.parseInt(edtSalary.getText().toString().trim()));
//                user.setRule(ruleList.get(spnRule.getSelectedItemPosition()));

                dismiss();
                onClickOkListener.onClickOk(user);
            }
        }
    }

    private boolean checkForm() {
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(edtSalary.getText())) {
            cancel = true;
            focusView = edtSalary;
            edtSalary.setError("Nhập mức lương");
        }
        if (TextUtils.isEmpty(edtWorkHours.getText())) {
            cancel = true;
            focusView = edtWorkHours;
            edtWorkHours.setError("Nhập Số giờ làm việc mỗi ngày");
        }
        if (TextUtils.isEmpty(edtSoDienThoai.getText())) {
            cancel = true;
            focusView = edtSoDienThoai;
            edtSoDienThoai.setError("Nhập số điện thoại");
        }
        if (TextUtils.isEmpty(edtDob.getText())) {
            cancel = true;
            focusView = edtDob;
            edtDob.setError("Nhập ngày sinh");
        }
        if (TextUtils.isEmpty(edtFullname.getText())) {
            cancel = true;
            focusView = edtFullname;
            edtFullname.setError("Nhập họ tên");
        }
        if (hinhAnhByte == null) {
            cancel = true;
            focusView = tvChonHinh;
            tvChonHinh.setError(Utils.getStringByRes(R.string.chon_hinh));
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    private void showImage(Bitmap bitmap) {
        //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        if (bitmap != null) {
            hinhAnhByte = Utils.getByteImage(bitmap);

            Glide.with(getContext())
                    .load(hinhAnhByte)
                    .into(ivAvatar);
        }

    }

    public void setPickImageRequest(PickImageRequest pickImageRequest) {
        this.pickImageRequest = pickImageRequest;
    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    @OnClick(R.id.btn_chon_hinh)
    public void onViewClicked() {
        pickImageRequest.pickImage(this::showImage);
    }

    @OnClick(R.id.edt_dob)
    public void onClickDob() {
        Calendar calendar = Calendar.getInstance();

        if (!TextUtils.isEmpty(edtDob.getText().toString().trim())) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(edtDob.getText().toString().trim());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        edtDob.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

        tvAge.setText("Tuổi: " + Period.between(LocalDate.of(year, month + 1, day), LocalDate.now()).getYears());
    }

    public interface OnClickOkListener {
        void onClickOk(User drink);
    }

    public interface PickImageRequest {
        void pickImage(OnPickImageResult onResult);
    }

    public interface OnPickImageResult {
        void onResult(Bitmap result);
    }
}
