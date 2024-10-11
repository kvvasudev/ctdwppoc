package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.Validator.AdultTicketRequestValidator;
import uk.gov.dwp.uc.pairtest.Validator.TotalTicketsRequestValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;

public class TicketServiceImplTest {

    public TicketServiceImpl ticketService;

    @BeforeEach
    void setup() {
        ticketService = new TicketServiceImpl(
                            new TicketPaymentServiceImpl(),
                            new SeatReservationServiceImpl(),
                            Arrays.asList(new AdultTicketRequestValidator(), new TotalTicketsRequestValidator()));
    }

    @Test
    void testPurchaseTickets() {
        ticketService.purchaseTickets(1L, getTicketTypeRequestArray());
    }

    @Test
    void testPurchaseTicketsWithNoAdult() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2)));
    }

    @Test
    void testPurchaseTicketsWithExceedingTotalTickets() {
        Assertions.assertThrows(InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2)));
    }

}