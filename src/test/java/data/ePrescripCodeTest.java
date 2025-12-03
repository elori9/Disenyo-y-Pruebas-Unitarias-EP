package data;

import exceptions.ePrescripCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ePrescripCodeTest {

    private ePrescripCode ePrescripCode;

    @Test
    @DisplayName("Test throws ePrescripCodeException for null code")
    void nullCode() {
        Assertions.assertThrows(ePrescripCodeException.class, () -> {
            ePrescripCode = new ePrescripCode(null);
        });
    }

    @Test
    @DisplayName("Test throws ePrescripCodeException for non 16 characters length")
    void invalidLength() {
        // Less than 16 chars
        Assertions.assertThrows(ePrescripCodeException.class, () -> {
            ePrescripCode = new ePrescripCode("1");
        });
        // More than 16 chars
        Assertions.assertThrows(ePrescripCodeException.class, () -> {
            ePrescripCode = new ePrescripCode("1234567890123456789");
        });
    }

    @Test
    @DisplayName("Test throws ePrescripCodeException for invalid characters")
    void invalidChars() {
        Assertions.assertThrows(ePrescripCodeException.class, () -> {
            ePrescripCode = new ePrescripCode("-%&$#+;:!¡¿?,._*");
        });
    }

    @Test
    @DisplayName("Valid code")
    void validCode() throws ePrescripCodeException {
        String code = "12345678ABCDEGHJ";
        ePrescripCode = new ePrescripCode(code);

        Assertions.assertEquals(code, ePrescripCode.getPrescripCode());
    }
}
