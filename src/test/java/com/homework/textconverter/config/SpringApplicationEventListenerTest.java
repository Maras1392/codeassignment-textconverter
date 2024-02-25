package com.homework.textconverter.config;

import com.homework.textconverter.service.TextConversionProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringApplicationEventListenerTest {

    @Mock
    private ApplicationArguments applicationArguments;

    @Mock
    private TextConversionProcessor conversionProcessor;

    @InjectMocks
    private SpringApplicationEventListener listener;

    @Test
    void twoValidArguments() {
        // arrange
        when(applicationArguments.getSourceArgs()).thenReturn(new String[]{"somePath", "csv"});

        // act
        listener.onApplicationEvent(null);

        //assert
        verify(conversionProcessor).convertText("somePath", "csv");
    }

    @Test
    void oneValidArguments() {
        // arrange
        when(applicationArguments.getSourceArgs()).thenReturn(new String[]{"somePath"});

        // act
        listener.onApplicationEvent(null);

        // assert
        verifyNoInteractions(conversionProcessor);
    }

    @Test
    void noValidArguments() {
        // arrange
        when(applicationArguments.getSourceArgs()).thenReturn(new String[]{});

        // act
        listener.onApplicationEvent(null);

        // assert
        verifyNoInteractions(conversionProcessor);
    }
}