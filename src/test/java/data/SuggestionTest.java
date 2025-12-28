package data;

import exceptions.ProductIDException;
import exceptions.SuggestionException;
import medicalconsultation.FqUnit;
import medicalconsultation.dayMoment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SuggestionTest {
    private Suggestion suggestion;
    private ProductID productID;

    @BeforeEach
    void setUp() throws ProductIDException {
        productID = new ProductID("123456789012");
    }

    @Test
    @DisplayName("Test throws SuggestionException for null ActionType")
    void nullActionType() {
        Assertions.assertThrows(SuggestionException.class, () -> {
            new Suggestion(null, productID, dayMoment.DURINGBREAKFAST, 1.0, 1.0, 1.0, FqUnit.DAY, "Instr");
        });
    }

    @Test
    @DisplayName("Test throws SuggestionException for null ProductID")
    void nullProductID() {
        Assertions.assertThrows(SuggestionException.class, () -> {
            new Suggestion(ActionType.ADD, null, dayMoment.AFTERMEALS, 1.0, 1.0, 1.0, FqUnit.DAY, "Instr");
        });
    }

    @Test
    @DisplayName("Test accepts nulls in optional fields")
    void optionalFieldsCanBeNull() {
        Assertions.assertDoesNotThrow(() -> {
            new Suggestion(ActionType.DELETE, productID, null, null, null, null, null, null);
        });
    }

    @Test
    @DisplayName("Valid suggestion check attributes and tabular format")
    void validSuggestion() throws SuggestionException {
        suggestion = new Suggestion(
                ActionType.ADD,
                productID,
                dayMoment.AFTERMEALS,
                15.0,
                1.0,
                1.0,
                FqUnit.DAY,
                "instructions"
        );

        String expectedString = "| ADD | ProductID{product code='123456789012'} | Day moment: AFTERMEALS       | Duration: 15.0  | Dose: 1.0   | Freq: 1.0   | Freq unit: DAY   | instructions";
        System.out.println(expectedString);
        Assertions.assertEquals(expectedString, suggestion.toString());


        Assertions.assertEquals(ActionType.ADD, suggestion.getActionType());
        Assertions.assertEquals(productID, suggestion.getProductID());
        Assertions.assertEquals(dayMoment.AFTERMEALS, suggestion.getDayMoment());
        Assertions.assertEquals(15.0, suggestion.getDuration());
        Assertions.assertEquals(1.0, suggestion.getDose());
        Assertions.assertEquals("instructions", suggestion.getInstructions());
    }
}
