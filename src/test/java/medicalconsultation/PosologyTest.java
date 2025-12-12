package medicalconsultation;

import exceptions.PosologyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PosologyTest {

    @Test
    @DisplayName("Test verifies that the creations are correct")
    void testValidPosologyCreation() throws PosologyException {
        Posology p = new Posology(50, 4, FqUnit.HOUR);

        assertEquals(50, p.getDose());
        assertEquals(4, p.getFreq());
        assertEquals(FqUnit.HOUR, p.getFreqUnit());

    }

    @Test
    @DisplayName("Test verifies the validation of the setters")
    void testValidSetters() throws PosologyException {
        Posology p = new Posology(10, 1, FqUnit.DAY);

        p.setDose(50);
        p.setFreq(1);
        p.setFreqUnit(FqUnit.WEEK);

        assertEquals(50, p.getDose());
        assertEquals(1, p.getFreq());
        assertEquals(FqUnit.WEEK, p.getFreqUnit());
    }


    @Test
    @DisplayName("Test throws PosologyException if the dose is negative")
    void testNegativeDoseThrowsException() {
        assertThrows(PosologyException.class, () -> {
            new Posology(-30, 4, FqUnit.HOUR);
        });
    }


    @Test
    @DisplayName("Test throws PosologyException for negative frequency that are invalid")
    void testNegativeFrequencyThrowsException() {
        assertThrows(PosologyException.class, () -> {
            new Posology(100, -10, FqUnit.DAY);
        });
    }

    @Test
    @DisplayName("Test throws PosologyException for null time Unit")
    void testNullUnitThrowsException() {
        assertThrows(PosologyException.class, () -> {
            new Posology(10, 2, null);
        });
    }
}
