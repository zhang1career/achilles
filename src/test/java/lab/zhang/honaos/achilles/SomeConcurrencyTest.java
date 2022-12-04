package lab.zhang.honaos.achilles;

import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;
import org.junit.*;

public class SomeConcurrencyTest {
    @Rule
    public ConcurrentRule concurrentRule = new ConcurrentRule();
    @Rule
    public RepeatingRule repeatingRule = new RepeatingRule();

    @AfterClass
    public static void annotated_test_run_thread10_time10() {
    }

    @Test
    @Concurrent(count = 10)
    @Repeating(repetition = 1000)
    public void test_run_thread10_time10() {
    }
}
