package lab.zhang.honaos.achilles.calculate.workerpool.impl;

import javafx.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.calculate.calculator.BasicCalculator;
import lab.zhang.honaos.achilles.calculate.calculator.StageableCalculator;
import lab.zhang.honaos.achilles.calculate.workable.impl.CalculatingWorkableImpl;
import lab.zhang.honaos.achilles.calculate.workerpool.WorkerPool;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.Optimizable;
import lab.zhang.honaos.achilles.optimizer.OptimizeFilter;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.priority.*;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.Valuable;
import lab.zhang.honaos.achilles.token.operand.Operand;
import lab.zhang.honaos.achilles.token.operand.instant.InstantInteger;
import lab.zhang.honaos.achilles.token.operand.instant.InstantMap;
import lab.zhang.honaos.achilles.token.operand.instant.InstantString;
import lab.zhang.honaos.achilles.token.operator.AdditionOfInteger;
import lab.zhang.honaos.achilles.token.operator.ArgGetter;
import lab.zhang.honaos.achilles.token.operator.HttpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DelegatedWorkerPoolImplTest {
    private WorkerPool target;

    private OptimizeFilter<Calculable> optimizeFilter;
    private List<PriorityOptimizer<Calculable>> optimizerList;
    private PriorityOptimizer<Calculable> calculatingCacheOptimizer;
    private PriorityOptimizer<Calculable> parallelPruningOptimizer;
    private PriorityOptimizer<Calculable> reverseGenerationOptimizer;
    private PriorityOptimizer<Calculable> stageRoutingOptimizer;
    private PriorityOptimizer<Calculable> resultGetterOptimizer;

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
        resultGetterOptimizer = new ResultValueOptimizer<>();
        // optimizer link
        optimizerList = new ArrayList<>();
        optimizerList.add(calculatingCacheOptimizer);
        optimizerList.add(parallelPruningOptimizer);
        optimizerList.add(reverseGenerationOptimizer);
        optimizerList.add(stageRoutingOptimizer);
        optimizerList.add(resultGetterOptimizer);
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
        nodeOp0.setChild(0, node1);
        nodeOp0.setChild(1, node2);

        // root prepare
        Calculable dummyOperator = ArgGetter.INSTANCE;
        TreeNode<Calculable> nodeDummy = new TreeNode<>(dummyOperator);
        nodeDummy.setChild(0, nodeOp0);

        Contextable context = optimizeFilter.filter(nodeDummy, optimizerList);
        target = new DelegatedWorkerPoolImpl<>(CalculatingWorkableImpl.class, BasicCalculator.class, 3, context);

        Object parallelPruningValueObject = context.get(Optimizable.CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            target.dispatch(treeNodeList);
        }

        Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>> writeMap = (Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>>) context.get(Optimizable.CONTEXT_CACHE_CALCULATING_WRITE_KEY);
        Pair<Map<Integer, Calculable>, Integer> resultPair = writeMap.get(nodeDummy);

        Object resultObj = resultPair.getKey().get(resultPair.getValue());
        if (!(resultObj instanceof Valuable)) {
            throw new RuntimeException("The result should be Valuable");
        }
        Valuable resultValue = (Valuable) resultObj;
        Object valueObj = resultValue.eval(context);
        if (!(valueObj instanceof Integer)) {
            throw new RuntimeException("The result should be Integer");
        }
        Integer value = (Integer) valueObj;
        assertEquals(300, (int) value);
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
        nodeOp1.setChild(0, node3);
        nodeOp1.setChild(1, node4);
        nodeOp1.setChild(2, node5);
        nodeOp1.setChild(3, node6);

        // root prepare
        Calculable dummyOperator = ArgGetter.INSTANCE;
        TreeNode<Calculable> nodeDummy = new TreeNode<>(dummyOperator);
        nodeDummy.setChild(0, nodeOp1);

        Contextable context = optimizeFilter.filter(nodeDummy, optimizerList);
        target = new DelegatedWorkerPoolImpl<>(CalculatingWorkableImpl.class, BasicCalculator.class, 10, context);

        Object parallelPruningValueObject = context.get(Optimizable.CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            target.dispatch(treeNodeList);
        }

        Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>> writeMap = (Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>>) context.get(Optimizable.CONTEXT_CACHE_CALCULATING_WRITE_KEY);
        Pair<Map<Integer, Calculable>, Integer> resultPair = writeMap.get(nodeDummy);
        System.out.println(resultPair.getKey().get(resultPair.getValue()));
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
        nodeOp0.setChild(0, node1);
        nodeOp0.setChild(1, nodeOp1);
        nodeOp1.setChild(0, node2);
        nodeOp1.setChild(1, node3);

        // root prepare
        Calculable dummyOperator = ArgGetter.INSTANCE;
        TreeNode<Calculable> nodeDummy = new TreeNode<>(dummyOperator);
        nodeDummy.setChild(0, nodeOp0);

        Contextable context = optimizeFilter.filter(nodeDummy, optimizerList);
        target = new DelegatedWorkerPoolImpl<>(CalculatingWorkableImpl.class, BasicCalculator.class, 2, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            target.dispatch(treeNodeList);
        }

        Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>> writeMap = (Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>>) context.get(Optimizable.CONTEXT_CACHE_CALCULATING_WRITE_KEY);
        Pair<Map<Integer, Calculable>, Integer> resultPair = writeMap.get(nodeDummy);

        Object resultObj = resultPair.getKey().get(resultPair.getValue());
        if (!(resultObj instanceof Valuable)) {
            throw new RuntimeException("The result should be Valuable");
        }
        Valuable resultValue = (Valuable) resultObj;
        Object valueObj = resultValue.eval(context);
        if (!(valueObj instanceof Integer)) {
            throw new RuntimeException("The result should be Integer");
        }
        Integer value = (Integer) valueObj;
        assertEquals(500, (int) value);
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
        nodeOp0.setChild(0, node1);
        nodeOp0.setChild(1, node2);
        nodeOp0.setChild(2, nodeOp1);
        nodeOp1.setChild(0, node3);
        nodeOp1.setChild(1, node4);
        nodeOp1.setChild(2, node5);
        nodeOp1.setChild(3, node6);

        // root prepare
        Calculable dummyOperator = ArgGetter.INSTANCE;
        TreeNode<Calculable> nodeDummy = new TreeNode<>(dummyOperator);
        nodeDummy.setChild(0, nodeOp0);

        Contextable context = optimizeFilter.filter(nodeDummy, optimizerList);
        target = new DelegatedWorkerPoolImpl<>(CalculatingWorkableImpl.class, StageableCalculator.class, 5, context);

        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY);
        if (!(parallelPruningValueObject instanceof List)) {
            throw new RuntimeException("The parallel pruned should be list");
        }
        List<List<TreeNode<Calculable>>> parallelPruningValueList = (List<List<TreeNode<Calculable>>>) parallelPruningValueObject;
        for (List<TreeNode<Calculable>> treeNodeList : parallelPruningValueList) {
            target.dispatch(treeNodeList);
        }

        Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>> writeMap = (Map<TreeNode<Calculable>, Pair<Map<Integer, Calculable>, Integer>>) context.get(Optimizable.CONTEXT_CACHE_CALCULATING_WRITE_KEY);
        Pair<Map<Integer, Calculable>, Integer> resultPair = writeMap.get(nodeDummy);
        System.out.println(resultPair.getKey().get(resultPair.getValue()));
    }
}