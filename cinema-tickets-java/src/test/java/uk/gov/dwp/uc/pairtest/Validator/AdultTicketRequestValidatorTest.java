package uk.gov.dwp.uc.pairtest.Validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;

public class AdultTicketRequestValidatorTest {

    public TicketRequestValidator adultTicketValidator;

    @BeforeEach
    void setup() {
        adultTicketValidator = new AdultTicketRequestValidator();
    }

    @Test
    void testValidateTicketRequestForNoError() {
        adultTicketValidator.validateTicketRequest(Arrays.asList(getTicketTypeRequestArray()));
    }

    @Test
    void testValidateTicketRequestWithChildOnlyError() {
        InvalidPurchaseException exception =
                Assertions.assertThrows(InvalidPurchaseException.class,
                        () -> adultTicketValidator.validateTicketRequest(
                                Collections.singletonList(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10))));
        assertEquals("Accompanying Adults are needed for infants and children", exception.getMessage());
    }

    @Test
    void testValidateTicketRequestWithInfantOnlyError() {
        InvalidPurchaseException exception =
                Assertions.assertThrows(InvalidPurchaseException.class,
                        () -> adultTicketValidator.validateTicketRequest(
                                Collections.singletonList(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5))));
        assertEquals("Accompanying Adults are needed for infants and children", exception.getMessage());
    }

    @Test
    public void testValidateTicketRequestWithNoAdultError() {
        InvalidPurchaseException exception =
                Assertions.assertThrows(InvalidPurchaseException.class,
                        () -> adultTicketValidator.validateTicketRequest(
                                Arrays.asList(
                                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5),
                                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 4))));
        assertEquals("Accompanying Adults are needed for infants and children", exception.getMessage());
    }

    @Test
    public void testValidateTicketRequestWithLessAdultError() {
        InvalidPurchaseException exception =
                Assertions.assertThrows(InvalidPurchaseException.class,
                        () -> adultTicketValidator.validateTicketRequest(
                                Arrays.asList(
                                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5),
                                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 4))));
        assertEquals("Accompanying Adults are needed for infants and children", exception.getMessage());
    }
}