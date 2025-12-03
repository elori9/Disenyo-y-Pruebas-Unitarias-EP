package data;

import exceptions.ProductIDException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductIDTest {
    private ProductID productID;

    @Test
    @DisplayName("Test throws ProductIDException for null code")
    void nullCode() {
        Assertions.assertThrows(ProductIDException.class, () -> {
            productID = new ProductID(null);
        });
    }

    @Test
    @DisplayName("Test throws ProductIDException for non 12 characters length")
    void invalidLength() {
        // Less than 12 chars
        Assertions.assertThrows(ProductIDException.class, () -> {
            productID = new ProductID("1");
        });
        // More than 12 chars
        Assertions.assertThrows(ProductIDException.class, () -> {
            productID = new ProductID("1234567890123");
        });
    }

    @Test
    @DisplayName("Test throws ProductIDException for invalid characters")
    void invalidChars() {
        Assertions.assertThrows(ProductIDException.class, () -> {
            productID = new ProductID("ABCDE+;:!¡¿?");
        });
    }

    @Test
    @DisplayName("Valid code")
    void validCode() throws ProductIDException {
        String code = "123456789012";
        productID = new ProductID(code);

        Assertions.assertEquals(code, productID.getProductID());
    }
}
