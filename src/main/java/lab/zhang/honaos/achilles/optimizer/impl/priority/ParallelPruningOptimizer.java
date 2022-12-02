package lab.zhang.honaos.achilles.optimizer.impl.priority;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangrj
 */
public class ParallelPruningOptimizer<V> extends PriorityOptimizer<V> {

    @Getter
    private final int priorityValue = 200;

    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        context.put(CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY, Collections.synchronizedList(new ArrayList<List<TreeNode<V>>>()));
        doTravel(root, context);
    }

    private int doTravel(TreeNode<V> node, Contextable context) {
        if (node == null) {
            return -1;
        }

        // retrieve data from the context
        Object valueObject = context.get(CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY);
        if (!(valueObject instanceof List)) {
            throw new RuntimeException("traversal value should be an instant of List");
        }
        List<List<TreeNode<V>>> valueList = (List<List<TreeNode<V>>>) valueObject;

        // leaf
        if (node.isLeaf()) {
            reverseLevelAdd(valueList, 0, node);
            return 0;
        }

        // children
        int maxLevel = 0;
        for (int i = 0; i < node.getChildren().size(); i++) {
            TreeNode<V> child = node.getChildren().get(i);
            if (child == null) {
                continue;
            }
            int tempLevel = doTravel(child, context);
            maxLevel = Math.max(maxLevel, tempLevel);
        }

        int currentLevel = maxLevel + 1;
        reverseLevelAdd(valueList, maxLevel + 1, node);

        return currentLevel;
    }

    private void reverseLevelAdd(List<List<TreeNode<V>>> valueList, int level, TreeNode<V> node) {
        enlargeList(valueList, level);

        List<TreeNode<V>> concurrentableList = valueList.get(level);
        if (concurrentableList == null) {
            concurrentableList = Collections.synchronizedList(new ArrayList<>());
            valueList.add(level, concurrentableList);
        }

        concurrentableList.add(node);
    }

    private void enlargeList(List<List<TreeNode<V>>> valueList, int level) {
        if (valueList.size() > level) {
            return;
        }

        for (int i = valueList.size(); i <= level; i++) {
            valueList.add(Collections.synchronizedList(new ArrayList<>()));
        }
    }
}
