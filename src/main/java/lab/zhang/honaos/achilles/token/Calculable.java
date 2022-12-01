package lab.zhang.honaos.achilles.token;

import lab.zhang.honaos.achilles.context.Contextable;

import java.util.Map;

public interface Calculable {
    Calculable calc(Map<Integer, Calculable> argMap, Contextable context);

    default boolean isStageable() {
        return false;
    }
}
