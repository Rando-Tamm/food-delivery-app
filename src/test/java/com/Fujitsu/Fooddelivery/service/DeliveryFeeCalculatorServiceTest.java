package com.Fujitsu.Fooddelivery.service;

import com.Fujitsu.Fooddelivery.service.DeliveryFeeCalculatorService;
import com.Fujitsu.Fooddelivery.service.WeatherDataService;
import com.Fujitsu.Fooddelivery.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeliveryFeeCalculatorServiceTest {

    @Mock
    private WeatherDataService weatherDataService;

    @InjectMocks
    private DeliveryFeeCalculatorService deliveryFeeCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateDeliveryFee() {
        // Mocking the weather data
        WeatherData mockWeatherData = new WeatherData();
        when(weatherDataService.findLatestByCity("Tallinn")).thenReturn(mockWeatherData);

        // Calculate the delivery fee
        double actualDeliveryFee = deliveryFeeCalculatorService.calculateDeliveryFee("Tallinn", "Car");

        // Assert the result
        assertEquals(5.0, actualDeliveryFee); // Assuming a delivery fee of 5.0 for the given conditions
    }
}

