package lab.zhang.honaos.achilles.compiler.context;

import java.util.Map;

/**
 * @author zhangrj
 */
public interface IContext {

    void put(Object key, Object value);

    Object get(Object key);

    Map<Object, Object> dump();
}
