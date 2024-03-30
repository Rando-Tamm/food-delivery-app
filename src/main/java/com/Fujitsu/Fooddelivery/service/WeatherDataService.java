package com.Fujitsu.Fooddelivery.service;

import com.Fujitsu.Fooddelivery.model.WeatherData;
import com.Fujitsu.Fooddelivery.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherDataService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    public List<WeatherData> getAllWeatherData() {
        return weatherDataRepository.findAll();
    }

    public WeatherData getLatestWeatherDataByCity(String city) {
        return weatherDataRepository.findFirstByStationNameOrderByTimestampDesc(city)
                .orElse(null); // Or throw an exception if desired
    }

    public WeatherData findLatestByCity(String city) {
        return weatherDataRepository.findFirstByCityOrderByTimestampDesc(city)
                .orElse(null); // Or throw an exception if desired
    }

    // Additional methods for weather data retrieval or manipulation can be added here
}

