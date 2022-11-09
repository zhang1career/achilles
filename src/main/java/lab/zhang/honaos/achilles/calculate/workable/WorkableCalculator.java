package lab.zhang.honaos.achilles.calculate.workable;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.calculate.calculator.Calculator;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;

public class WorkableCalculator implements Workable<TreeNode<Calculable>, Calculable> {

    private final Calculator calculator;

    public WorkableCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public Calculable work(TreeNode<Calculable> input, Contextable context) {
        return calculator.calculate(input, context);
    }
}
