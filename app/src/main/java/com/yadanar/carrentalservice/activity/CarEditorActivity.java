
package com.yadanar.carrentalservice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarTypeArrayAdapter;
import com.yadanar.carrentalservice.model.Car;
import com.yadanar.carrentalservice.model.CarType;
import com.yadanar.carrentalservice.storage.FirebaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.yadanar.carrentalservice.util.UiUtil.getNumber;
import static com.yadanar.carrentalservice.util.UiUtil.getText_;
import static com.yadanar.carrentalservice.util.UiUtil.setError;

public class CarEditorActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference(FirebaseHelper.CAR_LIST_TABLE_NAME);
    private DatabaseReference dbRefCarType = database.getReference(FirebaseHelper.CAR_TYPE_LIST_TABLE_NAME);
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    private Car car = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private List<CarType> carTypeList = new ArrayList<>();

    private AppCompatTextView tvTitle;
    private AppCompatImageView imgCar;
    private AppCompatSpinner spnCarType;
    private AppCompatEditText edtPrice,
            edtYear,
            edtSeat,
            edtColor,
            edtDescription;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_editor);

        dbRef.addChildEventListener(childEventListener);

        dialog = new ProgressDialog(this);

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

        final CarTypeArrayAdapter spnAdapter = new CarTypeArrayAdapter(CarEditorActivity.this);
        spnCarType.setAdapter(spnAdapter);

        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        dbRefCarType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    carTypeList.add(CarType.parseCarType(snapshot));
                }
                spnAdapter.setDataSet(carTypeList);

                if (car != null) {
                    int position = spnAdapter.getItemPosition(car.getType());
                    spnCarType.setSelection(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    public void cancel(View v) {
        finish();
    }

    public void save(View v) {
        if (car == null) {
            car = new Car();
        }

        car.setType(((CarType) spnCarType.getSelectedItem()).getId());
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

        dialog.setMessage("Saving...");
        dialog.setCancelable(false);
        dialog.show();
        uploadCar(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                uploadImage();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRef.removeEventListener(childEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    private void uploadCar(OnSuccessListener onSuccessListener) {
        if (TextUtils.isEmpty(car.getId())) {
            String key = dbRef.push().getKey();
            car.setId(key);
        }

        dbRef.child(car.getId())
                .setValue(car)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(CarEditorActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(onSuccessListener);
    }

    private void uploadImage() {
        // Get the data from an ImageView as bytes
        imgCar.setDrawingCacheEnabled(true);
        imgCar.buildDrawingCache();
        Drawable drawable = imgCar.getDrawable();
        if (drawable == null || TextUtils.isEmpty(car.getId())) {
            showSuccessToastAndFinishActivity();
            return;
        }

        Bitmap bitmap;
        try {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } catch (ClassCastException e) {
            showSuccessToastAndFinishActivity();
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        mStorageRef = FirebaseStorage.getInstance().getReference().child(car.getId());
        UploadTask uploadTask = mStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CarEditorActivity.this,
                        exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                showSuccessToastAndFinishActivity();
            }
        });
    }

    private void showSuccessToastAndFinishActivity() {
        dialog.dismiss();
        Toast.makeText(CarEditorActivity.this,
                "Saved successfully.",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}
