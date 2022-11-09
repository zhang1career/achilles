package lab.zhang.honaos.achilles.calculate.calculator;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.token.Calculable;

import java.util.List;

public class BasicCalculator extends AbstractCalculator {

    protected Calculable doCalculate(TreeNode<Calculable> node, Contextable context) {
        if (node.isLeaf()) {
            return node.getValue().calc(null, context);
        }
        List<Calculable> cachedParamList = CacheCalculatingOptimizer.readCache(node, context);
        return node.getValue().calc(cachedParamList, context);
    }

}
