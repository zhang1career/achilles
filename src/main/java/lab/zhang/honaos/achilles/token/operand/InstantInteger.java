package lab.zhang.honaos.achilles.token.operand;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Valuable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstantInteger extends Operand {

    private int value;

    @Override
    public Valuable evaluate(List<Valuable> paramList, Contextable context) {
        return this;
    }
}
