package com.homework.textconverter.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.homework.textconverter.CsvMapper;
import com.homework.textconverter.model.Sentence;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.homework.textconverter.utils.Constants.*;
import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.replacePeriodEndingAbbreviations;
import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.revertAbbreviationChange;

@Slf4j
@Component
public class TextParser {

    private static int maxWordsCounter = 0;
    private static synchronized void count(int wordsSize) {
        maxWordsCounter = Math.max(maxWordsCounter, wordsSize);
    }

    private static synchronized int getMaxWordsCount() {
        return maxWordsCounter;
    }
    public Map<Long, List<String>> convertToXml(String filepath) {
        Map<Long, List<String>> convertedSentences = new HashMap<>();
        String newFilepath = getNewFilepathXml(filepath);
        XmlMapper xmlMapper = new XmlMapper();
        try (OutputStream outputStream = Files.newOutputStream(Path.of(newFilepath))) {
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".getBytes(StandardCharsets.UTF_8));
            outputStream.write("<text>".getBytes(StandardCharsets.UTF_8));

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath, StandardCharsets.UTF_8))) {
                String line;
                String restText = "";
                while ((line = bufferedReader.readLine()) != null) {
                    restText = splitToSentences(line, restText, outputStream, xmlMapper);
                }
            } catch (
                    FileNotFoundException e) {
                System.out.println("ERROR: File could not be found!");
            } catch (
                    IOException e) {
                System.out.println("ERROR: Error triggered by readLine()");
            }
            outputStream.write("</text>".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedSentences;
    }

    private String splitToSentences(String newLine, String restLine, OutputStream outputStream, XmlMapper xmlMapper) throws IOException {
        // check abbreviations with period
        newLine = replacePeriodEndingAbbreviations(newLine);
        String line = restLine + " " + newLine;
        if (isEndingNewLine(newLine.trim(), line.trim(), outputStream, xmlMapper)) {
            return "";
        }

        String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);

        if (sentences.length < 1) {
            return line;
        }

        for (int i = 0; i < sentences.length - 1; i++) {
            write(sentences[i].trim(), outputStream, xmlMapper);
        }
        return sentences[sentences.length - 1];
    }


    private boolean isEndingNewLine(String newLine, String line, OutputStream outputStream, XmlMapper xmlMapper) throws IOException {
        Matcher matcher = sentenceEndingsPattern.matcher(newLine);
        if (matcher.find()) {
            String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);
            for (String sentence : sentences) {
                write(sentence.trim(), outputStream, xmlMapper);
            }
            return true;
        }
        return false;
    }

    private void write(String text, OutputStream outputStream, XmlMapper xmlMapper) throws IOException {
        text = revertAbbreviationChange(text);
        Sentence sentence = new Sentence(Arrays.stream(text.split(WORD_SEPARATORS))
                .filter(word -> !StringUtils.isBlank(word))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList()));
        outputStream.write(xmlMapper.writeValueAsBytes(sentence));
    }


    private String getNewFilepathXml(String filepath) {
        int dotIndex = filepath.lastIndexOf(".");
        if (dotIndex != -1) {
            // If the file has an extension, insert the suffix before the extension
            return filepath.substring(0, dotIndex) + "_new.xml";
        } else {
            // If the file has no extension, simply append the suffix
            return filepath + "_new.xml";
        }
    }

    public void convertToCsv(String filepath) {
        CsvMapper csvMapper = new CsvMapper();
        int maxWordsNumber = 0;
        String temporaryFilepath;
        try {
            temporaryFilepath = getTemporaryFilepath(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (OutputStream outputStream = Files.newOutputStream(Path.of(temporaryFilepath))) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath, StandardCharsets.UTF_8))) {
                String line;
                String restText = "";
                while ((line = bufferedReader.readLine()) != null) {
                    restText = splitToSentences(line, restText, outputStream, csvMapper);
                }
            } catch (
                    FileNotFoundException e) {
                System.out.println("ERROR: File could not be found!");
            } catch (
                    IOException e) {
                System.out.println("ERROR: Error triggered by readLine()");
            }
            outputStream.write("</text>".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newFilepath = getNewFilepath(filepath);
        try (OutputStream outputStream = Files.newOutputStream(Path.of(newFilepath))) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(temporaryFilepath, StandardCharsets.UTF_8))) {
                List<String> topLine = new ArrayList<>(maxWordsNumber);

                for (int i = 1; i <= getMaxWordsCount(); i++) {
                    topLine.add(("Word " + i));
                }

                String headerString = ", " + String.join(", ", topLine);
                outputStream.write(headerString.getBytes(StandardCharsets.UTF_8));

                outputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
                IOUtils.copy(bufferedReader, outputStream, StandardCharsets.UTF_8);
            } catch (
                    FileNotFoundException e) {
                System.out.println("ERROR: File could not be found!");
            } catch (
                    IOException e) {
                System.out.println("ERROR: Error triggered by readLine()");
            }
            outputStream.write("</text>".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String splitToSentences(String newLine, String restLine, OutputStream outputStream, CsvMapper csvMapper) throws IOException {
        // check abbreviations with period
        newLine = replacePeriodEndingAbbreviations(newLine);
        String line = restLine + " " + newLine;
        if (isEndingNewLine(newLine.trim(), line.trim(), outputStream, csvMapper)) {
            return "";
        }

        String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);

        if (sentences.length < 1) {
            return line;
        }

        for (int i = 0; i < sentences.length - 1; i++) {
            write(sentences[i].trim(), outputStream, csvMapper);
        }
        return sentences[sentences.length - 1];
    }


    private boolean isEndingNewLine(String newLine, String line, OutputStream outputStream, CsvMapper csvMapper) throws IOException {
        Matcher matcher = sentenceEndingsPattern.matcher(newLine);
        if (matcher.find()) {
            String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);
            for (String sentence : sentences) {
                write(sentence.trim(), outputStream, csvMapper);
            }
            return true;
        }
        return false;
    }

    private void write(String text, OutputStream outputStream, CsvMapper csvMapper) throws IOException {
        text = revertAbbreviationChange(text);
        Sentence sentence = new Sentence(Arrays.stream(text.split(WORD_SEPARATORS))
                .filter(word -> !StringUtils.isBlank(word))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList()));
        count(sentence.getWords().size());
        outputStream.write(csvMapper.writeValueAsBytes(sentence));
    }

    private String getNewFilepath(String filepath) {
        int dotIndex = filepath.lastIndexOf(".");
        if (dotIndex != -1) {
            // If the file has an extension, insert the suffix before the extension
            return filepath.substring(0, dotIndex) + "_new.csv";
        } else {
            // If the file has no extension, simply append the suffix
            return filepath + "_new.csv";
        }
    }

    private String getTemporaryFilepath(String filepath) throws IOException {
        int dotIndex = filepath.lastIndexOf(".");
        if (dotIndex != -1) {
            // If the file has an extension, insert the suffix before the extension
            return filepath.substring(0, dotIndex) + "_tmp.csv";
        } else {
            // If the file has no extension, simply append the suffix
            return filepath + "_tmp.csv";
        }
    }

}


