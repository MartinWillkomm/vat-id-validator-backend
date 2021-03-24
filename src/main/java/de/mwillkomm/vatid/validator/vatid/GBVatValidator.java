package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.GBChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GBVatValidator implements IVatIdValidator{

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "GB" };
    private static final Pattern PATTERN = Pattern.compile("^GB\\d{9}$|^GB\\d{12}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_GOUVERNMENT = Pattern.compile("^GBGD\\d{3}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_HEALTHAUTHORITIES = Pattern.compile("^GBHA\\d{3}$", Pattern.CASE_INSENSITIVE);
    private final GBChecksumUtil gbChecksumUtil;

    @Autowired
    public GBVatValidator(GBChecksumUtil gbChecksumUtil) {
        this.gbChecksumUtil = gbChecksumUtil;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN_GOUVERNMENT.matcher(input).matches()) {
            return gbChecksumUtil.verifyGovernmentDepartment(input);
        }
        else if (PATTERN_HEALTHAUTHORITIES.matcher(input).matches()) {
            return gbChecksumUtil.verifyHealthAuthorities(input);
        }
        else if (PATTERN.matcher(input).matches()) {
            return gbChecksumUtil.verifyStandard(input);
        }
        return false;
    }
}
