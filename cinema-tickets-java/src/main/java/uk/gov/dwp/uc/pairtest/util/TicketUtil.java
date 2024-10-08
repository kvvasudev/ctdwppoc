package uk.gov.dwp.uc.pairtest.util;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.List;

public final class TicketUtil {

    private TicketUtil() {
    }

    public static int getNumberOfTicketsByType(List<TicketTypeRequest> ticketTypeRequests, TicketTypeRequest.Type type) {
        return ticketTypeRequests.stream()
                .filter(ticketTypeRequest -> type.equals(ticketTypeRequest.getTicketType()))
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
    }

    public static int getTotalTickets(List<TicketTypeRequest> ticketTypeRequests) {
        return ticketTypeRequests.stream()
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
    }

}
