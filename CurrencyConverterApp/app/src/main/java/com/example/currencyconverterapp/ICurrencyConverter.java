package com.example.currencyconverterapp;

import android.view.View;
import android.widget.ProgressBar;

public interface ICurrencyConverter {
    public String getName();
    public void fetchCurrencySymbols();

    public void fetchCurrencyRate(ProgressBar progressBar,
                                  View overlayView,
                                  String keyFrom,
                                  String keyTo,
                                  double amount);
}
