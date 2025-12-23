package data;

import exceptions.SuggestionException;

public class Suggestion {

    private final ProductID productID;
    private final String indication;
    private final double newDose;
    private final ActionType actionType;

    /**
     * Constructor of the class
     *
     * @param productID  the ProductID
     * @param indication the indication
     * @param actionType the action type
     * @param newDose    the new Dose
     * @throws SuggestionException if the productID, indication or actionType are null or the newDose < 0
     */
    public Suggestion(ProductID productID, String indication, ActionType actionType, double newDose) throws SuggestionException {
        if (productID == null) throw new SuggestionException("ProductID can't be null");
        if (indication == null) throw new SuggestionException("Indication can't be null");
        if (actionType == null) throw new SuggestionException("Action type can't be null");
        if (newDose < 0) throw new SuggestionException("New dose must be > 0 ");
        this.productID = productID;
        this.indication = indication;
        this.actionType = actionType;
        this.newDose = newDose;
    }

    public String toString() {
        return "- " + productID + "\t" + actionType + "\t" + indication + "\tNew dose: " + newDose + "\n";
    }

    // getters

    public ProductID getProductID() {
        return productID;
    }

    public String getIndication() {
        return indication;
    }

    public double getNewDose() {
        return newDose;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
