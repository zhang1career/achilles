package lab.zhang.honaos.achilles.optimizer.impl.priority;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lombok.Getter;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhangrj
 */
public class StageRoutingOptimizer<V> extends PriorityOptimizer<V> {

    @Getter
    private final int priorityValue = 400;

    private final IsStageable<V> isStageable;

    public StageRoutingOptimizer(IsStageable<V> isStageable) {
        this.isStageable = isStageable;
    }

    @Override
    public void optimize(TreeNode<V> root, Contextable context) {
        context.put(CONTEXT_STAGE_ROUTING_OUTPUT_KEY, new ConcurrentLinkedQueue<TreeNode<V>>());
        doTravel(root, context, isStageable);
    }

    private void doTravel(TreeNode<V> node, Contextable context, IsStageable<V> isStageable) {
        if (node == null) {
            return;
        }

        // retrieve data from the context
        ConcurrentLinkedQueue<TreeNode<V>> stageRouteQueue = getStageNodes(context);

        // leaf
        if (node.isLeaf()) {
            if (isStageable.isStageable(node, context)) {
                stage(node, stageRouteQueue);
            }
            return;
        }

        // children
        for (int i = 0; i < node.getChildren().size(); i++) {
            TreeNode<V> child = node.getChildren().get(i);
            if (child == null) {
                continue;
            }
            doTravel(child, context, isStageable);
        }
        if (isStageable.isStageable(node, context)) {
            stage(node, stageRouteQueue);
        }
    }

    private void stage(TreeNode<V> node, ConcurrentLinkedQueue<TreeNode<V>> stagedRouteQueue) {
        stagedRouteQueue.add(node);
    }

    public static interface IsStageable<V> {
        boolean isStageable(TreeNode<V> node, Contextable context);
    }

    public static <V> ConcurrentLinkedQueue<TreeNode<V>> getStageNodes(Contextable context) {
        Object stageRouteObject = context.get(CONTEXT_STAGE_ROUTING_OUTPUT_KEY);
        if (!(stageRouteObject instanceof ConcurrentLinkedQueue)) {
            throw new RuntimeException("stage route should be an instant of ConcurrentLinkedQueue");
        }
        ConcurrentLinkedQueue<TreeNode<V>> stageRouteQueue = (ConcurrentLinkedQueue<TreeNode<V>>) stageRouteObject;
        return stageRouteQueue;
    }
}
