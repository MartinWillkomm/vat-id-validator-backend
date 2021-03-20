package de.mwillkomm.vatid.validator.vatid;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GBVatValidator implements IVatIdValidator{

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "GB" };
    private static final Pattern PATTERN = Pattern.compile("^GB\\d{9}$|^GB\\d{12}$|^GB(GD|HA)\\d{3}$", Pattern.CASE_INSENSITIVE);

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        return PATTERN.matcher(input).matches();
    }
}
