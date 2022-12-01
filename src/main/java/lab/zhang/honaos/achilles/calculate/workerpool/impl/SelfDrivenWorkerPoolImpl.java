package lab.zhang.honaos.achilles.calculate.workerpool.impl;

import io.github.daichim.jach.channel.Channel;
import io.github.daichim.jach.channel.UnbufferedChannel;
import lab.zhang.honaos.achilles.calculate.calculator.Calculator;
import lab.zhang.honaos.achilles.calculate.workable.Workable;
import lab.zhang.honaos.achilles.calculate.worker.SelfDrivenWorker;
import lab.zhang.honaos.achilles.calculate.workerpool.WorkerPool;
import lab.zhang.honaos.achilles.context.Contextable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static io.github.daichim.jach.JachChannels.go;
import static io.github.daichim.jach.JachChannels.make;

public class SelfDrivenWorkerPoolImpl<T, R> implements WorkerPool<T, R> {

    private static final int TIMEOUT_GET_BLOCKED_RESULT = 100;

    private final Channel<SelfDrivenWorker> readyWorkerChannel;

    public SelfDrivenWorkerPoolImpl(Class<? extends Workable<T, R>> workableClazz, Calculator calculator, int poolSize, Contextable context) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.readyWorkerChannel = make(SelfDrivenWorker.class, poolSize);

        for (int i = 0; i < poolSize; i++) {
            Workable<T, R> workable = workableClazz.getDeclaredConstructor(Calculator.class).newInstance(calculator);
            UnbufferedChannel<T> jobChannel = (UnbufferedChannel<T>) (make(Object.class));
            UnbufferedChannel<R> resultChannel = (UnbufferedChannel<R>) (make(Object.class));
            SelfDrivenWorker<T, R> worker = new SelfDrivenWorker<>(jobChannel, resultChannel);

            go(() -> jobChannel.forEach(job -> {
                R result = workable.work(job, context);
                resultChannel.write(result);
                readyWorkerChannel.shallowWrite(worker);
            }));

            readyWorkerChannel.shallowWrite(worker);
        }
    }

    @Override
    public List<R> dispatch(List<T> jobs) {
        List<UnbufferedChannel<R>> resultChannelList = new ArrayList<>();
        jobs.forEach(job -> resultChannelList.add(doDispatch(job)));

        List<R> resultList = new ArrayList<>();
        resultChannelList.forEach(resultChannel -> {
            resultList.add(resultChannel.read());
        });
        return resultList;
    }

    private UnbufferedChannel<R> doDispatch(T job) {
        SelfDrivenWorker<T, R> selfDrivenWorker = readyWorkerChannel.read();
        selfDrivenWorker.getJobChannel().write(job);
        return selfDrivenWorker.getResultChannel();
    }
}
