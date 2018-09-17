package org.blockchain_innovation.factom.client.api.model.types;

import org.blockchain_innovation.factom.client.api.FactomRuntimeException;
import org.blockchain_innovation.factom.client.api.ops.Digests;
import org.blockchain_innovation.factom.client.api.ops.Encoding;
import org.blockchain_innovation.factom.client.api.ops.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AddressType {
    FACTOID_PUBLIC("FA", "5fb1", Visibility.PUBLIC), FACTOID_SECRET("Fs", "6478", Visibility.PRIVATE),
    ENTRY_CREDIT_PUBLIC("EC", "592a", Visibility.PUBLIC), ENTRY_CREDIT_SECRET("Es", "5db6", Visibility.PRIVATE);

    private final String humanReadablePrefix;
    private final String addressPrefix;
    private final Visibility visibility;

    AddressType(String humanReadablePrefix, String addressPrefix, Visibility visibility) {
        this.humanReadablePrefix = humanReadablePrefix;
        this.addressPrefix = addressPrefix;
        this.visibility = visibility;
    }

    public enum Visibility {
        PUBLIC, PRIVATE
    }

    public String getHumanReadablePrefix() {
        return humanReadablePrefix;
    }

    public byte[] getAddressPrefix() {
        return Encoding.HEX.decode(addressPrefix);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isPublic() {
        return getVisibility() == Visibility.PUBLIC;
    }

    public boolean isPrivate() {
        return getVisibility() == Visibility.PRIVATE;
    }

    public boolean isValid(String address) {
        return isValidAddress(address) && address.startsWith(getHumanReadablePrefix());
    }

    public void assertValid(String address) {
        assertValidAddress(address);
        if (!address.startsWith(getHumanReadablePrefix())) {
            throw new FactomRuntimeException.AssertionException(String.format("Type of address '%s' is not a valid", address));
        }
    }

    public static boolean isValidAddress(String address) {
        try {
            assertValidAddress(address);
        } catch (FactomRuntimeException.AssertionException e) {
            return false;
        }
        return true;
    }

    public static void assertVisibility(String address, Visibility visibility) throws FactomRuntimeException.AssertionException {
        assertValidAddress(address);
        AddressType addressType = AddressType.getType(address);
        if (addressType.getVisibility() != visibility) {
            throw new FactomRuntimeException.AssertionException(String.format("Visibility of address '%s' is not the desired %s", address, visibility));
        }
    }

    public static void assertValidAddress(String address) throws FactomRuntimeException.AssertionException {
        if (StringUtils.isEmpty(address) || address.length() <= 2) {
            throw new FactomRuntimeException.AssertionException(String.format("Address '%s' is not a valid address", address));
        } else if (!getValidPrefixes().contains(address.substring(0, 2))) {
            throw new FactomRuntimeException.AssertionException(String.format("Address '%s' does not start with a valid humanReadablePrefix", address));
        }
        byte[] addressBytes = Encoding.BASE58.decode(address);
        if (addressBytes.length != 38) {
            throw new FactomRuntimeException.AssertionException(String.format("Address '%s' is not 38 bytes long!", address));
        }
        byte[] sha256d = Digests.SHA_256.doubleDigest(Arrays.copyOf(addressBytes, 34));
        byte[] checksum = Arrays.copyOf(sha256d, 4);
        if (!Arrays.equals(checksum, Arrays.copyOfRange(addressBytes, 34, 38))) {
            throw new FactomRuntimeException.AssertionException(String.format("Address '%s' checksum mismatch!", address));
        }
    }

    public static void assertValidAddress(String address, AddressType type) throws FactomRuntimeException.AssertionException {
        assertValidAddress(address);
        if (AddressType.getType(address) != type) {
            throw new FactomRuntimeException.AssertionException(String.format("Address %s is not of type %s", address, type.name()));
        }
    }

    public static AddressType getType(String address) {
        assertValidAddress(address);
        for (AddressType type : values()) {
            if (address.startsWith(type.getHumanReadablePrefix())) {
                return type;
            }
        }
        // Not possible anyway:
        throw new FactomRuntimeException.AssertionException("Could not deduct address type for " + address);
    }

    public static String getPrefix(String address) {
        assertValidAddress(address);
        return address.substring(0, 2);
    }

    public static List<String> getValidPrefixes() {
        return Arrays.stream(values()).map(AddressType::getHumanReadablePrefix).collect(Collectors.toList());
    }
}