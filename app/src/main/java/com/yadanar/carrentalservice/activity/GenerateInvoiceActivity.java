package com.yadanar.carrentalservice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.RentedCar;
import com.yadanar.carrentalservice.util.DateUtil;
import com.yadanar.carrentalservice.util.UiUtil;

import java.util.Date;

public class GenerateInvoiceActivity extends AppCompatActivity {
    private TextView tvInvoiceNo,
            tvDate,
            tvCustomer,
            tvEmail,
            tvNrc,
            tvAddress,
            tvPhone,
            tvCarType,
            tvPrice,
            tvStartDate,
            tvReturnDate,
            tvTotalDuration,
            tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_invoice);

        tvInvoiceNo = findViewById(R.id.tv_invoice_no);
        tvDate = findViewById(R.id.tv_date);
        tvCustomer = findViewById(R.id.tv_customer);
        tvEmail = findViewById(R.id.tv_email);
        tvNrc = findViewById(R.id.tv_nrc);
        tvAddress = findViewById(R.id.tv_address);
        tvPhone = findViewById(R.id.tv_phone);
        tvCarType = findViewById(R.id.tv_car_type);
        tvPrice = findViewById(R.id.tv_price);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvReturnDate = findViewById(R.id.tv_return_date);
        tvTotalDuration = findViewById(R.id.tv_total_duration);
        tvTotalAmount = findViewById(R.id.tv_total_amount);

        Bundle b = getIntent().getExtras();
        if (b != null
                && b.containsKey(DashboardActivity.KEY_RENTED_CAR_PARAM)
                && b.containsKey(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM)
                && b.containsKey(CheckOutActivity.KEY_RETURN_DATE_PARAM)
                && b.containsKey(CheckOutActivity.KEY_TOTAL_DURATION)
                && b.containsKey(CheckOutActivity.KEY_TOTAL_AMOUNT)) {
            RentedCar rentedCar = (RentedCar) b.get(DashboardActivity.KEY_RENTED_CAR_PARAM);
            rentedCar.getCar().setTypeName(b.getString(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM));

            tvInvoiceNo.setText(String.valueOf(new Date().getTime()));
            tvDate.setText(DateUtil.displayDateAndTimeFormat(new Date()));
            tvCustomer.setText(rentedCar.getCustomer().getName());
            tvEmail.setText(rentedCar.getCustomer().getEmail());
            tvNrc.setText(rentedCar.getCustomer().getNrc());
            tvAddress.setText(rentedCar.getCustomer().getAddress());
            tvPhone.setText(rentedCar.getCustomer().getPhone());
            tvCarType.setText(rentedCar.getCar().getTypeName());
            tvPrice.setText(String.valueOf(rentedCar.getCar().getPrice()));
            tvStartDate.setText(DateUtil.displayDateAndTimeFormat(new Date(rentedCar.getCustomer().getDate())));
            tvReturnDate.setText(DateUtil.displayDateAndTimeFormat((Date) b.get(CheckOutActivity.KEY_RETURN_DATE_PARAM)));
            tvTotalDuration.setText(b.getString(CheckOutActivity.KEY_TOTAL_DURATION));
            tvTotalAmount.setText(b.getString(CheckOutActivity.KEY_TOTAL_AMOUNT));
        }
    }

    public void showCompleteDialog(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invoice is send successfully!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UiUtil.checkConnectionAndFinishActivity(GenerateInvoiceActivity.this);

                DashboardActivity.recreateActivity = true;

                dialogInterface.dismiss();

                finish();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
