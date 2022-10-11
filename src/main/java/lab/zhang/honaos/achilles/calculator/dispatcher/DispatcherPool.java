package lab.zhang.honaos.achilles.calculator.dispatcher;

import io.github.daichim.jach.channel.Channel;
import io.github.daichim.jach.channel.UnbufferedChannel;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.calculator.worker.Workable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.daichim.jach.JachChannels.go;
import static io.github.daichim.jach.JachChannels.make;

public class DispatcherPool<T, R> {

    private final Channel<Dispatcher> readyDispatcherChannel;

    public DispatcherPool(Class<? extends Workable<T, R>> workerClazz, int poolSize, Contextable context) throws IllegalAccessException, InstantiationException {
        this.readyDispatcherChannel = make(Dispatcher.class, poolSize);

        for (int i = 0; i < poolSize; i++) {
            Workable<T, R> worker = workerClazz.newInstance();
            UnbufferedChannel<T> jobChannel = (UnbufferedChannel<T>) (make(Object.class));
            UnbufferedChannel<R> resultChannel = (UnbufferedChannel<R>) (make(Object.class));
            Dispatcher<T, R> dispatcher = new Dispatcher<>(jobChannel, resultChannel);

            go(() -> jobChannel.forEach(job -> {
                R result = worker.work(job, context);
                resultChannel.write(result);
                readyDispatcherChannel.write(dispatcher);
            }));

            readyDispatcherChannel.write(dispatcher);
        }
    }

    public UnbufferedChannel<R> layoutJob(T job) {
        Dispatcher<T, R> dispatcher = readyDispatcherChannel.read();
        dispatcher.getJobChannel().write(job);
        return dispatcher.getResultChannel();
    }

    public List<R> blockedLayoutJobs(Collection<T> jobs) {
        List<UnbufferedChannel<R>> resultChannelList = new ArrayList<>();
        jobs.forEach(job -> resultChannelList.add(layoutJob(job)));

        List<R> resultList = new ArrayList<>();
        resultChannelList.forEach(resultChannel -> resultList.add(resultChannel.read(100, TimeUnit.MILLISECONDS)));
        return resultList;
    }
}
