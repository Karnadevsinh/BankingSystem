package com.example.bankaccount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

    @Test
    void getExchangeRates_ReturnsNonEmptyMap() {
        // Arrange
        CurrencyConverter converter = new CurrencyConverter();

        // Act
        Map<String, Double> rates = converter.getExchangeRates();

        // Assert
        assertAll(
                () -> assertFalse(rates.isEmpty(), "Exchange rates map should not be empty"),
                () -> assertTrue(rates.size() >= 4, "Should contain at least 4 default currencies"),
                () -> assertNotNull(rates.get("USD"), "Should contain USD rate"),
                () -> assertNotNull(rates.get("EUR"), "Should contain EUR rate"),
                () -> assertNotNull(rates.get("GBP"), "Should contain GBP rate"),
                () -> assertNotNull(rates.get("INR"), "Should contain INR rate")
        );
    }

    @Test
    void getExchangeRates_ContainsCorrectDefaultValues() {
        // Arrange
        CurrencyConverter converter = new CurrencyConverter();

        // Act
        Map<String, Double> rates = converter.getExchangeRates();

        // Assert
        assertAll(
                () -> assertEquals(1.0, rates.get("USD"), "USD rate should be 1.0"),
                () -> assertEquals(0.9, rates.get("EUR"), "EUR rate should be 0.9"),
                () -> assertEquals(0.78, rates.get("GBP"), "GBP rate should be 0.78"),
                () -> assertEquals(83.0, rates.get("INR"), "INR rate should be 83.0")
        );
    }

    @Test
    void getExchangeRates_ReturnsDefensiveCopy() {
        // Arrange
        CurrencyConverter converter = new CurrencyConverter();
        Map<String, Double> rates = converter.getExchangeRates();

        // Act
        rates.put("TEST", 1.0); // Modify the returned map
        Map<String, Double> newRates = converter.getExchangeRates();

        // Assert
        assertFalse(newRates.containsKey("TEST"),
                "Modifying returned map should not affect internal state");
    }

    @Test
    void getExchangeRates_ReflectsUpdates() {
        // Arrange
        CurrencyConverter converter = new CurrencyConverter();
        double newRate = 0.95;

        // Act
        converter.updateExchangeRate("EUR", newRate);
        Map<String, Double> rates = converter.getExchangeRates();

        // Assert
        assertEquals(newRate, rates.get("EUR"),
                "Exchange rates should reflect updates");
    }

    @Test
    void getExchangeRates_UsedInConversion() {
        // Arrange
        CurrencyConverter converter = new CurrencyConverter();
        double amount = 100.0;

        // Act
        Map<String, Double> rates = converter.getExchangeRates();
        double expectedRate = rates.get("EUR") / rates.get("USD");
        double convertedAmount = converter.convert("USD", "EUR", amount);

        // Assert
        assertEquals(amount * expectedRate, convertedAmount, 0.001,
                "Conversion should use rates from getExchangeRates");
        assertNotEquals(0.0, convertedAmount,
                "Conversion result should not be zero");
    }

    @Test
    public void testConvert_ValidCurrencies() {
        CurrencyConverter converter = new CurrencyConverter();
        double result = converter.convert("USD", "EUR", 100.0);
        assertEquals(90.0, result, 0.01);
    }

    @Test
    public void testConvert_FromCurrencyInvalid() {
        CurrencyConverter converter = new CurrencyConverter();
        try {
            converter.convert("XXX", "EUR", 100.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported currency", e.getMessage());
        }
    }

    @Test
    public void testConvert_ToCurrencyInvalid() {
        CurrencyConverter converter = new CurrencyConverter();
        try {
            converter.convert("USD", "XXX", 100.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported currency", e.getMessage());
        }
    }

    @Test
    public void testConvert_BothCurrenciesInvalid() {
        CurrencyConverter converter = new CurrencyConverter();
        try {
            converter.convert("XXX", "YYY", 100.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported currency", e.getMessage());
        }
    }

    @Test
    public void testConvert_SameCurrency() {
        CurrencyConverter converter = new CurrencyConverter();
        double result = converter.convert("USD", "USD", 100.0);
        assertEquals(100.0, result, 0.01);
    }

    @Test
    public void testConvert_ZeroAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        double result = converter.convert("USD", "EUR", 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void testConvert_NegativeAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        double result = converter.convert("USD", "EUR", -100.0);
        assertEquals(-90.0, result, 0.01);
    }

    @Test
    public void testConvert_AllCombinations() {
        CurrencyConverter converter = new CurrencyConverter();
        String[] currencies = {"USD", "EUR", "GBP", "INR"};

        for (String fromCurrency : currencies) {
            for (String toCurrency : currencies) {
                double result = converter.convert(fromCurrency, toCurrency, 100.0);
                assertNotNull(result);
                assertTrue(result > 0);  // Since all rates are positive

                // Test that conversion rate is properly applied
                double expectedRate = converter.getExchangeRates().get(toCurrency) /
                        converter.getExchangeRates().get(fromCurrency);
                assertEquals(100.0 * expectedRate, result, 0.01);
            }
        }
    }
}
