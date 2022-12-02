package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantBigDecimal;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author zhangrj
 */
public enum AdditionOfBigDecimal implements Calculable {
    /**
     * singleton
     */
    INSTANCE;

    @Override
    public Calculable calc(Map<Integer, Calculable> argMap, Contextable context) {
        BigDecimal sum = BigDecimal.valueOf(0);

        for (int i = 0; i < argMap.size(); i++) {
            Calculable arg = argMap.get(i);
            if (!(arg instanceof InstantBigDecimal)) {
                throw new IllegalArgumentException("The argument should be an instance of InstantBigDecimal.");
            }
            sum = sum.add(((InstantBigDecimal) arg).eval(context));
        }
        return new InstantBigDecimal(sum);
    }
}
