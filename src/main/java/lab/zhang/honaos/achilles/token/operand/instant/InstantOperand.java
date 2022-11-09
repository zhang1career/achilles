package lab.zhang.honaos.achilles.token.operand.instant;

import lab.zhang.honaos.achilles.token.operand.Operand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
abstract public class InstantOperand<T> extends Operand<T> {
}
