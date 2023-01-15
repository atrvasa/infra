package com.atrvasa.infra;

public abstract class StringTools {
    /**
     * This function returns true if the function's parameter is Null or empty.
     *
     * @param str is a string variable
     * @return boolean
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;
        return str.equals("");
    }
}
