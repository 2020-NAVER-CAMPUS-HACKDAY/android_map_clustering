package com.naver.hackday2020

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


object TaskExecutors {
    private val handler = Handler(Looper.getMainLooper())
    private val mainThreadExecutors = Executor { command: Runnable -> handler.post(command) }
    private val workerThreadExecutors = Executors.newSingleThreadExecutor()

    fun runOnMainThread(task: () -> Unit) {
        mainThreadExecutors.execute(task)
    }

    fun runOnWorkerThread(task: () -> Unit) {
        workerThreadExecutors.execute(task)
    }

    fun cancelBackgroundTask() {
        workerThreadExecutors.shutdownNow()
    }
}