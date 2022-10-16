package lab.zhang.honaos.achilles.token.operand;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.util.BigDecimalUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstantBigDecimal extends Operand<String> {

    private BigDecimal value;

    public InstantBigDecimal(double value) {
        super();
        this.value = BigDecimal.valueOf(value);
    }

    @Override
    public String eval(Contextable context) {
        return BigDecimalUtil.formatWithThreeDecimals(value);
    }
}
