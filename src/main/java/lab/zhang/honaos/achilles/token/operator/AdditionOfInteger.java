package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantInteger;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhangrj
 */
@Slf4j
public enum AdditionOfInteger implements Calculable {
    /**
     * singleton
     */
    INSTANCE;

    @Override
    public Calculable calc(Map<Integer, Calculable> argMap, Contextable context) {
        int sum = 0;
        for (int i = 0; i < argMap.size(); i++) {
            Calculable calculable = argMap.get(i);
            if (!(calculable instanceof InstantInteger)) {
                log.trace("The paramList should be a list of InstantInteger.");
                return null;
            }
            sum += ((InstantInteger) calculable).eval(context);
        }
        return new InstantInteger(sum);
    }
}
