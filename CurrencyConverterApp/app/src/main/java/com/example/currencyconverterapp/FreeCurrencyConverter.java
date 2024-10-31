package com.example.currencyconverterapp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FreeCurrencyConverter implements ICurrencyConverter{
    private final String API_KEY = BuildConfig.FreeCurrencyAPI;
    private final String name = "Free Currency";
    private final String
            GET_SYMBOL_API_URL = "https://api.freecurrencyapi.com/v1/currencies?apikey=" +API_KEY;
    private final String
            GET_RATE_API_URL = "https://api.freecurrencyapi.com/v1/latest?apikey=" +API_KEY;

   // https://api.freecurrencyapi.com/v1/latest?apikey=..&base_currency=JPY&currencies=USD
    private final OkHttpClient client;
    private ISymbolCallback symbolCallback;
    private IRateCallback rateCallback;

    private static FreeCurrencyConverter instance;

    @Override
    public String getName() {
        return name;
    }

    private FreeCurrencyConverter(ISymbolCallback symbolCallback, IRateCallback rateCallback){
        this.client = new OkHttpClient();
        this.symbolCallback = symbolCallback;
        this.rateCallback = rateCallback;
    }

    public static FreeCurrencyConverter getInstance(
            ISymbolCallback symbolCallback,
            IRateCallback rateCallback){
        if (instance == null){
            instance = new FreeCurrencyConverter(symbolCallback, rateCallback);
        }
        return instance;
    }

    @Override
    public void fetchCurrencySymbols() {
        Request request = new Request.Builder()
                .url(GET_SYMBOL_API_URL)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //Success handling
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Log.d("data", responseData);
                    List<CurrencyModel> currencies =  parseJsonForSymbols(responseData);
                    if (currencies == null) {
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(() ->
                            symbolCallback.onDataFetched(currencies));
                }
                else {
                    new Handler(Looper.getMainLooper()).post(symbolCallback::onFetchFailed);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(symbolCallback::onFetchFailed);
            }
        });
    }

    @Override
    public void fetchCurrencyRate(ProgressBar progressBar,
                                  View overlayView,
                                  String keyFrom,
                                  String keyTo,
                                  double amount){

        progressBar.setVisibility(View.VISIBLE);
        overlayView.setVisibility(View.VISIBLE);

        String exactUrl =
                GET_RATE_API_URL + "&base_currency=" + keyFrom + "&currencies=" + keyTo;
        Request request = new Request.Builder()
                .url(exactUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Log.d("Check", responseData);
                    double rate =  parseJsonForRate(responseData);
                    if (rate == 0.0) {
                        //Handle no rate returned,
                        return;
                    }
                    new Handler(Looper.getMainLooper()).post(() ->
                            rateCallback.onDataFetched(rate));
                }
                else {
                    Log.d("Failed somehow", "Failed");
                }
            }
        });
    }

    private List<CurrencyModel> parseJsonForSymbols(String json) {
        List<CurrencyModel> currencies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject ratesObject = jsonObject.getJSONObject("data");
            Iterator<String> keys = ratesObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();

                CurrencyModel currency = new CurrencyModel(
                        ratesObject.getJSONObject(key).getString("name"),
                        key
                );
                currencies.add(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    private double parseJsonForRate(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject ratesObject = jsonObject.getJSONObject("data");

            Iterator<String> keys = ratesObject.keys();
            //will absolutely get 1 key.
            while (keys.hasNext()) {
                String key = keys.next();
                Double rate = ratesObject.getDouble(key);
                return rate;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
