package org.malacca.entry.common;

import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :chensheng 2020/4/3
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class SoapUrlResource extends UrlResource {

    public SoapUrlResource(URI uri) throws MalformedURLException {
        super(uri);
    }

    public SoapUrlResource(URL url) {
        super(url);
    }

    public SoapUrlResource(String path) throws MalformedURLException {
        super(path);
    }

    public SoapUrlResource(String protocol, String location) throws MalformedURLException {
        super(protocol, location);
    }

    public SoapUrlResource(String protocol, String location, String fragment) throws MalformedURLException {
        super(protocol, location, fragment);
    }

    @Override
    public boolean exists() {
        return true;
    }
}
