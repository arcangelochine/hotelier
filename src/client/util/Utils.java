package client.util;

import javax.swing.*;

public final class Utils {
    public static <T extends Comparable<? super T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0)
            return min;
        if (value.compareTo(max) > 0)
            return max;

        return value;
    }

    public static void errorDialog(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE));
    }

    public static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String lowerCaseStr = str.toLowerCase();
        return Character.toUpperCase(lowerCaseStr.charAt(0)) + lowerCaseStr.substring(1);
    }
}
