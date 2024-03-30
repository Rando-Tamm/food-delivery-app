package com.Fujitsu.Fooddelivery.util;

import com.Fujitsu.Fooddelivery.model.WeatherData;
import com.Fujitsu.Fooddelivery.repository.WeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDateTime;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Component
public class WeatherDataImportJob {

    private static final Logger logger = LoggerFactory.getLogger(WeatherDataImportJob.class);

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherDataImportJob(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @Scheduled(cron = "0 15 * * * *") // Runs every hour at HH:15:00
    public void importWeatherData() {
        try {
            // Fetch weather data from the URL
            ResponseEntity<String> responseEntity = new RestTemplate().getForEntity("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php", String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Parse and save weather data
                String xmlData = responseEntity.getBody();
                parseAndSaveWeatherData(xmlData);
            } else {
                throw new ResponseStatusException(responseEntity.getStatusCode(), "Failed to fetch weather data from the URL");
            }
        } catch (Exception e) {
            logger.error("Error occurred while importing weather data: {}", e.getMessage());
            // Handle the error appropriately, e.g., send an email notification to the admin
        }
    }

    private void parseAndSaveWeatherData(String xmlData) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlData));
        Document doc = builder.parse(is);

        NodeList stationList = doc.getElementsByTagName("station");

        for (int i = 0; i < stationList.getLength(); i++) {
            Node stationNode = stationList.item(i);
            if (stationNode.getNodeType() == Node.ELEMENT_NODE) {
                Element stationElement = (Element) stationNode;
                WeatherData weatherData = parseWeatherData(stationElement);
                weatherDataRepository.save(weatherData);
            }
        }

        logger.info("Weather data imported successfully at {}", LocalDateTime.now());
    }

    private WeatherData parseWeatherData(Element stationElement) {
        String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();
        String wmoCode = stationElement.getElementsByTagName("wmo").item(0).getTextContent();
        double airTemperature = Double.parseDouble(stationElement.getElementsByTagName("airtemperature").item(0).getTextContent());
        double windSpeed = Double.parseDouble(stationElement.getElementsByTagName("windspeed").item(0).getTextContent());
        String weatherPhenomenon = stationElement.getElementsByTagName("phenomenon").item(0).getTextContent();
        LocalDateTime timestamp = LocalDateTime.parse(stationElement.getElementsByTagName("datetime").item(0).getTextContent());

        WeatherData weatherData = new WeatherData();
        weatherData.setStationName(stationName);
        weatherData.setWmoCode(wmoCode);
        weatherData.setAirTemperature(airTemperature);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setWeatherPhenomenon(weatherPhenomenon);
        weatherData.setTimestamp(timestamp);

        return weatherData;
    }
}

