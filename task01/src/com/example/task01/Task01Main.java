package com.example.task01;

public class Task01Main
{
    public static void main(String[] args)
    {
        Logger logger = Logger.getLogger("BE BE BE");
        logger.setLevel(LogsLevel.WARNING);
        logger.info("This message should not be printed");
        logger.error("But this one should");
    }
}
