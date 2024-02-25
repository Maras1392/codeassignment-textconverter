package com.homework.textconverter.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FilepathUtils {

    /**
     * Generate new filepath based on given file path and suffix
     *
     * @param filepath filepath to be processed
     * @param suffix   suffix to be added
     * @return new filepath based on given file path and suffix
     */
    public static String getNewFilepath(String filepath, String suffix) {
        int dotIndex = filepath.lastIndexOf(".");
        if (dotIndex != -1) {
            // If the file has an extension, insert the suffix before the extension
            return getFormattedFilepath(filepath.substring(0, dotIndex), suffix);
        } else {
            // If the file has no extension, simply append the suffix
            return getFormattedFilepath(filepath, suffix);
        }
    }

    /**
     * Formats filepath based on current time and suffix
     *
     * @param filepath filepath to be processed
     * @param suffix   suffix to be added
     * @return formateed filepath based on current time and suffix
     */
    private static String getFormattedFilepath(String filepath, String suffix) {
        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd_hhmmssnn");
        String formattedDateTime = zonedDateTime.format(formatter);

        return String.format("%s_%s%s",
                filepath,
                formattedDateTime,
                suffix);
    }
}
