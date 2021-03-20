package de.mwillkomm.vatid.validator.vatid;

import de.mwillkomm.vatid.util.NLChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NLVatValidator implements IVatIdValidator {

    private static final String[] SUPPORTED_COUNTRY_CODES = new String[]{ "NL" };
    private static final Pattern PATTERN = Pattern.compile("^NL\\d{9}B\\d{2}$", Pattern.CASE_INSENSITIVE);
    private final NLChecksumUtil nlChecksumUtil;

    @Autowired
    public NLVatValidator(NLChecksumUtil nlChecksumUtil) {
        this.nlChecksumUtil = nlChecksumUtil;
    }

    @Override
    public String[] getSupportedCountryCodes() {
        return SUPPORTED_COUNTRY_CODES;
    }

    @Override
    public boolean isValidVatID(String input) {
        if (PATTERN.matcher(input).matches()) {
            return nlChecksumUtil.verify(input);
        }
        return false;
    }
}
