package data;

import exceptions.HealthCardIDException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HealthCardIDTest {

    private HealthCardID healthCardID;

    @Test
    @DisplayName("Test throws HealthCardIDException for null code")
    void nullCode() {
        Assertions.assertThrows(HealthCardIDException.class, () -> {
            healthCardID = new HealthCardID(null);
        });
    }


    

    @Test
    @DisplayName("Test throws HealthCardIDException for non 16 characters length")
    void invalidLength() {
        // Less than 16 chars
        Assertions.assertThrows(HealthCardIDException.class, () -> {
            healthCardID = new HealthCardID("1");
        });
        // More than 16 chars
        Assertions.assertThrows(HealthCardIDException.class, () -> {
            healthCardID = new HealthCardID("1234567890123456789");
        });
    }

    @Test
    @DisplayName("Test throws HealthCardIDException for invalid characters")
    void invalidChars() {
        Assertions.assertThrows(HealthCardIDException.class, () -> {
            healthCardID = new HealthCardID("-%&$#+;:!¡¿?,._*");
        });
    }

    @Test
    @DisplayName("Valid code")
    void validCode() throws HealthCardIDException {
        String code = "12345678ABCDEGHJ";
        healthCardID = new HealthCardID(code);

        Assertions.assertEquals(code, healthCardID.getPersonalID());
    }
}
