package lab.zhang.honaos.achilles.calculate.calculator;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.priority.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantNull;

abstract public class AbstractCalculator implements Calculator {

    public Calculable calculate(TreeNode<Calculable> node, Contextable context) {
        Calculable result = doCalculate(node, context);
        if (result == null) {
            result = new InstantNull();
        }
        CacheCalculatingOptimizer.writeCache(node, context, result);
        return result;
    }

    abstract protected Calculable doCalculate(TreeNode<Calculable> node, Contextable context);
}
