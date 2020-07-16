package com.weavernorth.datadeal.toread.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.weaver.com.cn
 *
 * @version V1.0
 * @Title: DealDataUtil
 * @Package com.weavernorth.datadeal.toread.manage.util
 * @Description:数据处理工具类
 * @author: liupeng(1203153229 @ qq.com)
 * @date: 2020/7/7 17:45
 * @Copyright: 2020 www.weaver.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于上海泛微网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public class DealDataUtil {
    public  String null2String(String var0) {
        return var0 == null ? "" : var0;
    }
    public  String getStringValueByMapKey(Map<String, String> var0, String var1) {
        return null2String((String)var0.get(var1));
    }

    public  String getStringValueByMapKey(Map<String, String> var0, String var1, String var2) {
        return null2String((String)var0.get(var1));
    }

    public  int getIntValueByMapKey(Map<String, String> var0, String var1, int var2) {
        return getIntValue((String)var0.get(var1), var2);
    }

    public  int getIntValue(String var0) {
        return getIntValue(var0, -1);
    }

    public  int getIntValue(String var0, int var1) {
        try {
            return Integer.parseInt(var0);
        } catch (Exception var3) {
            return var1;
        }
    }
    public static float getFloatValue(String var0) {
        return getFloatValue(var0, -1.0F);
    }

    public static float getFloatValue(String var0, float var1) {
        try {
            return Float.parseFloat(var0);
        } catch (Exception var3) {
            return var1;
        }
    }

    public  double getDoubleValue(String var0) {
        return getDoubleValue(var0, -1.0D);
    }

    public  double getDoubleValue(String var0, double var1) {
        try {
            return Double.parseDouble(var0);
        } catch (Exception var4) {
            return var1;
        }
    }
    public  Map<String, String> jsonToMap(String reqJsonData) {
        return (Map) JSON.parse(reqJsonData);
    }

    public  Map<String, String> xmlToMap(String reqXmlData) {
        HashMap rspDataHashMap = new HashMap();
        try {
            Document document = DocumentHelper.parseText(reqXmlData);
            if (document == null) {
                return rspDataHashMap;
            }
            Element rootElement = document.getRootElement();
            Iterator elementIterator = rootElement.elementIterator();
            while(elementIterator.hasNext()) {
                Element childElement = (Element)elementIterator.next();
                List childElementList = childElement.elements();
                rspDataHashMap.put(childElement.getName(), childElement.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rspDataHashMap;
    }

    public  String mapToJson(Map<String, String> reqDataMap) {
        return JSONObject.toJSONString(reqDataMap);
    }

    public  String mapToXml(Map<String, String> var0, String var1) {
        StringBuilder var2 = new StringBuilder();
        var2.append("<").append(var1).append(">");
        Iterator var3 = var0.entrySet().iterator();
        while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            var2.append("<").append((String)var4.getKey()).append(">");
            var2.append((String)var4.getValue());
            var2.append("</").append((String)var4.getKey()).append(">");
        }
        var2.append("</").append(var1).append(">");
        return var2.toString();
    }

}
