package com.naver.hackday2020

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssetFileReaderTest {
    private val assetFileReader = AssetFileReader(ApplicationProvider.getApplicationContext())

    @Test
    fun testExtractContent() {
        val testContent = assetFileReader.extractContent("sample.txt")
        assertEquals(testContent, "안녕\nhello\n123")
    }

    @Test
    fun testGetJsonReader() {
        assertNotNull(assetFileReader.getJsonReader("sample.txt"))
    }
}