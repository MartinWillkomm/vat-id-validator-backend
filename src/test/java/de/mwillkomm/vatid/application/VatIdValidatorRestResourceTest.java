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
public class VatIdValidatorRestResourceTest {

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
        this.mockMvc.perform(get("/validate/vatid/" + vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @ParameterizedTest
    @MethodSource("validVatIds")
    public void testCallPostResourceWithValidIds(String vatId) throws Exception {
        this.mockMvc.perform(post("/validate/vatid/").param("value", vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @ParameterizedTest
    @MethodSource("inValidVatIds")
    public void testCallGetResourceWithInvalidIds(String vatId) throws Exception {
        this.mockMvc.perform(get("/validate/vatid/" + vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }

    @ParameterizedTest
    @MethodSource("inValidVatIds")
    public void testCallPostResourceWithInvalidIds(String vatId) throws Exception {
        this.mockMvc.perform(post("/validate/vatid/").param("value", vatId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }

    @Test
    public void testCallPostResourceWithFRId() throws Exception {
        this.mockMvc.perform(post("/validate/vatid/").param("value", "FR83404833048"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void testCallPostResourceWithNLId() throws Exception {
        this.mockMvc.perform(post("/validate/vatid/").param("value", "NL004495445B01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    static Stream<String> validVatIds() {
        //TODO: angeblich auch Valide? Aber gehen mit dem o.g. Algorithmus nicht...
        return Stream.of("NL004495445B01", "NL123456782B12",
                "FR49348770561", "FR83404833048", "FR40303265045", "FR23334175221", //"FRK7399859412", "FR4Z123456782",
                "ATU13585627",
                "DK13585628", "DK54562519", "DK54562713",
                "DE195936843", "DE398517849", "DE812398835", "DE136695976", "DE146268101", "DE118579535", "DE175718116", "DE131170111", "DE812738852", "DE811871646", "DE133500876");
    }

    static Stream<String> inValidVatIds() {
        return Stream.of("NL123456789B90",
                "FR84323140391",
                "ATU13585626", "ATU13585628",
                "DK13585627", "DK13585629",
                "DE123456789", "DE987654321",
                "1", "SOMEVALUE", "DE99999999");
    }

    //TODO french id:
    //https://marosavat.com/manual/vat/france/siren-siret/
    // 399859412 Konvertiert zu: FR6399859412
}
