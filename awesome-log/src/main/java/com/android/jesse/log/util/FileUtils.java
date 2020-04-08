package com.android.jesse.log.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

    /**
     * 获取指定文件夹的大小
     *
     * @param f
     * @return
     */
    public static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();//文件夹目录下的所有文件
        if (flist == null) {//4.2的模拟器空指针。
            return 0;
        }
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {//判断是否父目录下还有子目录
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }

    /**
     * 获取指定文件的大小
     *
     * @return
     */
    public static long getFileSize(File file) {

        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);//使用FileInputStream读入file的数据流
                size = fis.available();//文件的大小
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
        }
        return size;
    }
}
