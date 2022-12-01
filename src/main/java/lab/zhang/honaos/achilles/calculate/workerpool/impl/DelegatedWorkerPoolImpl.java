package lab.zhang.honaos.achilles.calculate.workerpool.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import lab.zhang.honaos.achilles.calculate.calculator.Calculator;
import lab.zhang.honaos.achilles.calculate.workable.Workable;
import lab.zhang.honaos.achilles.calculate.workerpool.WorkerPool;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.exception.TimeoutException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;

public class DelegatedWorkerPoolImpl<T, R> implements WorkerPool<T, R> {

    private static final int WAIT_TIMEOUT = 1;

    private final ExecutorService workerThreadPool;

    private final Constructor<? extends Workable<T, R>> workableConstructor;

    private final Calculator calculator;

    private final Contextable context;

    public DelegatedWorkerPoolImpl(Class<? extends Workable<T, R>> workableClazz, Class<? extends Calculator> calculatorClazz, int poolSize, Contextable context) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.workerThreadPool = new ThreadPoolExecutor(
                poolSize, poolSize,
                1000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("ach-calc-worker-pool", false));

        this.workableConstructor = workableClazz.getDeclaredConstructor(Calculator.class);
        this.calculator = calculatorClazz.getDeclaredConstructor().newInstance();

        this.context = context;
    }

    @Override
    public List<R> dispatch(List<T> jobs) {
        CountDownLatch countDownLatch = new CountDownLatch(jobs.size());
        for (int i = 0; i < jobs.size(); i++) {
            int finalI = i;
            workerThreadPool.submit(() -> {
                T job = jobs.get(finalI);

                Workable<T, R> workable = null;
                try {
                    workable = workableConstructor.newInstance(calculator);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
                R result = workable.work(job, context);
            });
        }

        boolean await;
        try {
            await = countDownLatch.await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
            if (!await) {
                throw new TimeoutException(String.format("The countDownLatch is not finished in %d milli-seconds", WAIT_TIMEOUT));
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("The countDownLatch is interrupted", ex);
        }

        return null;
    }
}
