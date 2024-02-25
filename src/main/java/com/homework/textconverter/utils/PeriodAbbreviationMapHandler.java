package com.homework.textconverter.utils;

import java.util.Map;

public class PeriodAbbreviationMapHandler {

    private static final String PERIOD_SUB = "|\\/|";
    private static final Map<String, String> PERIOD_ENDING_ABBREVIATIONS = Map.of(
            "Mr.", "Mr" + PERIOD_SUB,
            "Mrs.", "Mrs" + PERIOD_SUB,
            "Dr.", "Dr" + PERIOD_SUB,
            "Ph.D.", "Ph" + PERIOD_SUB + "D" + PERIOD_SUB,
            "Jr.", "Jr" + PERIOD_SUB,
            "Sr.", "Sr" + PERIOD_SUB,
            "etc.", "etc" + PERIOD_SUB,
            "St.", "St" + PERIOD_SUB);

    /**
     * Replaces period ending abbreviations specified in PERIOD_ENDING_ABBREVIATIONS
     *
     * @param line line to be processed
     * @return processed line with changed abbreviations
     */
    public static String replacePeriodEndingAbbreviations(String line) {
        for (String abb : PERIOD_ENDING_ABBREVIATIONS.keySet()) {
            if (line.contains(abb)) {
                line = line.replace(abb, PERIOD_ENDING_ABBREVIATIONS.get(abb));
            }
        }
        return line;
    }

    /**
     * Reverts back abbreviations subs to period specified in PERIOD_ENDING_ABBREVIATIONS
     *
     * @param sentence sentence to be processed
     * @return sentence with original abbreviations
     */
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
