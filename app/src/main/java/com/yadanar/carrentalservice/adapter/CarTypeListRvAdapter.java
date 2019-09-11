package com.yadanar.carrentalservice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.CarType;

import java.util.List;

public class CarTypeListRvAdapter extends RecyclerView.Adapter<CarTypeListRvAdapter.MyViewHolder> {
    private List<CarType> dataSet;
    private CarTypeListItemOnClickListener onClickListener;

    public CarTypeListRvAdapter(List<CarType> dataSet, CarTypeListItemOnClickListener onClickListener) {
        this.dataSet = dataSet;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_item_1, parent, false);
        return new CarTypeListRvAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final CarType type = dataSet.get(position);
        holder.tvType.setText(type.getName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onClickListener.onLongClick(type, position);
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(type, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface CarTypeListItemOnClickListener {
        void onClick(CarType type, int position);

        void onLongClick(CarType type, int position);
    }
}
