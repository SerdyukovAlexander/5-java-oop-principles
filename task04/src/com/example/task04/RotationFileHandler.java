package com.example.task04;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RotationFileHandler implements MessageHandler
{
    private final String folder;
    private FileWriter fileWriter;
    private final Duration rotationInterval;
    private LocalDateTime lastRotation;

    public RotationFileHandler(String folder, Duration rotationInterval)
    {
        this.folder = folder;
        this.rotationInterval = rotationInterval;

        rotate();
    }

    @Override
    public void handle(String message)
    {
        if(shouldRotate())
        {
            rotate();
        }

        try
        {
            fileWriter.write(message);
            fileWriter.write("\n");
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean shouldRotate()
    {
        LocalDateTime lastRotationEnd = lastRotation.plus(rotationInterval);
        return lastRotationEnd.isBefore(LocalDateTime.now());
    }

    private void rotate()
    {
        try
        {
            if(fileWriter != null)
            {
                fileWriter.close();
            }

            File file = createNewLogFile();
            fileWriter = new FileWriter(file,true);

            lastRotation = LocalDateTime.now();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private File createNewLogFile() throws IOException
    {
        String filename = getFileNameNow();
        File file = Paths.get(folder, filename).toFile();
        file.createNewFile();
        return file;
    }

    private String getFileNameNow()
    {
        DateTimeFormatter datePartFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timePartFormatter = DateTimeFormatter.ofPattern("hh.mm.ss");

        LocalDateTime now = LocalDateTime.now();
        String datePart = datePartFormatter.format(now);
        String timePart = timePartFormatter.format(now);

        return String.format("%sT%s.log", datePart, timePart);
    }

    public void finish()
    {
        try
        {
            if(fileWriter != null)
            {
                fileWriter.close();
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}