package com.example.task04;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

public class Logger
{
    private static final ConcurrentHashMap<String, Logger> LOGGERS = new ConcurrentHashMap<>();
    private static final LogsLevel DEFAULT_LOG_LEVEL = LogsLevel.ERROR;
    private final String name;
    private LogsLevel logsLevel;

    public Logger(String name)
    {
        this(name, DEFAULT_LOG_LEVEL);
    }

    public Logger(String name, LogsLevel logsLevel)
    {
        this.name = name;
        this.logsLevel = logsLevel;
    }

    public static Logger getLogger(String name)
    {
        Logger logger = LOGGERS.getOrDefault(name, null);

        if(logger == null)
        {
            logger = new Logger(name);
            LOGGERS.put(name, logger);
        }

        return logger;
    }

    public void debug(String message)
    {
        log(LogsLevel.DEBUG, message);
    }

    public void debug(String message, Object... args)
    {
        log(LogsLevel.DEBUG, message, args);
    }

    public void info(String message)
    {
        log(LogsLevel.INFO, message);
    }

    public void info(String message, Object... args)
    {
        log(LogsLevel.INFO, message, args);
    }

    public void warning(String message)
    {
        log(LogsLevel.WARNING, message);
    }

    public void warning(String message, Object... args)
    {
        log(LogsLevel.WARNING, message, args);
    }

    public void error(String message)
    {
        log(LogsLevel.ERROR, message);
    }

    public void error(String message, Object... args)
    {
        log(LogsLevel.ERROR, message, args);
    }

    public void log(LogsLevel logsLevel, String message)
    {
        log(logsLevel, message, new Object[0]);
    }

    public void log(LogsLevel logsLevel, String template, Object... args)
    {
        if(logsLevel.ordinal() < this.logsLevel.ordinal())
        {
            return;
        }

        String formattedTemplate = String.format(template, args);
        String dateTimePart = formatDateNow("yyyy.mm.dd hh:mm:ss");
        String finalMessage = String.format("[%s] %s %s - %s", logsLevel, dateTimePart, this.name, formattedTemplate);
        System.out.println(finalMessage);
    }

    private String formatDateNow(String pattern)
    {
        LocalDateTime now = LocalDateTime.now();
        return formatDate(now, pattern);
    }

    private String formatDate(LocalDateTime date, String pattern)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    public LogsLevel getLevel()
    {
        return logsLevel;
    }

    public void setLevel(LogsLevel logsLevel)
    {
        this.logsLevel = logsLevel;
    }

    public String getName()
    {
        return this.name;
    }
}