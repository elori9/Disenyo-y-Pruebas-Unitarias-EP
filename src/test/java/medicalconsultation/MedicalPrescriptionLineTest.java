package medicalconsultation;

import data.ProductID;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MedicalPrescriptionLineTest {

    private MedicalPrescriptionLine medicalPrescriptionLine;
    private ProductID productID;
    private TakingGuideline takingGuideline;

    @BeforeEach
    void setup() throws ProductIDException, IncorrectTakingGuidelinesException, PosologyException {
        this.productID = new ProductID("123456789012");
        this.takingGuideline = new TakingGuideline(
                dayMoment.DURINGBREAKFAST, 1, 10, 1, FqUnit.DAY, "instruction"
        );
    }


    @Test
    @DisplayName("Test verifies that the creation is correct")
    void testValidMedicalPrescriptionLineCreation() throws MedicalPrescriptionLineException {
        medicalPrescriptionLine = new MedicalPrescriptionLine(productID, takingGuideline);

        assertEquals(productID, medicalPrescriptionLine.getProductID());
        assertEquals(takingGuideline, medicalPrescriptionLine.getTakingGuideline());
        assertEquals(1, medicalPrescriptionLine.getQuantity());
    }


    @Test
    @DisplayName("Test verifies the validation of the setters")
    void testValidSetters() throws MedicalPrescriptionLineException, PosologyException,
            IncorrectTakingGuidelinesException, ProductIDException {
        ProductID p = new ProductID("023456789012");
        TakingGuideline t = new TakingGuideline(
                dayMoment.AFTERMEALS, 2, 10, 4, FqUnit.MONTH, "instruction"
        );

        medicalPrescriptionLine = new MedicalPrescriptionLine(productID, takingGuideline);

        medicalPrescriptionLine.setProductID(p);
        medicalPrescriptionLine.setTakingGuideline(t);
        medicalPrescriptionLine.setQuantity(5);

        assertEquals(p, medicalPrescriptionLine.getProductID());
        assertEquals(t, medicalPrescriptionLine.getTakingGuideline());
        assertEquals(5, medicalPrescriptionLine.getQuantity());
    }


    @Test
    @DisplayName("Test throws MedicalPrescriptionLineException if the ProductId is null")
    void testNullProdIdThrowsException() {
        assertThrows(MedicalPrescriptionLineException.class, () -> {
            new MedicalPrescriptionLine(null, takingGuideline);
        });
    }


    @Test
    @DisplayName("Test throws MedicalPrescriptionLineException for null TakingGuideline")
    void testNullTakingGuidelineThrowsException() {
        assertThrows(MedicalPrescriptionLineException.class, () -> {
            new MedicalPrescriptionLine(productID, null);
        });
    }

    @Test
    @DisplayName("Test throws MedicalPrescriptionLineException for negative or zero quantity")
    void testSetQuantityInvalidThrowsException() throws MedicalPrescriptionLineException {
        medicalPrescriptionLine = new MedicalPrescriptionLine(productID, takingGuideline);

        assertThrows(MedicalPrescriptionLineException.class, () -> {
            medicalPrescriptionLine.setQuantity(-1);
        });
        assertThrows(MedicalPrescriptionLineException.class, () -> {
            medicalPrescriptionLine.setQuantity(0);
        });
    }
}
