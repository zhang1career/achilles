package lab.zhang.honaos.achilles.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lab.zhang.honaos.achilles.ast.TreeNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ParseService {

    public TreeNode parse(String cond) {
        if (cond == null || cond.length() == 0) {
            return null;
        }

        TreeNode token = JSON.parseObject(cond, TreeNode.class);
        // check name
        if (token.getName() == null || "".equals(token.getName())) {
            throw new RuntimeException("The name is missing or empty");
        }
        // check value
        if (token.getValue() == null) {
            return token;
        }
        if (!(token.getValue() instanceof JSONArray)) {
            return token;
        }

        Map<Integer, TreeNode> childTokenMap = new ConcurrentHashMap<>();
        JSONArray childrenCond = (JSONArray) token.getValue();
        for (int i = 0; i < childrenCond.size(); i++) {
            Object childCond = childrenCond.get(i);
            if (childCond == null) {
                continue;
            }
            String condStr = childCond.toString();
            if (condStr.isEmpty()) {
                continue;
            }
            childTokenMap.put(i, parse(condStr));
        }
        token.setChildren(childTokenMap);

        return token;
    }
}
