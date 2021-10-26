package org.malacca.engine.utils

import org.apache.commons.io.IOUtils
import org.malacca.utils.CloseableUtils
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.io.InputStream

object ResourceUtils {

    fun getFileContent(fileName: String?): String? {
        val templateResource = ClassPathResource(fileName!!)
        var inputStream: InputStream? = null
        val context: String
        try {
            inputStream = templateResource.inputStream
            context = IOUtils.toString(inputStream, "UTF-8")
        } catch (var8: IOException) {
            throw RuntimeException("IO异常", var8)
        } finally {
            CloseableUtils.close(inputStream)
        }
        return context
    }
}