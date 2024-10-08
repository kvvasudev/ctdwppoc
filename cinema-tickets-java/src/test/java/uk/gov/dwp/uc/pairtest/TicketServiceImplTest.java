package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.Validator.AdultTicketRequestValidator;
import uk.gov.dwp.uc.pairtest.Validator.TotalTicketsRequestValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestList;

public class TicketServiceImplTest {

    public TicketServiceImpl ticketService;

    @Before
    public void setup() {
        ticketService = new TicketServiceImpl(
                            new TicketPaymentServiceImpl(),
                            new SeatReservationServiceImpl(),
                            Arrays.asList(new AdultTicketRequestValidator(), new TotalTicketsRequestValidator()));
    }

    @Test
    public void testPurchaseTickets() {
        ticketService.purchaseTickets(1L, getTicketTypeRequestArray());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithNoAdult() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsWithExceedingTotalTickets() {
        ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
    }

    @Test
    public void testCalculateTotalSeatsToAllocateForEmptyList() {
        assertEquals(0, ticketService.calculateTotalSeatsToAllocate(Collections.emptyList()));
    }

    @Test
    public void testCalculateTotalSeatsToAllocate() {
        assertEquals(14, ticketService.calculateTotalSeatsToAllocate(getTicketTypeRequestList()));
    }

    @Test
    public void testCalculateTicketAmountForEmptyList() {
        assertEquals(0, ticketService.calculateTicketAmount(Collections.emptyList()));
    }

    @Test
    public void testCalculateTicketAmount() {
        assertEquals(300, ticketService.calculateTicketAmount(getTicketTypeRequestList()));
    }

}