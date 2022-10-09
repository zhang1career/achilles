package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Valuable;
import lab.zhang.honaos.achilles.token.operand.InstantInteger;
import lombok.Data;

import java.util.List;

@Data
public class Addition extends Operator {

    @Override
    public Valuable evaluate(List<Valuable> paramList, Contextable context) {
        int sum = 0;
        for (Valuable valuable : paramList) {
            if (!(valuable instanceof InstantInteger)) {
                throw new IllegalArgumentException("The paramList should be a list of InstantInteger.");
            }
            sum += ((InstantInteger) valuable).getValue();
        }
        return new InstantInteger(sum);
    }
}
