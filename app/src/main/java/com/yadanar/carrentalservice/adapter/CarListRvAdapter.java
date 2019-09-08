package com.yadanar.carrentalservice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.CarModel;

import java.util.List;

public class CarListRvAdapter extends RecyclerView.Adapter<CarListRvAdapter.MyViewHolder> {
    private List<CarModel> dataSet;
    private CarListItemOnClickListener carListItemOnClickListener;

    public CarListRvAdapter(List<CarModel> dataSet, CarListItemOnClickListener carListItemOnClickListener) {
        this.dataSet = dataSet;
        this.carListItemOnClickListener = carListItemOnClickListener;
    }

    @NonNull
    @Override
    public CarListRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarListRvAdapter.MyViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final CarModel car = dataSet.get(position);

        holder.tvType.setText(car.getType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carListItemOnClickListener.onClick(car, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface CarListItemOnClickListener {
        void onClick(CarModel car, int position);
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
