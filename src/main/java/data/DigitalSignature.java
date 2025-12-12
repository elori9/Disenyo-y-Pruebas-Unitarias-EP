package data;

import exceptions.DigitalSignatureException;

import java.util.Arrays;

public class DigitalSignature {


    private final byte[] digitalSignature;

    /**
     * Constructor of the class
     *
     * @param signature the Signature
     * @throws DigitalSignatureException if the code is null or the code has invalid format
     */
    public DigitalSignature(byte[] signature) throws DigitalSignatureException {
        if (signature == null)
            throw new DigitalSignatureException("DigitalSignature code can't be null");

        if (signature.length != 16)
            throw new DigitalSignatureException("DigitalSignature must be 16 characters long");

        this.digitalSignature = signature;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalSignature dSignature = (DigitalSignature) o;
        return Arrays.equals(dSignature.digitalSignature, digitalSignature);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(digitalSignature);
    }

    @Override
    public String toString() {
        return "DigitalSignature{" + "Signature='" + Arrays.toString(digitalSignature) + '\'' + '}';
    }
}
