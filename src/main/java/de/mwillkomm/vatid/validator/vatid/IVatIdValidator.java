package de.mwillkomm.vatid.validator.vatid;

public interface IVatIdValidator {

    /**
     * based on ISO 3166-1 alpha-2 Country Codes, except Greece (uses GR and EL).
     * Some validators can handle more than one country code and are suitable for more than one.
     *
     * There is no build-in-jdk-enum for this, Locale has a full iso 3166-1 list, but they
     * are also kept as Strings, so no benefit there to use them here.
     *
     * @return a String[] containing supported ISO 3166-1 Country Codes
     */
    String[] getSupportedCountryCodes();
    boolean isValidVatID(String input);

}
