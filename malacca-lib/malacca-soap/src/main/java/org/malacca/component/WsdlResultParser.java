package org.malacca.component;

import cn.hutool.core.util.StrUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/3/13
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class WsdlResultParser {

    public static String parse(String node, String soap) throws DocumentException {
        String result = "";
        Document doc = DocumentHelper.parseText(soap);
        Element root = doc.getRootElement();
        Element body = root.element("Body");
        List elements = body.elements();
        if (StrUtil.isBlank(node)) {
            for (Element childElement : (List<Element>) elements) {
                if (result != null) {
                    result += childElement.asXML();
                } else {
                    result = childElement.asXML();
                }
            }
        } else {
            for (Object element : elements) {
                Element e = (Element) element;
                result = getResult(node, e);
            }
        }
        return result;
    }

    private static String getResult(String node, Element element) {
        String result = null;
        List elements = element.elements();
        if (element.getName().equals(node)) {
            if (elements.size() > 0) {
                for (Element childElement : (List<Element>) elements) {
                    if (result != null) {
                        result += childElement.asXML();
                    } else {
                        result = childElement.asXML();
                    }
                }
            } else {
                result = element.getText();
            }
            if (result != null && result.startsWith("<![CDATA[")) {
                result = result.substring(9);
                result = result.substring(0, result.length() - 3);
            }
            return result;
        } else {
            for (Object o : elements) {
                Element e = (Element) o;
                result = getResult(node, e);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
