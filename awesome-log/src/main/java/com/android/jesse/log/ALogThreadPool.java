package com.android.jesse.log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class ALogThreadPool {

    private final static ExecutorService sGlobalFixedThreadPool = Executors.newFixedThreadPool(1, new DefaultThreadFactory());
    public static Executor getFixedThreadPool() {
        return sGlobalFixedThreadPool;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "Alog-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable var1) {
            Thread t = new Thread(this.group, var1, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
