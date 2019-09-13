
package com.yadanar.carrentalservice.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.Car;

import java.io.IOException;

import static com.yadanar.carrentalservice.util.UiUtil.getNumber;
import static com.yadanar.carrentalservice.util.UiUtil.getText_;
import static com.yadanar.carrentalservice.util.UiUtil.setError;

public class CarEditorActivity extends AppCompatActivity {
    private Car car = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private AppCompatTextView tvTitle;
    private AppCompatImageView imgCar;
    private AppCompatSpinner spnCarType;
    private AppCompatEditText edtPrice,
            edtYear,
            edtSeat,
            edtColor,
            edtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_editor);

        tvTitle = findViewById(R.id.tv_title);
        imgCar = findViewById(R.id.img_car);
        spnCarType = findViewById(R.id.spn_car_type);
        edtPrice = findViewById(R.id.edt_price);
        edtYear = findViewById(R.id.edt_year);
        edtSeat = findViewById(R.id.edt_seat);
        edtColor = findViewById(R.id.edt_color);
        edtDescription = findViewById(R.id.edt_description);

        String title = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey(CarDetailActivity.KEY_CAR_PARAM)) {
                car = (Car) b.get(CarDetailActivity.KEY_CAR_PARAM);

                if (car != null) {
                    title = getString(R.string.edit);

                    // TODO: 9/12/19 set to spinner
                    edtPrice.setText(String.valueOf(car.getPrice()));
                    edtYear.setText(String.valueOf(car.getYear()));
                    edtSeat.setText(String.valueOf(car.getSeats()));
                    edtColor.setText(car.getColor());
                    edtDescription.setText(car.getDescription());
                }
            }
        }

        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.add_new);
        }

        tvTitle.setText(title);

        final String[] types = new String[]{"A", "B", "C", "D", "E"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this,
                R.layout.simple_list_item,
                types);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnCarType.setAdapter(spnAdapter);

        spnCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CarEditorActivity.this, types[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void cancel(View v) {
        finish();
    }

    public void save(View v) {
        Car car = new Car();
        car.setType((String) spnCarType.getSelectedItem());
        car.setPrice(getNumber(edtPrice));
        car.setYear(getNumber(edtYear));
        car.setSeats(getNumber(edtSeat));
        car.setColor(getText_(edtColor));
        car.setDescription(getText_(edtDescription));

        if (car.getPrice() <= 0) {
            setError(edtPrice);
            return;
        }

        if (car.getYear() <= 0) {
            setError(edtYear);
            return;
        }

        if (car.getSeats() <= 0) {
            setError(edtSeat);
            return;
        }

        if (TextUtils.isEmpty(car.getColor())) {
            setError(edtColor);
            return;
        }

        // TODO: 9/12/19 save to firebase

        finish();
    }

    public void dispatchTakePictureIntent(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Checking can current activity handle the intent?
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void dispatchPickImageIntent(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data.getExtras() != null) {
                Bundle b = data.getExtras();
                Bitmap bitmap = (Bitmap) b.get("data");
                imgCar.setImageBitmap(bitmap);
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgCar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
