package lab.zhang.honaos.achilles.token.operand.instant;

import lab.zhang.honaos.achilles.context.Contextable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class InstantMap extends InstantOperand<Map<String, Object>> {

    private Map<String, Object> value;

    public InstantMap() {
        super();
    }

    public InstantMap(Map<String, Object> value) {
        this();
        this.value = value;
    }

    @Override
    public Map<String, Object> eval(Contextable context) {
        return value;
    }
}
