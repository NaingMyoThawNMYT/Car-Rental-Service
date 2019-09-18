package com.yadanar.carrentalservice.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.listener.CarListItemOnClickListener;
import com.yadanar.carrentalservice.model.Car;

import java.util.ArrayList;
import java.util.List;

public class CarListRvAdapter extends RecyclerView.Adapter<CarListRvAdapter.MyViewHolder> implements Filterable {
    private StorageReference storageRef;

    private List<Car> dataSet;
    private List<Car> filteredDataSet;
    private CarListItemOnClickListener onClickListener;

    public CarListRvAdapter(CarListItemOnClickListener onClickListener) {
        this.onClickListener = onClickListener;

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void setDataSet(List<Car> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarListRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CarListRvAdapter.MyViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final Car car = filteredDataSet.get(position);

        holder.tvType.setText(car.getTypeName());

        storageRef.child(car.getId()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgCar.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(car, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDataSet == null ? 0 : filteredDataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchTerm = charSequence.toString().trim().toLowerCase();

                final List<Car> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (Car car : dataSet) {
                        if (car.getType().toLowerCase().contains(searchTerm)) {
                            filteredList.add(car);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (List<Car>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCar;
        private TextView tvType,
                tvPrice;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCar = itemView.findViewById(R.id.img_car);
            tvType = itemView.findViewById(R.id.tv_type);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
