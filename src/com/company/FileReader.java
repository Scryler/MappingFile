package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class FileReader {
    private long position = 0;
    private long lastPosition = 0;
    private File path;

    public FileReader(String path) {
        this.path = new File(path);
    }

    public String readText(boolean flag) throws IOException {
        int mapsize = 520 * 500;
        byte[] text;
        try (FileChannel channel = FileChannel.open(path.toPath(), StandardOpenOption.READ)) {
            final long channelSize = channel.size();

            if (flag) position -= lastPosition;//влево
            if (flag && position != 0) position -= mapsize;//вправо

            long remaining = channelSize - position;//движение по файлу
            long limit = Math.min(remaining, mapsize);
            if (limit != 0) lastPosition = limit;
            text = new byte[(int)limit];
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, remaining);
            position += limit;

            for (int i = 0; i < limit; i++) {
                final byte b = buffer.get(i);
                text[i] = b;
            }
            return new String(text, "cp1251");
        }
    }

}
