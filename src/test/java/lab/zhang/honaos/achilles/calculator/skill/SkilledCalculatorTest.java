package lab.zhang.honaos.achilles.calculator.skill;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.calculator.worker.WorkerPool;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.Optimizable;
import lab.zhang.honaos.achilles.optimizer.OptimizeFilter;
import lab.zhang.honaos.achilles.optimizer.impl.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.ParallelPruningOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.ReverseGenerationOptimizer;
import lab.zhang.honaos.achilles.token.Valuable;
import lab.zhang.honaos.achilles.token.operand.InstantInteger;
import lab.zhang.honaos.achilles.token.operand.Operand;
import lab.zhang.honaos.achilles.token.operator.Addition;
import lab.zhang.honaos.achilles.token.operator.Operator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SkilledCalculatorTest {
    private SkilledCalculator target;

    private OptimizeFilter<Valuable> optimizeFilter;
    private List<Optimizable<Valuable>> optimizerList;
    private Optimizable<Valuable> calculatingCacheOptimizer;
    private Optimizable<Valuable> parallelPruningOptimizer;
    private Optimizable<Valuable> reverseGenerationOptimizer;

    private TreeNode<Valuable> node0;
    private TreeNode<Valuable> node1;
    private TreeNode<Valuable> node2;

    private Operator operator;
    private Operand operand1;
    private Operand operand2;

    @Before
    public void setUp() {
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
        operator = new Addition();
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
        node0.setValue(node1, 0);
        node0.setValue(node2, 1);
    }

    @Test
    public void test_calculate() throws Exception {
        Contextable context = optimizeFilter.filter(node0, optimizerList);
        WorkerPool<TreeNode<Valuable>, Valuable> workerPool = new WorkerPool<>(SkilledCalculator.class, 2, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Valuable>>> parallelPruningValueList = (List<List<TreeNode<Valuable>>>) parallelPruningValueObject;
        for (List<TreeNode<Valuable>> treeNodeList : parallelPruningValueList) {
            List<Valuable> resultList = workerPool.layoutBlockedJobs(treeNodeList);
            resultList.forEach(result -> System.out.println(((InstantInteger) result).getValue()));
        }
    }
}