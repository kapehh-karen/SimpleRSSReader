package com.nlt.karen.simplerssreader.core.cache;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Karen on 14.08.2014.
 */
public class CacheManager {
    private long maxCacheSize;
    private File cacheDir;

    public CacheManager(Context context, String subDir) {
        this(context, subDir, 52428800L); // Max dir size 50MB
    }

    public CacheManager(Context context, String subDir, long maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        this.cacheDir = new File(context.getCacheDir(), subDir);
		if (!this.cacheDir.exists()) {
			this.cacheDir.mkdirs();
		}
    }

    public File getFile(String fileName) {
        return new File(cacheDir, fileName);
    }

    public void cacheData(byte[] data, String name) throws IOException {
        long size = getCacheSize();
        long newSize = data.length + size;

        if (newSize > maxCacheSize) {
            cleanCache(newSize - maxCacheSize);
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

    public OutputStream cacheDataStream(String name) throws FileNotFoundException {
        long size = getCacheSize();

        if (size > maxCacheSize) {
            cleanCache(size - maxCacheSize);
        }

        File file = new File(cacheDir, name);
        return new FileOutputStream(file);
    }

    public byte[] retrieveData(String name) throws IOException {
        File file = new File(cacheDir, name);

        if (!file.exists()) {
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

    public InputStream retrieveDataStream(String name) throws FileNotFoundException {
        File file = new File(cacheDir, name);

        if (!file.exists()) {
            return null;
        }

        return new FileInputStream(file);
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
