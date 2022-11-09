package lab.zhang.honaos.achilles.token.operator;

import com.sun.tools.javac.util.List;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.context.impl.ConcurrentHashMapContext;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantMap;
import lab.zhang.honaos.achilles.token.operand.instant.InstantString;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpClientTest {
    private HttpClient target;

    private Contextable context;

    private Calculable operand3;
    private Calculable operand4;
    private Calculable operand5;
    private Calculable operand6;

    @Before
    public void setUp() {
        target = HttpClient.INSTANCE;
        context = new ConcurrentHashMapContext();
        operand3 = new InstantString("GET");
        operand4 = new InstantString("HTTP");
        operand5 = new InstantString("no-url.com");
        // param/head/body
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("value", "3");
        operand6 = new InstantMap(paramMap);
    }


    @Test
    public void test_additionOfInteger() throws Exception {
        String compareStr = "<html>\r\n" +
                "<head>\r\n" +
                "<title>NO-URL</title>\r\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\r\n" +
                "<meta http-equiv=\"Pragma\" content=\"no-cache\">\r\n" +
                "<meta name=\"description\" content=\"NO-URL found!\">\r\n" +
                "<meta name=\"keywords\" content=\"NO-URL\">\r\n" +
                "</head>\r\n" +
                "<html><body bgcolor=\"#000000\"><font face=\"arial, helvetica\" color=\"#EEEEEE\">\r\n" +
                "<b><center>\r\n" +
                "This is the NO-URL Homepage.<br><br>\r\n" +
                "If you are looking for nothing, you have found it.<br><br>\r\n" +
                "</b>\r\n" +
                "</body></html>\r\n\r\n";
        Calculable result = target.calc(List.of(operand3, operand4, operand5, operand6), context);
        assertTrue(result instanceof InstantString);
        assertEquals(compareStr, ((InstantString) result).eval(context));
    }
}