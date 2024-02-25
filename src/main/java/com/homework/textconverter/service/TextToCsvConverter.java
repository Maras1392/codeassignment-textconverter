package com.homework.textconverter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.textconverter.objectmapper.CsvMapper;
import com.homework.textconverter.model.Sentence;
import com.homework.textconverter.utils.FilepathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.homework.textconverter.utils.Constants.*;
import static com.homework.textconverter.utils.PeriodAbbreviationMapHandler.revertAbbreviationChange;

@Slf4j
@Component
public class TextToCsvConverter extends AbstractConverter {

    private static int maxWordsCounter = 0;

    /**
     * Load, parse and converts content of the file into the CSV file in the same directory
     *
     * @param filepath path to the input file
     */
    public void convertToCsv(String filepath) {
        ObjectMapper objectMapper = new CsvMapper();

        Path temporaryFilePath = Path.of(FilepathUtils.getNewFilepath(filepath, "_tmp" + CSV_EXTENSION));
        try (OutputStream outputStream = Files.newOutputStream(temporaryFilePath)) {

            processConversion(filepath, outputStream, objectMapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        createCsvWithToplineCounted(filepath, temporaryFilePath);
    }

    /**
     * Creates new CSV file with the top line counted number of maximum words
     *
     * @param filepath          input file path
     * @param temporaryFilePath path to the temporary folder without top line
     */
    private static void createCsvWithToplineCounted(String filepath, Path temporaryFilePath) {
        String newFilepath = FilepathUtils.getNewFilepath(filepath, CSV_EXTENSION);
        try (OutputStream outputStream = Files.newOutputStream(Path.of(newFilepath));
             BufferedReader bufferedReader = new BufferedReader(new FileReader(temporaryFilePath.toString(), DEFAULT_CHARSET))) {

            String topline = createTopline();
            outputStream.write(topline.getBytes(DEFAULT_CHARSET));

            outputStream.write(System.lineSeparator().getBytes(DEFAULT_CHARSET));
            IOUtils.copy(bufferedReader, outputStream, DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        deleteTemporaryFile(temporaryFilePath);
    }

    /**
     * Creates top line like this example for maximum rods of 4:", Word 1, Word 2, Word 3, Word 4"
     *
     * @return created and formatted top line
     */
    private static String createTopline() {
        List<String> words = new ArrayList<>(getMaxWordsCount());

        for (int i = 1; i <= getMaxWordsCount(); i++) {
            words.add(("Word " + i));
        }

        String topline = ", " + String.join(", ", words);
        return topline;
    }

    /**
     * Deletes temporary file
     *
     * @param temporaryFilePath path to the temporary file to be deleted
     */
    private static void deleteTemporaryFile(Path temporaryFilePath) {
        try {
            Files.deleteIfExists(temporaryFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes parsed and formatted sentences into the CSV file
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

        count(sentence.getWords().size());
        outputStream.write(objectMapper.writeValueAsBytes(sentence));
    }

    /**
     * Updates maxWordCounter with the highest value
     *
     * @param wordsSize wordSize of actual sentence
     */
    private static synchronized void count(int wordsSize) {
        maxWordsCounter = Math.max(maxWordsCounter, wordsSize);
    }

    /**
     * Returns highest value of maxWordsCount
     *
     * @return highest value for maxWordsCount
     */
    private static synchronized int getMaxWordsCount() {
        return maxWordsCounter;
    }
}
