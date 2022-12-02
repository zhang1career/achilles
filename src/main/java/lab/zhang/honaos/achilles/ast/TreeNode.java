package lab.zhang.honaos.achilles.ast;


import lab.zhang.zhangtool.util.UuidUtil;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangrj
 */
@Data
public class TreeNode<V> {

    private String uuid;

    private String name;

    private V value;

    private Map<Integer, TreeNode<V>> children;


    public TreeNode() {
        this.uuid = UuidUtil.uuid();
    }

    public TreeNode(V value) {
        this();
        this.value = value;
        this.children = new ConcurrentHashMap<>();
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

    public void setChild(int index, TreeNode<V> node) {
        children.put(index, node);
    }
}
