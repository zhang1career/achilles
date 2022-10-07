package lab.zhang.honaos.achilles.compiler.optimizer.impl;

import com.sun.tools.javac.util.Pair;
import lab.zhang.honaos.achilles.compiler.ast.TreeNode;
import lab.zhang.honaos.achilles.compiler.context.IContext;
import lab.zhang.honaos.achilles.compiler.optimizer.IOptimize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangrj
 */
public class CalculatingCacheOptimizeImpl<V> implements IOptimize<V> {

    public static final String CONTEXT_READ_KEY = "calc_cache_read";

    public static final String CONTEXT_WRITE_KEY = "calc_cache_write";

    @Override
    public void optimize(TreeNode<V> root, IContext context) {
        context.put(CONTEXT_READ_KEY, new HashMap<TreeNode<V>, List<V>>());
        context.put(CONTEXT_WRITE_KEY, new HashMap<TreeNode<V>, Pair<List<V>, Integer>>());
        doTravel(root, null, 0, context);
    }


    private void doTravel(TreeNode<V> node, TreeNode<V> parent, int indexFromParent, IContext context) {
        if (node == null) {
            return;
        }

        // retrieve read data from the context
        Object readObject = context.get(CONTEXT_READ_KEY);
        if (!(readObject instanceof HashMap)) {
            throw new RuntimeException("traversal of output should be an instant of HashMap");
        }
        HashMap<TreeNode<V>, List<V>> readMap = (HashMap<TreeNode<V>, List<V>>) readObject;
        if (!readMap.containsKey(node)) {
            readMap.put(node, initList(node.getChildren().size()));
        }

        if (parent != null) {
            List<V> cacheList = readMap.get(parent);
            // retrieve write data from the context
            Object writeObject = context.get(CONTEXT_WRITE_KEY);
            if (!(writeObject instanceof HashMap)) {
                throw new RuntimeException("traversal of input should be an instant of HashMap");
            }
            HashMap<TreeNode<V>, Pair<List<V>, Integer>> writeMap = (HashMap<TreeNode<V>, Pair<List<V>, Integer>>) writeObject;
            writeMap.put(node, new Pair<>(cacheList, indexFromParent));
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

    private List<V> initList(int size) {
        List<V> objectList = Collections.synchronizedList(new ArrayList<>(size));
        for (int i = 0; i < size; i++) {
            objectList.add(i, null);
        }
        return objectList;
    }
}
