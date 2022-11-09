package lab.zhang.honaos.achilles.token.operand.instant;

import lab.zhang.honaos.achilles.context.Contextable;

public class InstantNull extends InstantOperand<Object> {

    @Override
    public Object eval(Contextable context) {
        return null;
    }
}
