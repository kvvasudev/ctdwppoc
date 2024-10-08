package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.Validator.AdultTicketRequestValidator;
import uk.gov.dwp.uc.pairtest.Validator.TotalTicketsRequestValidator;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

    @InjectMocks
    TicketServiceImpl ticketService;

    @Mock
    TicketPaymentService ticketPaymentService;

    @Mock
    SeatReservationService seatReservationService;

    @Mock
    AdultTicketRequestValidator adultTicketRequestValidator;

    @Mock
    TotalTicketsRequestValidator totalTicketsRequestValidator;


    @Before
    public void setup() {
        ticketService = new TicketServiceImpl(ticketPaymentService, seatReservationService, Arrays.asList(adultTicketRequestValidator, totalTicketsRequestValidator));
    }

    @Test
    public void testPurchaseTickets() {
        doNothing().when(adultTicketRequestValidator).validateTicketRequest(anyList());
        doNothing().when(totalTicketsRequestValidator).validateTicketRequest(anyList());
        doNothing().when(ticketPaymentService).makePayment(anyLong(), anyInt());
        doNothing().when(seatReservationService).reserveSeat(anyLong(), anyInt());

        ticketService.purchaseTickets(1L, getTicketTypeRequestArray());

        Mockito.verify(adultTicketRequestValidator, times(1)).validateTicketRequest(anyList());
        Mockito.verify(totalTicketsRequestValidator, times(1)).validateTicketRequest(anyList());
        Mockito.verify(ticketPaymentService, times(1)).makePayment(anyLong(), anyInt());
        Mockito.verify(seatReservationService, times(1)).reserveSeat(anyLong(), anyInt());
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsForExceedingTicketCountError() {
        doThrow(InvalidPurchaseException.class).when(totalTicketsRequestValidator).validateTicketRequest(anyList());

        ticketService.purchaseTickets(1L, getTicketTypeRequestArray());

        verifyNoMoreInteractions(ticketPaymentService);
        verifyNoMoreInteractions(seatReservationService);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsForNoAdultError() {
        doThrow(InvalidPurchaseException.class).when(adultTicketRequestValidator).validateTicketRequest(anyList());

        ticketService.purchaseTickets(1L, getTicketTypeRequestArray());

        verifyNoMoreInteractions(ticketPaymentService);
        verifyNoMoreInteractions(seatReservationService);
    }
}
