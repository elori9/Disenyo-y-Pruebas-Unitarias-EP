package data;

import exceptions.ProductIDException;
import exceptions.SuggestionException;
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
    @DisplayName("Test throws SuggestionException for null productID")
    void nullProdID() {
        Assertions.assertThrows(SuggestionException.class, () -> {
            suggestion = new Suggestion(null, "indication", ActionType.ADD, 1);
        });
    }

    @Test
    @DisplayName("Test throws SuggestionException for null indication")
    void nullIndication() {
        Assertions.assertThrows(SuggestionException.class, () -> {
            suggestion = new Suggestion(productID, null, ActionType.DELETE, 1);
        });
    }

    @Test
    @DisplayName("Test throws SuggestionException for null ActionType")
    void nullActionType() {
        Assertions.assertThrows(SuggestionException.class, () -> {
            suggestion = new Suggestion(productID, "indication", null, 1);
        });
    }

    @Test
    @DisplayName("Test throws SuggestionException for negative dose")
    void negativeDose() {
        Assertions.assertThrows(SuggestionException.class, () -> {
            suggestion = new Suggestion(productID, "indication", ActionType.DELETE, -1);
        });
    }

    @Test
    @DisplayName("Test verifies that dose 0 is valid")
    void zeroDoseIsValid() {
        Assertions.assertDoesNotThrow(() -> {
            new Suggestion(productID, "indication", ActionType.DELETE, 0);
        });
    }

    @Test
    @DisplayName("Valid suggestion check attributes")
    void validSuggestion() throws SuggestionException {
        suggestion = new Suggestion(productID, "indication", ActionType.ADD, 1.0);

        // toString method
        String expectedString = "- ProductID{product code='123456789012'}\tADD\tindication\tNew dose: 1.0\n";
        Assertions.assertEquals(expectedString, suggestion.toString());

        // getters
        Assertions.assertEquals(productID, suggestion.getProductID());
        Assertions.assertEquals("indication", suggestion.getIndication());
        Assertions.assertEquals(ActionType.ADD, suggestion.getActionType());
        Assertions.assertEquals(1.0, suggestion.getNewDose());
    }
}
