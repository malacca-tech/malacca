package org.malacca.utils;

public class SoapUtils {

    public static String extractFilenameFromUrlPath(String urlPath) {
        return urlPath.replace(".wsdl", "");
    }
}
