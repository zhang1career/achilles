package lab.zhang.honaos.achilles.token.operand.instant;

import lab.zhang.honaos.achilles.context.Contextable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InstantString extends InstantOperand<String> {

    private String value;

    public InstantString() {
        super();
    }

    public InstantString(String value) {
        this();
        this.value = value;
    }

    @Override
    public String eval(Contextable context) {
        return value;
    }
}
