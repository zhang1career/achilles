package lab.zhang.honaos.achilles.util;

import java.util.UUID;

public class UuidUtil {
    public static String genUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
