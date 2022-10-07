package lab.zhang.honaos.achilles.compiler.optimizer;

import lab.zhang.honaos.achilles.compiler.ast.TreeNode;
import lab.zhang.honaos.achilles.compiler.context.IContext;
import lab.zhang.honaos.achilles.compiler.context.impl.ConcurrentHashMapContextImpl;

import java.util.List;

public class OptimizeFilter<V> {

    public IContext filter(TreeNode<V> root, List<IOptimize<V>> optimizerList) {
        IContext context = new ConcurrentHashMapContextImpl();

        if (optimizerList == null || optimizerList.size() == 0) {
            return context;
        }

        for (IOptimize<V> optimizer : optimizerList) {
            optimizer.optimize(root, context);
        }

        return context;
    }
}
