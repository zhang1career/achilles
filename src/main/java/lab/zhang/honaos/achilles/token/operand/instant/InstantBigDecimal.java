package lab.zhang.honaos.achilles.token.operand.instant;

import lab.zhang.honaos.achilles.context.Contextable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class InstantBigDecimal extends InstantOperand<BigDecimal> {

    private BigDecimal value;

    public InstantBigDecimal() {
        super();
    }

    public InstantBigDecimal(BigDecimal value) {
        this();
        this.value = value;
    }

    public InstantBigDecimal(String value) {
        this();
        this.value = new BigDecimal(value);
    }

    @Override
    public BigDecimal eval(Contextable context) {
        return value;
    }
}
