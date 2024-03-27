package com.personal.backend.utils;


import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * SearchCodeUtils is a class that provides utility methods to generate and extract the search code from a string.
 * The search code is a string that is used to identify a search in the system.
 */
public final class SearchCodeUtils {

    // If needed, you can change the length of the generated code or could be set in Environment Variable.
    private static final int GENERATE_LENGTH_CODE = 8;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private SearchCodeUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String generateNewCode() {
        StringBuilder builder = new StringBuilder();
        Random rdm = new Random();
        while(builder.length() < GENERATE_LENGTH_CODE) {
            int character = rdm.nextInt(75) + 48;
            if (ALPHA_NUMERIC_STRING.contains(String.valueOf((char) character).toUpperCase())) {
                builder.append((char) character);

            }
        }
        return builder.toString();
    }

    /***
     * Extracts the search code from a string.
     * I use to extract code from name of thread in case of exception.
     * @param stringWithCode
     * @return
     */
    public static String getCode(String stringWithCode){
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(stringWithCode);
        return matcher.find() ? matcher.group(1) : null;
    }

}
