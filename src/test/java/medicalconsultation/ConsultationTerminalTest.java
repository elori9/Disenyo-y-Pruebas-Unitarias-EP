package medicalconsultation;

import data.*;
import exceptions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import services.*;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationTerminalTest {
    private ConsultationTerminal consultationTerminal;
    private HealthCardID healthCardID;
    private String illness;
    private ProductID productID;
    private String[] instructions;

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
    void medicalAssesment() throws Exception {
        // Step 1
        consultationTerminal.initRevision(healthCardID, illness);
        // Test
        assertDoesNotThrow(() -> consultationTerminal.enterMedicalAssessmentInHistory("assessment"));
    }

    @Test
    @DisplayName("test Medical Prescription Edition: step 3")
    void initMedicalPrescriptionEdition() throws Exception {
        // Step 1, 2
        consultationTerminal.initRevision(healthCardID, illness);
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        // Test
        assertDoesNotThrow(() -> consultationTerminal.initMedicalPrescriptionEdition());
    }

    @Test
    @DisplayName("test call Decision MakingAI: step 4")
    void callDecisionMakingAI() throws Exception {
        // Step 1, 2, 3
        consultationTerminal.initRevision(healthCardID, illness);
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        // Test
        assertDoesNotThrow(() -> consultationTerminal.callDecisionMakingAI());
    }

    @Test
    @DisplayName("test ask AI For Suggest: step 5")
    void askAIForSuggest() throws Exception {
        // Step 1, 2, 3, 4
        consultationTerminal.initRevision(healthCardID, illness);
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        consultationTerminal.callDecisionMakingAI();
        // Test
        assertDoesNotThrow(() -> consultationTerminal.askAIForSuggest("prompt"));
    }

    @Test
    @DisplayName("test extract Guidelines From Sugg: step 6")
    void extractGuidelinesFromSugg() throws Exception {
        // Step 1, 2, 3, 4, 5
        consultationTerminal.initRevision(healthCardID, illness);
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        consultationTerminal.callDecisionMakingAI();
        consultationTerminal.askAIForSuggest("prompt");
        // Test
        assertDoesNotThrow(() -> consultationTerminal.extractGuidelinesFromSugg());
    }

    @Test
    @DisplayName("test enter Medicine With Guidelines: step 7")
    void enterMedicineWithGuidelines() throws Exception {
        // Step 1, 2, 3, 4, 5, 6
        consultationTerminal.initRevision(healthCardID, illness);
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        consultationTerminal.callDecisionMakingAI();
        consultationTerminal.askAIForSuggest("prompt");
        consultationTerminal.extractGuidelinesFromSugg();
        // Test
        assertDoesNotThrow(() -> consultationTerminal.enterMedicineWithGuidelines(productID, instructions));
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
    @DisplayName("Check all exceptions on enter medical assesment in history")
    void enterMedicalAssessmentInHistoryExceptions() {
        // Procedural exception
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.enterMedicalAssessmentInHistory("assess")
        );
    }

    @Test
    @DisplayName("Check all exceptions on init Medical Prescription Edition")
    void initMedicalPrescriptionEditionExceptions() throws Exception {
        // Procedural exception before nothing
        assertThrows(ProceduralException.class, () ->
                consultationTerminal.initMedicalPrescriptionEdition()
        );

        // Procedural exception before one step
        consultationTerminal.initRevision(healthCardID, "illness");

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
        // AI Exception
        consultationTerminal.initRevision(healthCardID, "illness");
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();

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
        consultationTerminal.initRevision(healthCardID, "illness");
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        consultationTerminal.callDecisionMakingAI();
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
        consultationTerminal.initRevision(healthCardID, "illness");
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        consultationTerminal.callDecisionMakingAI();
        consultationTerminal.askAIForSuggest("prompt");
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
    @DisplayName("Check other excpetions from enter Medicine With Guidelines")
    void enterMedicineWithGuidelinesExceptions() throws Exception {
        consultationTerminal.initRevision(healthCardID, illness);
        consultationTerminal.enterMedicalAssessmentInHistory("assessment");
        consultationTerminal.initMedicalPrescriptionEdition();
        consultationTerminal.callDecisionMakingAI();
        consultationTerminal.askAIForSuggest("prompt");
        consultationTerminal.extractGuidelinesFromSugg();
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

}
