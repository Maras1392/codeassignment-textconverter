package com.homework.textconverter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.homework.textconverter.model.Sentence;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class XmlGenerator {

    public void convertToXml(String filepath) {
        XmlMapper xmlMapper = new XmlMapper();

        String newFilepath = getNewFilepath(filepath);
        try (OutputStream outputStream = Files.newOutputStream(Path.of(newFilepath))) {
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".getBytes(StandardCharsets.UTF_8));
            outputStream.write("<text>".getBytes(StandardCharsets.UTF_8));
//            writeIteratedSentences(sentences, outputStream, xmlMapper);
            outputStream.write("</text>".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNewFilepath(String filepath) {
        int dotIndex = filepath.lastIndexOf(".");
        if (dotIndex != -1) {
            // If the file has an extension, insert the suffix before the extension
            return filepath.substring(0, dotIndex) + "_new.xml";
        } else {
            // If the file has no extension, simply append the suffix
            return filepath + "_new.xml";
        }
    }

    private void writeIteratedSentences(Map<Long, List<String>> sentences, OutputStream outputStream, XmlMapper objectMapper) throws IOException {
        for (long i = 1; i < sentences.size() + 1; i++) {
            outputStream.write("<sentence>".getBytes(StandardCharsets.UTF_8));
            for (String word : sentences.get(i)) {
                outputStream.write(("<word>" + word + "</word>").getBytes(StandardCharsets.UTF_8));
            }
//            outputStream.write(objectMapper.writeValueAsBytes(new Sentence(sentences.get(i))));
            outputStream.write("</sentence>".getBytes(StandardCharsets.UTF_8));
        }
    }
}
