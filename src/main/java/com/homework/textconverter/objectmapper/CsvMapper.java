package com.homework.textconverter.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.textconverter.model.Sentence;

import java.nio.charset.StandardCharsets;

public class CsvMapper extends ObjectMapper {

    @Override
    public byte[] writeValueAsBytes(Object value) {
        Sentence sentence = (Sentence) value;

        return String.format("Sentence %s, %s%s",
                ((Sentence) value).getId(),
                String.join(", ", sentence.getWords()),
                System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
    }
}
