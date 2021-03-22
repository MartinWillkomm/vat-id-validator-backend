package de.mwillkomm.vatid.application;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VatIdValidatorApplication.class)
@AutoConfigureMockMvc
public class VatIdFromOdsDataFileTest {

    private static final Path TEST_RESOURCES = Paths.get("src", "test", "resources");

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("vatIdsFromOdsFile")
    public void testCallGetResourceWithValidIds(String vatId, Boolean expectedResult) throws Exception {
        this.mockMvc.perform(get("/validation/v1/vatid/" + vatId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedResult.toString())));
    }

    /**
     * Ods file containing a lot of ids and validation status. 440 of 459 tests are currently successful;
     * the others are not. French VAT Number with letters are unclear, and i suspect the failing German VAT Ids tests
     * to be wrong. Data was taken from here:
     *
     * https://www.braemoor.co.uk/software/vattest.php
     *
     * @return
     * @throws IOException
     */
    static Stream<Arguments> vatIdsFromOdsFile() throws IOException {
        Path odsFile = TEST_RESOURCES.resolve(Paths.get("ods", "VAT-IDS.ods"));

        List<Arguments> arguments = new ArrayList<>();
        if (Files.exists(odsFile)) {
            SpreadSheet spread = new SpreadSheet(odsFile.toFile());
            for (Sheet sheet : spread.getSheets()) {
                Range range = sheet.getDataRange();
                if (range.getNumColumns() < 2) {
                    break;
                }
                for (int i = 0; i < range.getNumRows(); i++) {
                    String vatId = String.valueOf(range.getCell(i, 0).getValue());
                    String expected = String.valueOf(range.getCell(i, 1).getValue());
                    arguments.add(Arguments.of(cleanUp(vatId), cleanUp(expected).equals("valid") ? true : false));
                }
            }
        }
        return arguments.stream();
    }

    /**
     * cleans up unwanted non-printable characters present in the ods, like 0xA0, 0x20 etc
     * @param input
     * @return a clean String
     */
    public static String cleanUp(String input) {
        return input.replaceAll("\\P{Print}", "").trim();
    }
}
