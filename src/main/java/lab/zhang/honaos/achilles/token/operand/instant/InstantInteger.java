package lab.zhang.honaos.achilles.token.operand.instant;

import lab.zhang.honaos.achilles.context.Contextable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InstantInteger extends InstantOperand<Integer> {

    private int value;

    public InstantInteger() {
        super();
    }

    public InstantInteger(int value) {
        this();
        this.value = value;
    }

    @Override
    public Integer eval(Contextable context) {
        return value;
    }
}
