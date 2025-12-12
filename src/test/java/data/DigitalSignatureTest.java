package data;

import exceptions.DigitalSignatureException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DigitalSignatureTest {

    private DigitalSignature digitalSignature;

    @Test
    @DisplayName("Test throws DigitalSignatureException for null code")
    void nullCode() {
        Assertions.assertThrows(DigitalSignatureException.class, () -> {
            digitalSignature = new DigitalSignature(null);
        });
    }

    @Test
    @DisplayName("Test throws DigitalSignatureException for non 16 characters length")
    void invalidLength() {
        // Less than 16 chars
        Assertions.assertThrows(DigitalSignatureException.class, () -> {
            digitalSignature = new DigitalSignature("1".getBytes());
        });
        // More than 16 chars
        Assertions.assertThrows(DigitalSignatureException.class, () -> {
            byte[] bytes = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            digitalSignature = new DigitalSignature(bytes);
        });
    }

    @Test
    @DisplayName("Valid Signature")
    void validCode() throws DigitalSignatureException {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 'A', 'B', 'C', 'D', 'E', 'G', 'H', 'J'};
        digitalSignature = new DigitalSignature(bytes);

        Assertions.assertEquals(bytes, digitalSignature.getDigitalSignature());
    }
}
