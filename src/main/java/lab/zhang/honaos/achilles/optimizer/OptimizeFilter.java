package lab.zhang.honaos.achilles.optimizer;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContextableImpl;

import java.util.List;

public class OptimizeFilter<V> {

    public Contextable filter(TreeNode<V> root, List<Optimizable<V>> optimizerList) {
        Contextable context = new ConcurrentHashMapContextableImpl();

        if (optimizerList == null || optimizerList.size() == 0) {
            return context;
        }

        for (Optimizable<V> optimizer : optimizerList) {
            optimizer.optimize(root, context);
        }

        return context;
    }
}
