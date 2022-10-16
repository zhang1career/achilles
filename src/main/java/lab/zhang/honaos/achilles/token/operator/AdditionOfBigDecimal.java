package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.InstantBigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangrj
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdditionOfBigDecimal extends Operator {

    @Override
    public Calculable calc(List<Calculable> paramList, Contextable context) {
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Calculable calculable : paramList) {
            if (!(calculable instanceof InstantBigDecimal)) {
                throw new IllegalArgumentException("The paramList should be a list of InstantBigDecimal.");
            }
            sum = sum.add(((InstantBigDecimal) calculable).getValue());
        }
        return new InstantBigDecimal(sum);
    }
}
