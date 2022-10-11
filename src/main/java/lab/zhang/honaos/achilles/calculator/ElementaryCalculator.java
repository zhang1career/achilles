package lab.zhang.honaos.achilles.calculator;

import com.sun.tools.javac.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Valuable;
import lab.zhang.honaos.achilles.optimizer.impl.CalculatingCacheOptimizableImpl;

import java.util.HashMap;
import java.util.List;

public class ElementaryCalculator {

    public Valuable calculate(TreeNode<Valuable> node, Contextable context) {
        Valuable result = doCalculate(node, context);
        writeCache(node, context, result);
        return result;
    }

    private Valuable doCalculate(TreeNode<Valuable> node, Contextable context) {
        if (node.isLeaf()) {
            return node.getValue().evaluate(null, context);
        }
        List<Valuable> cachedParamList = readCache(node, context);
        return node.getValue().evaluate(cachedParamList, context);
    }

    private List<Valuable> readCache(TreeNode<Valuable> node, Contextable context) {
        Object cacheReadObject = context.get(CalculatingCacheOptimizableImpl.CONTEXT_READ_KEY);
        if (!(cacheReadObject instanceof HashMap)) {
            throw new RuntimeException("traversal of readCache should be an instant of HashMap");
        }
        HashMap<TreeNode<Valuable>, List<Valuable>> readMap = (HashMap<TreeNode<Valuable>, List<Valuable>>) cacheReadObject;
        return readMap.get(node);
    }

    private void writeCache(TreeNode<Valuable> node, Contextable context, Valuable result) {
        Object cacheWriteObject = context.get(CalculatingCacheOptimizableImpl.CONTEXT_WRITE_KEY);
        if (!(cacheWriteObject instanceof HashMap)) {
            throw new RuntimeException("traversal of writeCache should be an instant of HashMap");
        }
        HashMap<TreeNode<Valuable>, Pair<List<Valuable>, Integer>> writeMap = (HashMap<TreeNode<Valuable>, Pair<List<Valuable>, Integer>>) cacheWriteObject;
        Pair<List<Valuable>, Integer> listIndexPair = writeMap.get(node);
        if (listIndexPair == null) {
            return;
        }
        listIndexPair.fst.set(listIndexPair.snd, result);
    }
}
