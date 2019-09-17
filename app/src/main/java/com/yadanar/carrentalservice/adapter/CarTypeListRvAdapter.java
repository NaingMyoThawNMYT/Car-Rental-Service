package com.yadanar.carrentalservice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.CarType;

import java.util.ArrayList;
import java.util.List;

public class CarTypeListRvAdapter extends RecyclerView.Adapter<CarTypeListRvAdapter.MyViewHolder> {
    private List<CarType> dataSet = new ArrayList<>();
    private CarTypeListItemOnClickListener onClickListener;

    public CarTypeListRvAdapter(CarTypeListItemOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void add(CarType type) {
        this.dataSet.add(type);
        notifyItemInserted(this.dataSet.size());
    }

    public void update(CarType type) {
        for (int i = 0; i < this.dataSet.size(); i++) {
            if (type.getId().equals(this.dataSet.get(i).getId())) {
                this.dataSet.set(i, type);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void remove(CarType type) {
        for (int i = 0; i < this.dataSet.size(); i++) {
            if (type.getId().equals(this.dataSet.get(i).getId())) {
                this.dataSet.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
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
