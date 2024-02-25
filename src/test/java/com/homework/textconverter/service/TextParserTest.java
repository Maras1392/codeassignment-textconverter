package com.homework.textconverter.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TextParserTest {

    private final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    private TextParser parser;

    @BeforeAll
    void init() {
        this.parser = new TextParser();
    }

    @Test
    void parseLargeTextInput() throws Exception {
        Resource resource = RESOURCE_LOADER.getResource("large.in");
        Map<Long, List<String>> result = parser.convertToXml(resource.getFile().getPath());
        assertThat(result).isNotEmpty();
    }
    @Test
    void parseSmallTextInput() throws Exception {
        Resource resource = RESOURCE_LOADER.getResource("small.in");
        Map<Long, List<String>> result = parser.convertToXml(resource.getFile().getPath());
        assertThat(result).isNotEmpty();
    }
}