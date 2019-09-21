package com.yadanar.carrentalservice.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.RentedCar;
import com.yadanar.carrentalservice.util.DateUtil;
import com.yadanar.carrentalservice.util.NumberUtil;

import java.util.Calendar;
import java.util.Date;

public class CheckOutActivity extends AppCompatActivity {
    private RentedCar rentedCar = null;
    private int mYear,
            mMonth,
            mDay,
            mHour,
            mMinute;

    private AppCompatButton btnStartTime,
            btnEndTime;
    private TextView tvCustomer,
            tvCarType,
            tvPrice,
            tvYear,
            tvSeat,
            tvColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        btnStartTime = findViewById(R.id.btn_start_time);
        btnEndTime = findViewById(R.id.btn_end_time);
        tvCustomer = findViewById(R.id.tv_customer);
        tvCarType = findViewById(R.id.tv_car_type);
        tvPrice = findViewById(R.id.tv_price);
        tvYear = findViewById(R.id.tv_year);
        tvSeat = findViewById(R.id.tv_seats);
        tvColor = findViewById(R.id.tv_color);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(DashboardActivity.KEY_RENTED_CAR_PARAM)
                && b.containsKey(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM)) {
            rentedCar = (RentedCar) b.get(DashboardActivity.KEY_RENTED_CAR_PARAM);
            rentedCar.getCar().setTypeName(b.getString(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM));

            if (rentedCar != null) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                btnStartTime.setText(DateUtil.displayDateAndTimeFormat(new Date(rentedCar.getCustomer().getDate())));
                btnEndTime.setText(DateUtil.displayDateAndTimeFormat(getEndDate()));
                tvCustomer.setText(rentedCar.getCustomer().getName());
                tvCarType.setText(rentedCar.getCar().getTypeName());
                tvPrice.setText(String.valueOf(rentedCar.getCar().getPrice()));
                tvYear.setText(String.valueOf(rentedCar.getCar().getYear()));
                tvSeat.setText(String.valueOf(rentedCar.getCar().getSeats()));
                tvColor.setText(rentedCar.getCar().getColor());
            }
        }
    }

    public void openCalculateAndReturnDialog(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_amount_and_return);

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        AppCompatTextView tvTotalDuration = dialog.findViewById(R.id.tv_total_duration);
        AppCompatTextView tvTotalAmount = dialog.findViewById(R.id.tv_total_amount);
        AppCompatButton btnReturn = dialog.findViewById(R.id.btn_return);

        tvTitle.setText(rentedCar.getCar().getTypeName());

        double diffHours = NumberUtil.getOneDigit(DateUtil.hoursDifference(getEndDate(), new Date(rentedCar.getCustomer().getDate())));

        tvTotalDuration.setText(diffHours + " hr");
        tvTotalAmount.setText(rentedCar.getCar().getPrice() * diffHours + " ks");

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 9/15/19 delete from dashboard list

                dialog.dismiss();

                finish();
            }
        });

        dialog.show();
    }

    public void openDatePickerDialog(final View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        openTimePickerDialog(v);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(rentedCar.getCustomer().getDate());
        datePickerDialog.show();
    }

    public void openTimePickerDialog(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int bkHour = mHour;
                        int bkMinute = mMinute;

                        mHour = hourOfDay;
                        mMinute = minute;

                        if (getEndDate().getTime() < rentedCar.getCustomer().getDate()) {
                            Toast.makeText(CheckOutActivity.this,
                                    "Reverse date cannot be apply",
                                    Toast.LENGTH_SHORT).show();

                            mHour = bkHour;
                            mMinute = bkMinute;

                            return;
                        }

                        btnEndTime.setText(DateUtil.displayDateAndTimeFormat(getEndDate()));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private Date getEndDate() {
        final Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, mDay, mHour, mMinute);
        return c.getTime();
    }
}
