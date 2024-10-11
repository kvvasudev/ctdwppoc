package uk.gov.dwp.uc.pairtest.data;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public final class TicketRequestCreator {

    private TicketRequestCreator(){
    }

    public static TicketTypeRequest[] getTicketTypeRequestArray() {
        return  new TicketTypeRequest[] {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5),
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2) };
    }
}
