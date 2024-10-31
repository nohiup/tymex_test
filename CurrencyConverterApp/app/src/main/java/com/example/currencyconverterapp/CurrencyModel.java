package com.example.currencyconverterapp;

public class CurrencyModel {
    String name;
    String symbols;

    public CurrencyModel(String name, String symbols) {
        this.name = name;
        this.symbols = symbols;
    }

    public String getName() {
        return name;
    }

    public String getSymbols() {
        return symbols;
    }

}
