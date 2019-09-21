package com.yadanar.carrentalservice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yadanar.carrentalservice.R;
import com.yadanar.carrentalservice.model.RentedCar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardCarListRvAdapter extends RecyclerView.Adapter<DashboardCarListRvAdapter.MyViewHolder> implements Filterable {
    private List<RentedCar> dataSet = new ArrayList<>();
    private List<RentedCar> filteredDataSet = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public DashboardCarListRvAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDataSet(List<RentedCar> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_car_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final RentedCar rentedCar = filteredDataSet.get(position);

        holder.tvCustomer.setText(rentedCar.getCustomer().getName());
        holder.tvCarType.setText(rentedCar.getCar().getTypeName());
        holder.tvStartTime.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm")
                .format(rentedCar.getCustomer().getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(rentedCar, position);
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

                final List<RentedCar> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (RentedCar rentedCar : dataSet) {
                        if (rentedCar.getCar().getType().toLowerCase().contains(searchTerm)
                                || rentedCar.getCustomer().getName().contains(searchTerm)) {
                            filteredList.add(rentedCar);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (List<RentedCar>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer,
                tvCarType,
                tvStartTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCustomer = itemView.findViewById(R.id.tv_customer);
            tvCarType = itemView.findViewById(R.id.tv_car_type);
            tvStartTime = itemView.findViewById(R.id.tv_start_time);
        }
    }

    public interface OnItemClickListener {
        void onClick(RentedCar rentedCar, int position);
    }
}
