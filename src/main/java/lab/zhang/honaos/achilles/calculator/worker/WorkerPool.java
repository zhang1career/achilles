package lab.zhang.honaos.achilles.calculator.worker;

import io.github.daichim.jach.channel.Channel;
import io.github.daichim.jach.channel.UnbufferedChannel;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.calculator.skill.Skilled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.daichim.jach.JachChannels.go;
import static io.github.daichim.jach.JachChannels.make;

public class WorkerPool<T, R> {

    private static final int TIMEOUT_GET_BLOCKED_RESULT = 100;

    private final Channel<Worker> readyWorkerChannel;

    public WorkerPool(Class<? extends Skilled<T, R>> skillClazz, int poolSize, Contextable context) throws IllegalAccessException, InstantiationException {
        this.readyWorkerChannel = make(Worker.class, poolSize);

        for (int i = 0; i < poolSize; i++) {
            Skilled<T, R> skill = skillClazz.newInstance();
            UnbufferedChannel<T> jobChannel = (UnbufferedChannel<T>) (make(Object.class));
            UnbufferedChannel<R> resultChannel = (UnbufferedChannel<R>) (make(Object.class));
            Worker<T, R> worker = new Worker<>(jobChannel, resultChannel);

            go(() -> jobChannel.forEach(job -> {
                R result = skill.work(job, context);
                resultChannel.write(result);
                readyWorkerChannel.shallowWrite(worker);
            }));

            readyWorkerChannel.shallowWrite(worker);
        }
    }

    public UnbufferedChannel<R> layoutJob(T job) {
        Worker<T, R> worker = readyWorkerChannel.read();
        worker.getJobChannel().write(job);
        return worker.getResultChannel();
    }

    public List<R> layoutBlockedJobs(Collection<T> jobs) {
        List<UnbufferedChannel<R>> resultChannelList = new ArrayList<>();
        jobs.forEach(job -> resultChannelList.add(layoutJob(job)));

        List<R> resultList = new ArrayList<>();
        resultChannelList.forEach(resultChannel -> {
            resultList.add(resultChannel.read(TIMEOUT_GET_BLOCKED_RESULT, TimeUnit.MILLISECONDS));
        });
        return resultList;
    }
}
