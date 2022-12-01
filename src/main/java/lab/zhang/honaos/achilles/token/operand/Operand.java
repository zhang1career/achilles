package lab.zhang.honaos.achilles.token.operand;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.Valuable;
import lombok.Data;

import java.util.Map;

@Data
abstract public class Operand<T> implements Calculable, Valuable<T> {
    @Override
    public Calculable calc(Map<Integer, Calculable> argMap, Contextable context) {
        return this;
    }
}
