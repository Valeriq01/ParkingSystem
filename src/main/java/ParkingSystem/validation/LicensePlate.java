package ParkingSystem.validation;

import java.util.regex.Pattern;

public final class LicensePlate {

    private static final Pattern PATTERN =
            Pattern.compile("^[ABEKMHOPCTYX]{1,2}[0-9]{4}[ABEKMHOPCTYX]{2}$");

    private LicensePlate() {
    }

    public static String normalize(String plate) {
        return plate == null ? "" : plate.replaceAll("\\s+", "").toUpperCase();
    }

    public static boolean isValid(String normalizedPlate) {
        return normalizedPlate != null
                && PATTERN.matcher(normalizedPlate).matches();
    }
}