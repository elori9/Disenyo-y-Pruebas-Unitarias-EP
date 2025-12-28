package medicalconsultation;

import data.*;
import exceptions.*;
import services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationTerminalTest {
    private ConsultationTerminal consultationTerminal;
    private HealthCardID healthCardID;
    private String illness;
    private ProductID productID;
    private String[] instructions;
    private Date validDate;
    private DigitalSignature digitalSignature;

    private final float newDose = 2;

    // Services
    private HealthNationalServiceMock healthNationalService;
    private DecisionMakingAIMock decisionMakingAI;

    @BeforeEach
    void setUp() throws Exception {
        consultationTerminal = new ConsultationTerminal();
        healthCardID = new HealthCardID("1234567890123456");
        illness = "illness";
        productID = new ProductID("123456789012");
        instructions = new String[]{"DURINGBREAKFAST", "3", "10", "2", "DAY", "instruction"};
        validDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 'A', 'B', 'C', 'D', 'E', 'G', 'H', 'J'};
        digitalSignature = new DigitalSignature(bytes);
        healthNationalService = new HealthNationalServiceMock();
        decisionMakingAI = new DecisionMakingAIMock();

        // Injections
        consultationTerminal.setHealthNationalService(healthNationalService);
        consultationTerminal.setAi(decisionMakingAI);

    }



    // ---- NOT EXCEPTIONS TESTS ----

    @Test
    @DisplayName("test init revision success: step 1")
    void initRevision() throws Exception {
        consultationTerminal.initRevision(healthCardID, illness);

        assertNotNull(consultationTerminal.getMedicalPrescription());
        assertNotNull(consultationTerminal.getMedicalHistory());

    }

    @Test
    @DisplayName("test enter medical assessment in history: step 2")
    void medicalAssessment() throws Exception {
        reachState(1);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.enterMedicalAssessmentInHistory("assessment"));
    }

    @Test
    @DisplayName("test Medical Prescription Edition: step 3")
    void initMedicalPrescriptionEdition() throws Exception {
        reachState(2);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.initMedicalPrescriptionEdition());
    }

    @Test
    @DisplayName("test call Decision MakingAI: step 4")
    void callDecisionMakingAI() throws Exception {
        reachState(3);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.callDecisionMakingAI());
    }

    @Test
    @DisplayName("test ask AI For Suggest: step 5")
    void askAIForSuggest() throws Exception {
       reachState(4);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.askAIForSuggest("prompt"));
    }

    @Test
    @DisplayName("test extract Guidelines From Sugg: step 6")
    void extractGuidelinesFromSugg() throws Exception {
        reachState(5);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.extractGuidelinesFromSugg());
    }

    @Test
    @DisplayName("test enter Medicine With Guidelines: step 7")
    void enterMedicineWithGuidelines() throws Exception {
        reachState(6);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.enterMedicineWithGuidelines(productID, instructions));
    }

    @Test
    @DisplayName("test modify dose in line: step 8")
    void modifyDoseInLine() throws Exception {
        reachState(7);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.modifyDoseInLine(productID, 2));
    }

    @Test
    @DisplayName("test remove line: step 9")
    void removeLine() throws Exception {
        reachState(8);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.removeLine(productID));
    }

    @Test
    @DisplayName("test enter treatment end date: step 10")
    void enterTreatmentEndingDate() throws Exception {
        reachState(9);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.enterTreatmentEndingDate(validDate));
    }

    @Test
    @DisplayName("test finish medical prescription edition: step 11")
    void finishMedicalPrescriptionEdition() throws Exception {
        reachState(10);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.finishMedicalPrescriptionEdition());
    }

    @Test
    @DisplayName("test stamp eSignature: step 12")
    void stampeeSignature() throws Exception {
        reachState(11);
        // Test
        consultationTerminal.setDoctorSignature(digitalSignature);
        assertDoesNotThrow(() -> consultationTerminal.stampeeSignature());
    }

    @Test
    @DisplayName("test send history and prescription: step 13")
    void sendHistoryAndPrescription() throws Exception {
        reachState(12);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.sendHistoryAndPrescription());
    }

    @Test
    @DisplayName("test print medical prescription: step 14")
    void printMedicalPrescrip() throws Exception {
        reachState(13);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.printMedicalPrescrip());
    }


    // ---- EXCEPTIONS TESTS ----

    @Test
    @DisplayName("Check all exceptions on init revision")
    void initRevisionExceptions() {
        // HealthCardID exception
        healthNationalService.setFailWithHealthCardIDException(true);
        assertThrows(HealthCardIDException.class, () ->
                consultationTerminal.initRevision(healthCardID, "illness")
        );
        healthNationalService.setFailWithHealthCardIDException(false);

        // Any current prescription exception
        healthNationalService.setFailWithAnyCurrentPrescriptionException(true);
        assertThrows(AnyCurrentPrescriptionException.class, () ->
                consultationTerminal.initRevision(healthCardID, "illness")
        );
        healthNationalService.setFailWithAnyCurrentPrescriptionException(false);

        // Connection exception
        healthNationalService.setFailWithConnectException(true);
        assertThrows(ConnectException.class, () ->
                consultationTerminal.initRevision(healthCardID, "illness")
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions on enter medical assessment in history")
    void enterMedicalAssessmentInHistoryProceduralExceptions() {
        // Procedural exception
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicalAssessmentInHistory("assess")
        );
    }

    @Test
    @DisplayName("Check all exceptions on init Medical Prescription Edition")
    void initMedicalPrescriptionEditionProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.initMedicalPrescriptionEdition()
        );

        // Procedural exception before one step
        reachState(1);

        assertThrows(ProceduralException.class, () ->
                consultationTerminal.initMedicalPrescriptionEdition()
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions on call Decision Making AI")
    void callDecisionMakingAIProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.callDecisionMakingAI()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.callDecisionMakingAI()
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.callDecisionMakingAI()
        );
    }

    @Test
    @DisplayName("Check other exceptions on call Decision Making AI")
    void callDecisionMakingAIExceptions() throws Exception {
        reachState(3);

        // AI Exception
        decisionMakingAI.setFailWithAIException(true);
        assertThrows(AIException.class, () ->
                consultationTerminal.callDecisionMakingAI()
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions on ask for ai suggest")
    void askAIForSuggestProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.askAIForSuggest("prompt")
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.askAIForSuggest("prompt")
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.askAIForSuggest("prompt")
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.askAIForSuggest("prompt")
        );
    }

    @Test
    @DisplayName("Check other exceptions on ask for ai suggest")
    void askAIForSuggestExceptions() throws Exception {
        reachState(4);
        // Bad prompt exception
        assertThrows(BadPromptException.class, () ->
                consultationTerminal.askAIForSuggest(null)
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from extract guide lines from suggestions")
    void extractGuidelinesFromSuggProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.extractGuidelinesFromSugg()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.extractGuidelinesFromSugg()
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.extractGuidelinesFromSugg()
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.extractGuidelinesFromSugg()
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.extractGuidelinesFromSugg()
        );
    }

    @Test
    @DisplayName("Check other exceptions extract guide lines from suggestions")
    void extractGuidelinesFromSuggExceptions() throws Exception {
        reachState(5);
        decisionMakingAI.setFailWithsuggestions(true);
        // Bad prompt exception
        assertThrows(BadPromptException.class, () ->
                consultationTerminal.extractGuidelinesFromSugg()
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from enter Medicine With Guidelines")
    void enterMedicineWithGuidelinesProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );
    }

    @Test
    @DisplayName("Check other exceptions from enter Medicine With Guidelines")
    void enterMedicineWithGuidelinesExceptions() throws Exception {
        reachState(6);
        String[] instruct = new String[]{""};

        // Incorrect Taking guidelines exception
        assertThrows(IncorrectTakingGuidelinesException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instruct)
        );
        // Product already in prescription exception
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProductAlreadyInPrescriptionException.class, () ->
                consultationTerminal.enterMedicineWithGuidelines(productID, instructions)
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from modify dose in line")
    void enterModifyDoseInLineProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.modifyDoseInLine(productID, newDose)
        );
    }

    @Test
    @DisplayName("Check other exceptions from enter modify dose in line")
    void enterModifyDoseInLineExceptions() throws Exception {
        reachState(7);
        ProductID productID1 = new ProductID("012345678901");

        // Product Not In Prescription Exception
        assertThrows(ProductNotInPrescriptionException.class, () ->
                consultationTerminal.modifyDoseInLine(productID1, newDose)
        );

    }

    @Test
    @DisplayName("Check all procedural exceptions from remove line")
    void enterRemoveLineProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
        // Procedural exception before seven steps
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.removeLine(productID)
        );
    }

    @Test
    @DisplayName("Check other exceptions from remove line")
    void enterRemoveLineExceptions() throws Exception {
        reachState(8);
        ProductID productID2 = new ProductID("999999999999");

        // Product Not In Prescription Exception
        assertThrows(ProductNotInPrescriptionException.class, () ->
                consultationTerminal.removeLine(productID2)
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from enter treatment ending date")
    void enterEnterTreatmentEndingDateProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before seven steps
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
        // Procedural exception before eight steps
        consultationTerminal.modifyDoseInLine(productID, 2);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(validDate)
        );
    }

    @Test
    @DisplayName("Check other exceptions from enter treatment ending date")
    void enterEnterTreatmentEndingDateExceptions() throws Exception {
        reachState(9);
        // Date of a day before
        Date pastDate = new Date(System.currentTimeMillis() - 3600000 * 24);

        // Incorrect date
        assertThrows(IncorrectEndingDateException.class, () ->
                consultationTerminal.enterTreatmentEndingDate(pastDate)
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from finish medical prescription edition")
    void enterFinishMedicalPrescriptionEditionProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before seven steps
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before eight steps
        consultationTerminal.modifyDoseInLine(productID, newDose);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
        // Procedural exception before nine steps
        consultationTerminal.removeLine(productID);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.finishMedicalPrescriptionEdition()
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from stamp  eSignature")
    void enterStampeeSignatureProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before seven steps
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before eight steps
        consultationTerminal.modifyDoseInLine(productID, newDose);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before nine steps
        consultationTerminal.removeLine(productID);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
        // Procedural exception before ten steps
        consultationTerminal.enterTreatmentEndingDate(validDate);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.stampeeSignature()
        );
    }

    @Test
    @DisplayName("Check other exceptions from stamp eSignature Exceptions")
    void enterStampeeSignatureExceptions() throws Exception {
        reachState(11);

        // eSignature exception
        assertThrows(eSignatureException.class, () ->
                consultationTerminal.stampeeSignature()
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from send history and prescription")
    void sendHistoryAndPrescriptionProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before seven steps
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before eight steps
        consultationTerminal.modifyDoseInLine(productID, newDose);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before nine steps
        consultationTerminal.removeLine(productID);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before ten steps
        consultationTerminal.enterTreatmentEndingDate(validDate);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        // Procedural exception before eleven steps
        consultationTerminal.finishMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
    }

    @Test
    @DisplayName("Check other exceptions from send history and prescription")
    void sendHistoryAndPrescriptionExceptions() throws Exception {
        reachState(12);

        // Connect exception
        healthNationalService.setFailWithConnectException(true);
        assertThrows(ConnectException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        healthNationalService.setFailWithConnectException(false);

        // Health Card ID exception
        healthNationalService.setFailWithHealthCardIDException(true);
        assertThrows(HealthCardIDException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        healthNationalService.setFailWithHealthCardIDException(false);

        // Any current prescription exception
        healthNationalService.setFailWithAnyCurrentPrescriptionException(true);
        assertThrows(AnyCurrentPrescriptionException.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
        healthNationalService.setFailWithAnyCurrentPrescriptionException(false);

        // Not competed medical prescription
        healthNationalService.setFailWithNotCompletedMedicalPrescription(true);
        assertThrows(NotCompletedMedicalPrescription.class, () ->
                consultationTerminal.sendHistoryAndPrescription()
        );
    }

    @Test
    @DisplayName("Check all procedural exceptions from print medical prescription")
    void printMedicalPrescripProceduralExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before two steps
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before three steps
        consultationTerminal.initMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before four steps
        consultationTerminal.callDecisionMakingAI();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before five steps
        consultationTerminal.askAIForSuggest("prompt");
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before six steps
        consultationTerminal.extractGuidelinesFromSugg();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before seven steps
        consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before eight steps
        consultationTerminal.modifyDoseInLine(productID, newDose);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before nine steps
        consultationTerminal.removeLine(productID);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before ten steps
        consultationTerminal.enterTreatmentEndingDate(validDate);
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before eleven steps
        consultationTerminal.finishMedicalPrescriptionEdition();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
        // Procedural exception before twelve steps
        consultationTerminal.setDoctorSignature(digitalSignature);
        consultationTerminal.stampeeSignature();
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.printMedicalPrescrip()
        );
    }

    // Helper method to fast-forward the state
    private void reachState(int step) throws Exception {
        if (step >= 1) consultationTerminal.initRevision(healthCardID, illness);
        if (step >= 2) consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        if (step >= 3) consultationTerminal.initMedicalPrescriptionEdition();
        if (step >= 4) consultationTerminal.callDecisionMakingAI();
        if (step >= 5) consultationTerminal.askAIForSuggest("prompt");
        if (step >= 6) consultationTerminal.extractGuidelinesFromSugg();
        if (step >= 7) consultationTerminal.enterMedicineWithGuidelines(productID, instructions);
        if (step >= 8) consultationTerminal.modifyDoseInLine(productID, newDose);
        if (step >= 9) consultationTerminal.removeLine(productID);
        if (step >= 10) consultationTerminal.enterTreatmentEndingDate(validDate);
        if (step >= 11) consultationTerminal.finishMedicalPrescriptionEdition();
        if (step >= 12) {
            consultationTerminal.setDoctorSignature(digitalSignature);
            consultationTerminal.stampeeSignature();
        }
        if (step >= 13) consultationTerminal.sendHistoryAndPrescription();
    }
}
