package org.malacca.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
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
    public void init() {
        if (StrUtil.isNullOrUndefined(path)) {
            Assert.state(true,"未配置class-path");
            return;
        }
        URL resource = FileServiceProvider.class.getClassLoader().getResource(path);
        String path = resource.getPath();
        File file = FileUtil.file(path);
        if (FileUtil.isDirectory(file)) {
            readFile(file.listFiles());
        } else {
            String yml = FileUtil.readString(file, "UTF-8");
            getServiceManager().loadService(yml);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public void readFile(File[] files) {
        if (files == null) {// 如果目录为空，直接退出
            return;
        }
        for (File f : files) {
            if (f.isFile()) {
                // TODO: 2020/3/3 判断yml格式
                String yml = FileUtil.readString(f, "UTF-8");
                getServiceManager().loadService(yml);
            } else if (f.isDirectory()) {
                readFile(f.listFiles());
            }
        }
    }

}
