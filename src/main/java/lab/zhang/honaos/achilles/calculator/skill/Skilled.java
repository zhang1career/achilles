package lab.zhang.honaos.achilles.calculator.skill;

import lab.zhang.honaos.achilles.context.Contextable;

public interface Skilled<T, R> {
    R work(T job, Contextable context);
}
