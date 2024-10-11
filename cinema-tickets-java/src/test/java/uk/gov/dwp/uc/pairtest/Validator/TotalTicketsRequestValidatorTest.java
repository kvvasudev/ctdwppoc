package uk.gov.dwp.uc.pairtest.Validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;

public class TotalTicketsRequestValidatorTest {

    public TicketRequestValidator totalTicketValidator;

    @BeforeEach
    void setup() {
        totalTicketValidator = new TotalTicketsRequestValidator();
    }

    @Test
    void testValidateTicketRequestForNoError() {
        totalTicketValidator.validateTicketRequest(Arrays.asList(getTicketTypeRequestArray()));
    }

    @Test
    void testValidateTicketRequestWithChildOnlyError() {
        InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class,
                () -> totalTicketValidator.validateTicketRequest(
                        Arrays.asList(
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10))));

        assertEquals("Only a maximum of 25 tickets that can be purchased at a time" ,exception.getMessage());

    }

}