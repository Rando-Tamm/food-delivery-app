package com.Fujitsu.Fooddelivery.service;

import com.Fujitsu.Fooddelivery.model.WeatherData;
import com.Fujitsu.Fooddelivery.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryFeeCalculatorService {

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public DeliveryFeeCalculatorService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    public double calculateDeliveryFee(String city, String vehicleType) {
        // Fetch latest weather data for the specified city from the database
        Optional<WeatherData> weatherDataOptional = weatherDataRepository.findFirstByStationNameOrderByTimestampDesc(city);
        if (weatherDataOptional.isEmpty()) {
            throw new RuntimeException("No weather data found for the specified city: " + city);
        }
        WeatherData latestWeatherData = weatherDataOptional.get();

        // Apply business rules for delivery fee calculation
        double baseFee = calculateBaseFee(city, vehicleType);
        double extraFee = calculateExtraFee(latestWeatherData, vehicleType);

        // Calculate total delivery fee
        return baseFee + extraFee;
    }

    private double calculateBaseFee(String city, String vehicleType) {
        switch (city) {
            case "Tallinn":
                return switch (vehicleType) {
                    case "Car" -> 4.0;
                    case "Scooter" -> 3.5;
                    case "Bike" -> 3.0;
                    default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
                };
            case "Tartu":
                return switch (vehicleType) {
                    case "Car" -> 3.5;
                    case "Scooter" -> 3.0;
                    case "Bike" -> 2.5;
                    default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
                };
            case "Pärnu":
                return switch (vehicleType) {
                    case "Car" -> 3.0;
                    case "Scooter" -> 2.5;
                    case "Bike" -> 2.0;
                    default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
                };
            default:
                throw new IllegalArgumentException("Invalid city: " + city);
        }
    }

    private double calculateExtraFee(WeatherData weatherData, String vehicleType) {
        double extraFee = 0.0;

        // Check for extra fee based on air temperature (ATEF)
        if ((vehicleType.equals("Scooter") || vehicleType.equals("Bike")) && weatherData.getAirTemperature() < -10) {
            extraFee += 1.0;
        } else if ((vehicleType.equals("Scooter") || vehicleType.equals("Bike")) && weatherData.getAirTemperature() >= -10 && weatherData.getAirTemperature() < 0) {
            extraFee += 0.5;
        }

        // Check for extra fee based on wind speed (WSEF)
        if (vehicleType.equals("Bike") && weatherData.getWindSpeed() >= 10 && weatherData.getWindSpeed() <= 20) {
            extraFee += 0.5;
        } else if (vehicleType.equals("Bike") && weatherData.getWindSpeed() > 20) {
            throw new IllegalArgumentException("Usage of selected vehicle type is forbidden: Wind speed too high");
        }

        // Check for extra fee based on weather phenomenon (WPEF)
        if ((vehicleType.equals("Scooter") || vehicleType.equals("Bike")) && (weatherData.getWeatherPhenomenon().equalsIgnoreCase("snow") || weatherData.getWeatherPhenomenon().equalsIgnoreCase("sleet"))) {
            extraFee += 1.0;
        } else if ((vehicleType.equals("Scooter") || vehicleType.equals("Bike")) && weatherData.getWeatherPhenomenon().equalsIgnoreCase("rain")) {
            extraFee += 0.5;
        } else if ((vehicleType.equals("Scooter") || vehicleType.equals("Bike")) && (weatherData.getWeatherPhenomenon().equalsIgnoreCase("glaze") || weatherData.getWeatherPhenomenon().equalsIgnoreCase("hail") || weatherData.getWeatherPhenomenon().equalsIgnoreCase("thunder"))) {
            throw new IllegalArgumentException("Usage of selected vehicle type is forbidden: Adverse weather condition");
        }

        return extraFee;
    }
}

