package lab.zhang.honaos.achilles.compiler.optimizer.impl;

import lab.zhang.honaos.achilles.compiler.ast.TreeNode;
import lab.zhang.honaos.achilles.compiler.context.IContext;
import lab.zhang.honaos.achilles.compiler.optimizer.IOptimize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangrj
 */
public class ParallelPruningOptimizeImpl<V> implements IOptimize<V> {

    public static final String CONTEXT_OUTPUT_KEY = "para_prune";

    @Override
    public void optimize(TreeNode<V> root, IContext context) {
        context.put(CONTEXT_OUTPUT_KEY, Collections.synchronizedList(new ArrayList<List<TreeNode<V>>>()));
        doTravel(root, context);
    }

    private int doTravel(TreeNode<V> node, IContext context) {
        if (node == null) {
            return -1;
        }

        // retrieve data from the context
        Object valueObject = context.get(CONTEXT_OUTPUT_KEY);
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
        for (TreeNode<V> child : node.getChildren()) {
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
