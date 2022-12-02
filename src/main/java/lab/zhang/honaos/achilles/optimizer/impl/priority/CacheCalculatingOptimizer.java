package lab.zhang.honaos.achilles.optimizer.impl.priority;

import javafx.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lab.zhang.honaos.achilles.token.Calculable;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrj
 */
public class CacheCalculatingOptimizer<V> extends PriorityOptimizer<V> {

    @Getter
    private final int priorityValue = 100;

    public static Map<Integer, Calculable> readCache(TreeNode<Calculable> node, Contextable context) {
        Object cacheReadObject = context.get(CONTEXT_CACHE_CALCULATING_READ_KEY);
        if (!(cacheReadObject instanceof Map)) {
            throw new RuntimeException("traversal of readCache should be an instant of Map");
        }
        Map<TreeNode<Calculable>, Map<Integer, Calculable>> readMap = (Map<TreeNode<Calculable>, Map<Integer, Calculable>>) cacheReadObject;
        return readMap.get(node);
    }

    public static void writeCache(TreeNode<Calculable> node, Contextable context, Calculable result) {
        Object cacheWriteObject = context.get(CONTEXT_CACHE_CALCULATING_WRITE_KEY);
        if (!(cacheWriteObject instanceof Map)) {
            throw new RuntimeException("traversal of writeCache should be an instant of Map");
        }
        Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>> writeMap = (ConcurrentHashMap<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>>) cacheWriteObject;
        Pair<Map<Integer, Calculable>, Integer> mapIndexPair = writeMap.get(node);
        if (mapIndexPair == null) {
            return;
        }
        mapIndexPair.getKey().put(mapIndexPair.getValue(), result);
    }

    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        Map<TreeNode<V>, Map<Integer, V>> readMap = new ConcurrentHashMap<>();
        context.put(CONTEXT_CACHE_CALCULATING_READ_KEY, readMap);
        Map<TreeNode<V>, Pair<Map<Integer, V>, Integer>> writeMap = new ConcurrentHashMap<>();
        context.put(CONTEXT_CACHE_CALCULATING_WRITE_KEY, writeMap);
        doTravel(root, null, 0, context);
    }

    private void doTravel(TreeNode<V> node, TreeNode<V> parent, int indexFromParent, Contextable context) {
        if (node == null) {
            return;
        }

        // retrieve read data from the context
        Object readObject = context.get(CONTEXT_CACHE_CALCULATING_READ_KEY);
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
            Object writeObject = context.get(CONTEXT_CACHE_CALCULATING_WRITE_KEY);
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
