package medicalconsultation;

import data.DigitalSignature;
import data.HealthCardID;
import data.ProductID;
import data.ePrescripCode;
import exceptions.*;

import java.util.*;


public class MedicalPrescription {
    private HealthCardID cip; // the healthcard ID of the patient
    private int membShipNumb; // the membership number of the family doctor
    private String illness; // illness associated
    private ePrescripCode prescCode; // the prescription code
    private Date prescDate; // the current date
    private Date endDate; // the date when the new treatment ends
    private DigitalSignature eSign; // the eSignature of the doctor
    private Map<ProductID, MedicalPrescriptionLine> lines;


    /**
     * Constructor.
     *
     * @param cip          the HealthCard cip
     * @param membShipNumb the membership number of the family doctor
     * @param illness      the illness associated
     * @throws MedicalPrescriptionException when cip or illness are null or when membShipNumb is negative
     */
    public MedicalPrescription(HealthCardID cip, int membShipNumb, String illness) throws MedicalPrescriptionException {
        if (cip == null) throw new MedicalPrescriptionException("CIP can't be null");
        if (membShipNumb < 0) throw new MedicalPrescriptionException("MembShipNumb must be > 0");
        if (illness == null) throw new MedicalPrescriptionException("illness can't be null");
        this.cip = cip;
        this.membShipNumb = membShipNumb;
        this.illness = illness;
        this.lines = new HashMap<>();
        this.prescDate = new Date();
    }


    /**
     * Method to add a Line on the medical prescription.
     *
     * @param prodID  the id of the product to be added
     * @param instruc the array with the instructions of the taking guideline
     * @throws ProductAlreadyInPrescriptionException when the product is already on the medical prescription
     * @throws IncorrectTakingGuidelinesException    when the instructions are different of 6 or have incorrect instructions
     * @throws MedicalPrescriptionException          when the prodID or the instruc are null
     * @throws PosologyException                     when the instructions related to posology are incorrect
     * @throws MedicalPrescriptionLineException      when takingGuideline created is null or when quantity is negative
     */
    public void addLine(ProductID prodID, String[] instruc) throws
            ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException,
            MedicalPrescriptionException, PosologyException,
            MedicalPrescriptionLineException {
        if (prodID == null) throw new MedicalPrescriptionException("Product can't be null");
        if (instruc == null) throw new MedicalPrescriptionException("Instructions can't be null");
        if (lines.containsKey(prodID))
            throw new ProductAlreadyInPrescriptionException("Product already on the prescription lines");

        // Check if instruct has the exact number of instructions
        if (instruc.length < 6) throw new IncorrectTakingGuidelinesException("Instructions incomplete");
        if (instruc.length > 6) throw new IncorrectTakingGuidelinesException("More instructions than ned");

        // Parse the instructions
        TakingGuideline takingGuideline = parseIntructions(instruc);

        // Add the line
        MedicalPrescriptionLine newLine = new MedicalPrescriptionLine(prodID, takingGuideline);
        lines.put(prodID, newLine);
    }

    private TakingGuideline parseIntructions(String[] instructions) throws IncorrectTakingGuidelinesException, PosologyException {
        try {
            dayMoment moment = dayMoment.valueOf(instructions[0]);
            float duration = Float.parseFloat(instructions[1]);
            float dose = Float.parseFloat(instructions[2]);
            float frequency = Float.parseFloat(instructions[3]);
            FqUnit fqUnit = FqUnit.valueOf(instructions[4]);
            String instruction = instructions[5];

            return new TakingGuideline(moment, duration, dose, frequency, fqUnit, instruction);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IncorrectTakingGuidelinesException("Format error: " + e);
        }
    }


    /**
     * Method to modify a dose Line on the medical prescription.
     *
     * @param prodID  the id of the product to be added
     * @param newDose the new quantity of the dose
     * @throws ProductNotInPrescriptionException when the product is not en the medical prescription
     * @throws MedicalPrescriptionException      when the prodId is null
     * @throws PosologyException                 when the new Dose is negative
     */
    public void modifyDoseInLine(ProductID prodID, float newDose) throws ProductNotInPrescriptionException, MedicalPrescriptionException, PosologyException {
        if (prodID == null) throw new MedicalPrescriptionException("Product can't be null");

        // Search for the product
        MedicalPrescriptionLine line = lines.get(prodID);
        if (line == null) throw new ProductNotInPrescriptionException("Product not in taking guide line");

        // Modify the line
        line.getTakingGuideline().getPosology().setDose(newDose);
    }


    /**
     * Method to add a Line on the medical prescription.
     *
     * @param prodID the id of the product to be added
     * @throws ProductNotInPrescriptionException when the product is not en the medical prescription
     * @throws MedicalPrescriptionException      when the prodId is null
     */
    public void removeLine(ProductID prodID) throws ProductNotInPrescriptionException, MedicalPrescriptionException {
        if (prodID == null) throw new MedicalPrescriptionException("Product can't be null");

        // Search for the product
        MedicalPrescriptionLine line = lines.get(prodID);
        if (line == null) throw new ProductNotInPrescriptionException("Product not in taking guide line");

        // Delete the product
        lines.remove(prodID);
    }


    // the getters and setters for some of the class members

    public List<MedicalPrescriptionLine> getLines() {
        return new ArrayList<>(lines.values());
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getIllness() {
        return illness;
    }

    public ePrescripCode getPrescCode() {
        return prescCode;
    }

    public DigitalSignature geteSign() {
        return eSign;
    }

    public HealthCardID getCip() {
        return cip;
    }

    public Date getPrescDate() {
        return prescDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) throws MedicalPrescriptionException {
        if (endDate == null) throw new MedicalPrescriptionException("End date can't be null");
        this.endDate = endDate;
    }

    public void setEsig(DigitalSignature eSign) throws MedicalPrescriptionException {
        if (eSign == null) throw new MedicalPrescriptionException("eSign can't be null");
        this.eSign = eSign;
    }

    public void setPrescCode(ePrescripCode code) throws MedicalPrescriptionException {
        if (code == null) throw new MedicalPrescriptionException("ePrescripCode can't be null");
        this.prescCode = code;
    }
}
