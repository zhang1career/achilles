package lab.zhang.honaos.achilles.calculate;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.calculate.calculator.BasicCalculator;
import lab.zhang.honaos.achilles.calculate.calculator.Calculator;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.OptimizeFilter;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.priority.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.priority.ParallelPruningOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.priority.ReverseGenerationOptimizer;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.Operand;
import lab.zhang.honaos.achilles.token.operand.instant.InstantInteger;
import lab.zhang.honaos.achilles.token.operator.AdditionOfInteger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    private Calculator target;

    private OptimizeFilter<Calculable> optimizeFilter;
    private List<PriorityOptimizer<Calculable>> optimizerList;
    private PriorityOptimizer<Calculable> calculatingCacheOptimizer;
    private PriorityOptimizer<Calculable> parallelPruningOptimizer;
    private PriorityOptimizer<Calculable> reverseGenerationOptimizer;

    private TreeNode<Calculable> node0;
    private TreeNode<Calculable> node1;
    private TreeNode<Calculable> node2;

    private AdditionOfInteger operator;
    private Operand operand1;
    private Operand operand2;

    @Before
    public void setUp() {
        target = new BasicCalculator();
        // optimizer filter
        optimizeFilter = new OptimizeFilter<>();
        // optimizer
        calculatingCacheOptimizer = new CacheCalculatingOptimizer<>();
        parallelPruningOptimizer = new ParallelPruningOptimizer<>();
        reverseGenerationOptimizer = new ReverseGenerationOptimizer<>();
        // optimizer link
        optimizerList = new ArrayList<>();
        optimizerList.add(calculatingCacheOptimizer);
        optimizerList.add(parallelPruningOptimizer);
        optimizerList.add(reverseGenerationOptimizer);
        // token
        operator = AdditionOfInteger.INSTANCE;
        operand1 = new InstantInteger(100);
        operand2 = new InstantInteger(200);
        // tree node
        node0 = new TreeNode<>(operator);
        node1 = new TreeNode<>(operand1);
        node2 = new TreeNode<>(operand2);
        // tree
        /**
         *     0
         *  /     \
         * 1       2
         */
        node0.setChild(0, node1);
        node0.setChild(1, node2);
    }

    @Test
    public void test_calculate() throws Exception {
        Contextable context = optimizeFilter.filter(node0, optimizerList);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        Calculable result = null;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            for (TreeNode<Calculable> treeNode : treeNodeList) {
                result = target.calculate(treeNode, context);
                System.out.println(result);
            }
        }
        assertEquals(300, ((InstantInteger) result).getValue());
    }
}