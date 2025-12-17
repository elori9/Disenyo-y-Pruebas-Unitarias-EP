package exceptions;

public class ProductNotInPrescriptionException extends RuntimeException {
  public ProductNotInPrescriptionException(String message) {
    super(message);
  }
}
