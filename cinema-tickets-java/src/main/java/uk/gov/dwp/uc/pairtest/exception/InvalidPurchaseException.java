package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {

    String errorMessage;

    public InvalidPurchaseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
