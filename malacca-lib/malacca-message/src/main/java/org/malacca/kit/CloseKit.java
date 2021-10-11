package org.malacca.kit;

public class CloseKit {

    private CloseKit() {
    }

    public static void close(AutoCloseable s) {
        if(s != null) {
            try {
                s.close();
                s = null;
            } catch (Exception var2) {
            }
        }
    }
}
