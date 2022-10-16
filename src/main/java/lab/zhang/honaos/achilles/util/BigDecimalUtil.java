package lab.zhang.honaos.achilles.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalUtil {

    public static String formatWithTwoDecimals(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj);
        } else {
            return df.format(obj);
        }
    }

    public static String formatWithThreeDecimals(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.000");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.000";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj);
        } else {
            return df.format(obj);
        }
    }
}
