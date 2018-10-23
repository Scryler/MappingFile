package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static List<File> arrayfiles = new ArrayList<>();

    public static boolean findFiles(File entry, String strSomeText) throws IOException {//маппинг

        long mapSize = 4 * 1024 * 1024;
        final byte[] someTextBytes = strSomeText.getBytes("Cp1251");//переводим текст в байты
        try (FileChannel channel = FileChannel.open(entry.toPath(), StandardOpenOption.READ)) {//переводим текст в найденном файле в байты
            final long channelSize = channel.size();
            long posFile = 0;
            while (posFile < channelSize) {

                long remaining = channelSize - posFile;
                long trymap = mapSize + someTextBytes.length;
                long tomap = Math.min(trymap, remaining);
                long limit = trymap == tomap ? mapSize : (tomap - someTextBytes.length + 1);

                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, posFile, tomap);
                posFile += (trymap == tomap) ? mapSize : tomap;
                for (int i = 0; i < limit; i++) {
                    if (findSubstring(buffer, i, someTextBytes)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private static boolean findSubstring(MappedByteBuffer buffer, int pos, byte[] someTextBytes) throws IOException {
        byte endLine = 13;//13-ый байт - конец строки
        int byteN = 0;
        for (int i = 0; i < someTextBytes.length; ++i) {
            if (buffer.get(pos + i + byteN) == endLine) {//вылавливаем /n
                ++i;
                ++byteN;
            }
            if (i == someTextBytes.length) break;//конец заданного текста
            if (someTextBytes[i] != buffer.get(pos + i + byteN)) {
                return false;
            }
        }
        return true;
    }

    public static List<File> findArrayFiles(String strPath, String strFileType) throws IOException {
        File file = new File(strPath);
        File[] listFiles = file.listFiles(new MyFileNameFilter(strFileType));
            for (File f : listFiles) {
                arrayfiles.add(f);
            }
        return arrayfiles;
    }
}
