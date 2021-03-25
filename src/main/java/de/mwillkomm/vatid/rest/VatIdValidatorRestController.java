package de.mwillkomm.vatid.rest;

import de.mwillkomm.vatid.json.ValidationResult;
import de.mwillkomm.vatid.service.VatValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    /*
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/vatid/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Boolean isValidMappingBooleanGet(@PathVariable @NonNull String id) {
        try {
            return vatValidatorService.isValid(id);
        } catch (Exception e) {
            return false;
        }
    }
    */

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/vatid/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ValidationResult isValidMappingValidationResult(@PathVariable String id) {
        try {
            return new ValidationResult("validation successful", false, vatValidatorService.isValid(id), id);
        } catch (Exception e) {
            return new ValidationResult(e.getMessage(), true, null, id);
        }
    }
}
