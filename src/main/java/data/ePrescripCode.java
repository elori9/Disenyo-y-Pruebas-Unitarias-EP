package data;

import exceptions.ePrescripCodeException;

public class ePrescripCode {

    private final String prescripCode;

    /**
     * Constructor of the class
     *
     * @param code the Identification Code
     * @throws ePrescripCodeException if the code is null or the code has invalid format
     */
    public ePrescripCode(String code) throws ePrescripCodeException {
        if (code == null)
            throw new ePrescripCodeException("ePrescripCode code can't be null");

        if (code.length() != 16)
            throw new ePrescripCodeException("ePrescripCode must be 16 characters long");

        for (char c : code.toCharArray())
            if (!Character.isDigit(c) && !Character.isLetter(c))
                throw new ePrescripCodeException("ePrescripCode contains invalid characters");

        this.prescripCode = code;
    }

    public String getPrescripCode() {
        return prescripCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ePrescripCode ePrescCode = (ePrescripCode) o;
        return prescripCode.equals(ePrescCode.prescripCode);
    }

    @Override
    public int hashCode() {
        return prescripCode.hashCode();
    }

    @Override
    public String toString() {
        return "ePrescripCode{" + "prescription code='" + prescripCode + '\'' + '}';
    }
}
