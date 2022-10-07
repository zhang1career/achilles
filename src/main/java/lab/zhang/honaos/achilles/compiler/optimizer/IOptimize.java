package lab.zhang.honaos.achilles.compiler.optimizer;

import lab.zhang.honaos.achilles.compiler.ast.TreeNode;
import lab.zhang.honaos.achilles.compiler.context.IContext;

public interface IOptimize<V> {
    void optimize(TreeNode<V> root, IContext context);
}
