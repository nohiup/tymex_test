package com.example.currencyconverterapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ACurrencyAdapter extends ArrayAdapter<CurrencyModel> {
    private final Context context;
    private final List<CurrencyModel> currencies;
    public ACurrencyAdapter(@NonNull Context context, @NonNull List<CurrencyModel> currencies) {
        super(context, android.R.layout.simple_list_item_1, currencies);
        this.context = context;
        this.currencies = currencies;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    android.R.layout.simple_spinner_dropdown_item,
                    parent,
                    false);
        }

        CurrencyModel currency = currencies.get(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(currency.getName() + " (" + currency.getSymbols() + ")");

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false
            );
        }

        CurrencyModel currency = currencies.get(position);

        TextView text1 = convertView.findViewById(android.R.id.text1);
        text1.setText(currency.getSymbols());

        return convertView;
    }
}
