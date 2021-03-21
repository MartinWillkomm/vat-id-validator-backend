package de.mwillkomm.vatid.application;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = VatIdValidatorApplication.class)
@AutoConfigureMockMvc
public class VatIdValidatorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAutowiredMvc() {
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    public void testCallGreeting() throws Exception {
        this.mockMvc.perform(get("/greeting"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("hello world!")));
    }

    @ParameterizedTest
    @MethodSource("validVatIds")
    public void testCallGetResourceWithValidIds(String vatId) throws Exception {
        this.mockMvc.perform(get("/api/validation/v1/vatid/" + vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @ParameterizedTest
    @MethodSource("validVatIds")
    public void testCallPostResourceWithValidIds(String vatId) throws Exception {
        this.mockMvc.perform(post("/api/validation/v1/vatid/").param("value", vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @ParameterizedTest
    @MethodSource("inValidVatIds")
    public void testCallGetResourceWithInvalidIds(String vatId) throws Exception {
        this.mockMvc.perform(get("/api/validation/v1/vatid/" + vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }

    @ParameterizedTest
    @MethodSource("inValidVatIds")
    public void testCallPostResourceWithInvalidIds(String vatId) throws Exception {
        this.mockMvc.perform(post("/api/validation/v1/vatid/").param("value", vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
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
    public void testCallPostResourceWithFRId() throws Exception {
        this.mockMvc.perform(post("/api/validation/v1/vatid/").param("value", "FR06399859412"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void testCallPostResourceWithNLId() throws Exception {
        this.mockMvc.perform(post("/api/validation/v1/vatid/").param("value", "NL004495445B01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void testCallPostResourceWithGBId() throws Exception {
        this.mockMvc.perform(post("/api/validation/v1/vatid/").param("value", "GB000000000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }

    @Test
    public void testCallPostResourceWithDEId() throws Exception {
        this.mockMvc.perform(post("/api/validation/v1/vatid/").param("value", "DE114189102"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }

    static Stream<String> validVatIds() {
        return Stream.of("NL004495445B01", "NL123456782B12",
                "FR49348770561", "FR83404833048", "FR40303265045", "FR23334175221" /* TODO: not possible to verify with algorithm: "FRK7399859412", "FR4Z123456782" */,
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
                "1", "SOMEVALUE", "DE99999999");
    }


}
