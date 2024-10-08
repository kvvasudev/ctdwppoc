package uk.gov.dwp.uc.pairtest.Validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.List;

public interface TicketRequestValidator {

    void validateTicketRequest(List<TicketTypeRequest> ticketTypeRequests) throws InvalidPurchaseException;

}
