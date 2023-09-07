package http;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Consumer;

public class Utils {

    static String getCurrentDate() {
        // Get the current date
        Date date = new Date();

        // Set the time zone to GMT
        TimeZone gmt = TimeZone.getTimeZone("GMT");

        // Create a SimpleDateFormat object
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(gmt);

        // Format the date
        return sdf.format(date);
    }

    public static String appendFileIntoString(String pathToFile) {

        // Initialize an empty string to store the file contents
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Append each line to the StringBuilder
                fileContent.append(line);// Add a newline character if needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert the StringBuilder to a String
        return fileContent.toString();
    }

    public static String appendFileIntoString(File file) {

        // Initialize an empty string to store the file contents
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Append each line to the StringBuilder
                fileContent.append(line);// Add a newline character if needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Convert the StringBuilder to a String
        return fileContent.toString();
    }

}
