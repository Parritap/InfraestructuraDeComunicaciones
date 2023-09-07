package HTTP;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static void main(String[] args) {
        System.out.println(getCurrentDate());
    }

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
}
