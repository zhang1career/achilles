package lab.zhang.honaos.achilles.optimizer.impl;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.Optimizable;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhangrj
 */
public class StageRoutingOptimizer<V> implements Optimizable<V> {

    public static final String CONTEXT_OUTPUT_KEY = "stg_route";

    public static interface IsStageable<V> {
        boolean isStageable(TreeNode<V> node, Contextable context);
    }

    private final IsStageable<V> isStageable;

    public StageRoutingOptimizer(IsStageable<V> isStageable) {
        this.isStageable = isStageable;
    }

    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        context.put(CONTEXT_OUTPUT_KEY, new ConcurrentLinkedQueue<TreeNode<V>>());
        doTravel(root, context, isStageable);
    }

    private void doTravel(TreeNode<V> node, Contextable context, IsStageable<V> isStageable) {
        if (node == null) {
            return;
        }

        // retrieve data from the context
        Object stagedRouteObject = context.get(CONTEXT_OUTPUT_KEY);
        if (!(stagedRouteObject instanceof ConcurrentLinkedQueue)) {
            throw new RuntimeException("staged route should be an instant of ConcurrentLinkedQueue");
        }
        ConcurrentLinkedQueue<TreeNode<V>> stagedRouteQueue = (ConcurrentLinkedQueue<TreeNode<V>>) stagedRouteObject;
        if (isStageable.isStageable(node, context)) {
            stagedRouteQueue.add(node);
        }

        // leaf
        if (node.isLeaf()) {
            return;
        }

        // children
        for (TreeNode<V> child : node.getChildren()) {
            doTravel(child, context, isStageable);
        }
    }
}
