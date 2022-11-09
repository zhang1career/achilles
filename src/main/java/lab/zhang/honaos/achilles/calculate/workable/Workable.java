package lab.zhang.honaos.achilles.calculate.workable;

import lab.zhang.honaos.achilles.context.Contextable;

public interface Workable<T, R> {
    R work(T job, Contextable context);
}
