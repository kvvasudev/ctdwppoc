package uk.gov.dwp.uc.pairtest.util;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;

public class TicketUtilTest {

    @Test
    void testGetNumberOfTicketsByTypeForEmptyList() {
        assertEquals(0, TicketUtil.getNumberOfTicketsByType(Collections.emptyList(), TicketTypeRequest.Type.ADULT));
    }

    @Test
    void testGetNumberOfTicketsByType() {

        List<TicketTypeRequest> ticketTypeRequests = Arrays.asList(getTicketTypeRequestArray());

        assertEquals(9, TicketUtil.getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.ADULT));
        assertEquals(5, TicketUtil.getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.CHILD));
        assertEquals(2, TicketUtil.getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.INFANT));

    }

    @Test
    void testGetTotalTicketsForEmptyList() {
        assertEquals(0, TicketUtil.getTotalTickets(Collections.emptyList()));
    }

    @Test
    void testGetTotalTickets() {
        assertEquals(16, TicketUtil.getTotalTickets(Arrays.asList(getTicketTypeRequestArray())));
    }

}