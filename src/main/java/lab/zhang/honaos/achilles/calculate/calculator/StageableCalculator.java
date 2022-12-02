package lab.zhang.honaos.achilles.calculate.calculator;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.priority.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.token.Calculable;

import java.util.Map;

public class StageableCalculator extends AbstractCalculator {

    protected Calculable doCalculate(TreeNode<Calculable> node, Contextable context) {
        Calculable calculable = node.getValue();
        if (calculable.isStageable()) {
            return null;
        }

        if (node.isLeaf()) {
            return node.getValue().calc(null, context);
        }
        Map<Integer, Calculable> cachedParamValueMap = CacheCalculatingOptimizer.readCache(node, context);
        return node.getValue().calc(cachedParamValueMap, context);
    }

}
