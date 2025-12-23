package medicalconsultation;

import data.HealthCardID;
import exceptions.IncorrectParametersException;

public class MedicalHistory {// A class that represents a medical history
    private HealthCardID cip; // the CIP of the patient
    private int membShipNumb; // the membership number of the family doctor
    private String history; // the diverse annotations in the patient’s HCE

    /**
     * Constructor
     * @param cip Código de identificación del paciente
     * @param memberShipNum Número de colegiado del médico
     * @throws IncorrectParametersException si el cip és null
     */
    public MedicalHistory (HealthCardID cip, int memberShipNum) throws IncorrectParametersException {
        if (cip ==null) {
            throw new IncorrectParametersException("HealthCardID cannot be null");
        }
        // Makes its inicialization
        this.cip =cip;
        this.membShipNumb = memberShipNum;
        this.history =  "";
    }

    /**
     * Añade una nueva anotación al historial
     */
    public void addMedicalHistoryAnnotations (String annot) {
        if (annot != null && !annot.isEmpty()) {
            if (this.history.isEmpty()) {
                this.history = annot;
            } else {
                this.history = this.history + "\n" + annot; // Separamos las anotacions
            }
        }
    }
    /**
     * Modifica el médico de familia asignado
     */
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

