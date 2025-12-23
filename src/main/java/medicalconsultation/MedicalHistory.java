package medicalconsultation;

import data.HealthCardID;
import exceptions.IncorrectParametersException;

public class MedicalHistory {// A class that represents a medical history
    private HealthCardID cip; // the CIP of the patient
    private int membShipNumb; // the membership number of the family doctor
    private String history; // the diverse annotations in the patient’s HCE

    /**
     * Constructor
     * @param cip the CIP of the patient
     * @param memberShipNum the membership number of the family doctor
     * @throws IncorrectParametersException if the cip is null or memberShipNumber <= 0
     */
    public MedicalHistory (HealthCardID cip, int memberShipNum) throws IncorrectParametersException {
        if (cip ==null) {
            throw new IncorrectParametersException("HealthCardID cannot be null");
        }
        if (memberShipNum <= 0) {
            throw new IncorrectParametersException("Membership number must be greater than 0");
        }
        // Makes its inicialization
        this.cip =cip;
        this.membShipNumb = memberShipNum;
        this.history =  "";
    }

    /**
     * Adds new annotations to the patient history
     * @param annot the annotation to add
     */
    public void addMedicalHistoryAnnotations (String annot) {
        if (annot != null && !annot.isEmpty()) {
            if (this.history.isEmpty()) {
                this.history = annot;
            } else {
                this.history = this.history + "\n" + annot; // Separates annotations
            }
        }
    }

    public void setNewDoctor (int mshN) {
        this.membShipNumb = mshN;
    }
    // Getters
    public HealthCardID getCip() {
        return cip;
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getHistory() {
        return history;
    }
}

