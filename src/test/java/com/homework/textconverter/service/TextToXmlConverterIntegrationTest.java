package com.homework.textconverter.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(args = {"src/test/resources/small.in", "xml"})
public class TextToXmlConverterIntegrationTest {
    @AfterEach
    void checkAndRemoveGeneratedFiles() throws Exception {
        Path path = Path.of("src/test/resources/small.in");
        File[] generatedFiles = path.getParent().toFile().listFiles((dir, name) -> name.matches(".*\\d\\.xml$"));

        if (generatedFiles != null) {
            for (File file : generatedFiles) {
                Files.deleteIfExists(file.toPath());
            }
        }
    }

    @Test
    void test() throws Exception {
        // assert
        Path path = Path.of("src/test/resources/small.in");
        File[] generatedFiles = path.getParent().toFile().listFiles((dir, name) -> name.matches(".*\\d\\.xml$"));
        assertions(generatedFiles);
    }

    void assertions(File[] generatedFiles) throws IOException {
        Path originalPath = Path.of("src/test/resources/small.xml");
        File originalFile = originalPath.toFile();

        if (generatedFiles != null && generatedFiles.length == 1) {
            String original = FileUtils.readFileToString(originalFile, StandardCharsets.UTF_8);
            String generated = FileUtils.readFileToString(generatedFiles[0], StandardCharsets.UTF_8);
            assertThat(original)
                    .isEqualToIgnoringWhitespace(generated);
        }

    }
}
