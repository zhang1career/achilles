package lab.zhang.honaos.achilles.optimizer;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContext;
import lab.zhang.honaos.achilles.optimizer.impl.PriorityOptimizer;

import java.util.Collections;
import java.util.List;

public class OptimizeFilter<V> {

    public Contextable filter(TreeNode<V> root, List<PriorityOptimizer<V>> optimizerList) {
        Contextable context = new ConcurrentHashMapContext();

        if (optimizerList == null || optimizerList.size() == 0) {
            return context;
        }

        prioritize(optimizerList);

        for (Optimizable<V> optimizer : optimizerList) {
            optimizer.optimize(root, context);
        }

        return context;
    }

    private void prioritize(List<PriorityOptimizer<V>> optimizerList) {
        Collections.sort(optimizerList);
        Collections.reverse(optimizerList);
    }
}
