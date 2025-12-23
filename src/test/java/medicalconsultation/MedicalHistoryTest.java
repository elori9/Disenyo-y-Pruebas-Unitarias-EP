package medicalconsultation;

import data.HealthCardID;
import exceptions.HealthCardIDException;
import exceptions.IncorrectParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicalHistoryTest {

    @Test
    @DisplayName("Test verifies the correct initialization of the class")
    void testConstructorValid() throws HealthCardIDException, IncorrectParametersException {
        HealthCardID cip = new HealthCardID("123456789ABCDEFG");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 13);

        assertEquals(cip, medicalHistory.getCip());
        assertEquals(13, medicalHistory.getMembShipNumb());
        assertEquals("", medicalHistory.getHistory());
    }

    @Test
    @DisplayName("Test verifies that the constructor throws exception for null CIP")
    void testConstructorThrowsException() {
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(null, 13);
        });
    }

    @Test
    @DisplayName("Test verifies that the constructor throws exception for invalid membership number")
    void testConstructorThrowsExceptionForInvalidDoctor() throws HealthCardIDException {
        HealthCardID cip = new HealthCardID("123456789ABCDEFG");

        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(cip, -5);
        });

        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(cip, 0);
        });
    }

    @Test
    @DisplayName("Test verifies the addition of annotations to the history")
    void testAddAnnotations() throws HealthCardIDException, IncorrectParametersException {
        HealthCardID cip = new HealthCardID("987654321ZYXWVUT");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 33);

        String nota1 = "Patient with fever";
        String nota2 = "Paracetamol prescribed.";

        medicalHistory.addMedicalHistoryAnnotations(nota1);
        assertEquals(nota1, medicalHistory.getHistory());

        medicalHistory.addMedicalHistoryAnnotations(nota2);
        assertEquals(nota1 + "\n" + nota2, medicalHistory.getHistory());
    }

    @Test
    @DisplayName("Test verifies the modification of the assigned doctor")
    void testSetNewDoctor() throws HealthCardIDException, IncorrectParametersException {
        HealthCardID cip = new HealthCardID("123456789ABCDEFG");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 33);

        medicalHistory.setNewDoctor(777);
        assertEquals(777, medicalHistory.getMembShipNumb());
    }
}