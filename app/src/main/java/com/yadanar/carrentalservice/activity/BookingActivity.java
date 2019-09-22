package com.yadanar.carrentalservice.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.Customer;
import com.yadanar.carrentalservice.storage.FirebaseHelper;
import com.yadanar.carrentalservice.util.UiUtil;

import java.util.Calendar;
import java.util.Date;

import static com.yadanar.carrentalservice.util.DateUtil.displayDateOnlyFormat;
import static com.yadanar.carrentalservice.util.DateUtil.displayTimeOnlyFormat;
import static com.yadanar.carrentalservice.util.UiUtil.getText_;
import static com.yadanar.carrentalservice.util.UiUtil.setError;

public class BookingActivity extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference customerTable = db.getReference(FirebaseHelper.CUSTOMER_LIST_TABLE_NAME);
    private DatabaseReference carTable = db.getReference(FirebaseHelper.CAR_LIST_TABLE_NAME);

    public static final String KEY_CAR_ID_PARAM = "key_car_id_param";

    private int mYear,
            mMonth,
            mDay,
            mHour,
            mMinute;
    private String carId;

    private EditText edtName,
            edtId,
            edtEmail,
            edtPhone,
            edtAddress;
    private AppCompatButton btnStartDate,
            btnStartTime;
    private AppCompatRadioButton rdMale,
            rdFemale;
    private ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(KEY_CAR_ID_PARAM)) {
            carId = b.getString(KEY_CAR_ID_PARAM);
        }
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
        UiUtil.checkConnectionAndFinishActivity(this);

        Customer customer = new Customer();
        customer.setName(getText_(edtName));
        customer.setNrc(getText_(edtId));
        customer.setEmail(getText_(edtEmail));
        customer.setPhone(getText_(edtPhone));
        customer.setAddress(getText_(edtAddress));
        customer.setGender(rdMale.isChecked() ? "Male" : "Female");
        customer.setCarId(carId);
        customer.setDate(getDate().getTime());

        if (TextUtils.isEmpty(customer.getName())) {
            setError(edtName);
            return;
        }

        if (TextUtils.isEmpty(customer.getNrc())) {
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

        if (TextUtils.isEmpty(customer.getCarId())) {
            Toast.makeText(BookingActivity.this,
                    "No car is selected!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        String key = customerTable.push().getKey();
        customer.setId(key);

        customerTable.child(customer.getId())
                .setValue(customer)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(BookingActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        carTable.child(carId)
                                .child("available")
                                .setValue(false)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(BookingActivity.this,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();

                                        setResult(RESULT_OK, null);

                                        finish();
                                    }
                                });
                    }
                });
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
