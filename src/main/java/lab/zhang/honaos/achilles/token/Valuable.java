package lab.zhang.honaos.achilles.token;

import lab.zhang.honaos.achilles.context.Contextable;

public interface Valuable<T> {
    T eval(Contextable context);
}
