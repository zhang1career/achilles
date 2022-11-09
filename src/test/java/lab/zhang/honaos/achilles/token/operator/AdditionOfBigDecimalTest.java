package lab.zhang.honaos.achilles.token.operator;

import com.sun.tools.javac.util.List;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContext;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantBigDecimal;
import lab.zhang.zhangtool.util.BigDecimalUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdditionOfBigDecimalTest {

    private AdditionOfBigDecimal target;

    private Contextable context;

    private Calculable op1;
    private Calculable op2;


    @Before
    public void setUp() {
        target = AdditionOfBigDecimal.INSTANCE;
        context = new ConcurrentHashMapContext();
        op1 = new InstantBigDecimal("1.99");
        op2 = new InstantBigDecimal("8.01");
    }


    @Test
    public void test_additionOfInteger() throws Exception {
        Calculable result = target.calc(List.of(op1, op2), context);
        assertTrue(result instanceof InstantBigDecimal);
        assertEquals("10.000", BigDecimalUtil.formatWithThreeDecimals(((InstantBigDecimal) result).eval(context)));
    }

    @Test
    public void test_hashcode_equal() throws Exception {
        AdditionOfBigDecimal compare = AdditionOfBigDecimal.INSTANCE;
        assertEquals(target, target);
        assertEquals(compare, target);

        assertEquals(target.hashCode(), target.hashCode());
        assertEquals(compare.hashCode(), target.hashCode());
    }
}