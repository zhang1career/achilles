package lab.zhang.honaos.achilles.token;

import lab.zhang.honaos.achilles.context.Contextable;

import java.util.List;

public interface Calculable {
    Calculable calc(List<Calculable> paramList, Contextable context);
}
