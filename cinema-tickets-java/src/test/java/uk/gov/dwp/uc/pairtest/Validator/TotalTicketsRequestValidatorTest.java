package uk.gov.dwp.uc.pairtest.Validator;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestList;

public class TotalTicketsRequestValidatorTest {

    public TicketRequestValidator totalTicketValidator;

    @Before
    public void setup() {
        totalTicketValidator = new TotalTicketsRequestValidator();
    }

    @Test
    public void testValidateTicketRequestForNoError() {
        totalTicketValidator.validateTicketRequest(getTicketTypeRequestList());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testValidateTicketRequestWithChildOnlyError() {
        totalTicketValidator.validateTicketRequest(
                Arrays.asList(
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 10),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10)));
    }

}