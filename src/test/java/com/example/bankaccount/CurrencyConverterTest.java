package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CurrencyConverterTest {
    private CurrencyConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CurrencyConverter();
    }

    @Test
    void testConversion() {
        double usdToEur = converter.convert("USD", "EUR", 100);
        assertEquals(90, usdToEur, 0.01);

        double inrToUsd = converter.convert("INR", "USD", 8300);
        assertEquals(100, inrToUsd, 0.01);
    }

    @Test
    void testUnsupportedCurrency() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convert("USD", "ABC", 100);
        });
        assertTrue(exception.getMessage().contains("Unsupported currency"));
    }

    @Test
    void testUpdateExchangeRate() {
        converter.updateExchangeRate("JPY", 110.0);
        double usdToJpy = converter.convert("USD", "JPY", 1);
        assertEquals(110.0, usdToJpy, 0.01);
    }
}
