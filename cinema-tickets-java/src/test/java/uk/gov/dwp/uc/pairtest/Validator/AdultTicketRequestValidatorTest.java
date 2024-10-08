package uk.gov.dwp.uc.pairtest.Validator;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Collections;

import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestList;

public class AdultTicketRequestValidatorTest {

    public TicketRequestValidator adultTicketValidator;

    @Before
    public void setup() {
        adultTicketValidator = new AdultTicketRequestValidator();
    }

    @Test
    public void testValidateTicketRequestForNoError() {
        adultTicketValidator.validateTicketRequest(getTicketTypeRequestList());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testValidateTicketRequestWithChildOnlyError() {
        adultTicketValidator.validateTicketRequest(
                Collections.singletonList(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10)));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testValidateTicketRequestWithInfantOnlyError() {
        adultTicketValidator.validateTicketRequest(
                Collections.singletonList(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5)));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testValidateTicketRequestWithNoAdultError() {
        adultTicketValidator.validateTicketRequest(
                Arrays.asList(
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 4)));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testValidateTicketRequestWithLessAdultError() {
        adultTicketValidator.validateTicketRequest(
                Arrays.asList(
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 4)));
    }

}