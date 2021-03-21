package de.mwillkomm.vatid.rest;

import de.mwillkomm.vatid.service.VatValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VatIdValidatorRestController {

    private static final String VERSION = "1";
    private static final String BASE_URL = "/api/validation/v" + VERSION;
    private final VatValidatorService vatValidatorService;

    @Autowired
    public VatIdValidatorRestController(VatValidatorService vatValidatorService) {
        this.vatValidatorService = vatValidatorService;
    }

    @GetMapping(value = "/greeting", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        return "hello world!";
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = BASE_URL + "/vatid/{value}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isValidMappingGet(@PathVariable @NonNull String value) {
        return vatValidatorService.isValid(value);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = BASE_URL + "/vatid",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isValidMappingPost(@RequestParam @NonNull String value) {
        return vatValidatorService.isValid(value);
    }
}
