package com.yadanar.carrentalservice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.listener.CarListItemOnClickListener;
import com.yadanar.carrentalservice.model.Car;

import java.util.List;

public class CarListManageRvAdapter extends RecyclerView.Adapter<CarListManageRvAdapter.MyViewHolder> {
    private List<Car> dataSet;
    private CarListItemOnClickListener onClickListener;

    public CarListManageRvAdapter(List<Car> dataSet, CarListItemOnClickListener onClickListener) {
        this.dataSet = dataSet;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_manage_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final Car car = dataSet.get(position);

        holder.tvType.setText(car.getType());
        holder.tvPrice.setText(String.valueOf(car.getPrice()));
        holder.tvColor.setText(car.getColor());
        holder.tvSeats.setText(String.valueOf(car.getSeats()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(car, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType,
                tvPrice,
                tvColor,
                tvSeats;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tv_car_type);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvColor = itemView.findViewById(R.id.tv_color);
            tvSeats = itemView.findViewById(R.id.tv_seats);
        }
    }
}
