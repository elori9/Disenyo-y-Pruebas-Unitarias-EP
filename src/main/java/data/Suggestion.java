package data;

import exceptions.SuggestionException;
import medicalconsultation.FqUnit;
import medicalconsultation.dayMoment;

public class Suggestion {
    private final ActionType actionType;
    private final ProductID productID;

    // Optional depending on action type
    private final dayMoment dayMoment;
    private final Double duration;
    private final Double dose;
    private final Double frequency;
    private final FqUnit fqUnit;
    private final String instructions;


    /**
     * Constructor of the class
     *
     * @param actionType   the action type
     * @param productID    the ProductID
     * @param dayMoment    the day moment
     * @param duration     the duration
     * @param dose         the new Dose
     * @param frequency    the frequency
     * @param fqUnit       the frequency unit
     * @param instructions the instructions
     * @throws SuggestionException if the productID, indication or actionType are null or the newDose < 0
     */
    public Suggestion(ActionType actionType, ProductID productID, dayMoment dayMoment,
                      Double duration, Double dose, Double frequency,
                      FqUnit fqUnit, String instructions) throws SuggestionException {
        if (actionType == null) throw new SuggestionException("Action type can't be null");
        if (productID == null) throw new SuggestionException("ProductID can't be null");
        this.actionType = actionType;
        this.productID = productID;
        this.dayMoment = dayMoment;
        this.duration = duration;
        this.dose = dose;
        this.frequency = frequency;
        this.fqUnit = fqUnit;
        this.instructions = instructions;
    }

    // Make an easy format to parse using |
    public String toString() {
        // As there won't be more actions to scalate it use if
        StringBuilder out = new StringBuilder();

        // If smt is null will be '-'
        String pid = productID.toString();
        String mom = (dayMoment != null) ? dayMoment.toString() : "-";
        String dur = (duration == null) ? "-" : duration.toString();
        String dos = (dose == null) ? "-" : dose.toString();
        String freq = (frequency == null) ? "-" : frequency.toString();
        String unit = (fqUnit != null) ? fqUnit.toString() : "-";
        String instr = (instructions != null) ? instructions : "-";

        out.append(String.format("| %-2s | %-15s | Day moment: %-16s | Duration: %-5s | Dose: %-5s | Freq: %-5s | Freq unit: %-5s | %s",
                actionType, pid, mom, dur, dos, freq, unit, instr));

        return out.toString();
    }

    // getters
    public ActionType getActionType() {
        return actionType;
    }

    public ProductID getProductID() {
        return productID;
    }

    public dayMoment getDayMoment() {
        return dayMoment;
    }

    public Double getDuration() {
        return duration;
    }

    public Double getDose() {
        return dose;
    }

    public Double getFrequency() {
        return frequency;
    }

    public FqUnit getFqUnit() {
        return fqUnit;
    }

    public String getInstructions() {
        return instructions;
    }
}