//package com.homework.textconverter.service;
//
//import com.homework.textconverter.model.Sentence;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.stream.Collectors;
//
//import static com.homework.textconverter.utils.Constants.*;
//import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.replacePeriodEndingAbbreviations;
//import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.revertAbbreviationChange;
//
//@Slf4j
//@Service
//public class TextParser {
//
//    public Map<Long, List<String>> parseTextInput(String filepath) {
//        Map<Long, List<String>> convertedSentences = new HashMap<>();
//        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath, StandardCharsets.UTF_8))) {
//            String line;
//            String restText = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                restText = splitToSentences(line, restText, convertedSentences);
//            }
//        } catch (
//                FileNotFoundException e) {
//            System.out.println("ERROR: File could not be found!");
//        } catch (
//                IOException e) {
//            System.out.println("ERROR: Error triggered by readLine()");
//        }
//        return convertedSentences;
//    }
//
//    private String splitToSentences(String newLine, String restLine, Map<Long, List<String>> convertedSentences) {
//        // check abbreviations with period
//        newLine = replacePeriodEndingAbbreviations(newLine);
//        String line = restLine + " " + newLine;
//        if (isEndingNewLine(newLine.trim(), line.trim(), convertedSentences)) {
//            return "";
//        }
//
//        String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);
//
//        if (sentences.length < 1) {
//            return line;
//        }
//
//        for (int i = 0; i < sentences.length - 1; i++) {
//            write(sentences[i].trim(), convertedSentences);
//        }
//        return sentences[sentences.length - 1];
//    }
//
//
//    private boolean isEndingNewLine(String newLine, String line, Map<Long, List<String>> convertedSentences) {
//        Matcher matcher = sentenceEndingsPattern.matcher(newLine);
//        if (matcher.find()) {
//            String[] sentences = line.split(SENTENCE_SEPARATORS_REGEX);
//            for (String sentence : sentences) {
//                write(sentence.trim(), convertedSentences);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    private void write(String text, Map<Long, List<String>> convertedSentences) {
//        text = revertAbbreviationChange(text);
//        Sentence sentence = new Sentence(Arrays.stream(text.split(WORD_SEPARATORS))
//                .filter(word -> !StringUtils.isBlank(word))
//                .sorted(String.CASE_INSENSITIVE_ORDER)
//                .collect(Collectors.toList()));
//        convertedSentences.put(sentence.getId(), sentence.getWords());
//    }
//}
