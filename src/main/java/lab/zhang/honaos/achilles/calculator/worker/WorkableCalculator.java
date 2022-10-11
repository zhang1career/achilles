package lab.zhang.honaos.achilles.calculator.worker;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.calculator.ElementaryCalculator;
import lab.zhang.honaos.achilles.token.Valuable;

public class WorkableCalculator implements Workable<TreeNode<Valuable>, Valuable> {

    private final ElementaryCalculator calculator;

    public WorkableCalculator() {
        this.calculator = new ElementaryCalculator();
    }

    @Override
    public Valuable work(TreeNode<Valuable> input, Contextable context) {
        return calculator.calculate(input, context);
    }
}