package services.interfaces;

import exceptions.*;
import medicalconsultation.*;
import data.HealthCardID;

public interface HealthNationalService {
    MedicalHistory getMedicalHistory(HealthCardID cip)
            throws ConnectException, HealthCardIDException;

    MedicalPrescription getMedicalPrescription(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException;

    MedicalPrescription sendHistoryAndPrescription(HealthCardID cip,
                                                   MedicalHistory hce, String illness, MedicalPrescription mPresc)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescription, MedicalPrescriptionException;
    // Internal operation

    MedicalPrescription generateTreatmCodeAndRegister(MedicalPrescription ePresc)
            throws ConnectException;
}
