package lab.zhang.honaos.achilles.optimizer.impl.priority;

import javafx.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.exception.NotExistException;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultValueOptimizer<V> extends PriorityOptimizer<V> {

    @Getter
    private final int priorityValue = Integer.MAX_VALUE;

    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        Object writeObj = context.get(CONTEXT_CACHE_CALCULATING_WRITE_KEY);
        if (writeObj == null) {
            throw new NotExistException(String.format("The context of %s is not exist", CONTEXT_CACHE_CALCULATING_WRITE_KEY));
        }
        Map<TreeNode<V>, Pair<Map<Integer, V>, Integer>> writeMap = (Map<TreeNode<V>, Pair<Map<Integer, V>, Integer>>) writeObj;
        writeMap.put(root, new Pair<>(new ConcurrentHashMap<>(1), 0));
    }
}
