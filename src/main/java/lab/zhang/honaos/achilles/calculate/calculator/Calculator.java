package lab.zhang.honaos.achilles.calculate.calculator;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;

public interface Calculator {
    Calculable calculate(TreeNode<Calculable> node, Contextable context);
}
