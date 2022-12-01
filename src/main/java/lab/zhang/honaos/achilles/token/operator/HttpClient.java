package lab.zhang.honaos.achilles.token.operator;

import cn.hutool.http.HttpUtil;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantMap;
import lab.zhang.honaos.achilles.token.operand.instant.InstantString;

import java.util.Map;

public enum HttpClient implements Calculable {
    /**
     * singleton
     */
    INSTANCE;

    @Override
    public Calculable calc(Map<Integer, Calculable> argMap, Contextable context) {
        MethodEnum method = getMethod(argMap, context);
        ProtocolEnum protocol = getProtocol(argMap, context);
        String url = getUrl(argMap, context);
        Map<String, Object> paramMap = getParamMap(argMap, context);
//        Map<String, Object> headMap = getHeadMap(argMap, context);
//        Map<String, Object> bodyMap = getBodyMap(argMap, context);

        String result = "";
        switch (method) {
            case GET:
                result = HttpUtil.get(url, paramMap);
                break;
            case POST:
//                result = HttpUtil.post(url, bodyMap);
                break;
            default:
                throw new IllegalArgumentException("The method is not supported.");
        }

        return new InstantString(result);
    }

    private static MethodEnum getMethod(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable methodCalculable = argMap.get(0);
        if (!(methodCalculable instanceof InstantString)) {
            throw new IllegalArgumentException("The method should be an InstantString.");
        }
        InstantString methodOperand = (InstantString) methodCalculable;
        String methodStr = methodOperand.eval(context);
        return MethodEnum.safeValueOfName(methodStr);
    }

    private static ProtocolEnum getProtocol(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable protocolCalculable = argMap.get(1);
        if (!(protocolCalculable instanceof InstantString)) {
            throw new IllegalArgumentException("The protocol should be an InstantString.");
        }
        InstantString protocolOperand = (InstantString) protocolCalculable;
        String protocolStr = protocolOperand.eval(context);
        return ProtocolEnum.safeValueOfName(protocolStr);
    }

    private static String getUrl(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable urlCalculable = argMap.get(2);
        if (!(urlCalculable instanceof InstantString)) {
            throw new IllegalArgumentException("The url should be an InstantString.");
        }
        InstantString urlOperand = (InstantString) urlCalculable;
        return urlOperand.eval(context);
    }

    private static Map<String, Object> getParamMap(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable paramCalculable = argMap.get(3);
        if (!(paramCalculable instanceof InstantMap)) {
            throw new IllegalArgumentException("The param should be an InstantMap.");
        }
        InstantMap paramOperand = (InstantMap) paramCalculable;
        return paramOperand.eval(context);
    }

    private static Map<String, Object> getHeadMap(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable headCalculable = argMap.get(4);
        if (!(headCalculable instanceof InstantMap)) {
            throw new IllegalArgumentException("The head should be an InstantMap.");
        }
        InstantMap headOperand = (InstantMap) headCalculable;
        return headOperand.eval(context);
    }

    private static Map<String, Object> getBodyMap(Map<Integer, Calculable> argMap, Contextable context) {
        Calculable bodyCalculable = argMap.get(5);
        if (!(bodyCalculable instanceof InstantMap)) {
            throw new IllegalArgumentException("The body should be an InstantMap.");
        }
        InstantMap bodyOperand = (InstantMap) bodyCalculable;
        return bodyOperand.eval(context);
    }

    @Override
    public boolean isStageable() {
        return true;
    }


    public enum MethodEnum {
        UNDEFINED,
        GET,
        POST,
        PUT,
        DELETE,
        PATCH,
        HEAD,
        OPTIONS,
        TRACE,
        ;

        public static MethodEnum safeValueOfName(String name) {
            MethodEnum[] enums = MethodEnum.values();
            for (MethodEnum anEnum : enums) {
                if (anEnum.name().equalsIgnoreCase(name)) {
                    return anEnum;
                }
            }
            return UNDEFINED;
        }
    }

    public enum ProtocolEnum {
        UNDEFINED,
        HTTP,
        HTTPS,
        ;

        public static ProtocolEnum safeValueOfName(String name) {
            ProtocolEnum[] enums = ProtocolEnum.values();
            for (ProtocolEnum anEnum : enums) {
                if (anEnum.name().equalsIgnoreCase(name)) {
                    return anEnum;
                }
            }
            return UNDEFINED;
        }
    }
}
