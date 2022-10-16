package lab.zhang.honaos.achilles.token.operator;

import com.sun.tools.javac.util.List;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContext;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.InstantBigDecimal;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AdditionOfBigDecimalTest {

    private AdditionOfBigDecimal target;

    private Contextable context;

    private Calculable op1;
    private Calculable op2;


    @Before
    public void setUp() {
        target = new AdditionOfBigDecimal();
        context = new ConcurrentHashMapContext();
        op1 = new InstantBigDecimal(1.99);
        op2 = new InstantBigDecimal(8.01);
    }


    @Test
    public void test_additionOfInteger() throws Exception {
        Calculable result = target.calc(List.of(op1, op2), context);
        assertTrue(result instanceof InstantBigDecimal);
        assertEquals("10.000", ((InstantBigDecimal) result).eval(context));
    }
}