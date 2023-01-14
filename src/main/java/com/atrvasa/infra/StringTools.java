package com.atrvasa.infra;

public abstract class StringTools {
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;
        return str.equals("");
    }
}
