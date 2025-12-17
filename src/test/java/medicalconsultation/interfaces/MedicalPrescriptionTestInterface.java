package medicalconsultation.interfaces;

import data.DigitalSignature;
import data.HealthCardID;
import data.ProductID;
import data.ePrescripCode;
import exceptions.*;
import medicalconsultation.MedicalPrescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public interface MedicalPrescriptionTestInterface {

    MedicalPrescription getMedicalPrescription();

    default MedicalPrescription createFreshPrescription() throws MedicalPrescriptionException, HealthCardIDException {
        HealthCardID healthCardID = new HealthCardID("12345678ABCDEGHJ");
        return new MedicalPrescription(healthCardID, 1, "illness");
    }

    @Test
    @DisplayName("Test verifies that the creations are correct")
    default void testConstructorValid() throws MedicalPrescriptionException, HealthCardIDException {
        HealthCardID healthCardID = new HealthCardID("12345678ABCDEGHJ");
        MedicalPrescription mp = new MedicalPrescription(healthCardID, 1, "illness");

        assertNotNull(mp.getLines());
        assertEquals(healthCardID, mp.getCip());
        assertEquals("illness", mp.getIllness());
    }

    @Test
    void addLine() throws Exception;
    // Checks the addLine() method

    @Test
    void modifyDoseInLine() throws Exception;
    // Checks the modifyDoseInLine() method

    @Test
    void removeLine() throws Exception;
    // Checks the removeLine() method

    @Test
    @DisplayName("Test the setters")
    default void testSetters() throws Exception {
        MedicalPrescription mp = getMedicalPrescription();

        DigitalSignature digitalSignature = new DigitalSignature(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 'A', 'B', 'C', 'D', 'E', 'G', 'H', 'J'});
        ePrescripCode ePrescripCode = new ePrescripCode("12345678ABCDEGHJ");
        Date date = new Date();

        mp.setEsig(digitalSignature);
        mp.setEndDate(date);
        mp.setPrescCode(ePrescripCode);

        assertEquals(digitalSignature, mp.geteSign());
        assertEquals(date, mp.getEndDate());
        assertEquals(ePrescripCode, mp.getPrescCode());
    }


    // The exceptions test

    @Test
    void getProductAlreadyInPrescriptionExceptionTest() throws Exception;
    // Checks if the ProductAlreadyInPrescriptionException throws adequately

    @Test
    @DisplayName("Checks if the ProductNotInPrescriptionException throws adequately")
    default void getProductNotInPrescriptionException() throws HealthCardIDException, MedicalPrescriptionException, ProductIDException {
        MedicalPrescription medicalPrescription = createFreshPrescription();
        ProductID prod = new ProductID("123456789012");
        // remove
        assertThrows(ProductNotInPrescriptionException.class, () ->
                medicalPrescription.removeLine(prod)
        );
        // modify
        assertThrows(ProductNotInPrescriptionException.class, () ->
                medicalPrescription.modifyDoseInLine(prod, 2)
        );
    }

    @Test
    @DisplayName("Checks if the IncorrectTakingGuidelinesException throws adequately")
    default void getIncorrectTakingGuidelinesExceptionTest() throws HealthCardIDException, MedicalPrescriptionException, ProductIDException{
        MedicalPrescription medicalPrescription = createFreshPrescription();
        ProductID prod = new ProductID("123456789012");


        assertThrows(IncorrectTakingGuidelinesException.class, () ->
                medicalPrescription.addLine(prod, new String[]{"1"})
        );
        assertThrows(IncorrectTakingGuidelinesException.class, () ->
                medicalPrescription.addLine(prod, new String[]{"1", "2", "3", "4", "5", "6", "7"})
        );
        assertThrows(IncorrectTakingGuidelinesException.class, () ->
                medicalPrescription.addLine(prod, new String[]{"UNREALCONSTANT", "0", "5", "2", "HOUR", "instructions"})
        );
    }

    @Test
    @DisplayName("Checks if the MedicalPrescriptionException creating a new object throws adequately")
    default void getMedicalPrescriptionExceptionOnConstructor() throws HealthCardIDException {
        // CIP NULL on creation
        assertThrows(MedicalPrescriptionException.class, () -> {
            new MedicalPrescription(null, 1, "illness");
        });

        HealthCardID healthCardID = new HealthCardID("12345678ABCDEGHJ");

        // MembShipNUmb < 0
        assertThrows(MedicalPrescriptionException.class, () -> {
            new MedicalPrescription(healthCardID, -1, "illness");
        });

        // Illness null
        assertThrows(MedicalPrescriptionException.class, () -> {
            new MedicalPrescription(healthCardID, 1, null);
        });
    }

    @Test
    @DisplayName("Throws MedicalPrescriptionException if the arguments are null")
    default void testNullArgumentsInMethodsThrowsException() throws MedicalPrescriptionException, HealthCardIDException, ProductIDException {
        MedicalPrescription medicalPrescription = createFreshPrescription();
        ProductID prod = new ProductID("123456789012");

        // addLine null
        assertThrows(MedicalPrescriptionException.class, () ->
                medicalPrescription.addLine(null, new String[]{})
        );

        assertThrows(MedicalPrescriptionException.class, () ->
                medicalPrescription.addLine(prod, null)
        );

        // removeLine null
        assertThrows(MedicalPrescriptionException.class, () ->
                medicalPrescription.removeLine(null)
        );

        // modify dose null
        assertThrows(MedicalPrescriptionException.class, () ->
                medicalPrescription.modifyDoseInLine(null, 1)
        );
    }
}
