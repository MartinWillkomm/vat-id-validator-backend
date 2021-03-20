package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.ATChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ATVatValidator implements IVatIdValidator{

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "AT" };
    private static final Pattern PATTERN = Pattern.compile("^ATU\\d{8}$", Pattern.CASE_INSENSITIVE);
    private final ATChecksumUtil atChecksumUtil;

    @Autowired
    public ATVatValidator(ATChecksumUtil atChecksumUtil) {
        this.atChecksumUtil = atChecksumUtil;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN.matcher(input).matches()) {
            return atChecksumUtil.verify(input);
        }
        return false;
    }
}
