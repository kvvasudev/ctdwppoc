package uk.gov.dwp.uc.pairtest.Validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.List;

import static uk.gov.dwp.uc.pairtest.util.TicketUtil.getNumberOfTicketsByType;

public class AdultTicketRequestValidator implements TicketRequestValidator {

    @Override
    public void validateTicketRequest(List<TicketTypeRequest> ticketTypeRequests) throws InvalidPurchaseException {

        int adultTickets = getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.ADULT);

        int infantTickets = getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.INFANT);

        if (adultTickets == 0 || infantTickets > adultTickets) {
            throw new InvalidPurchaseException("Accompanying Adults are needed for infants and children");
        }
    }


}
