package de.mwillkomm.vatid.service;

import de.mwillkomm.vatid.exception.InvalidInputException;
import de.mwillkomm.vatid.exception.UnsupportedVATCountryCodeException;
import de.mwillkomm.vatid.validator.vatid.IVatIdValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VatValidatorService {

    private static final Logger logger = LoggerFactory.getLogger(VatValidatorService.class);
    private final List<? extends IVatIdValidator> list;
    private final Map<String, IVatIdValidator> validatorMap;

    @Autowired
    public VatValidatorService(List<? extends IVatIdValidator> list) {
        this.list = list;
        this.validatorMap = new HashMap<>(list.size());
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("found {} VAT-ID validators", list != null ? list.size() : 0);
        list.forEach(item -> Arrays.stream(item.getSupportedCountryCodes()).forEach(countryCode -> validatorMap.put(countryCode, item)));
    }

    public Boolean isValid(String input) throws Exception {
        if (input.length() < 2) {
            throw new InvalidInputException("could not recognize country code");
        }
        String countryCode = input.substring(0,2).toUpperCase();
        IVatIdValidator iVatIdValidator = validatorMap.get(countryCode);
        if (iVatIdValidator != null) {
            return iVatIdValidator.isValidVatID(input);
        } else {
            logger.info("could not find a suitable validator for {}", countryCode);
            throw new UnsupportedVATCountryCodeException(String.format("validation of country code %s currently not supported", countryCode));
        }
    }

}
