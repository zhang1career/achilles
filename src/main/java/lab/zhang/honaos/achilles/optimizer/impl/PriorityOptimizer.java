package lab.zhang.honaos.achilles.optimizer.impl;

import lab.zhang.honaos.achilles.optimizer.Optimizable;
import lombok.Getter;

public abstract class PriorityOptimizer<V> implements Optimizable<V>, Comparable<PriorityOptimizer<?>> {

    @Getter
    protected int priorityValue = -1;

    @Override
    public int compareTo(PriorityOptimizer that) {
        if (priorityValue == that.getPriorityValue()) {
            return 0;
        }
        return priorityValue < that.getPriorityValue() ? -1 : 1;
    }
}
