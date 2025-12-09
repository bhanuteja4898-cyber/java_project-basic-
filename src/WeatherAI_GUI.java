import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class WeatherAI_GUI extends JFrame {

    private static final String API_KEY = "026f9e71bbd20786da6bcdc7329d256c";  // your key
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    JTextField cityField;
    JTextArea outputArea;

    public WeatherAI_GUI() {

        setTitle("AI Weather-Based Clothes Recommendation");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel cityLabel = new JLabel("Enter City:");
        cityField = new JTextField(20);
        JButton fetchButton = new JButton("Get Recommendation");

        fetchButton.addActionListener(this::fetchWeather);

        topPanel.add(cityLabel);
        topPanel.add(cityField);
        topPanel.add(fetchButton);
        add(topPanel, BorderLayout.NORTH);

        // ---------- OUTPUT AREA ----------
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setVisible(true);
    }

    // FREE API → Get current weather
    private JsonObject getCurrentWeather(String city) throws Exception {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + java.net.URLEncoder.encode(city, "UTF-8")
                + "&units=metric&appid=" + API_KEY;

        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(res.body(), JsonObject.class);
    }

    // FREE API → 3-hour forecast → gives POP (rain probability)
    private JsonObject getForecast(String city) throws Exception {

        String url = "https://api.openweathermap.org/data/2.5/forecast?q="
                + java.net.URLEncoder.encode(city, "UTF-8")
                + "&units=metric&appid=" + API_KEY;

        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(res.body(), JsonObject.class);
    }

    // Clothing Recommendation
    private String recommendClothes(double temp, double humidity, double rain, double wind) {

        StringBuilder rec = new StringBuilder();

        if (temp >= 32) rec.append("• Light Cotton T-shirt + Shorts\n");
        else if (temp >= 25) rec.append("• T-shirt + Jeans\n");
        else if (temp >= 18) rec.append("• Full-sleeve Shirt / Light Jacket\n");
        else rec.append("• Warm Clothes / Hoodie\n");

        if (rain > 60) rec.append("• Carry Umbrella / Raincoat\n");
        else if (rain > 30) rec.append("• Keep a Small Umbrella\n");

        if (humidity > 75) rec.append("• Prefer Breathable Cotton\n");
        else if (humidity < 30) rec.append("• Air Dry: Use Moisturizer\n");

        if (wind > 25) rec.append("• Wear Windcheater\n");

        if (rain > 50) rec.append("• Shoes: Waterproof Footwear\n");
        else rec.append("• Shoes: Sneakers\n");

        return rec.toString();
    }

    // When user clicks the button
    private void fetchWeather(ActionEvent event) {

        String city = cityField.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city!");
            return;
        }

        try {
            outputArea.setText("Fetching weather...\n");

            // ----------- CURRENT WEATHER (FREE API) ----------
            JsonObject current = getCurrentWeather(city);

            if (current == null || !current.has("main")) {
                outputArea.setText("Invalid city or API error.\n");
                return;
            }

            double temp = current.getAsJsonObject("main").get("temp").getAsDouble();
            double humidity = current.getAsJsonObject("main").get("humidity").getAsDouble();
            double wind = current.getAsJsonObject("wind").get("speed").getAsDouble() * 3.6;

            // ----------- FORECAST (FREE API) ----------
            JsonObject forecast = getForecast(city);
            JsonArray list = forecast.getAsJsonArray("list");

            double rainProb = 0;
            if (list.size() > 0) {
                JsonObject first = list.get(0).getAsJsonObject();
                if (first.has("pop")) {
                    rainProb = first.get("pop").getAsDouble() * 100;
                }
            }

            // ----------- DISPLAY ----------
            outputArea.setText(
                    "City: " + city + "\n" +
                            "Temperature: " + temp + "°C\n" +
                            "Humidity: " + humidity + "%\n" +
                            "Rain Probability: " + rainProb + "%\n" +
                            "Wind: " + wind + " km/h\n\n" +
                            "--- AI Recommendation ---\n" +
                            recommendClothes(temp, humidity, rainProb, wind)
            );

        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WeatherAI_GUI();
    }
}