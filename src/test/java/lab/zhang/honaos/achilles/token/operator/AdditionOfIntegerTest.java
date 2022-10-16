package lab.zhang.honaos.achilles.token.operator;

import com.sun.tools.javac.util.List;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContext;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.InstantInteger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdditionOfIntegerTest {

    private AdditionOfInteger target;

    private Contextable context;

    private Calculable op1;
    private Calculable op2;


    @Before
    public void setUp() {
        target = new AdditionOfInteger();
        context = new ConcurrentHashMapContext();
        op1 = new InstantInteger(1);
        op2 = new InstantInteger(2);
    }


    @Test
    public void test_additionOfInteger() throws Exception {
        Calculable result = target.calc(List.of(op1, op2), context);
        assertTrue(result instanceof InstantInteger);
        assertEquals(3, ((InstantInteger) result).eval(context).intValue());
    }
}