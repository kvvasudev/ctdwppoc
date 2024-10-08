package uk.gov.dwp.uc.pairtest.Validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.List;

import static uk.gov.dwp.uc.pairtest.util.TicketUtil.getTotalTickets;

public class TotalTicketsRequestValidator implements TicketRequestValidator {

    @Override
    public void validateTicketRequest(List<TicketTypeRequest> ticketTypeRequests) throws InvalidPurchaseException {

        long totalTicket = getTotalTickets(ticketTypeRequests);

        if (totalTicket > 25) {
            throw new InvalidPurchaseException("Only a maximum of 25 tickets that can be purchased at a time");
        }
    }
}
