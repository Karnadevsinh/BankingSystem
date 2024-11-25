package com.example.bankaccount;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private final Map<String, Double> exchangeRates;

    public CurrencyConverter() {
        exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0); // Base currency
        exchangeRates.put("EUR", 0.9);
        exchangeRates.put("GBP", 0.78);
        exchangeRates.put("INR", 83.0);
    }

    public void updateExchangeRate(String currency, double rate) {
        exchangeRates.put(currency, rate);
    }

    public double convert(String fromCurrency, String toCurrency, double amount) {
        if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported currency");
        }
        double rate = exchangeRates.get(toCurrency) / exchangeRates.get(fromCurrency);
        return amount * rate;
    }

    public Map<String, Double> getExchangeRates() {
        return new HashMap<>(exchangeRates);
    }
}
