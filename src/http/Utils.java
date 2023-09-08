package http;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

    public static String fileToString(String pathToFile) {

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

    public static String fileToString(File file) {

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

  // public static void sendBinaryFile (File file , Socket socket) throws IOException {
  //     var dataOutput = new DataOutputStream(socket.getOutputStream());

  //     dataOutput.write();
  // }


    public static void sendFile(String pathToFile, Socket socket) {
        try {
            System.out.println("filename = " + pathToFile);
            File localFile = new File(pathToFile);
            BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));

            long size = localFile.length();
            System.out.println("size = " + size);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

            Thread.sleep(50);
            byte[] blockToSend = new byte[512];
            int bytesRead;
            while ((bytesRead = fromFile.read(blockToSend)) != -1) {
                toNetwork.write(blockToSend, 0, bytesRead);
            }

            // Close the streams and socket
            fromFile.close();
            toNetwork.close();
            printWriter.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTextFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int character;
            while ((character = reader.read()) != -1) {
                if (character < 32 && character != '\t' && character != '\n' && character != '\r') {
                    // If a non-printable ASCII character (excluding tabs, newline, and carriage return) is found, it's likely a binary file.
                    return false;
                }
            }
            // If no non-printable characters are found, it's likely a text file.
            return true;
        } catch (IOException e) {
            // Handle any IOExceptions (e.g., file not found).
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isTextFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int character;
            while ((character = reader.read()) != -1) {
                if (character < 32 && character != '\t' && character != '\n' && character != '\r') {
                    // If a non-printable ASCII character (excluding tabs, newline, and carriage return) is found, it's likely a binary file.
                    return false;
                }
            }
            // If no non-printable characters are found, it's likely a text file.
            return true;
        } catch (IOException e) {
            // Handle any IOExceptions (e.g., file not found).
            e.printStackTrace();
            return false;
        }
    }
}
