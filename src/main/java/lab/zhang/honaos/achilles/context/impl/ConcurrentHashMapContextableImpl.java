package lab.zhang.honaos.achilles.context.impl;

import lab.zhang.honaos.achilles.context.Contextable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrj
 */
public class ConcurrentHashMapContextableImpl implements Contextable {

    private Map<Object, Object> valueMap;

    public ConcurrentHashMapContextableImpl() {
        this.valueMap = new ConcurrentHashMap<>();
    }

    @Override
    public void put(Object key, Object value) {
        valueMap.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return valueMap.get(key);
    }

    @Override
    public Map<Object, Object> dump() {
        return valueMap;
    }
}
