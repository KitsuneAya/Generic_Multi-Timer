package app;

/**
 * @author <a href="https://steamcommunity.com/id/KitsuneAya/">Ayaya</a>
 * @version 1.0
 */
public final class UnitConverter {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Time Conversion /////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Downwards Time Conversion

    // Hours
    public static int hoursToMinutes(int hours) {
        return hours * 60;
    }
    public static double hoursToMinutes(double hours) {
        return hours * 60;
    }
    public static int hoursToSeconds(int hours) {
        return minutesToSeconds(hoursToMinutes(hours));
    }
    public static double hoursToSeconds (double hours) {
        return minutesToSeconds(hoursToMinutes(hours));
    }
    public static int hoursToMilliseconds (int hours) {
        return secondsToMilliseconds(hoursToSeconds(hours));
    }
    public static double hoursToMilliseconds (double hours) {
        return secondsToMilliseconds(hoursToSeconds(hours));
    }

    // Minutes
    public static int minutesToSeconds(int minutes) {
        return minutes * 60;
    }
    public static double minutesToSeconds(double minutes) {
        return minutes * 60;
    }
    public static int minutesToMilliseconds(int minutes) {
        return secondsToMilliseconds(minutesToSeconds(minutes));
    }
    public static double minutesToMilliseconds(double minutes) {
        return secondsToMilliseconds(minutesToSeconds(minutes));
    }

    // Seconds
    public static int secondsToMilliseconds(int seconds) {
        return seconds * 1000;
    }
    public static double secondsToMilliseconds(double seconds) {
        return seconds * 1000;
    }

    // Upwards Time Conversion

    // Milliseconds
    public static double millisecondsToSeconds(int milliseconds) {
        return milliseconds / 1000.;
    }
    public static double millisecondsToSeconds(double milliseconds) {
        return milliseconds / 1000.;
    }
    public static double millisecondsToMinutes(int milliseconds) {
        return secondsToMinutes(millisecondsToSeconds(milliseconds));
    }
    public static double millisecondsToMinutes(double milliseconds) {
        return secondsToMinutes(millisecondsToSeconds(milliseconds));
    }
    public static double millisecondsToHours(int milliseconds) {
        return minutesToHours(millisecondsToMinutes(milliseconds));
    }
    public static double millisecondsToHours(double milliseconds) {
        return minutesToHours(millisecondsToMinutes(milliseconds));
    }

    // Seconds
    public static double secondsToMinutes(int seconds) {
        return seconds/60.;
    }
    public static double secondsToMinutes(double seconds) {
        return seconds/60.;
    }
    public static double secondsToHours(int seconds) {
        return minutesToHours(secondsToMinutes(seconds));
    }
    public static double secondsToHours(double seconds) {
        return minutesToHours(secondsToMinutes(seconds));
    }

    // Minutes
    public static double minutesToHours(int minutes) {
        return minutes / 60.;
    }
    public static double minutesToHours(double minutes) {
        return minutes / 60.;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Distance Conversion /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Metric to Metric

    // Imperial to Imperial

    // Metric to Imperial

    // Imperial to Metric

}
