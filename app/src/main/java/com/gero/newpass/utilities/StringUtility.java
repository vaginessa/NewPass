package com.gero.newpass.utilities;

public class StringUtility {
    private static String sharedString;

    public static void setSharedString(String value) {
        sharedString = value;
    }

    public static String getSharedString() {
        return sharedString;
    }
}
