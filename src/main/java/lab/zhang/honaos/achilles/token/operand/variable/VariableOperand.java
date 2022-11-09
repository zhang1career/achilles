package lab.zhang.honaos.achilles.token.operand.variable;

import lab.zhang.honaos.achilles.token.operand.Operand;
import lab.zhang.zhangtool.util.UuidUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
abstract public class VariableOperand<T> extends Operand<T> {

    private String uuid;

    public VariableOperand() {
        this.uuid = UuidUtil.uuid();
    }

}