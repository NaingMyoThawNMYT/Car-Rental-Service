package com.yadanar.carrentalservice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarListRvAdapter;
import com.yadanar.carrentalservice.listener.CarListItemOnClickListener;
import com.yadanar.carrentalservice.model.Car;
import com.yadanar.carrentalservice.model.CarType;
import com.yadanar.carrentalservice.storage.FirebaseHelper;
import com.yadanar.carrentalservice.util.UiUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefCar = database.getReference(FirebaseHelper.CAR_LIST_TABLE_NAME);
    private DatabaseReference dbRefCarType = database.getReference(FirebaseHelper.CAR_TYPE_LIST_TABLE_NAME);
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private boolean adminMode = false;

    private AppCompatEditText edtSearch;
    private RecyclerView rvCarList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        UiUtil.checkConnectionAndFinishActivity(this);

        edtSearch = findViewById(R.id.edt_search);
        rvCarList = findViewById(R.id.rv_car_list);

        dialog = new ProgressDialog(this);

        rvCarList.setHasFixedSize(true);

        final CarListRvAdapter carListRvAdapter = new CarListRvAdapter(
                new CarListItemOnClickListener() {
                    @Override
                    public void onClick(Car car, int position) {
                        Intent i = new Intent(CustomerActivity.this,
                                adminMode
                                        ? CarEditorActivity.class
                                        : CarDetailActivity.class);
                        i.putExtra(CarDetailActivity.KEY_CAR_PARAM, car);
                        i.putExtra(CarDetailActivity.KEY_CAR_TYPE_NAME_PARAM, car.getTypeName());
                        if (car.getImageByteArray() != null) {
                            i.putExtra(CarDetailActivity.KEY_CAR_IMAGE_BYTE_ARRAY_PARAM, car.getImageByteArray());
                        }
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(Car car, int position) {
                        openCarTypeDeleteDialog(CustomerActivity.this, car);
                    }
                });
        rvCarList.setAdapter(carListRvAdapter);

        Bundle b = getIntent().getExtras();
        if (b != null
                && b.containsKey(AdminActivity.ADMIN_KEY)
                && b.getBoolean(AdminActivity.ADMIN_KEY)) {
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
            adminMode = true;
        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                carListRvAdapter.getFilter().filter(editable.toString());
            }
        });

        setLayoutManager(getResources().getConfiguration());

        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        dbRefCarType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot carTypeDataSnapshot) {
                final List<CarType> carTypeList = new ArrayList<>();
                for (DataSnapshot snapshot : carTypeDataSnapshot.getChildren()) {
                    carTypeList.add(CarType.parseCarType(snapshot));
                }

                dbRefCar.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        List<Car> carList = parseCarList((Map<String, Object>) dataSnapshot.getValue(), carTypeList);
                        carListRvAdapter.setDataSet(carList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayoutManager(newConfig);
    }

    private void setLayoutManager(Configuration configuration) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? 3
                        : 2);
        rvCarList.setLayoutManager(gridLayoutManager);
    }

    public void addNewCar(View v) {
        startActivity(new Intent(this, CarEditorActivity.class));
    }

    public static List<Car> parseCarList(Map<String, Object> carMap, List<CarType> carTypeList) {
        List<Car> carList = new ArrayList<>();

        if (carMap == null) {
            return carList;
        }

        for (Map.Entry<String, Object> entry : carMap.entrySet()) {
            //Get car map
            Map singleCar = (Map) entry.getValue();
            Car car = new Car();
            car.setAvailable(Boolean.valueOf(String.valueOf(singleCar.get("available"))));
            car.setColor(String.valueOf(singleCar.get("color")));
            car.setDescription(singleCar.get("description") == null
                    ? ""
                    : String.valueOf(singleCar.get("description")));
            car.setId(String.valueOf(singleCar.get("id")));
            car.setPrice(Double.valueOf(String.valueOf(singleCar.get("price"))));
            car.setSeats(Integer.valueOf(String.valueOf(singleCar.get("seats"))));
            for (CarType carType : carTypeList) {
                if (String.valueOf(singleCar.get("type")).equals(carType.getId())) {
                    car.setType(carType.getId());
                    car.setTypeName(carType.getName());
                }
            }
            car.setYear(Integer.valueOf(String.valueOf(singleCar.get("year"))));
            carList.add(car);
        }

        return carList;
    }

    private void openCarTypeDeleteDialog(final Context context, final Car car) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("Delete " + car.getTypeName() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UiUtil.checkConnectionAndFinishActivity(CustomerActivity.this);

                dbRefCar.child(car.getId()).removeValue();

                storageRef.child(car.getId()).delete();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
