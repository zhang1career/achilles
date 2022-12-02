package lab.zhang.honaos.achilles.optimizer;

import lab.zhang.honaos.achilles.ast.TreeNode;
import lab.zhang.honaos.achilles.context.Contextable;

public interface Optimizable<V> {

    String CONTEXT_CACHE_CALCULATING_READ_KEY = "cc_read";

    String CONTEXT_CACHE_CALCULATING_WRITE_KEY = "cc_write";

    String CONTEXT_PARALLEL_PRUNING_OUTPUT_KEY = "para_prune";

    String CONTEXT_REVERSE_GENERATION_OUTPUT_KEY = "rev_gen";

    String CONTEXT_STAGE_ROUTING_OUTPUT_KEY = "stg_route";

    void optimize(TreeNode<V> root, Contextable context);
}
