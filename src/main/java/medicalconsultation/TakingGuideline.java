package medicalconsultation;

import exceptions.IncorrectTakingGuidelinesException;
import exceptions.PosologyException;

public class TakingGuideline {
    private dayMoment dMoment;
    private float duration;
    private Posology posology;
    private String instructions;

    /**
     * Constructor.
     *
     * @param dM dayMoment
     * @param du duration
     * @param d  Dose
     * @param f  Frequency
     * @param fu Frequency Unit
     * @param i  instructions
     * @throws IncorrectTakingGuidelinesException if the day moment or instructions are null, or the duration is < 0
     * @throws PosologyException                  if the dose, frequency are < 0, or the frequency unit is null
     */
    public TakingGuideline(dayMoment dM, float du, float d, float f,
                           FqUnit fu, String i) throws IncorrectTakingGuidelinesException, PosologyException {
        this.setDMoment(dM);
        this.setDuration(du);
        this.setInstructions(i);
        this.posology = new Posology(d, f, fu);
    }


    public dayMoment getDMoment() {
        return dMoment;
    }

    public float getDuration() {
        return duration;
    }

    public Posology getPosology() {
        return posology;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setDMoment(dayMoment dMoment) throws IncorrectTakingGuidelinesException {
        if (dMoment == null) throw new IncorrectTakingGuidelinesException("DayMoment can't be null");
        this.dMoment = dMoment;
    }

    public void setDuration(float duration) throws IncorrectTakingGuidelinesException {
        if (duration < 0) throw new IncorrectTakingGuidelinesException("Duration can't be negative");
        this.duration = duration;
    }

    public void setPosology(Posology posology) throws PosologyException {
        if (posology == null) throw new PosologyException("Posology can not be null");
        this.posology = posology;
    }

    public void setInstructions(String instructions) throws IncorrectTakingGuidelinesException {
        if (instructions == null) throw new IncorrectTakingGuidelinesException("Instructions can't be null");
        this.instructions = instructions;
    }

}
