package lab.zhang.honaos.achilles.calculator.skill;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.calculator.ElementaryCalculator;
import lab.zhang.honaos.achilles.token.Calculable;

public class SkilledCalculator implements Skilled<TreeNode<Calculable>, Calculable> {

    private final ElementaryCalculator calculator;

    public SkilledCalculator() {
        this.calculator = new ElementaryCalculator();
    }

    @Override
    public Calculable work(TreeNode<Calculable> input, Contextable context) {
        return calculator.calculate(input, context);
    }
}
