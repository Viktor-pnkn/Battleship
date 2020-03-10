package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean isInt(String s) {
        if (s.equals("")) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        return true;
        /*Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return true;
        }
        return false;*/
    }

    public static boolean outSide(int pos, int maxPos) {
        return pos > maxPos || pos < 0;
    }
}
