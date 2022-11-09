package lab.zhang.honaos.achilles.ast;


import lab.zhang.zhangtool.util.UuidUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrj
 */
@Data
public class TreeNode<V> {

    private String uuid;

    private String name;

    private V value;

    private List<TreeNode<V>> children;


    public TreeNode() {
        this.uuid = UuidUtil.uuid();
    }

    public TreeNode(V value) {
        this();
        this.value = value;
        this.children = new ArrayList<>();
    }

    public TreeNode(String name, V value) {
        this(value);
        this.name = name;
    }

    public boolean isLeaf() {
        if (children == null) {
            throw new RuntimeException("TreeNode's children should not be null.");
        }

        if (children.isEmpty()) {
            return true;
        }

        return false;
    }

    public void setValue(TreeNode<V> node, int index) {
        enlargeList(children, index);
        children.set(index, node);
    }

    private void enlargeList(List<TreeNode<V>> children, int level) {
        if (children.size() > level) {
            return;
        }

        for (int i = children.size(); i <= level; i++) {
            children.add(null);
        }
    }
}
