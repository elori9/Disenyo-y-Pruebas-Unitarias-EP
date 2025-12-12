package medicalconsultation;

import exceptions.PosologyException;

public class Posology {

    private float dose;
    private float freq;
    private FqUnit freqUnit;

    /**
     * Constructor.
     *
     * @param d Dose
     * @param f Frequency
     * @param u Frequency Unit
     */
    public Posology(float d, float f, FqUnit u) throws PosologyException {
        this.setDose(d);
        this.setFreq(f);
        this.setFreqUnit(u);
    }


    public float getDose() {
        return dose;
    }


    public float getFreq() {
        return freq;
    }


    public FqUnit getFreqUnit() {
        return freqUnit;
    }


    public void setDose(float dose) throws PosologyException {
        if (dose < 0) {
            throw new PosologyException("Dose can not be negative.");
        }
        this.dose = dose;
    }


    public void setFreq(float freq) throws PosologyException {
        if (freq <= 0) {
            throw new PosologyException("Frequency must be bigger than 0.");
        }
        this.freq = freq;
    }


    public void setFreqUnit(FqUnit freqUnit) throws PosologyException {
        if (freqUnit == null) {
            throw new PosologyException("Frequency Unit can not be null.");
        }
        this.freqUnit = freqUnit;
    }

}
