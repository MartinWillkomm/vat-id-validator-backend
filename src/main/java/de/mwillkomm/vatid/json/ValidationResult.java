package de.mwillkomm.vatid.json;

public class ValidationResult {

    private String message;
    private Boolean errorOccurred;
    private Boolean result;
    private String input;

    public ValidationResult() {

    }

    public ValidationResult(String message, Boolean errorOccurred, Boolean result, String input) {
        this.message = message;
        this.errorOccurred = errorOccurred;
        this.result = result;
        this.input = input;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(Boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "message='" + message + '\'' +
                ", errorOccurred=" + errorOccurred +
                ", result=" + result +
                ", input='" + input + '\'' +
                '}';
    }
}
