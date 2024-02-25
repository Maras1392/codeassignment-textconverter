package com.homework.textconverter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.homework.textconverter.model.Sentence;
import com.homework.textconverter.utils.FilepathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.homework.textconverter.utils.Constants.*;
import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.revertAbbreviationChange;

@Slf4j
@Component
public class TextToXmlConverter extends AbstractConverter {

    /**
     * Load, parse and converts content of the file into the XML file in the same directory
     *
     * @param filepath path to the input file
     */
    public void convertToXml(String filepath) {
        ObjectMapper xmlMapper = new XmlMapper();
        String newFilepath = FilepathUtils.getNewFilepath(filepath, XML_EXTENSION);
        try (OutputStream outputStream = Files.newOutputStream(Path.of(newFilepath))) {
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".getBytes(DEFAULT_CHARSET));
            outputStream.write("<text>".getBytes(DEFAULT_CHARSET));

            processConversion(filepath, outputStream, xmlMapper);

            outputStream.write("</text>".getBytes(DEFAULT_CHARSET));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes parsed and formatted sentences into the XML file
     *
     * @param text         parsed text
     * @param outputStream OS used for writing
     * @param objectMapper CsvMapper used for mapping of the Sentence object to the CSV
     */
    @Override
    protected void write(String text, OutputStream outputStream, ObjectMapper objectMapper) throws IOException {
        text = revertAbbreviationChange(text);
        Sentence sentence = Sentence.builder()
                .id(getId())
                .words(Arrays.stream(text.split(WORD_SEPARATORS))
                        .filter(word -> !StringUtils.isBlank(word))
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .collect(Collectors.toList()))
                .build();

        outputStream.write(objectMapper.writeValueAsBytes(sentence));
    }

}

