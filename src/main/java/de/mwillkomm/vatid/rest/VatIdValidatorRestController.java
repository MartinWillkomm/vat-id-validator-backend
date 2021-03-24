package de.mwillkomm.vatid.rest;

import de.mwillkomm.vatid.json.ValidationResult;
import de.mwillkomm.vatid.service.VatValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            path = "/vatid2/{value}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isValidMappingBooleanGet(@PathVariable @NonNull String value) {
        try {
            return vatValidatorService.isValid(value);
        } catch (Exception e) {
            return false;
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/vatid/{value}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ValidationResult isValidMappingValidationResultGet(@PathVariable @NonNull String value) {
        try {
            return new ValidationResult("validation successful", false, vatValidatorService.isValid(value), value);
        } catch (Exception e) {
            return new ValidationResult(e.getMessage(), true, null, value);
        }
    }
}
