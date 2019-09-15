package com.yadanar.carrentalservice.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.Customer;

import java.util.Calendar;
import java.util.Date;

import static com.yadanar.carrentalservice.util.DateUtil.displayDateOnlyFormat;
import static com.yadanar.carrentalservice.util.DateUtil.displayTimeOnlyFormat;
import static com.yadanar.carrentalservice.util.UiUtil.getText_;
import static com.yadanar.carrentalservice.util.UiUtil.setError;

public class BookingActivity extends AppCompatActivity {

    private int mYear,
            mMonth,
            mDay,
            mHour,
            mMinute;

    private EditText edtName,
            edtId,
            edtEmail,
            edtPhone,
            edtAddress;
    private AppCompatButton btnStartDate,
            btnStartTime;
    private AppCompatRadioButton rdMale,
            rdFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        edtName = findViewById(R.id.edt_name);
        edtId = findViewById(R.id.edt_id);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        btnStartDate = findViewById(R.id.btn_start_date);
        btnStartTime = findViewById(R.id.btn_start_time);
        rdMale = findViewById(R.id.rd_male);
        rdFemale = findViewById(R.id.rd_female);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        btnStartDate.setText(displayDateOnlyFormat(getDate()));
        btnStartTime.setText(displayTimeOnlyFormat(getDate()));
    }

    public void openDatePickerDialog(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear + 1;
                        mDay = dayOfMonth;

                        btnStartDate.setText(displayDateOnlyFormat(getDate()));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void openTimePickerDialog(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        btnStartTime.setText(displayTimeOnlyFormat(getDate()));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void submitForm(View v) {
        Customer customer = new Customer();
        customer.setName(getText_(edtName));
        customer.setId(getText_(edtId));
        customer.setEmail(getText_(edtEmail));
        customer.setPhone(getText_(edtPhone));
        customer.setAddress(getText_(edtAddress));
        customer.setGender(rdMale.isChecked() ? "Male" : "Female");
        customer.setDate(getDate().getTime());

        if (TextUtils.isEmpty(customer.getName())) {
            setError(edtName);
            return;
        }

        if (TextUtils.isEmpty(customer.getId())) {
            setError(edtId);
            return;
        }

        if (TextUtils.isEmpty(customer.getEmail())) {
            setError(edtEmail);
            return;
        }

        if (TextUtils.isEmpty(customer.getPhone())) {
            setError(edtPhone);
            return;
        }

        if (TextUtils.isEmpty(customer.getAddress())) {
            setError(edtAddress);
            return;
        }

        // TODO: 9/8/19 save customer data to database

        finish();
    }

    public void cancelBooking(View v) {
        finish();
    }

    private Date getDate() {
        final Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, mDay, mHour, mMinute);
        return c.getTime();
    }
}
