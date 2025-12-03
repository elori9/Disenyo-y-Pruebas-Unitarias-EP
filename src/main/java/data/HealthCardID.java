package data;

import exceptions.HealthCardIDException;

/**
 * The personal identifying code in the National Health Service.
 */
final public class HealthCardID {

    private final String personalID;

    /**
     * Constructor of the class
     *
     * @param code the Identification Code
     * @throws HealthCardIDException if the code is null or the code has invalid format
     */
    public HealthCardID(String code) throws HealthCardIDException {
        if (code == null)
            throw new HealthCardIDException("HealthCardID code can't be null");

        if (code.length() != 16)
            throw new HealthCardIDException("HealthCardID must be 16 characters long");

        for (char c : code.toCharArray())
            if (!Character.isLetter(c) && !Character.isDigit(c))
                throw new HealthCardIDException("HealthCardID contains invalid characters");

        this.personalID = code;
    }

    public String getPersonalID() {
        return personalID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthCardID hcardID = (HealthCardID) o;
        return personalID.equals(hcardID.personalID);
    }

    @Override
    public int hashCode() {
        return personalID.hashCode();
    }

    @Override
    public String toString() {
        return "HealthCardID{" + "personal code='" + personalID + '\'' + '}';
    }
}
