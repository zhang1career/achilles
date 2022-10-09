package lab.zhang.honaos.achilles.token;

import lab.zhang.honaos.achilles.context.Contextable;

import java.util.List;

public interface Valuable {
    Valuable evaluate(List<Valuable> paramList, Contextable context);
}
