package com.homework.textconverter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;

import static com.homework.textconverter.utils.Constants.*;
import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.replacePeriodEndingAbbreviations;

@Slf4j
public abstract class AbstractConverter {

    private static long idNumber = 1;

    /**
     * Process conversion from the text
     *
     * @param filepath     path to the input file
     * @param outputStream OS used for writing
     * @param objectMapper OM used for parsing and formatting of the current object
     */
    protected void processConversion(String filepath, OutputStream outputStream, ObjectMapper objectMapper) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath, DEFAULT_CHARSET))) {
            checkFilePathExists(filepath);
            String line;
            String restText = "";
            while ((line = bufferedReader.readLine()) != null) {
                restText = splitToSentences(line, restText, outputStream, objectMapper);
            }
        } catch (IOException e) {
            log.error("Conversion process failed: {}", e.getMessage());
            throw new RuntimeException("Conversion process failed!", e);
        }
    }

    /**
     * Replace abbreviations with special sigh and split lines into the sentences
     *
     * @param newLine      Line to be splitted
     * @param restLine     Rest from processed line
     * @param outputStream OS for writing to the file
     * @param objectMapper OM used for parsing and formatting of the object
     * @return Splitted sentences
     */
    private String splitToSentences(String newLine, String restLine, OutputStream outputStream, ObjectMapper objectMapper) throws IOException {
        // check abbreviations with period
        newLine = replacePeriodEndingAbbreviations(newLine);
        String line = restLine + " " + newLine;
        if (isEndingNewLine(newLine.trim(), line.trim(), outputStream, objectMapper)) {
            return "";
        }

        String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);

        if (sentences.length < 1) {
            return line;
        }

        for (int i = 0; i < sentences.length - 1; i++) {
            write(sentences[i].trim(), outputStream, objectMapper);
        }
        return sentences[sentences.length - 1];
    }

    /**
     * Checks whether line is ending with full sentence. If yes, then it immediately writes to the file.
     *
     * @param newLine      line to be checked
     * @param line         United rest from previous line and a new line.
     * @param outputStream OS used for writing
     * @param objectMapper OM use for parsing and formatting
     * @return true if new line ends with period, false if not
     */
    private boolean isEndingNewLine(String newLine, String line, OutputStream outputStream, ObjectMapper objectMapper) throws IOException {
        Matcher matcher = sentenceEndingsPattern.matcher(newLine);
        if (matcher.find()) {
            String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);
            for (String sentence : sentences) {
                write(sentence.trim(), outputStream, objectMapper);
            }
            return true;
        }
        return false;
    }

    /**
     * Check whether File exists for given filepath
     *
     * @param filepath file path to be processed
     */
    protected void checkFilePathExists(String filepath) throws FileNotFoundException {
        if (!Files.exists(Path.of(filepath))) {
            throw new FileNotFoundException("File does not exists on given path: " + filepath);
        }
    }


    abstract void write(String text, OutputStream outputStream, ObjectMapper objectMapper) throws IOException;

    /**
     * idNumber counter
     *
     * @return actual idNumber
     */
    protected static synchronized long getId() {
        return idNumber++;
    }
}
