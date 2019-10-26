package com.zanfolin.pokedex.base.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.RuntimeException

object FileUtils {

    fun readJsonForEndpoint(url: String): String {
        return readJson("$url.json")
    }

    fun readJson(path: String): String {
        val url = FileUtils::class.java.getClassLoader().getResource(path)

        var inputStream: InputStream? = null
        try {
            inputStream = url.openStream()
            val reader = BufferedReader(InputStreamReader(inputStream!!))
            var file = ""
            while (reader.ready()) {
                file += reader.readLine()
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        throw JsonParsingException()
    }

}

class JsonParsingException : RuntimeException("Error during JSON file parsing")
