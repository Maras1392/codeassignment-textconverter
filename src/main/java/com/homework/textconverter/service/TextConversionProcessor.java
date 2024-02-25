package com.homework.textconverter.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class TextConversionProcessor {

    private final TextParser textParser;
    private final XmlGenerator xmlGenerator;

    public void convertText(String filepath, String conversionType) {
        /*if ("csv".equals(conversionType)) {
            log.info("Processing CSV conversion.");
            textParser.convertToCsv(filepath);

            // create csv file
            return;
        } else*/ if ("xml".equals(conversionType)) {
            log.info("Processing XML conversion.");
            Map<Long, List<String>> convertedSentences = textParser.convertToXml(filepath);
            log.info("Text was loaded. Number of sentences: {}", convertedSentences.size());
//            xmlGenerator.convertToXml(filepath, convertedSentences);
            return;
        } else if (conversionType == null) {
            log.info("Processing CSV and XML conversion.");
            textParser.convertToCsv(filepath);
            // convert to csv file
//            xmlGenerator.convertToXml(filepath);
            return;
        }
        log.error("Invalid conversion type: {}", conversionType);
        throw new RuntimeException("Invalid conversion type.");
    }
}
