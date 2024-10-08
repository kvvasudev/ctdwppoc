package uk.gov.dwp.uc.pairtest.util;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestList;

public class TicketUtilTest {

    @Test
    public void testGetNumberOfTicketsByTypeForEmptyList() {
        assertEquals(0, TicketUtil.getNumberOfTicketsByType(Collections.emptyList(), TicketTypeRequest.Type.ADULT));
    }

    @Test
    public void testGetNumberOfTicketsByType() {

        List<TicketTypeRequest> ticketTypeRequests = getTicketTypeRequestList();

        assertEquals(9, TicketUtil.getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.ADULT));
        assertEquals(5, TicketUtil.getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.CHILD));
        assertEquals(2, TicketUtil.getNumberOfTicketsByType(ticketTypeRequests, TicketTypeRequest.Type.INFANT));

    }

    @Test
    public void testGetTotalTicketsForEmptyList() {
        assertEquals(0, TicketUtil.getTotalTickets(Collections.emptyList()));
    }

    @Test
    public void testGetTotalTickets() {
        assertEquals(16, TicketUtil.getTotalTickets(getTicketTypeRequestList()));
    }

}