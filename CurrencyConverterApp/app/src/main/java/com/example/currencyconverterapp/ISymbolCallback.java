package com.example.currencyconverterapp;

import java.util.List;

public interface ISymbolCallback {
    void onDataFetched(List<CurrencyModel> currencies);
    void onFetchFailed();
}