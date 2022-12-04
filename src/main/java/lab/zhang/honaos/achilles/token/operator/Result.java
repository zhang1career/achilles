package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhangrj
 */
@Slf4j
public enum Result implements Calculable {
    /**
     * singleton
     */
    INSTANCE;

    @Override
    public Calculable calc(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable arg = argMap.get(0);
        if (arg == null) {
            log.trace("The argument is not found.");
        }
        return null;
    }
}
