package com.Fujitsu.Fooddelivery.repository;

import com.Fujitsu.Fooddelivery.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    Optional<WeatherData> findFirstByStationNameOrderByTimestampDesc(String stationName);
    Optional<WeatherData> findLatestByCity(String city);
    Optional<WeatherData> findFirstByCityOrderByTimestampDesc(String city);
}

