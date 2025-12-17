package exceptions;

public class ProductAlreadyInPrescriptionException extends RuntimeException {
  public ProductAlreadyInPrescriptionException(String message) {
    super(message);
  }
}
