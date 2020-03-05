package org.malacca.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.malacca.exception.ServiceLoadException;
import org.malacca.exception.constant.SystemExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.net.URL;

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
@Component
public class FileServiceProvider extends AbstractServiceProvider implements InitializingBean {

    public static final Logger LOG = LoggerFactory.getLogger(FileServiceProvider.class);

    @Value("${malacca.class-path}")
    private String path;

    public FileServiceProvider(SpringServiceManager springServiceManager) {
        this.setServiceManager(springServiceManager);
    }

    @Override
    public void init() {
        if (StrUtil.isNullOrUndefined(path)) {
            Assert.state(true, "未配置class-path");
            return;
        }
        LOG.info("开始读取{}路径下的服务配置文件", path);
        URL resource = FileServiceProvider.class.getClassLoader().getResource(path);
        String path = resource.getPath();
        File file = FileUtil.file(path);
        try {
            if (FileUtil.isDirectory(file)) {
                readFile(file.listFiles());
            } else {
                String yml = FileUtil.readString(file, "UTF-8");
                getServiceManager().loadService(yml);
            }
        } catch (Exception e) {
            LOG.error("{}服务配置文件异常", path, e);
            throw new ServiceLoadException(SystemExceptionCode.MALACCA_100007_FILE_READ_ERROR, "读取文件" + path + "失败", e);
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
                String yml = FileUtil.readString(f, "UTF-8");
                getServiceManager().loadService(yml);
            } else if (f.isDirectory()) {
                readFile(f.listFiles());
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
