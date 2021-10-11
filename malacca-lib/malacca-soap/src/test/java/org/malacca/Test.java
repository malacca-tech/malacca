package org.malacca;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/8/8
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class Test {
    public static void main(String args[]) throws IOException, DocumentException {
        String s = FileUtils.readFileToString(new File("/Users/chensheng/Desktop/wtf.xml"));
        Document document = DocumentHelper.parseText(s);
        System.out.println(document.getRootElement());
    }
}
