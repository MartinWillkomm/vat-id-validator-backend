package de.mwillkomm.vatid.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mwillkomm.vatid.json.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VatIdValidatorApplication.class)
@AutoConfigureMockMvc
public class VatIdValidatorRestControllerTest {

    public static final String VALIDATION_V_1_VATID = "/validation/v1/vatid/";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAutowiredMvc() {
        Assertions.assertNotNull(mockMvc);
    }

    @ParameterizedTest
    @MethodSource("validVatIds")
    public void testCallResourceWithValidIds(String vatId) throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation successful", false, true, vatId);
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + vatId))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    @ParameterizedTest
    @MethodSource("inValidVatIds")
    public void testCallResourceWithInvalidIds(String vatId) throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation successful", false, false, vatId);
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + vatId))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    /**
     * Angeblich valide: FRK7399859412, beinhaltet aber check = K7,
     * was der implementierte Algorithmus gar nicht erzeugen kann.
     * Aber für die Siren 399859412 kommt laut[1] heraus:
     * FR6399859412
     *
     * laut dieser Website[2] lautet die Nummer noch anders:
     * FR06399859412
     *
     *
     * Ist FRK7399859412 wirklich valide? Warum lese ich haeufig, dass FRXX123456 mit XX = a-z definiert ist,
     * wenn das mit mod 97 gar nicht herauskommen kann?!
     *
     * [1] https://marosavat.com/manual/vat/france/siren-siret/
     * [2] https://www.societe.com/societe/altea-expertise-comptable-399859412.html
     */
    @Test
    public void testCallResourceWithFRId() throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation successful", false, true, "FR06399859412");
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + "FR06399859412"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    @Test
    public void testCallResourceWithNLId() throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation successful", false, true, "NL004495445B01");
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + "NL004495445B01"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    @Test
    public void testCallResourceWithGBId() throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation successful", false, true, "FR06399859412");
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + "FR06399859412"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    @Test
    public void testCallResourceWithDEId() throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation successful", false, true, "DE114189102");
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + "DE114189102"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    @Test
    public void testCallResourceWithUnknownContryCode() throws Exception {
        ValidationResult expectedResult = new ValidationResult("validation of country code SO currently not supported",true, null, "SOMEVALUE");
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + "SOMEVALUE"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    @Test
    public void testCallResourceWithInvalidInput() throws Exception {
        ValidationResult expectedResult = new ValidationResult("could not recognize country code",true, null, "1");
        this.mockMvc.perform(get(VALIDATION_V_1_VATID + "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expectedResult)));
    }

    static Stream<String> validVatIds() {
        return Stream.of("NL004495445B01", "NL123456782B12",
                "FR49348770561", "FR83404833048", "FR40303265045", "FR23334175221" /* TODO: not possible to validate with current algorithm: "FRK7399859412", "FR4Z123456782" */,
                "ATU13585627",
                "DK13585628", "DK54562519", "DK54562713",
                "DE195936843", "DE398517849", "DE812398835", "DE136695976", "DE146268101", "DE118579535", "DE175718116", "DE131170111", "DE812738852", "DE811871646", "DE133500876",
                "GBGD001", "GBGD499", "GBHA599", "GBHA999", "GB314159283", "GB980780684", "GB220430231", "GB446692026", "GB852913221", /* new vat-id mod97-55 */ "GB562235987" );
    }

    static Stream<String> inValidVatIds() {
        return Stream.of("NL123456789B90",
                "FR84323140391",
                "ATU13585626", "ATU13585628",
                "DK13585627", "DK13585629",
                "DE123456789", "DE987654321",
                "GBGD501", "GBGB11A", "GBHA499", "GBHB501", "GB562235933",
                "DE99999999");
    }
}
