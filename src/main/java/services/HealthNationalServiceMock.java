package services;

import exceptions.*;
import data.HealthCardID;
import data.ePrescripCode;
import medicalconsultation.MedicalHistory;
import medicalconsultation.MedicalPrescription;
import services.interfaces.HealthNationalService;

public class HealthNationalServiceMock implements HealthNationalService {

    private boolean failWithConnectException = false;
    private boolean failWithHealthCardIDException = false;
    private boolean failWithAnyCurrentPrescriptionException = false;
    private boolean failWithNotCompletedMedicalPrescription = false;
    private boolean failWithMedicalPrescriptionException = false;

    private MedicalHistory medicalHistory;
    private MedicalPrescription medicalPrescription;

    public HealthNationalServiceMock() throws IncorrectParametersException, HealthCardIDException, MedicalPrescriptionException {
        HealthCardID healthCardID = new HealthCardID("1234567891234567");
        this.medicalHistory = new MedicalHistory(healthCardID, 777);
        this.medicalPrescription = new MedicalPrescription(healthCardID, 777, "illness");
    }

    @Override
    public medicalconsultation.MedicalHistory getMedicalHistory(HealthCardID cip) throws ConnectException, HealthCardIDException, IncorrectParametersException {
        if (failWithConnectException) {
            throw new ConnectException("Connection error");
        }
        if (failWithHealthCardIDException) {
            throw new HealthCardIDException("CIP not registered in the system");
        }
        return medicalHistory;
    }

    @Override
    public MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness) throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException, MedicalPrescriptionException {
        if (failWithConnectException) {
            throw new ConnectException("Connection error");
        }
        if (failWithHealthCardIDException) {
            throw new HealthCardIDException("CIP not registered in the system");
        }
        if (failWithAnyCurrentPrescriptionException) {
            throw new AnyCurrentPrescriptionException("No active prescription found for this illness");
        }
        if (failWithMedicalPrescriptionException) {
            throw new MedicalPrescriptionException("Error retrieving medical prescription");
        }
        return medicalPrescription;
    }

    @Override
    public MedicalPrescription sendHistoryAndPrescription(HealthCardID cip, MedicalHistory hce, String illness, MedicalPrescription mPresc) throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException, NotCompletedMedicalPrescription, MedicalPrescriptionException, ePrescripCodeException {
        if (failWithConnectException) {
            throw new ConnectException("Connection error");
        }
        if (failWithHealthCardIDException) {
            throw new HealthCardIDException("Incorrect CIP");
        }
        if (failWithAnyCurrentPrescriptionException) {
            throw new AnyCurrentPrescriptionException("Prescription not found");
        }
        if (failWithNotCompletedMedicalPrescription) {
            throw new NotCompletedMedicalPrescription("Prescription validation failed: Mandatory fields missing");
        }
        if (failWithMedicalPrescriptionException) {
            throw new MedicalPrescriptionException("Medical Prescription Error");
        }

        return generateTreatmCodeAndRegister(mPresc);
    }

    @Override
    public MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc) throws ConnectException, ePrescripCodeException, MedicalPrescriptionException {
        if (failWithConnectException) {
            throw new ConnectException("Connection error");
        }
        ePrescripCode newCode = new ePrescripCode("123456789ABCDEFG");
        ePresc.setPrescCode(newCode);

        return ePresc;
    }

    // Setters
    public void setFailWithConnectException(boolean fail) {
        this.failWithConnectException = fail;
    }

    public void setFailWithHealthCardIDException(boolean fail) {
        this.failWithHealthCardIDException = fail;
    }

    public void setFailWithAnyCurrentPrescriptionException(boolean fail) {
        this.failWithAnyCurrentPrescriptionException = fail;
    }

    public void setFailWithNotCompletedMedicalPrescription(boolean fail) {
        this.failWithNotCompletedMedicalPrescription = fail;
    }

    public void setFailWithMedicalPrescriptionException(boolean fail) {
        this.failWithMedicalPrescriptionException = fail;
    }
}
