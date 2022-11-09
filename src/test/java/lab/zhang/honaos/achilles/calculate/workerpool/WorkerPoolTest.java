package lab.zhang.honaos.achilles.calculate.workerpool;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.calculate.calculator.BasicCalculator;
import lab.zhang.honaos.achilles.calculate.calculator.StageableCalculator;
import lab.zhang.honaos.achilles.calculate.workable.WorkableCalculator;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.Optimizable;
import lab.zhang.honaos.achilles.optimizer.OptimizeFilter;
import lab.zhang.honaos.achilles.optimizer.impl.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.ParallelPruningOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.ReverseGenerationOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.StageRoutingOptimizer;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantInteger;
import lab.zhang.honaos.achilles.token.operand.instant.InstantMap;
import lab.zhang.honaos.achilles.token.operand.instant.InstantString;
import lab.zhang.honaos.achilles.token.operand.Operand;
import lab.zhang.honaos.achilles.token.operator.AdditionOfInteger;
import lab.zhang.honaos.achilles.token.operator.HttpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkerPoolTest {
    private WorkerPool target;

    private OptimizeFilter<Calculable> optimizeFilter;
    private List<Optimizable<Calculable>> optimizerList;
    private Optimizable<Calculable> calculatingCacheOptimizer;
    private Optimizable<Calculable> parallelPruningOptimizer;
    private Optimizable<Calculable> reverseGenerationOptimizer;
    private Optimizable<Calculable> stageRoutingOptimizer;

    private TreeNode<Calculable> nodeOp0;
    private TreeNode<Calculable> nodeOp1;
    private TreeNode<Calculable> node1;
    private TreeNode<Calculable> node2;
    private TreeNode<Calculable> node3;
    private TreeNode<Calculable> node4;
    private TreeNode<Calculable> node5;
    private TreeNode<Calculable> node6;
    private TreeNode<Calculable> node7;
    private TreeNode<Calculable> node8;

    private AdditionOfInteger operator0;
    private HttpClient operator1;
    private Operand operand1;
    private Operand operand2;
    private Operand operand3;
    private Operand operand4;
    private Operand operand5;
    private Operand operand6;
    private Operand operand7;
    private Operand operand8;

    @Before
    public void setUp() {
        // optimizer filter
        optimizeFilter = new OptimizeFilter<>();
        // optimizer
        calculatingCacheOptimizer = new CacheCalculatingOptimizer<>();
        parallelPruningOptimizer = new ParallelPruningOptimizer<>();
        reverseGenerationOptimizer = new ReverseGenerationOptimizer<>();
        stageRoutingOptimizer = new StageRoutingOptimizer<>((node, context) -> node.getValue().isStageable());
        // optimizer link
        optimizerList = new ArrayList<>();
        optimizerList.add(calculatingCacheOptimizer);
        optimizerList.add(parallelPruningOptimizer);
        optimizerList.add(reverseGenerationOptimizer);
        optimizerList.add(stageRoutingOptimizer);
        // token
        operator0 = AdditionOfInteger.INSTANCE;
        operator1 = HttpClient.INSTANCE;
        operand1 = new InstantInteger(100);
        operand2 = new InstantInteger(200);
        operand3 = new InstantString("GET");
        operand4 = new InstantString("HTTP");
        operand5 = new InstantString("no-url.com");
        // param/head/body
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("value", "3");
        operand6 = new InstantMap(paramMap);
        operand7 = new InstantMap(new HashMap<>());
        operand8 = new InstantMap(new HashMap<>());
    }

    /***************************************************
     * single operator under test
     **************************************************/
    @Test
    public void test_single_operator_additionOfInteger() throws Exception {
        nodeOp0 = new TreeNode<>(operator0);
        node1 = new TreeNode<>(operand1);
        node2 = new TreeNode<>(operand2);
        /**
         *    op0
         *  /     \
         * 1       2
         */
        nodeOp0.setValue(node1, 0);
        nodeOp0.setValue(node2, 1);

        Contextable context = optimizeFilter.filter(nodeOp0, optimizerList);
        target = new WorkerPool<>(WorkableCalculator.class, new BasicCalculator(), 2, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            List<Calculable> resultList = target.dispatch(treeNodeList);
            resultList.forEach(result -> System.out.println(((InstantInteger) result).getValue()));
        }
    }

    @Test
    public void test_single_operator_httpClient() throws Exception {
        nodeOp1 = new TreeNode<>(operator1);
        node3 = new TreeNode<>(operand3);
        node4 = new TreeNode<>(operand4);
        node5 = new TreeNode<>(operand5);
        node6 = new TreeNode<>(operand6);
        /**
         *      op1(GET http://no-url.com?value=3)
         *    /  |  \
         *  3   4   5   6
         * GET HTTP url (value, 3)
         */
        nodeOp1.setValue(node3, 0);
        nodeOp1.setValue(node4, 1);
        nodeOp1.setValue(node5, 2);
        nodeOp1.setValue(node6, 3);
        Contextable context = optimizeFilter.filter(nodeOp1, optimizerList);
        target = new WorkerPool<>(WorkableCalculator.class, new BasicCalculator(), 10, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            List<Calculable> resultList = target.dispatch(treeNodeList);
            resultList.forEach(result -> System.out.println(result));
        }
    }


    /***************************************************
     * nested operator under test
     **************************************************/
    @Test
    public void test_nested_operators_additionOfInteger() throws Exception {
        nodeOp0 = new TreeNode<>(operator0);
        nodeOp1 = new TreeNode<>(operator0);
        node1 = new TreeNode<>(operand1);
        node2 = new TreeNode<>(operand2);
        node3 = new TreeNode<>(operand2);
        /**
         *    op0
         *  /     \
         * 1      op1
         *      /     \
         *     2       3
         */
        nodeOp0.setValue(node1, 0);
        nodeOp0.setValue(nodeOp1, 1);
        nodeOp1.setValue(node2, 0);
        nodeOp1.setValue(node3, 1);

        Contextable context = optimizeFilter.filter(nodeOp0, optimizerList);
        target = new WorkerPool<>(WorkableCalculator.class, new BasicCalculator(), 2, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            List<Calculable> resultList = target.dispatch(treeNodeList);
            resultList.forEach(result -> System.out.println(((InstantInteger) result).getValue()));
        }
    }

    @Test
    public void test_stage_route() throws Exception {
        nodeOp0 = new TreeNode<>(operator0);
        nodeOp1 = new TreeNode<>(operator1);
        node1 = new TreeNode<>(operand1);
        node2 = new TreeNode<>(operand2);
        node3 = new TreeNode<>(operand3);
        node4 = new TreeNode<>(operand4);
        node5 = new TreeNode<>(operand5);
        node6 = new TreeNode<>(operand6);
        node7 = new TreeNode<>(operand7);
        node8 = new TreeNode<>(operand8);
        /**
         *    op0
         *  /  |  \
         * 1   2   op1(GET http://no-url.com?value=3)
         *       /  |  \
         *  3   4   5   6
         * GET HTTP url (value, 3)
         */
        nodeOp0.setValue(node1, 0);
        nodeOp0.setValue(node2, 1);
        nodeOp0.setValue(nodeOp1, 2);
        nodeOp1.setValue(node3, 0);
        nodeOp1.setValue(node4, 1);
        nodeOp1.setValue(node5, 2);
        nodeOp1.setValue(node6, 3);
        Contextable context = optimizeFilter.filter(nodeOp0, optimizerList);
        target = new WorkerPool<>(WorkableCalculator.class, new StageableCalculator(), 5, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            List<Calculable> resultList = target.dispatch(treeNodeList);
            resultList.forEach(result -> System.out.println(result));
        }
    }
}