package data;

import exceptions.ProductIDException;

public class ProductID {

    private final String productID;

    /**
     * Constructor of the class
     *
     * @param code the Identification Code
     * @throws ProductIDException if the code is null or the code has invalid format
     */
    public ProductID(String code) throws ProductIDException {
        if (code == null)
            throw new ProductIDException("ProductID code can't be null");

        if (code.length() != 12)
            throw new ProductIDException("ProductID must be 12 characters long");

        for (char c : code.toCharArray())
            if (!Character.isDigit(c))
                throw new ProductIDException("ProductID contains invalid characters");

        this.productID = code;
    }

    public String getProductID() {
        return productID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductID prdID = (ProductID) o;
        return productID.equals(prdID.productID);
    }

    @Override
    public int hashCode() {
        return productID.hashCode();
    }

    @Override
    public String toString() {
        return "ProductID{" + "product code='" + productID + '\'' + '}';
    }
}
