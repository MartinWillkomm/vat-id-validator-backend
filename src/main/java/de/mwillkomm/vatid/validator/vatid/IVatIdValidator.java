package de.mwillkomm.vatid.validator.vatid;

public interface IVatIdValidator {

    /**
     * based on ISO 3166-1 alpha-2 Country Codes, except Greece (uses GR and EL).
     * Some validators can handle more than one country code and are suitable for more than one.
     *
     * @return a String[] containing supported ISO 3166-1 Country Codes
     */
    String[] getSupportedCountryCodes();
    boolean isValidVatID(String input);

}
