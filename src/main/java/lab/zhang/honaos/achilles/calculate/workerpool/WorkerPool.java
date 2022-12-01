package lab.zhang.honaos.achilles.calculate.workerpool;

import java.util.List;

public interface WorkerPool<T, R> {
    List<R> dispatch(List<T> jobs) throws InterruptedException;
}
