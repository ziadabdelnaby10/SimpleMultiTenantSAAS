package org.ziad.mutlitenantsaas.util;

public final class StringHelper {
    public static String extractFirstName(String input) {
        return input.substring(0, input.indexOf(' '));
    }

    public static String extractLastName(String input) {
        String fullName = input.substring(input.lastIndexOf(' ') + 1);
        if (fullName.isEmpty())
            return input;
        return fullName;
    }
}
