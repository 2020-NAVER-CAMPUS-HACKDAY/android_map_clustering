package com.naver.hackday2020

import android.util.JsonReader
import java.io.IOException

interface Readable<T> {

    @Throws(IOException::class)
    fun readItem(reader: JsonReader): T
}