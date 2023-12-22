package com.digsol.main.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@UtilityClass
@Slf4j
public class GeoCoder {

    private static final String GEOCODING_RESOURCE = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "${GOOGLE_API_KEY}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static Address geocode(Double lat, Double lon) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            String requestUri = GEOCODING_RESOURCE + "?latlng=" + lat + "," + lon + "&key=" + API_KEY;

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(requestUri))
                    .timeout(Duration.ofMillis(2000))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String response = extractFormattedAddress(httpResponse.body());

            if (response != null) {
                return new Address(response);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Geocoding error:", e);
        }
        return null;
    }

    private static String extractFormattedAddress(String jsonResponse) {

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            for (int i = 0; i < rootNode.size(); i++) {

                JsonNode formattedAddressNode = rootNode.path("results").get(i).path("formatted_address");

                if (formattedAddressNode.isTextual() && formattedAddressNode.textValue().contains(", ")) {

                    return formattedAddressNode.textValue();
                }
            }
        } catch (Exception e) {

            log.error(e.getMessage() + e.getClass() + e.getCause());
        }
        return null;
    }

}