package lab.zhang.honaos.achilles.service;

import lab.zhang.honaos.achilles.ast.TreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ParseServiceTest {

    private ParseService target;

    @Before
    public void setUp() {
        target = new ParseService();
    }

    @Test
    public void test_parse_with_simpleAstCond() {
        String inputCond = "{\"id\":0,\"name\":\"在【赠礼】名单中\",\"type\":10,\"value\":\"isInGiftNamelist\"}";
        TreeNode node = target.parse(inputCond);
        assertNotNull(node);

        assertEquals("在【赠礼】名单中", node.getName());
//        assertEquals(10, token.getType().getId());
        assertEquals("isInGiftNamelist", node.getValue());
    }

    @Test
    public void test_parse_with_combinationAstCond() {
        String inputCond = "{\"id\":0,\"name\":\">\",\"type\":28,\"value\":[{\"id\":0,\"name\":\"年龄\",\"type\":11,\"value\":\"age\"},{\"id\":0,\"name\":\"18\",\"type\":1,\"value\":18}]}";
        TreeNode node = target.parse(inputCond);
        assertNotNull(node);

        assertEquals(">", node.getName());
//        assertEquals(28, node.getType().getId());
    }
}