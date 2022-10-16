package lab.zhang.honaos.achilles.token.operand;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.Valuable;
import lombok.Data;

import java.util.List;

@Data
abstract public class Operand<T> implements Calculable, Valuable<T> {
    @Override
    public Calculable calc(List<Calculable> paramList, Contextable context) {
        return this;
    }
}
