package lab.zhang.honaos.achilles.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lab.zhang.honaos.achilles.ast.TreeNode;

import java.util.ArrayList;
import java.util.List;


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

        List<TreeNode> childTokenList = new ArrayList<>();
        for (Object childCond : (JSONArray) token.getValue()) {
            if (childCond == null) {
                continue;
            }
            String condStr = childCond.toString();
            if (condStr.isEmpty()) {
                continue;
            }
            childTokenList.add(parse(condStr));
        }
        token.setValue(childTokenList);

        return token;
    }
}
