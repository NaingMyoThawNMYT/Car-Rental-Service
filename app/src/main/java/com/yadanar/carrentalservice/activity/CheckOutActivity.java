package com.yadanar.carrentalservice.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.RentedCar;
import com.yadanar.carrentalservice.storage.FirebaseHelper;
import com.yadanar.carrentalservice.util.DateUtil;
import com.yadanar.carrentalservice.util.NumberUtil;
import com.yadanar.carrentalservice.util.UiUtil;

import java.util.Calendar;
import java.util.Date;

public class CheckOutActivity extends AppCompatActivity {
    public static final String KEY_RETURN_DATE_PARAM = "key_return_date_param";
    public static final String KEY_TOTAL_DURATION = "key_total_duration_param";
    public static final String KEY_TOTAL_AMOUNT = "key_total_amount_param";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference customerTable = db.getReference().child(FirebaseHelper.CUSTOMER_LIST_TABLE_NAME);
    private DatabaseReference carTable = db.getReference().child(FirebaseHelper.CAR_LIST_TABLE_NAME);

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
        AppCompatButton btnGenerateInvoice = dialog.findViewById(R.id.btn_generate_invoice);

        tvTitle.setText(rentedCar.getCar().getTypeName());

        final double diffHours = NumberUtil.getOneDigit(DateUtil.hoursDifference(getEndDate(), new Date(rentedCar.getCustomer().getDate())));
        final String duration = diffHours + " hr";
        final String amount = rentedCar.getCar().getPrice() * diffHours + " ks";

        tvTotalDuration.setText(duration);
        tvTotalAmount.setText(amount);

        btnGenerateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtil.checkConnectionAndFinishActivity(CheckOutActivity.this);

                customerTable.child(rentedCar.getCustomer().getId()).removeValue();

                carTable.child(rentedCar.getCar().getId()).child("available").setValue(true);

                dialog.dismiss();

                Intent i = new Intent(CheckOutActivity.this,
                        GenerateInvoiceActivity.class);
                i.putExtra(DashboardActivity.KEY_RENTED_CAR_PARAM, rentedCar);
                i.putExtra(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM, rentedCar.getCar().getTypeName());
                i.putExtra(KEY_RETURN_DATE_PARAM, getEndDate());
                i.putExtra(KEY_TOTAL_DURATION, duration);
                i.putExtra(KEY_TOTAL_AMOUNT, amount);
                startActivity(i);

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
