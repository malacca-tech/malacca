package org.malacca.engine.core

import org.malacca.engine.utils.ResourceUtils
import org.malacca.service.AbstractServiceProvider
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.FileFilter

class FileProvider : AbstractServiceProvider() {

    var packageName = "flow";

    override fun init() {
        var classPathResource = ClassPathResource(packageName);
        var files = classPathResource.file.listFiles(FileFilter { pathname: File -> pathname.name.toLowerCase().endsWith(".yml") })
        for (file in files) {
            var fileContent = ResourceUtils.getFileContent("flows/" + file.name)
            serviceManager.loadService(fileContent)
        }
    }
}