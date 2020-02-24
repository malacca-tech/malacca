package org.malacca.service;

import java.io.InputStream;
import java.util.Scanner;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/2/24
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class FileServiceProvider extends AbstractServiceProvider {

    private String path;

    @Override
    protected String findYml() {
        InputStream inputStream = FileServiceProvider.class.getClassLoader().getResourceAsStream(path);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String serviceYml = scanner.hasNext() ? scanner.next() : "";
        return serviceYml;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
