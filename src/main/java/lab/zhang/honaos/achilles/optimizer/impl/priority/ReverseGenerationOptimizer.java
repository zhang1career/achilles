package lab.zhang.honaos.achilles.optimizer.impl.priority;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrj
 */
public class ReverseGenerationOptimizer<V> extends PriorityOptimizer<V> {

    @Getter
    private final int priorityValue = 300;

    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        context.put(CONTEXT_REVERSE_GENERATION_OUTPUT_KEY, new ConcurrentHashMap<TreeNode<V>, TreeNode<V>>());
        doTravel(root, context);
    }

    private void doTravel(TreeNode<V> node, Contextable context) {
        if (node == null) {
            return;
        }

        // leaf
        if (node.isLeaf()) {
            return;
        }

        // retrieve data from the context
        Object traversalObject = context.get(CONTEXT_REVERSE_GENERATION_OUTPUT_KEY);
        if (!(traversalObject instanceof ConcurrentHashMap)) {
            throw new RuntimeException("traversal should be an instant of ConcurrentHashMap");
        }
        ConcurrentHashMap<TreeNode<V>, TreeNode<V>> traversalMap = (ConcurrentHashMap<TreeNode<V>, TreeNode<V>>) traversalObject;

        // children
        for (int i = 0; i < node.getChildren().size(); i++) {
            TreeNode<V> child = node.getChildren().get(i);
            if (child == null) {
                continue;
            }
            traversalMap.put(child, node);
            doTravel(child, context);
        }
    }
}
