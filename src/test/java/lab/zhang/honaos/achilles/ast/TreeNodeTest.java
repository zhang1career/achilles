package lab.zhang.honaos.achilles.ast;

import lab.zhang.honaos.achilles.token.operand.instant.InstantInteger;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeNodeTest {
    @Test
    public void test_equals() throws Exception {
        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node2 = new TreeNode<>(1);
        assertFalse(node1.equals(node2));

        InstantInteger instantInteger = new InstantInteger(11);
        TreeNode<InstantInteger> node11 = new TreeNode<>(instantInteger);
        TreeNode<InstantInteger> node12 = new TreeNode<>(instantInteger);
        assertFalse(node11.equals(node12));

    }
}