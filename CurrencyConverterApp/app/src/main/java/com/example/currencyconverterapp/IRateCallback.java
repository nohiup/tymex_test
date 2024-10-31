package com.example.currencyconverterapp;

import java.util.List;

public interface IRateCallback {
    void onDataFetched(Double rate);
    void onFetchFailed();
}
