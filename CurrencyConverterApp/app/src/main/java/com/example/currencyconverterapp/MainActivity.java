package com.example.currencyconverterapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Spinner apiSourceS, currencyFromS, currencyToS;
    private EditText amountET;
    private TextView valueTV;
    private Button convertBtn, reverseBtn;
    private ProgressBar progressBar;

    private View overlayView;

    private List<CurrencyModel> currencyList;
    private List<ICurrencyConverter> apiList;


    private ACurrencyAdapter listFromAdapter;
    private ACurrencyAdapter listToAdapter;
    private AConverterApiAdapter apiAdapter;


    private CurrencyModel selectedFromCurrency, selectedToCurrency;

    private ICurrencyConverter selectedConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initSpinners();

        apiSourceS.setOnItemSelectedListener(apiAdapterListener);

        selectedConverter.fetchCurrencySymbols();

        currencyFromS.setOnItemSelectedListener(fromAdapterListener);
        currencyToS.setOnItemSelectedListener(toAdapterListener);

        convertBtn.setOnClickListener(convertButtonListener);
        reverseBtn.setOnClickListener(reverseButtonListener);
    }

    private void initView(){
        this.apiSourceS = findViewById(R.id.main_api_spinner);
        this.currencyToS = findViewById(R.id.main_currency_to_spinner);
        this.currencyFromS = findViewById(R.id.main_currency_from_spinner);

        this.amountET = findViewById(R.id.main_tf_amount);
        this.valueTV = findViewById(R.id.main_tf_value);

        this.convertBtn = findViewById(R.id.main_convert_button);
        this.reverseBtn = findViewById(R.id.main_reverse_button);

        this.progressBar = findViewById(R.id.main_progress_bar);
        this.overlayView = findViewById(R.id.main_overlay_view);
    }

    private void initSpinners(){
        currencyList = new ArrayList<>();
        currencyList.add(new CurrencyModel("", "Select"));
        listFromAdapter = new ACurrencyAdapter(
                this,
                currencyList
        );

        listToAdapter =  new ACurrencyAdapter(
                this,
                currencyList
        );

        currencyFromS.setAdapter(listFromAdapter);
        currencyToS.setAdapter(listToAdapter);

        //API List
        apiList = new ArrayList<>();
        apiList.add(FreeCurrencyConverter.getInstance(symbolCallback, rateCallback));

        apiAdapter = new AConverterApiAdapter(
                this,
                apiList
        );
        apiSourceS.setAdapter(apiAdapter);
        selectedConverter = apiAdapter.getItem(0);
    }

    ISymbolCallback symbolCallback = new ISymbolCallback() {
        @Override
        public void onDataFetched(List<CurrencyModel> data) {
            currencyList.addAll(data);
            listFromAdapter.notifyDataSetChanged();
            listToAdapter.notifyDataSetChanged();

            disableProgressBar();
        }

        @Override
        public void onFetchFailed() {
            disableProgressBar();
            Toast.makeText(MainActivity.this,
                    "Connection failed. Please try again",
                    Toast.LENGTH_SHORT).show();
        }
    };

    IRateCallback rateCallback = new IRateCallback() {
        @Override
        public void onDataFetched(Double rate) {
            disableProgressBar();
            Log.d("Data test", rate.toString());
            valueTV.setText(calculateValue(rate).toString());
        }

        @Override
        public void onFetchFailed() {
            disableProgressBar();
            Toast.makeText(MainActivity.this,
                    "Connection failed. Please try again",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private AdapterView.OnItemSelectedListener fromAdapterListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            CurrencyModel selectedItem = (CurrencyModel) parent.getItemAtPosition(position);
            selectedFromCurrency = selectedItem;
            Log.d("picked", "onItemSelected: " + selectedItem.getSymbols());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener toAdapterListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            CurrencyModel selectedItem = (CurrencyModel) parent.getItemAtPosition(position);
            selectedToCurrency = selectedItem;
            Log.d("picked", "onItemSelected: " + selectedItem.getSymbols());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener apiAdapterListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ICurrencyConverter selectedItem = (ICurrencyConverter) parent.getItemAtPosition(position);
                    selectedConverter = selectedItem;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
    View.OnClickListener convertButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isNotSelected(selectedFromCurrency) || isNotSelected(selectedToCurrency)
            || selectedFromCurrency == null || selectedToCurrency == null
            || amountET.getText().toString().equals("") || amountET.getText().toString().isEmpty())
            {
                Toast.makeText(
                        MainActivity.this,
                        "Not enough data to convert.",
                        Toast.LENGTH_SHORT
                ).show();
            }
            else {
                selectedConverter.fetchCurrencyRate(
                        progressBar,
                        overlayView,
                        selectedFromCurrency.getSymbols(),
                        selectedToCurrency.getSymbols(),
                        Double.parseDouble(amountET.getText().toString())
                );
                disableProgressBar();

            }
        }
    };

    View.OnClickListener reverseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int fromPos = currencyList.indexOf(selectedFromCurrency);
            int toPos = currencyList.indexOf(selectedToCurrency);

            if (fromPos != 0 && toPos != 0){
                currencyFromS.setSelection(toPos);
                currencyToS.setSelection(fromPos);

                String amountText = amountET.getText().toString();

                amountET.setText(valueTV.getText());
                valueTV.setText(amountText);
            }
        }
    };

    private boolean isNotSelected(CurrencyModel c){
        if (c.name.isEmpty() || c.name.equals("")) return true;
        return false;
    }

    private Double calculateValue(Double rate){
        Double value = 1.0;
        value *= Double.parseDouble(amountET.getText().toString());
        value *= rate;
        return value;
    }

    private void disableProgressBar(){
        progressBar.setVisibility(View.GONE);
        overlayView.setVisibility(View.GONE);
    }
}
