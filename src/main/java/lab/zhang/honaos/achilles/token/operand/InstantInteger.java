package lab.zhang.honaos.achilles.token.operand;

import lab.zhang.honaos.achilles.context.Contextable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstantInteger extends Operand<Integer> {

    private int value;

    @Override
    public Integer eval(Contextable context) {
        return value;
    }
}
