package lab.zhang.honaos.achilles.optimizer;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;

public interface Optimizable<V> {
    void optimize(TreeNode<V> root, Contextable context);
}