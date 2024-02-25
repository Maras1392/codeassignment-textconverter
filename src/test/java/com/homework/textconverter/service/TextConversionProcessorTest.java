package com.homework.textconverter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextConversionProcessorTest {

    @Mock
    private TextToXmlConverter xmlConverter;

    @Mock
    private TextToCsvConverter csvConverter;

    @InjectMocks
    private TextConversionProcessor processor;

    @Test
    void csvConversion() {
        // act
        processor.convertText("somePath", "csv");

        // assert
        verify(csvConverter).convertToCsv("somePath");
    }

    @Test
    void xmlConversion() {
        // act
        processor.convertText("somePath", "xml");

        // assert
        verify(xmlConverter).convertToXml("somePath");
    }

    @Test
    void invalidConversionType() {
        // act
        Throwable thrown = catchThrowable(() -> processor.convertText("somePath", "pdf"));

        // assert
        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid conversion type.");

        verifyNoInteractions(csvConverter, xmlConverter);
    }
}