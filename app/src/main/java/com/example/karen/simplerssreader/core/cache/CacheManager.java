package com.example.karen.simplerssreader.core.cache;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Karen on 14.08.2014.
 */
public class CacheManager {
    private static final long MAX_SIZE = 52428800L; // 50MB

    private File cacheDir;

    public CacheManager(Context context) {
        this.cacheDir = context.getCacheDir();
    }

    public void cacheData(byte[] data, String name) throws IOException {
        long size = getCacheSize();
        long newSize = data.length + size;

        if (newSize > MAX_SIZE) {
            cleanCache(newSize - MAX_SIZE);
        }

        File file = new File(cacheDir, name);
        FileOutputStream os = new FileOutputStream(file);
        try {
            os.write(data);
        } finally {
            os.flush();
            os.close();
        }
    }

    public byte[] retrieveData(String name) throws IOException {
        File file = new File(cacheDir, name);

        if (!file.exists()) {
            // Data doesn't exist
            return null;
        }

        byte[] data = new byte[(int) file.length()];
        FileInputStream is = new FileInputStream(file);
        try {
            is.read(data);
        } finally {
            is.close();
        }

        return data;
    }

    private void cleanCache(long bytes) {
        long bytesDeleted = 0;
        File[] files = cacheDir.listFiles();

        for (File file : files) {
            bytesDeleted += file.length();
            file.delete();

            if (bytesDeleted >= bytes) {
                break;
            }
        }
    }

    private long getCacheSize() {
        long size = 0;
        File[] files = cacheDir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }
        }

        return size;
    }
}
