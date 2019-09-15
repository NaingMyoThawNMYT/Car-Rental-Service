package com.yadanar.carrentalservice.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.RentedCar;

public class CheckOutActivity extends AppCompatActivity {
    private RentedCar rentedCar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(DashboardActivity.KEY_RENTED_CAR_PARAM)) {
            rentedCar = (RentedCar) b.get(DashboardActivity.KEY_RENTED_CAR_PARAM);

            if (rentedCar != null) {
                Toast.makeText(this, rentedCar.getCar().getType(), Toast.LENGTH_SHORT).show();
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

        tvTitle.setText(rentedCar.getCar().getType());
        tvTotalDuration.setText("4:30 hr");
        tvTotalAmount.setText("32000 ks");

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
}
