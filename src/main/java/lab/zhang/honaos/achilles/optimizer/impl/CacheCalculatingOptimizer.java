package lab.zhang.honaos.achilles.optimizer.impl;

import javafx.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.Optimizable;
import lab.zhang.honaos.achilles.token.Calculable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrj
 */
public class CacheCalculatingOptimizer<V> implements Optimizable<V> {

    public static final String CONTEXT_READ_KEY = "cc_read";

    public static final String CONTEXT_WRITE_KEY = "cc_write";

    public static final String OPTIMIZE_INFO_RESULT = "result";

    private final Map<String, Object> optimizedResultMap = new HashMap<>();

    public static Map<Integer, Calculable> readCache(TreeNode<Calculable> node, Contextable context) {
        Object cacheReadObject = context.get(CONTEXT_READ_KEY);
        if (!(cacheReadObject instanceof Map)) {
            throw new RuntimeException("traversal of readCache should be an instant of Map");
        }
        Map<TreeNode<Calculable>, Map<Integer, Calculable>> readMap = (Map<TreeNode<Calculable>, Map<Integer, Calculable>>) cacheReadObject;
        return readMap.get(node);
    }

    public static void writeCache(TreeNode<Calculable> node, Contextable context, Calculable result) {
        Object cacheWriteObject = context.get(CONTEXT_WRITE_KEY);
        if (!(cacheWriteObject instanceof Map)) {
            throw new RuntimeException("traversal of writeCache should be an instant of Map");
        }
        Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>> writeMap = (ConcurrentHashMap<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>>) cacheWriteObject;
        Pair<Map<Integer, Calculable>, Integer> listIndexPair = writeMap.get(node);
        if (listIndexPair == null) {
            return;
        }
        listIndexPair.getKey().put(listIndexPair.getValue(), result);
    }


    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        Map<TreeNode<V>, Map<Integer, V>> readMap = new ConcurrentHashMap<>();
        context.put(CONTEXT_READ_KEY, readMap);
        Map<TreeNode<V>, Pair<Map<Integer, V>, Integer>> writeMap = new ConcurrentHashMap<>();
        context.put(CONTEXT_WRITE_KEY, writeMap);
        doTravel(root, null, 0, context);

        Map<Integer, V> resultMap = initMap(1);
        writeMap.put(root, new Pair<>(resultMap, 0));
        optimizedResultMap.put(OPTIMIZE_INFO_RESULT, resultMap);
    }

    @Override
    public Object getOptimizeInfo() {
        return optimizedResultMap;
    }

    private void doTravel(TreeNode<V> node, TreeNode<V> parent, int indexFromParent, Contextable context) {
        if (node == null) {
            return;
        }

        // retrieve read data from the context
        Object readObject = context.get(CONTEXT_READ_KEY);
        if (!(readObject instanceof Map)) {
            throw new RuntimeException("traversal of output should be an instant of Map");
        }
        Map<TreeNode<V>, Map<Integer, V>> readMap = (ConcurrentHashMap<TreeNode<V>, Map<Integer, V>>) readObject;
        if (!readMap.containsKey(node)) {
            readMap.put(node, initMap(node.getChildren().size()));
        }

        if (parent != null) {
            Map<Integer, V> cacheParamValueMap = readMap.get(parent);
            // retrieve write data from the context
            Object writeObject = context.get(CONTEXT_WRITE_KEY);
            if (!(writeObject instanceof Map)) {
                throw new RuntimeException("traversal of input should be an instant of Map");
            }
            Map<TreeNode<V>, Pair<Map<Integer, V>, Integer>> writeMap = (ConcurrentHashMap<TreeNode<V>, Pair<Map<Integer, V>, Integer>>) writeObject;
            writeMap.put(node, new Pair<>(cacheParamValueMap, indexFromParent));
        }

        // children
        for (int i = 0; i < node.getChildren().size(); i++) {
            TreeNode<V> child = node.getChildren().get(i);
            if (child == null) {
                continue;
            }
            doTravel(child, node, i, context);
        }
    }

    private Map<Integer, V> initMap(int size) {
        return new ConcurrentHashMap<>(size);
    }
}
