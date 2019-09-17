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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.adapter.CarTypeListRvAdapter;
import com.yadanar.carrentalservice.model.CarType;
import com.yadanar.carrentalservice.storage.FirebaseHelper;

public class CarTypeListActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference(FirebaseHelper.CAR_TYPE_LIST_TABLE_NAME);
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            carTypeListRvAdapter.add(CarType.parseCarType(dataSnapshot));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            carTypeListRvAdapter.update(CarType.parseCarType(dataSnapshot));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            carTypeListRvAdapter.remove(CarType.parseCarType(dataSnapshot));
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Toast.makeText(CarTypeListActivity.this, "onChildMoved()", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(CarTypeListActivity.this, "onCancelled()", Toast.LENGTH_SHORT).show();
        }
    };

    private CarTypeListRvAdapter carTypeListRvAdapter;

    private RecyclerView rvCarTypeList;
    private FloatingActionButton fabAddNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_manage);

        rvCarTypeList = findViewById(R.id.rv_car_list);
        fabAddNew = findViewById(R.id.fab_add_new);

        ((TextView) findViewById(R.id.tv_title)).setText(R.string.car_types);

        carTypeListRvAdapter = new CarTypeListRvAdapter(
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

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarTypeEditDialog(CarTypeListActivity.this, null);
            }
        });

        setLayoutManager(getResources().getConfiguration());

        dbRef.addChildEventListener(childEventListener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayoutManager(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRef.removeEventListener(childEventListener);
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

        final TextView tvTitle = dialog.findViewById(R.id.tv_title);
        final EditText edtCarType = dialog.findViewById(R.id.edt_car_type);
        AppCompatButton btnAdd = dialog.findViewById(R.id.btn_add_new);

        tvTitle.setText(getString(type == null
                ? R.string.add_new_car_type
                : R.string.edit_car_type));

        edtCarType.setText(type == null
                ? ""
                : type.getName());
        edtCarType.requestFocus();

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
                    String key = dbRef.push().getKey();
                    dbRef.child(key).setValue(s);
                } else {
                    dbRef.child(type.getId()).setValue(s);
                }

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
                dbRef.child(type.getId()).removeValue();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
