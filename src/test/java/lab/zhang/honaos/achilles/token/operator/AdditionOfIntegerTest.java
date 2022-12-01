package lab.zhang.honaos.achilles.token.operator;

import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContext;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantInteger;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdditionOfIntegerTest {

    private AdditionOfInteger target;

    private Contextable context;

    private Calculable op1;
    private Calculable op2;


    @Before
    public void setUp() {
        target = AdditionOfInteger.INSTANCE;
        context = new ConcurrentHashMapContext();
        op1 = new InstantInteger(1);
        op2 = new InstantInteger(2);
    }


    @Test
    public void test_additionOfInteger() throws Exception {
        ConcurrentHashMap<Integer, Calculable> argMap = new ConcurrentHashMap<>();
        argMap.put(0, op1);
        argMap.put(1, op2);
        Calculable result = target.calc(argMap, context);
        assertTrue(result instanceof InstantInteger);
        assertEquals(3, ((InstantInteger) result).eval(context).intValue());
    }

    @Test
    public void test_hashcode_equal() throws Exception {
        AdditionOfInteger compare = AdditionOfInteger.INSTANCE;
        assertEquals(target, target);
        assertEquals(compare, target);

        assertEquals(target.hashCode(), target.hashCode());
        assertEquals(compare.hashCode(), target.hashCode());
    }
}