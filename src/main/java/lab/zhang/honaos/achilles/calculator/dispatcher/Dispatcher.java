package lab.zhang.honaos.achilles.calculator.dispatcher;

import io.github.daichim.jach.channel.UnbufferedChannel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dispatcher<T, R> {
    private UnbufferedChannel<T> jobChannel;
    private UnbufferedChannel<R> resultChannel;
}
