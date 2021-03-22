package de.mwillkomm.vatid.rest;

import de.mwillkomm.vatid.service.VatValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation/v1")
public class VatIdValidatorRestController {

    private final VatValidatorService vatValidatorService;

    @Autowired
    public VatIdValidatorRestController(VatValidatorService vatValidatorService) {
        this.vatValidatorService = vatValidatorService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/vatid/{value}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isValidMappingGet(@PathVariable @NonNull String value) {
        return vatValidatorService.isValid(value);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/vatid",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isValidMappingPost(@RequestParam @NonNull String value) {
        return vatValidatorService.isValid(value);
    }
}
