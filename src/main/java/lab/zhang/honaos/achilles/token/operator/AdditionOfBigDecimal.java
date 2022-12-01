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
            Calculable calculable = argMap.get(i);
            if (!(calculable instanceof InstantBigDecimal)) {
                throw new IllegalArgumentException("The paramList should be a list of InstantBigDecimal.");
            }
            sum = sum.add(((InstantBigDecimal) calculable).eval(context));
        }
        return new InstantBigDecimal(sum);
    }
}
