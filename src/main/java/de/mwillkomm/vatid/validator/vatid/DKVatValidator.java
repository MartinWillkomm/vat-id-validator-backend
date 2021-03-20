package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.DKChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DKVatValidator implements IVatIdValidator{

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "DK" };
    private static final Pattern PATTERN = Pattern.compile("^DK\\d{8}$", Pattern.CASE_INSENSITIVE);
    private final DKChecksumUtil dkChecksumUtil;

    @Autowired
    public DKVatValidator(DKChecksumUtil dkChecksumUtil) {
        this.dkChecksumUtil = dkChecksumUtil;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN.matcher(input).matches()) {
            return dkChecksumUtil.verify(input);
        }
        return false;
    }
}
