package com.homework.textconverter.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class TextConversionProcessor {

    private final TextToXmlConverter xmlConverter;
    private final TextToCsvConverter csvConverter;

    /**
     * Chooses correct process conversion based on conversion type
     *
     * @param filepath       path to the file which should be converted
     * @param conversionType type of Conversion
     */
    public void convertText(String filepath, String conversionType) {
        // in more cases switch makes sense
        if ("csv".equals(conversionType)) {

            log.info("Processing CSV conversion - started.");
            csvConverter.convertToCsv(filepath);
            log.info("Processing CSV conversion - finished.");

        } else if ("xml".equals(conversionType)) {

            log.info("Processing XML conversion - started.");
            xmlConverter.convertToXml(filepath);
            log.info("Processing XML conversion - finished.");

        } else {

            log.error("Invalid conversion type: {}", conversionType);
            throw new RuntimeException("Invalid conversion type.");
        }
    }


}
