package lab.zhang.honaos.achilles.compiler.optimizer.impl;

import lab.zhang.honaos.achilles.compiler.ast.TreeNode;
import lab.zhang.honaos.achilles.compiler.context.IContext;
import lab.zhang.honaos.achilles.compiler.optimizer.IOptimize;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrj
 */
public class ReverseGenerationOptimizeImpl<V> implements IOptimize<V> {

    public static final String CONTEXT_OUTPUT_KEY = "rev_gen";

    @Override
    public void optimize(TreeNode<V> root, IContext context) {
        context.put(CONTEXT_OUTPUT_KEY, new ConcurrentHashMap<TreeNode<V>, TreeNode<V>>());
        doTravel(root, context);
    }


    private void doTravel(TreeNode<V> node, IContext context) {
        if (node == null) {
            return;
        }

        // leaf
        if (node.isLeaf()) {
            return;
        }

        // retrieve data from the context
        Object traversalObject = context.get(CONTEXT_OUTPUT_KEY);
        if (!(traversalObject instanceof ConcurrentHashMap)) {
            throw new RuntimeException("traversal should be an instant of ConcurrentHashMap");
        }
        ConcurrentHashMap<TreeNode<V>, TreeNode<V>> traversalMap = (ConcurrentHashMap<TreeNode<V>, TreeNode<V>>) traversalObject;

        // children
        for (TreeNode<V> child : node.getChildren()) {
            if (child == null) {
                continue;
            }
            traversalMap.put(child, node);
            doTravel(child, context);
        }
    }
}
