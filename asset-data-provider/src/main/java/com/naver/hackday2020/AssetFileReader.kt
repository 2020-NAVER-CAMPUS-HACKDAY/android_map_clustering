package com.naver.hackday2020

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class AssetFileReader(context: Context) {
    private val assetManager = context.resources.assets

    companion object {
        private val tag = AssetFileReader::class.java.name
    }

    fun extractContent(
        fileName: String,
        charset: String = StandardCharsets.UTF_8.name()
    ): String {
        val inputStream = extractStreamFromAsset(fileName) ?: return ""
        val bufferedReader = inputStream.bufferedReader(Charset.forName(charset))

        return try {
            bufferedReader.use(BufferedReader::readText)
        } catch (e: Exception) {
            Log.e(tag, "failed to extractAssetContent.")
            e.printStackTrace()
            ""
        } finally {
            inputStream.close()
        }
    }

    private fun extractStreamFromAsset(fileName: String): InputStream? {
        if (fileName.isBlank()) {
            Log.e(tag, "fileName is blank")
            return null
        }

        return try {
            assetManager.open(fileName)
        } catch (e: IOException) {
            Log.e(tag, "failed to load $fileName")
            e.printStackTrace()
            null
        }
    }
}