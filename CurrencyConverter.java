import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/YOUR_API_KEY/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input for base and target currencies
        System.out.print("Enter the base currency code (e.g., USD): ");
        String baseCurrency = scanner.next().toUpperCase();
        System.out.print("Enter the target currency code (e.g., EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        // Fetch and display exchange rate
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate == -1) {
            System.out.println("Error fetching exchange rate.");
            return;
        }
        System.out.println("Exchange Rate (" + baseCurrency + " to " + targetCurrency + "): " + exchangeRate);

        // Get user input for the amount to convert
        System.out.print("Enter the amount in " + baseCurrency + ": ");
        double amount = scanner.nextDouble();

        // Perform currency conversion
        double convertedAmount = amount * exchangeRate;

        // Display the result
        System.out.printf("%.2f %s is equal to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String urlStr = API_URL + baseCurrency;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
            JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");
            return conversionRates.get(targetCurrency).getAsDouble();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
