package medicalconsultation;

import data.*;
import exceptions.*;
import services.interfaces.DecisionMakingAI;
import services.interfaces.HealthNationalService;

import java.security.Signature;
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


    // Booleans for states
    private boolean assessmentEntered = false;
    private boolean medicalPrescriptionStarted = false;
    private boolean callDecisionMakeAIStarted = false;
    private boolean aiConversationEnded = false;
    private boolean aiRecommendations = false;
    private boolean medicineEntered = false;
    private boolean prescriptionLineModified = false;
    private boolean prescriptionLineRemoved = false;
    private boolean prescriptionReadyToSign = false;
    private boolean medicalPrescriptionEditionEnded = false;


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
        if (medicalPrescription == null) throw new ProceduralException("Medical prescription not initialized");
        medicalHistory.addMedicalHistoryAnnotations(assess);
        assessmentEntered = true;
        showHCE();
    }

    /**
     * Starts the edition process of the medical prescription
     *
     * @throws ProceduralException if there was not entered the medical assessment in history
     */
    public void initMedicalPrescriptionEdition() throws ProceduralException {
        if (!assessmentEntered) {
            throw new ProceduralException("Medical prescription not initialized");
        }
        medicalPrescriptionStarted = true;
        showPrescriptionEdition();
    }

    /**
     * Starts the ia decision
     *
     * @throws AIException         if there is a problem with the ia
     * @throws ProceduralException if there was not initiated the medical prescription edition
     */
    public void callDecisionMakingAI() throws AIException, ProceduralException {
        if (!medicalPrescriptionStarted)
            throw new ProceduralException("Not initiated the medical prescription edition");
        ai.initDecisionMakingAI();
        callDecisionMakeAIStarted = true;
        showIAHello();
    }

    /**
     * @param prompt the prompt
     * @throws ProceduralException if there was not initialized the call decision with ia
     * @throws BadPromptException  if the prompt is not enough clear
     */
    public void askAIForSuggest(String prompt) throws ProceduralException, BadPromptException {
        if (!callDecisionMakeAIStarted)
            throw new ProceduralException("Not called the decision making ai");
        lastAIAnswer = ai.getSuggestions(prompt);
        showIAsuggestionsTab();
        aiConversationEnded = true;
    }

    /**
     * Extracts the IA responses by the doctor
     *
     * @throws ProceduralException if there was not asked fot ia suggestions
     * @throws BadPromptException  if the prompt is not enough clear
     */
    public void extractGuidelinesFromSugg() throws ProceduralException, BadPromptException {
        if (!aiConversationEnded)
            throw new ProceduralException("Not asked for the ai suggest");
        suggestions = ai.parseSuggest(lastAIAnswer);
        if (suggestions == null || suggestions.isEmpty())
            throw new BadPromptException("The prompt provided is not clear for the AI, so there where no suggestions found");
        showIAsuggestions();
        aiRecommendations = true;
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
        if (!aiRecommendations)
            throw new ProceduralException("Not extracted the guidelines form suggestion");
        medicalPrescription.addLine(prodID, instruc);
        medicineEntered = true;
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
        if (!medicineEntered)
            throw new ProceduralException("There was not entered the Medicine with Guidelines");
        medicalPrescription.modifyDoseInLine(prodID, newDose);
        prescriptionLineModified = true;
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
        if (!prescriptionLineModified)
            throw new ProceduralException("There was not modified the dose in line");
        medicalPrescription.removeLine(prodID);
        prescriptionLineRemoved = true;
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
        if (!prescriptionLineRemoved)
            throw new ProceduralException("There was not a remove line");

        if (incorrectDate(date))
            throw new IncorrectEndingDateException("The date is incorrect ");

        medicalPrescription.setEndDate(date);

        prescriptionReadyToSign = true;
        showMedicalPrescriptionReadyToSign();
    }

    /**
     * @throws ProceduralException if there is no treatment ending date
     */
    public void finishMedicalPrescriptionEdition() throws ProceduralException {
        if (!prescriptionReadyToSign)
            throw new ProceduralException("There is not treatment ending date");

        medicalPrescriptionEditionEnded = true;
        showHCEandEReceiptRead();
    }

    /**
     * The doctor sign with his eSignature
     *
     * @throws eSignatureException if there is an error with the eSignature
     * @throws ProceduralException if the prescription edition has not ended
     */
    public void stampeeSignature() throws eSignatureException, ProceduralException {
        if (!medicalPrescriptionEditionEnded)
            throw new ProceduralException("There was not enter a treatment ending date");
        if (this.doctorSignature == null)
            throw new eSignatureException("There is an error with the eSignature");

        doctorSignature.getDigitalSignature();

        medicalPrescriptionEditionEnded = true;

        showMedicalPrescriptionPendingValidation();
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

    private void showIAsuggestionsTab() {
        System.out.println("AI suggestion: " + lastAIAnswer);
    }

    private void showIAsuggestions() {
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

    private void showHCEandEReceiptRead() {
        System.out.println("Screen of just reading of HCE and e-receipt");
    }

    private void showMedicalPrescriptionPendingValidation() {
        System.out.println("Medical prescription is waiting fot validation");
    }

    private void showMedicalPrescription() {
        System.out.println(this.medicalPrescription.toString());
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
