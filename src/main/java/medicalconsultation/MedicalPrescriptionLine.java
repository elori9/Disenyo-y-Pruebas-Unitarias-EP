package medicalconsultation;

import data.ProductID;
import exceptions.MedicalPrescriptionLineException;

public class MedicalPrescriptionLine {
    private int quantity;
    private ProductID productID;
    private TakingGuideline takingGuideline;

    /**
     * Constructor.
     *
     * @param productID       the ProductID of the medicament
     * @param takingGuideline the taking guideline of the medicament
     * @throws MedicalPrescriptionLineException when takingGuideline or productID is null
     */
    public MedicalPrescriptionLine(ProductID productID, TakingGuideline takingGuideline) throws MedicalPrescriptionLineException {
        this.setProductID(productID);
        this.setTakingGuideline(takingGuideline);
        this.setQuantity(1);
    }


    // Getters and setters

    public int getQuantity() {
        return quantity;
    }

    public ProductID getProductID() {
        return productID;
    }

    public TakingGuideline getTakingGuideline() {
        return takingGuideline;
    }

    public void setQuantity(int quantity) throws MedicalPrescriptionLineException {
        if (quantity < 1) throw new MedicalPrescriptionLineException("Quantity must be > 0");
        this.quantity = quantity;
    }

    public void setProductID(ProductID productID) throws MedicalPrescriptionLineException {
        if (productID == null) throw new MedicalPrescriptionLineException("Product can't be null");
        this.productID = productID;
    }

    public void setTakingGuideline(TakingGuideline takingGuideline) throws MedicalPrescriptionLineException {
        if (takingGuideline == null) throw new MedicalPrescriptionLineException("Taking guideline can't be null");
        this.takingGuideline = takingGuideline;
    }

}
