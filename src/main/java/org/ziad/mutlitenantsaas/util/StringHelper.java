package org.ziad.mutlitenantsaas.util;

/**
 * Utility class providing helper methods for string manipulation used across mappers.
 *
 * <p>This is a non-instantiable utility class; all methods are static.
 */
public final class StringHelper {

    private StringHelper() {
        // prevent instantiation
    }

    /**
     * Extracts the first word from a full name (text before the first space).
     *
     * <p>Example: {@code extractFirstName("John Doe")} → {@code "John"}.
     *
     * @param input the full name string; must contain at least one space
     * @return the substring before the first space character
     * @throws StringIndexOutOfBoundsException if {@code input} contains no space
     */
    public static String extractFirstName(String input) {
        return input.substring(0, input.indexOf(' '));
    }

    /**
     * Extracts the last word from a full name (text after the last space).
     *
     * <p>Example: {@code extractLastName("John van Doe")} → {@code "Doe"}.
     * Falls back to the entire input if no space is found (single-word name).
     *
     * @param input the full name string
     * @return the substring after the last space character, or the entire {@code input}
     *         if there is no space
     */
    public static String extractLastName(String input) {
        String fullName = input.substring(input.lastIndexOf(' ') + 1);
        if (fullName.isEmpty())
            return input;
        return fullName;
    }
}
