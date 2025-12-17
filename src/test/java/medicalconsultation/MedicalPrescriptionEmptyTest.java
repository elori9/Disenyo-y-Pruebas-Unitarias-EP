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

class MedicalPrescriptionEmptyTest implements MedicalPrescriptionTestInterface {
    private MedicalPrescription medicalPrescription;
    private ProductID prod1;

    private final String[] validInstructions = {"BEFOREBREAKFAST", "10", "5", "2", "HOUR", "instructions"};

    @Override
    public MedicalPrescription getMedicalPrescription() {
        return this.medicalPrescription;
    }

    @BeforeEach
    void setUp() throws Exception {
        HealthCardID healthCardID = new HealthCardID("12345678ABCDEGHJ");
        medicalPrescription = new MedicalPrescription(healthCardID, 0, "illness");
        prod1 = new ProductID("123456789012");
    }

    private void addTestLine() throws Exception {
        medicalPrescription.addLine(prod1, validInstructions);
    }

    @Override
    @Test
    @DisplayName("Add a new line to the medical prescription")
    public void addLine() throws Exception {
        medicalPrescription.addLine(prod1, validInstructions);

        assertFalse(medicalPrescription.getLines().isEmpty());
    }

    @Override
    @Test
    @DisplayName("Modify a dose from a line")
    public void modifyDoseInLine() throws Exception {
        addTestLine();
        medicalPrescription.modifyDoseInLine(prod1, 10);
        List<MedicalPrescriptionLine> lines = medicalPrescription.getLines();
        // Get the first line because just have one line
        MedicalPrescriptionLine line = lines.get(0);
        float dose = line.getTakingGuideline().getPosology().getDose();

        assertEquals(10, dose);
    }

    @Override
    @Test
    @DisplayName("Create a new line and remove it")
    public void removeLine() throws Exception {
        addTestLine();
        medicalPrescription.removeLine(prod1);

        assertTrue(medicalPrescription.getLines().isEmpty());
    }

    @Override
    @Test
    @DisplayName("Checks if the ProductAlreadyInPrescriptionException throws adequately")
    public void getProductAlreadyInPrescriptionExceptionTest() throws Exception {
        addTestLine();
        assertThrows(ProductAlreadyInPrescriptionException.class, () ->
                medicalPrescription.addLine(prod1, validInstructions)
        );
    }

}
