import java.io.*;
import java.util.*;

public class QuickLinkShortener {
    private static final String FILE_NAME = "url_mappings.txt";
    private static final Map<String, String> urlMap = new HashMap<>();
    private static final String BASE_URL = "http://short.ly/";

    public static void main(String[] args) {
        loadUrlMappings();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nQuickLink Shortener");
            System.out.println("1. Shorten URL");
            System.out.println("2. Retrieve Original URL");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    shortenUrl(scanner);
                    break;
                case 2:
                    retrieveUrl(scanner);
                    break;
                case 3:
                    saveUrlMappings();
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 3);
    }

    // Method to shorten a URL
    private static void shortenUrl(Scanner scanner) {
        System.out.print("Enter the long URL: ");
        String longUrl = scanner.nextLine();

        // Check if the URL is already shortened
        if (urlMap.containsValue(longUrl)) {
            String shortUrl = getKeyByValue(urlMap, longUrl);
            System.out.println("This URL is already shortened: " + BASE_URL + shortUrl);
        } else {
            String shortUrl = generateShortUrl();
            urlMap.put(shortUrl, longUrl);
            System.out.println("Shortened URL: " + BASE_URL + shortUrl);
        }
    }

    // Method to retrieve the original URL
    private static void retrieveUrl(Scanner scanner) {
        System.out.print("Enter the shortened URL: ");
        String shortUrl = scanner.nextLine().replace(BASE_URL, "");

        if (urlMap.containsKey(shortUrl)) {
            System.out.println("Original URL: " + urlMap.get(shortUrl));
        } else {
            System.out.println("Shortened URL not found.");
        }
    }

    // Method to generate a unique short URL
    private static String generateShortUrl() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder();

        // Generate a random 6-character string
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        // Ensure the short URL is unique
        while (urlMap.containsKey(shortUrl.toString())) {
            shortUrl.setCharAt(random.nextInt(6), characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }

    // Helper method to get key by value (long URL -> short URL)
    private static String getKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Method to save URL mappings to a file
    private static void saveUrlMappings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Error saving URL mappings: " + e.getMessage());
        }
    }

    // Method to load URL mappings from a file
    private static void loadUrlMappings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    urlMap.put(parts[0], parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            // File not found, start with an empty map
        } catch (IOException e) {
            System.out.println("Error loading URL mappings: " + e.getMessage());
        }
    }
}
