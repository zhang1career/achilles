package lab.zhang.honaos.achilles.calculator;

import javafx.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.optimizer.impl.CacheCalculatingOptimizer;

import java.util.HashMap;
import java.util.List;

public class ElementaryCalculator {

    public Calculable calculate(TreeNode<Calculable> node, Contextable context) {
        Calculable result = doCalculate(node, context);
        writeCache(node, context, result);
        return result;
    }

    private Calculable doCalculate(TreeNode<Calculable> node, Contextable context) {
        if (node.isLeaf()) {
            return node.getValue().calc(null, context);
        }
        List<Calculable> cachedParamList = readCache(node, context);
        return node.getValue().calc(cachedParamList, context);
    }

    private List<Calculable> readCache(TreeNode<Calculable> node, Contextable context) {
        Object cacheReadObject = context.get(CacheCalculatingOptimizer.CONTEXT_READ_KEY);
        if (!(cacheReadObject instanceof HashMap)) {
            throw new RuntimeException("traversal of readCache should be an instant of HashMap");
        }
        HashMap<TreeNode<Calculable>, List<Calculable>> readMap = (HashMap<TreeNode<Calculable>, List<Calculable>>) cacheReadObject;
        return readMap.get(node);
    }

    private void writeCache(TreeNode<Calculable> node, Contextable context, Calculable result) {
        Object cacheWriteObject = context.get(CacheCalculatingOptimizer.CONTEXT_WRITE_KEY);
        if (!(cacheWriteObject instanceof HashMap)) {
            throw new RuntimeException("traversal of writeCache should be an instant of HashMap");
        }
        HashMap<TreeNode<Calculable>, Pair<List<Calculable>, Integer>> writeMap = (HashMap<TreeNode<Calculable>, Pair<List<Calculable>, Integer>>) cacheWriteObject;
        Pair<List<Calculable>, Integer> listIndexPair = writeMap.get(node);
        if (listIndexPair == null) {
            return;
        }
        listIndexPair.getKey().set(listIndexPair.getValue(), result);
    }
}
