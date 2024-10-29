package com.example.task04;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryHandler implements MessageHandler
{
    private static final int DEFAULT_BUFFER_SIZE = 100;

    private final MessageHandler sink;
    private final int maxBufferedMessages;
    private final ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();
    private final AtomicInteger messageCount = new AtomicInteger(0);
    private final AtomicBoolean isFlushing = new AtomicBoolean(false);
    private final Object lock = new Object();
    private final ExecutorService executor;

    public MemoryHandler(MessageHandler sink, int maxBufferedMessages)
    {
        this.sink = sink;
        this.maxBufferedMessages = maxBufferedMessages;
        executor = Executors.newSingleThreadExecutor();
    }

    public MemoryHandler(MessageHandler sink)
    {
        this(sink, DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void handle(String message)
    {
        messages.add(message);

        int currentMessageCount = messageCount.incrementAndGet();
        if(currentMessageCount >= maxBufferedMessages
                && isFlushing.compareAndSet(false, true))
        {
            executor.execute(this::flush); // possible bottleneck? it's only one thread
        }
    }

    public void flush()
    {
        synchronized (lock)
        {
            String message;
            while ((message = messages.poll()) != null)
            {
                sink.handle(message);
                messageCount.decrementAndGet();
            }
            isFlushing.set(false);
        }
    }

    public void finish()
    {
        executor.shutdownNow();
    }
}