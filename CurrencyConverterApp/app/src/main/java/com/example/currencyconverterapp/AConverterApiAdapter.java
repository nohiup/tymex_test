package com.example.currencyconverterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AConverterApiAdapter extends ArrayAdapter<ICurrencyConverter> {
    private final Context context;
    private final List<ICurrencyConverter> converters;

    public AConverterApiAdapter(@NonNull Context context, @NonNull List<ICurrencyConverter> converters) {
        super(context, android.R.layout.simple_list_item_1, converters);
        this.context =context;
        this.converters = converters;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return createView(position, convertView, parent,
                android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, android.R.layout.simple_list_item_1);
    }

    private View createView(int position, View convertView, ViewGroup parent, int viewHolder) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewHolder, parent, false);
        }

        ICurrencyConverter converter = converters.get(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(converter.getName());

        return convertView;
    }

}
