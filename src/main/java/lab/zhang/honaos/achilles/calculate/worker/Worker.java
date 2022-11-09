package lab.zhang.honaos.achilles.calculate.worker;

import io.github.daichim.jach.channel.UnbufferedChannel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Worker<T, R> {
    private UnbufferedChannel<T> jobChannel;
    private UnbufferedChannel<R> resultChannel;
}
