package com.naver.hackday2020

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssetFileReaderTest {
    private lateinit var assetFileReader: AssetFileReader

    @Before
    fun setUp() {
        assetFileReader = AssetFileReader(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testExtractContent() {
        val testContent = assetFileReader.extractContent("sample.txt")
        assertEquals(testContent, "안녕\nhello\n123")
    }
}