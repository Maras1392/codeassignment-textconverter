package com.homework.textconverter.utils;

import java.util.regex.Pattern;

public class Constants {

    public static final String SENTENCE_SEPARATORS_REGEX = "[.!?]";

    public static final String SENTENCE_ENDINGS_REGEX = "[.!?]$";

    public static final String WORD_SEPARATORS ="[\\s-,;:\\\"()\\[\\]{}<>]+";

    public static final Pattern sentenceEndingsPattern = Pattern.compile(SENTENCE_ENDINGS_REGEX);
}
