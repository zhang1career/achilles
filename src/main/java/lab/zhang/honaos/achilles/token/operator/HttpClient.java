package lab.zhang.honaos.achilles.token.operator;

import cn.hutool.http.HttpUtil;
import lab.zhang.honaos.achilles.context.Contextable;
import lab.zhang.honaos.achilles.token.Calculable;
import lab.zhang.honaos.achilles.token.operand.instant.InstantMap;
import lab.zhang.honaos.achilles.token.operand.instant.InstantString;

import java.util.List;
import java.util.Map;

public enum HttpClient implements Calculable {
    /**
     * singleton
     */
    INSTANCE;

    @Override
    public Calculable calc(List<Calculable> paramList, Contextable context) {
        MethodEnum method = getMethod(paramList, context);
        ProtocolEnum protocol = getProtocol(paramList, context);
        String url = getUrl(paramList, context);
        Map<String, Object> paramMap = getParamMap(paramList, context);
//        Map<String, Object> headMap = getHeadMap(paramList, context);
//        Map<String, Object> bodyMap = getBodyMap(paramList, context);

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

    private static MethodEnum getMethod(List<Calculable> paramList, Contextable context) {
        Calculable methodCalculable = paramList.get(0);
        if (!(methodCalculable instanceof InstantString)) {
            throw new IllegalArgumentException("The method should be an InstantString.");
        }
        InstantString methodOperand = (InstantString) methodCalculable;
        String methodStr = methodOperand.eval(context);
        return MethodEnum.safeValueOfName(methodStr);
    }

    private static ProtocolEnum getProtocol(List<Calculable> paramList, Contextable context) {
        Calculable protocolCalculable = paramList.get(1);
        if (!(protocolCalculable instanceof InstantString)) {
            throw new IllegalArgumentException("The protocol should be an InstantString.");
        }
        InstantString protocolOperand = (InstantString) protocolCalculable;
        String protocolStr = protocolOperand.eval(context);
        return ProtocolEnum.safeValueOfName(protocolStr);
    }

    private static String getUrl(List<Calculable> paramList, Contextable context) {
        Calculable urlCalculable = paramList.get(2);
        if (!(urlCalculable instanceof InstantString)) {
            throw new IllegalArgumentException("The url should be an InstantString.");
        }
        InstantString urlOperand = (InstantString) urlCalculable;
        return urlOperand.eval(context);
    }

    private static Map<String, Object> getParamMap(List<Calculable> paramList, Contextable context) {
        Calculable paramCalculable = paramList.get(3);
        if (!(paramCalculable instanceof InstantMap)) {
            throw new IllegalArgumentException("The param should be an InstantMap.");
        }
        InstantMap paramOperand = (InstantMap) paramCalculable;
        return paramOperand.eval(context);
    }

    private static Map<String, Object> getHeadMap(List<Calculable> paramList, Contextable context) {
        Calculable headCalculable = paramList.get(4);
        if (!(headCalculable instanceof InstantMap)) {
            throw new IllegalArgumentException("The head should be an InstantMap.");
        }
        InstantMap headOperand = (InstantMap) headCalculable;
        return headOperand.eval(context);
    }

    private static Map<String, Object> getBodyMap(List<Calculable> paramList, Contextable context) {
        Calculable bodyCalculable = paramList.get(5);
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
