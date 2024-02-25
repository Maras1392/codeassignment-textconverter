package com.homework.textconverter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JacksonXmlRootElement(localName = "sentence")
public class Sentence {

    private static Long nextId = 1L;

    @JsonIgnore
    private Long id;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    private List<String> words;

    public Sentence(List<String> words) {
        this.words = words;
    }
}
