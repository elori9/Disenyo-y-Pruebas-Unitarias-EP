package medicalconsultation;

import data.*;
import exceptions.*;
import services.interfaces.DecisionMakingAI;
import services.interfaces.HealthNationalService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultationTerminal {
    // Services
    private HealthNationalService healthNationalService;
    private DecisionMakingAI ai;

    // Class members
    private HealthCardID cip;
    private DigitalSignature doctorSignature;
    private MedicalPrescription medicalPrescription;
    private MedicalHistory medicalHistory;
    private List<Suggestion> suggestions;
    private String lastAIAnswer;

    // States
    private States currentState = States.REVISION_NO_STARTED;



    /**
     * The constructor of the class
     */
    public ConsultationTerminal() {
        suggestions = new ArrayList<>();
    }

    /**
     * Starts a new visit for attending a chronic patient.
     * With HNS connection it downloads the medical history and the medical prescription
     *
     * @param cip     the HealthcardID cip
     * @param illness the illness
     * @throws HealthCardIDException           when the HealthCardID is not registered on the HNS
     * @throws AnyCurrentPrescriptionException when the patient doesn't have prescription associated to the illness
     * @throws ConnectException                when the connection with the HNS fails
     */

    public void initRevision(HealthCardID cip, String illness)
            throws HealthCardIDException, AnyCurrentPrescriptionException, ConnectException,
            IncorrectParametersException, MedicalPrescriptionException {
        this.cip = cip;
        this.medicalHistory = healthNationalService.getMedicalHistory(cip);
        this.medicalPrescription = healthNationalService.getMedicalPrescription(cip, illness);
        currentState = States.REVISION_STARTED;
        showHCE();
        showPrescription();
    }

    /**
     * The Doctor assess the patient during the visit
     *
     * @param assess assess
     * @throws ProceduralException if the prescription has not been loaded
     */
    public void enterMedicalAssessmentInHistory(String assess) throws ProceduralException {
        if (medicalPrescription == null && currentState != States.REVISION_STARTED) throw new ProceduralException("Medical prescription not initialized");
        medicalHistory.addMedicalHistoryAnnotations(assess);
        currentState = States.ASSESSMENT_ENTERED;
        showHCE();
    }

    /**
     * Starts the edition process of the medical prescription
     *
     * @throws ProceduralException if there was not entered the medical assessment in history
     */
    public void initMedicalPrescriptionEdition() throws ProceduralException {
        if (currentState != States.ASSESSMENT_ENTERED) {
            throw new ProceduralException("Medical prescription not initialized");
        }
        currentState = States.PRESCRIPTION_EDITING;
        showPrescriptionEdition();
    }

    /**
     * Starts the ia decision
     *
     * @throws AIException         if there is a problem with the ia
     * @throws ProceduralException if there was not initiated the medical prescription edition
     */
    public void callDecisionMakingAI() throws AIException, ProceduralException {
        if (currentState != States.PRESCRIPTION_EDITING)
            throw new ProceduralException("Not initiated the medical prescription edition");
        ai.initDecisionMakingAI();
        currentState = States.AI_CALLED;
        showIAHello();
    }

    /**
     * @param prompt the prompt
     * @throws ProceduralException if there was not initialized the call decision with ia
     * @throws BadPromptException  if the prompt is not enough clear
     */
    public void askAIForSuggest(String prompt) throws ProceduralException, BadPromptException {
        if (currentState != States.AI_CALLED)
            throw new ProceduralException("Not called the decision making ai");
        lastAIAnswer = ai.getSuggestions(prompt);
        showIASuggestionsTab();
        currentState = States.AI_ANSWERED;
    }

    /**
     * Extracts the IA responses by the doctor
     *
     * @throws ProceduralException if there was not asked fot ia suggestions
     * @throws BadPromptException  if the prompt is not enough clear
     */
    public void extractGuidelinesFromSugg() throws ProceduralException, BadPromptException {
        if (currentState != States.AI_ANSWERED)
            throw new ProceduralException("Not asked for the ai suggest");
        suggestions = ai.parseSuggest(lastAIAnswer);
        if (suggestions == null || suggestions.isEmpty())
            throw new BadPromptException("The prompt provided is not clear for the AI, so there where no suggestions found");
        showIASuggestions();
        currentState = States.GUIDELINES_EXTRACTED;
    }

    /**
     * Introduce the prodID and the instructions to the prescription
     *
     * @param prodID  the productID
     * @param instruc the instructions
     * @throws ProductAlreadyInPrescriptionException if the product was already in the prescription
     * @throws IncorrectTakingGuidelinesException    if the format of the taking guideline is incorrect or incomplete
     * @throws ProceduralException                   if there was not initiated the medical prescription edition
     */
    public void enterMedicineWithGuidelines(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException, ProceduralException,
            MedicalPrescriptionException, PosologyException, MedicalPrescriptionLineException {
        // Let introduce > 1 medicine (Although on DSS its just one)
        if (currentState != States.GUIDELINES_EXTRACTED && currentState != States.MEDICINE_ENTERED)
            throw new ProceduralException("Not extracted the guidelines form suggestion");
        medicalPrescription.addLine(prodID, instruc);
        currentState = States.MEDICINE_ENTERED;
        showPrescriptionCompleted();
    }

    /**
     * Modify the line of the prescription of the prodID, replacing it for the dose for the new dose
     *
     * @param prodID  the productID
     * @param newDose the new Dose
     * @throws ProductNotInPrescriptionException if the product does not from part of the treatment
     * @throws ProceduralException               if there was not entered the Medicine with Guidelines
     */
    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException, MedicalPrescriptionException, PosologyException, ProceduralException {
        if (currentState != States.MEDICINE_ENTERED)
            throw new ProceduralException("There was not entered the Medicine with Guidelines");
        medicalPrescription.modifyDoseInLine(prodID, newDose);
        currentState = States.DOSE_MODIFIED;
        showPrescriptionLineModified();
    }

    /**
     * Deletes the line of the medical prescription of the prodID of the treatment
     *
     * @param prodID the productID
     * @throws ProductNotInPrescriptionException if the product is no in the prescription
     * @throws ProceduralException               if there is not a medical prescription in process of edition
     */
    public void removeLine(ProductID prodID) throws
            ProductNotInPrescriptionException, MedicalPrescriptionException, ProceduralException {
        if (currentState != States.DOSE_MODIFIED)
            throw new ProceduralException("There was not modified the dose in line");
        medicalPrescription.removeLine(prodID);
        currentState = States.LINE_REMOVED;
        showRemovedLine();
    }

    /**
     * Introduce the date of the ending of the treatment
     *
     * @param date the date
     * @throws IncorrectEndingDateException if the date is not correct
     * @throws ProceduralException          if there is not a prescription line removed
     */
    public void enterTreatmentEndingDate(Date date) throws
            IncorrectEndingDateException, ProceduralException, MedicalPrescriptionException {
        if (currentState != States.LINE_REMOVED)
            throw new ProceduralException("There was not a remove line");

        if (incorrectDate(date))
            throw new IncorrectEndingDateException("The date is incorrect ");

        medicalPrescription.setEndDate(date);

        currentState = States.TREATMENT_ENTERED;
        showMedicalPrescriptionReadyToSign();
    }

    /**
     * @throws ProceduralException if there is no treatment ending date
     */
    public void finishMedicalPrescriptionEdition() throws ProceduralException {
        if (currentState != States.TREATMENT_ENTERED)
            throw new ProceduralException("There is not treatment ending date");

        currentState = States.MEDICAL_PRESCRIPTION_FINISHED;
        showHCEAndEReceiptRead();
    }

    /**
     * The doctor sign with his eSignature
     *
     * @throws eSignatureException if there is an error with the eSignature
     * @throws ProceduralException if the prescription edition has not ended
     */
    public void stampeeSignature() throws eSignatureException, ProceduralException {
        if (currentState != States.MEDICAL_PRESCRIPTION_FINISHED)
            throw new ProceduralException("There was not enter a treatment ending date");
        if (this.doctorSignature == null)
            throw new eSignatureException("There is an error with the eSignature");

        doctorSignature.getDigitalSignature();

        currentState = States.SIGNATURE_STAMPED;

        showMedicalPrescriptionPendingValidation();
    }


    /**
     * Transmit the hce and medical prescription of the illness of the patient to the HNS,
     * if all is correct, the HNS will generate a treatment code on the medical prescription
     *
     * @throws ConnectException                if the connection with the HNS fails
     * @throws HealthCardIDException           if the HealthCardID is not registered on the HNS
     * @throws AnyCurrentPrescriptionException if the patient doesn't have prescription associated to the illness
     * @throws NotCompletedMedicalPrescription if the HNS can't validate the prescription
     * @throws ProceduralException             if there wasn't a signature stamped
     */
    public void sendHistoryAndPrescription()
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescription, ProceduralException, MedicalPrescriptionException, ePrescripCodeException {
        if (currentState != States.SIGNATURE_STAMPED)
            throw new ProceduralException("There wasn't a signature stamped");

        // Send the medical prescription to the HNS and replace it
        this.medicalPrescription = healthNationalService.sendHistoryAndPrescription(
                cip, medicalHistory, medicalPrescription.getIllness(), medicalPrescription);

        currentState = States.HISTORY_AND_PRESCRIPTION_SENT;

        showPrescriptionValidAndComplete();
    }

    /**
     * Prints the medical prescription
     *
     * @throws ProceduralException if history wasn't send
     */
    public void printMedicalPrescrip() throws ProceduralException {
        if (currentState != States.HISTORY_AND_PRESCRIPTION_SENT)
            throw new ProceduralException("History wasn't send");
        currentState = States.MEDICAL_PRESCRIPTION_PRINTED;
        sendToPrintMedicalPrescription();
    }


    //Additional functions

    private boolean incorrectDate(Date date) {
        if (date == null) return true;

        Date today = new Date();

        return !date.after(today);
    }


    // Helpers, those will be modified as needed on the real implementation

    private void showHCE() {
        System.out.println("HCE: " + medicalHistory.toString());
    }

    private void showPrescription() {
        System.out.println("PRESCRIPTION: " + medicalPrescription.toString());
    }

    private void showPrescriptionEdition() {
        System.out.println("Prescription edition ...");
    }

    private void showIAHello() {
        System.out.println("IA says HI...");
    }

    private void showIASuggestionsTab() {
        System.out.println("AI suggestion: " + lastAIAnswer);
    }

    private void showIASuggestions() {
        System.out.println("----------IA SUGGESTIONS----------");
        for (Suggestion suggestion : suggestions)
            System.out.println(suggestion.toString());
        System.out.println("----------------------------------");
    }

    private void showPrescriptionCompleted() {
        System.out.println("Prescription line modification completed");
    }

    private void showPrescriptionLineModified() {
        System.out.println("Prescription line modified");
    }

    private void showRemovedLine() {
        System.out.println("Prescription line removed");
    }

    private void showMedicalPrescriptionReadyToSign() {
        System.out.println("Prescription ready to firm");
    }

    private void showHCEAndEReceiptRead() {
        System.out.println("Screen of just reading of HCE and e-receipt");
    }

    private void showMedicalPrescriptionPendingValidation() {
        System.out.println("Medical prescription is waiting fot validation");
    }

    public void showPrescriptionValidAndComplete() {
        System.out.println("Medical prescription valid and complete:");
        showPrescription();
    }

    public void sendToPrintMedicalPrescription() {
        System.out.println("printing medical prescription...");
    }


    // Injection setters

    public void setAi(DecisionMakingAI ai) {
        this.ai = ai;
    }

    public void setHealthNationalService(HealthNationalService healthNationalService) {
        this.healthNationalService = healthNationalService;
    }


    // Getters

    public HealthNationalService getHealthNationalService() {
        return healthNationalService;
    }

    public MedicalPrescription getMedicalPrescription() {
        return medicalPrescription;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }


    // Setters

    public void setDoctorSignature(DigitalSignature digitalSignature) {
        this.doctorSignature = digitalSignature;
    }
}
