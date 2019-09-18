package com.yadanar.carrentalservice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yadanar.carrentalservice.model.CarType;

import java.util.ArrayList;
import java.util.List;

public class CarTypeArrayAdapter extends ArrayAdapter<CarType> {
    private List<CarType> dataSet = new ArrayList<>();

    public CarTypeArrayAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line);
    }

    public void setDataSet(List<CarType> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public CarType getItem(int position) {
        return dataSet.get(position);
    }

    public int getItemPosition(String itemId) {
        for (int i = 0; i < this.dataSet.size(); i++) {
            if (itemId.equals(this.dataSet.get(i).getId())) {
                return i;
            }
        }

        return -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), android.R.layout.simple_dropdown_item_1line, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(dataSet.get(position).getName());
        return textView;
    }
}
