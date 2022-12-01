package lab.zhang.honaos.achilles.calculate.worker;

import lab.zhang.honaos.achilles.calculate.workable.Workable;
import lab.zhang.honaos.achilles.context.Contextable;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class DelegatedWorker<T, R> implements Callable<R> {

    private final Workable<T, R> workable;

    private final T job;

    private final Contextable context;

    private final CountDownLatch count;


    public DelegatedWorker(Workable<T, R> workable, T job, Contextable context, CountDownLatch count) {
        this.workable = workable;
        this.job = job;
        this.context = context;
        this.count = count;
    }

    @Override
    public R call() {
        R result = this.workable.work(job, context);
        count.countDown();
        return result;
    }
}
