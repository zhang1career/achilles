package lab.zhang.honaos.achilles.context;

import java.util.Map;

/**
 * @author zhangrj
 */
public interface Contextable {

    void put(Object key, Object value);

    Object get(Object key);

    Map<Object, Object> dump();
}
