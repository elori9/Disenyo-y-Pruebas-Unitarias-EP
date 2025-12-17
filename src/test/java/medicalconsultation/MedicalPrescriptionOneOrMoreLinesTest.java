package medicalconsultation;

import data.HealthCardID;
import data.ProductID;
import exceptions.*;
import medicalconsultation.interfaces.MedicalPrescriptionTestInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MedicalPrescriptionOneOrMoreLinesTest implements MedicalPrescriptionTestInterface {
    private MedicalPrescription medicalPrescription;
    private ProductID prod1;
    private ProductID prod2;

    private final String[] validInstructions1 = {"BEFOREBREAKFAST", "10", "5", "2", "HOUR", "instructions1"};
    private final String[] validInstructions2 = {"AFTERMEALS", "6", "10", "5", "DAY", "instructions2"};

    @Override
    public MedicalPrescription getMedicalPrescription() {
        return this.medicalPrescription;
    }

    @BeforeEach
    void setup() throws Exception {
        HealthCardID healthCardID = new HealthCardID("12345678ABCDEGHJ");
        medicalPrescription = new MedicalPrescription(healthCardID, 1, "illness");
        prod1 = new ProductID("123456789012");
        prod2 = new ProductID("023456789012");

        medicalPrescription.addLine(prod1, validInstructions1);
        medicalPrescription.addLine(prod2, validInstructions2);
    }

    @Override
    @Test
    @DisplayName("Add a new line to the medical prescription")
    public void addLine() throws Exception {
        ProductID prod3 = new ProductID("223456789012");
        medicalPrescription.addLine(prod3, validInstructions1);

        assertEquals(3, medicalPrescription.getLines().size());
    }

    @Override
    @Test
    @DisplayName("Modify doses from lines")
    public void modifyDoseInLine() throws Exception {
        medicalPrescription.modifyDoseInLine(prod1, 20);
        medicalPrescription.modifyDoseInLine(prod2, 10);

        List<MedicalPrescriptionLine> lines = medicalPrescription.getLines();

        MedicalPrescriptionLine lineProd1 = null;
        MedicalPrescriptionLine lineProd2 = null;

        // Search for the product in hash map
        for (MedicalPrescriptionLine line : lines) {
            if (line.getProductID().equals(prod1)) {
                lineProd1 = line;
            } else if (line.getProductID().equals(prod2)) {
                lineProd2 = line;
            }
        }
        // Verify the product is in
        assertNotNull(lineProd1);
        assertNotNull(lineProd2);

        assertEquals(20, lineProd1.getTakingGuideline().getPosology().getDose());
        assertEquals(10, lineProd2.getTakingGuideline().getPosology().getDose());
    }

    @Override
    @Test
    @DisplayName("Remove lines")
    public void removeLine() throws Exception {
        medicalPrescription.removeLine(prod1);
        assertEquals(1, medicalPrescription.getLines().size());

        medicalPrescription.removeLine(prod2);
        assertTrue(medicalPrescription.getLines().isEmpty());
    }

    @Override
    @Test
    @DisplayName("Checks if the ProductAlreadyInPrescriptionException throws adequately")
    public void getProductAlreadyInPrescriptionExceptionTest() {
        assertThrows(ProductAlreadyInPrescriptionException.class, () ->
                medicalPrescription.addLine(prod1, validInstructions1)
        );
        assertThrows(ProductAlreadyInPrescriptionException.class, () ->
                medicalPrescription.addLine(prod2, validInstructions2)
        );
    }

}
