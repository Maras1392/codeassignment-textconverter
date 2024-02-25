package com.homework.textconverter.objectmapper;

import com.homework.textconverter.model.Sentence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CsvMapperTest {

    private CsvMapper mapper;

    @BeforeAll
    public void init() {
        this.mapper = new CsvMapper();
    }

    @Test
    void writeValueAsBytes() {
        // arrange
        Sentence sentence = new Sentence(List.of("some", "fake", "Data"));

        // act
        byte[] bytes = mapper.writeValueAsBytes(sentence);

        byte[] bytes2 = mapper.writeValueAsBytes(new Sentence(List.of("another", "fake", "Data")));

        // assert
        assertThat(bytes)
                .containsSequence((", some, fake, Data" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        assertThat(bytes2)
                .containsSequence((", another, fake, Data" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));

    }
}