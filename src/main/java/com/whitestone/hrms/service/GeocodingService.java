package com.whitestone.hrms.service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeocodingService {

    // Nominatim API URL
    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/reverse?format=json&lat=%s&lon=%s&addressdetails=1";

    public String getLocationName(double lat, double lon) {
        try {
            // Construct the URL for the API request
            String geocodeUrl = String.format(NOMINATIM_API_URL, lat, lon);

            // Open a connection to the API
            HttpURLConnection connection = (HttpURLConnection) new URL(geocodeUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "YourAppName"); // Nominatim requires a User-Agent
            connection.connect();

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the response (this is a simple approach; you can use a JSON parser like Jackson or Gson)
            String responseString = response.toString();
            int addressStartIndex = responseString.indexOf("\"address\":") + 10;
            int addressEndIndex = responseString.indexOf("}", addressStartIndex);

            if (addressStartIndex != -1 && addressEndIndex != -1) {
                return responseString.substring(addressStartIndex, addressEndIndex);  // Return the address part of the JSON response
            } else {
                return "Location not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching location";
        }
    }
}
