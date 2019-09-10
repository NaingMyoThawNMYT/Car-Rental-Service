package com.yadanar.carrentalservice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarTypeListRvAdapter;
import com.yadanar.carrentalservice.model.CarType;

import java.util.ArrayList;
import java.util.List;

public class CarTypeListActivity extends AppCompatActivity {

    private RecyclerView rvCarTypeList;
    private AppCompatButton btnAddNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_manage);

        rvCarTypeList = findViewById(R.id.rv_car_list);
        btnAddNew = findViewById(R.id.btn_add_new);

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.car_types);

        List<CarType> carTypeList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            CarType type = new CarType();
            type.setName("Type " + i);
            carTypeList.add(type);
        }
        CarTypeListRvAdapter carTypeListRvAdapter = new CarTypeListRvAdapter(carTypeList,
                new CarTypeListRvAdapter.CarTypeListItemOnClickListener() {
                    @Override
                    public void onClick(CarType type, int position) {
                        openCarTypeEditDialog(CarTypeListActivity.this, type);
                    }

                    @Override
                    public void onLongClick(CarType type, int position) {
                        openCarTypeDeleteDialog(CarTypeListActivity.this, type);
                    }
                });
        rvCarTypeList.setAdapter(carTypeListRvAdapter);
        rvCarTypeList.setHasFixedSize(true);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarTypeEditDialog(CarTypeListActivity.this, null);
            }
        });

        setLayoutManager(getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayoutManager(newConfig);
    }

    private void setLayoutManager(Configuration configuration) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? 2
                        : 1);
        rvCarTypeList.setLayoutManager(gridLayoutManager);
    }

    private void openCarTypeEditDialog(final Context context, final CarType type) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_car_type);

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        final EditText edtCarType = dialog.findViewById(R.id.edt_car_type);
        AppCompatButton btnAdd = dialog.findViewById(R.id.btn_add_new);

        tvTitle.setText(getString(type == null
                ? R.string.add_new_car_type
                : R.string.edit_car_type));

        btnAdd.setText(getString(type == null
                ? R.string.add
                : R.string.update));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = edtCarType.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    edtCarType.setError("*require");
                    return;
                }

                if (type == null) {
                    // TODO: 9/9/19 add new car type
                } else {
                    type.setName(s);

                    // TODO: 9/9/19 update car type
                }

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openCarTypeDeleteDialog(final Context context, final CarType type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("Delete " + type.getName() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, type.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
