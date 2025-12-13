package medicalconsultation;

import exceptions.IncorrectTakingGuidelinesException;
import exceptions.PosologyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TakingGuidelineTest {

    @Test
    @DisplayName("Test verifies that the creations are correct")
    void testValidTakingGuidelineCreation() throws PosologyException, IncorrectTakingGuidelinesException {
        TakingGuideline takingGuideline = new TakingGuideline(dayMoment.DURINGBREAKFAST, 3, 10, 2, FqUnit.DAY, "instruction");

        assertEquals(dayMoment.DURINGBREAKFAST, takingGuideline.getDMoment());
        assertEquals(3, takingGuideline.getDuration());
        assertEquals("instruction", takingGuideline.getInstructions());
        assertNotNull(takingGuideline.getPosology());
    }

    @Test
    @DisplayName("Test verifies the validation of the setters")
    void testValidSetters() throws IncorrectTakingGuidelinesException, PosologyException {
        TakingGuideline takingGuideline = new TakingGuideline(dayMoment.DURINGBREAKFAST, 3, 10, 2, FqUnit.DAY, "instruction");
        Posology posology = new Posology(10, 2, FqUnit.DAY);
        takingGuideline.setDMoment(dayMoment.DURINGBREAKFAST);
        takingGuideline.setDuration(2);
        takingGuideline.setPosology(posology);
        takingGuideline.setInstructions("i");

        assertEquals(dayMoment.DURINGBREAKFAST, takingGuideline.getDMoment());
        assertEquals(2, takingGuideline.getDuration());
        assertEquals(posology, takingGuideline.getPosology());
        assertEquals("i", takingGuideline.getInstructions());
    }

    @Test
    @DisplayName("Test throws IncorrectTakingGuidelinesException if the day moment is null")
    void testNullDayMomentThrowsException() {
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            new TakingGuideline(null, 3, 10, 2, FqUnit.DAY, "instruction");
        });
    }

    @Test
    @DisplayName("Test throws IncorrectTakingGuidelinesException for negative duration")
    void testNegativeDurationThrowsException() {
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            new TakingGuideline(dayMoment.DURINGBREAKFAST, -1, 10, 2, FqUnit.DAY, "instruction");
        });
    }

    @Test
    @DisplayName("Test throws IncorrectTakingGuidelinesException for null instructions")
    void testNullInstructionsThrowsException() {
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            new TakingGuideline(dayMoment.DURINGBREAKFAST, 1, 10, 2, FqUnit.DAY, null);
        });
    }

}
