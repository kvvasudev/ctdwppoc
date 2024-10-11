package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.Validator.AdultTicketRequestValidator;
import uk.gov.dwp.uc.pairtest.Validator.TotalTicketsRequestValidator;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.data.TicketRequestCreator.getTicketTypeRequestArray;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    TicketPaymentService ticketPaymentService;

    @Mock
    SeatReservationService seatReservationService;

    @Mock
    AdultTicketRequestValidator adultTicketRequestValidator;

    @Mock
    TotalTicketsRequestValidator totalTicketsRequestValidator;

    @Captor
    ArgumentCaptor<Integer> totalCostCaptor;

    @Captor
    ArgumentCaptor<Integer> totalSeatsCaptor;


    @BeforeEach
    void setup() {
        ticketService = new TicketServiceImpl(ticketPaymentService, seatReservationService, Arrays.asList(adultTicketRequestValidator, totalTicketsRequestValidator));
    }

    @Test
    void testPurchaseTickets() {
        doNothing().when(adultTicketRequestValidator).validateTicketRequest(anyList());
        doNothing().when(totalTicketsRequestValidator).validateTicketRequest(anyList());
        doNothing().when(ticketPaymentService).makePayment(anyLong(), anyInt());
        doNothing().when(seatReservationService).reserveSeat(anyLong(), anyInt());

        ticketService.purchaseTickets(1L, getTicketTypeRequestArray());

        verify(adultTicketRequestValidator, times(1)).validateTicketRequest(anyList());
        verify(totalTicketsRequestValidator, times(1)).validateTicketRequest(anyList());
        verify(ticketPaymentService, times(1)).makePayment(anyLong(), totalCostCaptor.capture());
        assertEquals(300, totalCostCaptor.getValue());
        verify(seatReservationService, times(1)).reserveSeat(anyLong(), totalSeatsCaptor.capture());
        assertEquals(14, totalSeatsCaptor.getValue());
    }

    @Test
    void testPurchaseTicketsForExceedingTicketCountError() {
        doThrow(InvalidPurchaseException.class).when(totalTicketsRequestValidator).validateTicketRequest(anyList());

        assertThrows(InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L, getTicketTypeRequestArray()));

        verifyNoMoreInteractions(ticketPaymentService);
        verifyNoMoreInteractions(seatReservationService);
    }

    @Test
    void testPurchaseTicketsForNoAdultError() {
        doThrow(InvalidPurchaseException.class).when(adultTicketRequestValidator).validateTicketRequest(anyList());

        assertThrows(InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(1L, getTicketTypeRequestArray()));

        verifyNoMoreInteractions(ticketPaymentService);
        verifyNoMoreInteractions(seatReservationService);
    }
}
