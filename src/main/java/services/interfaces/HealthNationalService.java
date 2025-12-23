package services.interfaces;

import exceptions.*;
import medicalconsultation.*;
import data.HealthCardID;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, HealthCardIDException, IncorrectParametersException;

    MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException, MedicalPrescriptionException;

    MedicalPrescription sendHistoryAndPrescription(HealthCardID cip,
                                                   MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescription, MedicalPrescriptionException, ePrescripCodeException;

    // Internal operation
    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc)
            throws ConnectException, ePrescripCodeException, MedicalPrescriptionException;
}
