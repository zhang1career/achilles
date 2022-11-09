package lab.zhang.honaos.achilles.optimizer;

import javafx.util.Pair;
import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.optimizer.impl.CacheCalculatingOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.ParallelPruningOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.ReverseGenerationOptimizer;
import lab.zhang.honaos.achilles.optimizer.impl.StageRoutingOptimizer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.*;

public class OptimizeFilterTest {

    private OptimizeFilter<Integer> target;

    private List<Optimizable<Integer>> optimizerList;
    private Optimizable<Integer> calculatingCacheOptimizer;
    private Optimizable<Integer> parallelPruningOptimizer;
    private Optimizable<Integer> reverseGenerationOptimizer;
    private Optimizable<Integer> stageRoutingOptimizer;

    private TreeNode<Integer> node0;
    private TreeNode<Integer> node1;
    private TreeNode<Integer> node2;
    private TreeNode<Integer> node3;
    private TreeNode<Integer> node4;
    private TreeNode<Integer> node5;

    @Before
    public void setUp() {
        target = new OptimizeFilter<>();
        // optimizer
        calculatingCacheOptimizer = new CacheCalculatingOptimizer<>();
        parallelPruningOptimizer = new ParallelPruningOptimizer<>();
        reverseGenerationOptimizer = new ReverseGenerationOptimizer<>();
        stageRoutingOptimizer = new StageRoutingOptimizer<>((node, context) -> node.getValue() < 0);
        // optimizer link
        optimizerList = new ArrayList<>();
        optimizerList.add(calculatingCacheOptimizer);
        optimizerList.add(parallelPruningOptimizer);
        optimizerList.add(reverseGenerationOptimizer);
        optimizerList.add(stageRoutingOptimizer);
        // tree node
        node0 = new TreeNode<>(0);
        node1 = new TreeNode<>(-1);
        node2 = new TreeNode<>(2);
        node3 = new TreeNode<>(3);
        node4 = new TreeNode<>(4);
        node5 = new TreeNode<>(-5);
        // tree
        /**
         *      0
         *   /     \
         * (-1)    2
         *   \     /
         *    3  (-5)
         *     \
         *      4
         */
        node0.setValue(node1, 0);
        node0.setValue(node2, 1);
        node1.setValue(node3, 1);
        node3.setValue(node4, 1);
        node2.setValue(node5, 0);
    }

    @Test
    public void test_filter() throws Exception {
        Contextable context = target.filter(node0, optimizerList);

        // calculating cache
        Object cacheReadObject = context.get(CacheCalculatingOptimizer.CONTEXT_READ_KEY);
        if (!(cacheReadObject instanceof HashMap)) {
            throw new RuntimeException("traversal of output should be an instant of HashMap");
        }
        HashMap<TreeNode<Integer>, List<Integer>> readMap = (HashMap<TreeNode<Integer>, List<Integer>>) cacheReadObject;

        Object cacheWriteObject = context.get(CacheCalculatingOptimizer.CONTEXT_WRITE_KEY);
        if (!(cacheWriteObject instanceof HashMap)) {
            throw new RuntimeException("traversal of input should be an instant of HashMap");
        }
        HashMap<TreeNode<Integer>, Pair<List<Integer>, Integer>> writeMap = (HashMap<TreeNode<Integer>, Pair<List<Integer>, Integer>>) cacheWriteObject;

        Pair<List<Integer>, Integer> listIndexPair1 = writeMap.get(node1);
        listIndexPair1.getKey().set(listIndexPair1.getValue(), 100);
        Pair<List<Integer>, Integer> listIndexPair2 = writeMap.get(node2);
        listIndexPair2.getKey().set(listIndexPair2.getValue(), 200);
        List<Integer> readList0 = readMap.get(node0);
        assertTrue(readList0.get(0).equals(100));
        assertTrue(readList0.get(1).equals(200));

        // parallel
        assertNotNull(context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY));
        Object parallelPruningValueObject = context.get(ParallelPruningOptimizer.CONTEXT_OUTPUT_KEY);
        assertTrue(parallelPruningValueObject instanceof List);
        List<List<TreeNode<Integer>>> parallelPruningValueList = (List<List<TreeNode<Integer>>>) parallelPruningValueObject;
        assertEquals(4, parallelPruningValueList.size());
        assertEquals(2, parallelPruningValueList.get(0).size());
        assertTrue(parallelPruningValueList.get(0).contains(node4));
        assertTrue(parallelPruningValueList.get(0).contains(node5));
        assertEquals(2, parallelPruningValueList.get(1).size());
        assertTrue(parallelPruningValueList.get(1).contains(node2));
        assertTrue(parallelPruningValueList.get(1).contains(node3));
        assertEquals(1, parallelPruningValueList.get(2).size());
        assertTrue(parallelPruningValueList.get(2).contains(node1));
        assertEquals(1, parallelPruningValueList.get(3).size());
        assertTrue(parallelPruningValueList.get(3).contains(node0));

        // reverse generation
        assertNotNull(context.get(ReverseGenerationOptimizer.CONTEXT_OUTPUT_KEY));
        Object reverseGenerationObject = context.get(ReverseGenerationOptimizer.CONTEXT_OUTPUT_KEY);
        assertTrue(reverseGenerationObject instanceof ConcurrentHashMap);
        ConcurrentHashMap<TreeNode<Integer>, TreeNode<Integer>> reverseGenerationMap = (ConcurrentHashMap<TreeNode<Integer>, TreeNode<Integer>>) reverseGenerationObject;
        assertEquals(5, reverseGenerationMap.size());
        assertNull(reverseGenerationMap.get(node0));
        assertEquals(reverseGenerationMap.get(node1), node0);
        assertEquals(reverseGenerationMap.get(node2), node0);
        assertEquals(reverseGenerationMap.get(node3), node1);
        assertEquals(reverseGenerationMap.get(node4), node3);
        assertEquals(reverseGenerationMap.get(node5), node2);

        // stage routing
        assertNotNull(context.get(StageRoutingOptimizer.CONTEXT_OUTPUT_KEY));
        Object stageRoutingObject = context.get(StageRoutingOptimizer.CONTEXT_OUTPUT_KEY);
        assertTrue(stageRoutingObject instanceof ConcurrentLinkedQueue);
        ConcurrentLinkedQueue<TreeNode<Integer>> stageRoutingQueue = (ConcurrentLinkedQueue<TreeNode<Integer>>) stageRoutingObject;
        assertEquals(2, stageRoutingQueue.size());
        assertEquals(-1, stageRoutingQueue.poll().getValue().intValue());
        assertEquals(-5, stageRoutingQueue.poll().getValue().intValue());
    }
}