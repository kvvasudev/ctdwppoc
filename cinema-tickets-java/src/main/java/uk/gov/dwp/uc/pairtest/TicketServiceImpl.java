package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.Validator.TicketRequestValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.List;

import static uk.gov.dwp.uc.pairtest.util.TicketUtil.*;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private static final int CHILD_TICKET_PRICE = 15;
    private static final int ADULT_TICKET_PRICE = 25;

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final List<TicketRequestValidator> ticketRequestValidators;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService,
                             SeatReservationService seatReservationService,
                             List<TicketRequestValidator> ticketRequestValidators) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.ticketRequestValidators = ticketRequestValidators;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        List<TicketTypeRequest> ticketTypeRequestList = Arrays.asList(ticketTypeRequests);
        validateTicketRequests(ticketTypeRequestList);

        int totalAmountToPay = calculateTotalCost(ticketTypeRequestList);
        ticketPaymentService.makePayment(accountId, totalAmountToPay);

        int totalSeatsToAllocate = calculateTotalSeats(ticketTypeRequestList);
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);

    }

    private void validateTicketRequests(List<TicketTypeRequest> requests) throws InvalidPurchaseException {
        for (TicketRequestValidator validator : ticketRequestValidators) {
            validator.validateTicketRequest(requests);
        }
    }

    private int calculateTotalSeats(List<TicketTypeRequest> ticketTypeRequestList) {

        int adultTickets = getNumberOfTicketsByType(ticketTypeRequestList, TicketTypeRequest.Type.ADULT);
        int childTickets = getNumberOfTicketsByType(ticketTypeRequestList, TicketTypeRequest.Type.CHILD);

        return adultTickets + childTickets ;
    }

    private int calculateTotalCost(List<TicketTypeRequest> ticketTypeRequestList) {

        int adultTickets = getNumberOfTicketsByType(ticketTypeRequestList, TicketTypeRequest.Type.ADULT);
        int childTickets = getNumberOfTicketsByType(ticketTypeRequestList, TicketTypeRequest.Type.CHILD);

        return (adultTickets * ADULT_TICKET_PRICE) + (childTickets * CHILD_TICKET_PRICE);
    }

}
