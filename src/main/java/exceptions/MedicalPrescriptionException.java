package exceptions;

public class MedicalPrescriptionException extends RuntimeException {
  public MedicalPrescriptionException(String message) {
    super(message);
  }
}
