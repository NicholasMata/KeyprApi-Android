package com.matadesigns.keyprapi;

import android.os.Handler;
import android.os.Message;

import java.net.HttpURLConnection;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

public class RequestQueue extends Thread {

    private PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<>();

    public RequestQueue(final String threadName) {
        setName(threadName);
        start();
    }

    public void add(Runnable runnable) {
        queue.put(runnable);
    }

    @Override
    public void run() {
        if (queue.peek() != null) {
            try {
                queue.take().run();
            } catch (InterruptedException ignored) {

            }
        }
    }
}
