package uk.gov.dwp.uc.pairtest.Validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdultTicketRequestValidatorTest {

    public TicketRequestValidator adultTicketValidator;

    @BeforeEach
    void setup() {
        adultTicketValidator = new AdultTicketRequestValidator();
    }

    @ParameterizedTest
    @MethodSource("adultTicketValidatorTestData")
    void should_return_optional_empty_when_input_is_valid(final String testCategory, final boolean throwsException,
                                                          final Stream<String> taskParamsStream) {
        List<String> testCategoriesWithoutException = Arrays.asList("SufficientAdultsForInfants", "MatchingAdultsWithInfants", "MatchingAdultsForChildren", "AdultsOnly", "SufficientAdultsForChildren", "SufficientAdults", "MatchingAdults");
        List<String> testCategoriesWithException = Arrays.asList("LessAdultsForInfants", "InsufficientAdults");
        List<TicketTypeRequest> ticketTypeRequests = new ArrayList<>();
        taskParamsStream.forEach( tps -> {
            String[] data = tps.split(",");
            ticketTypeRequests.add(new TicketTypeRequest(TicketTypeRequest.Type.valueOf(data[0]), Integer.parseInt(data[1])));
        });

        if (throwsException) {
            InvalidPurchaseException exception = Assertions.assertThrows(InvalidPurchaseException.class,
                                () -> adultTicketValidator.validateTicketRequest(ticketTypeRequests));
            assertEquals("Accompanying Adults are needed for infants and children", exception.getMessage());
            assertTrue(testCategoriesWithException.stream().anyMatch(x -> x.equals(testCategory) ));
        } else {
            adultTicketValidator.validateTicketRequest(ticketTypeRequests);
            assertTrue(testCategoriesWithoutException.stream().anyMatch(x -> x.equals(testCategory) ));
        }
    }

    private static Stream<Arguments> adultTicketValidatorTestData() {
        return Stream.of(
                Arguments.of("SufficientAdultsForInfants", false, Stream.of("ADULT,1", "INFANT,0")),
                Arguments.of("MatchingAdultsWithInfants", false, Stream.of("ADULT,2", "INFANT,2")),
                Arguments.of("LessAdultsForInfants", true, Stream.of("ADULT,1", "INFANT,3")),

                Arguments.of("MatchingAdultsForChildren", false, Stream.of("ADULT,2", "CHILD,2")),
                Arguments.of("AdultsOnly", false, Stream.of("ADULT,2", "CHILD,0")),
                Arguments.of("SufficientAdultsForChildren", false, Stream.of("ADULT,2", "CHILD,3")),

                Arguments.of("SufficientAdults", false, Stream.of("ADULT,3", "INFANT,1", "CHILD,1")),
                Arguments.of("MatchingAdults", false, Stream.of("ADULT,3", "INFANT,3", "CHILD,2")),
                Arguments.of("InsufficientAdults", true, Stream.of("ADULT,3", "INFANT,4", "CHILD,1"))
            );
    }

}
