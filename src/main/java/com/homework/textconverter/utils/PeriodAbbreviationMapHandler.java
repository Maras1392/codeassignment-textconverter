package com.homework.textconverter.utils;

import java.util.Map;

public class PeriodAbbreviationMapHandler {

    private static final Map<String, String> PERIOD_ENDING_ABBREVIATIONS = Map.of("Mr.", "Mr|\\/|", "Mrs.", "Mrs|\\/|", "Dr.", "Dr|\\/|", "Ph.D.", "Ph|\\/|D|\\/|", "Jr.", "Jr|\\/|", "Sr.", "Sr|\\/|", "etc.", "etc|\\/|", "St.", "St|\\/|");

    public static String replacePeriodEndingAbbreviations(String line) {
        for (String abb : PERIOD_ENDING_ABBREVIATIONS.keySet()) {
            if (line.contains(abb)) {
                line = line.replace(abb, PERIOD_ENDING_ABBREVIATIONS.get(abb));
            }
        }
        return line;
    }

    public static String revertAbbreviationChange(String sentence) {
        for (String abb : PERIOD_ENDING_ABBREVIATIONS.values()) {
            if (sentence.contains(abb)) {
                sentence = sentence.replace(abb, getKeyByValue(abb));
            }
        }
        return sentence;
    }

    private static String getKeyByValue(String value) {
        for (Map.Entry<String, String> entry : PERIOD_ENDING_ABBREVIATIONS.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return value;
    }
}
