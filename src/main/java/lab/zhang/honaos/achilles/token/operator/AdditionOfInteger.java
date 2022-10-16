package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.InstantInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author zhangrj
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdditionOfInteger extends Operator {

    @Override
    public Calculable calc(List<Calculable> paramList, Contextable context) {
        int sum = 0;
        for (Calculable calculable : paramList) {
            if (!(calculable instanceof InstantInteger)) {
                throw new IllegalArgumentException("The paramList should be a list of InstantInteger.");
            }
            sum += ((InstantInteger) calculable).getValue();
        }
        return new InstantInteger(sum);
    }
}
