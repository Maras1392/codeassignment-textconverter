package com.homework.textconverter.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodAbbreviationMapHandlerTest {

    @Test
    void replacePeriodEndingAbbreviations() {
        String line = "I was just standing there watching Mr. Young marching around";

        String result = PeriodAbbreviationMapHandler.replacePeriodEndingAbbreviations(line);

        assertThat(result)
                .isEqualTo("I was just standing there watching Mr|\\/| Young marching around");
    }

    @Test
    void revertAbbreviationChange() {
        String line = "I was just standing there watching Mr|\\/| Young marching around";

        String result = PeriodAbbreviationMapHandler.revertAbbreviationChange(line);

        assertThat(result)
                .isEqualTo("I was just standing there watching Mr. Young marching around");
    }

}