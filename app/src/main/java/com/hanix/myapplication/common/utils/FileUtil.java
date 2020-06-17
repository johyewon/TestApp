package com.hanix.myapplication.common.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtil {


    public static void saveTextToFile(String fileFullPath, String msg, boolean isAppend) {
        try {
            File file = new File(fileFullPath);
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), isAppend);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.newLine();
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StringBuffer loadTextFile(String fileFullPath) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileFullPath));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {br.close();}catch (Exception ee){}
            }
        }
        return sb;
    }

    public static boolean delFile(String fileFullPath) {
        try {
            File existfile = new File(fileFullPath);
            if(existfile.exists()) {
                return existfile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static boolean isCacheFileExist(Context context, String fileName) {
        try {
            File existfile = new File(context.getCacheDir(), fileName);
            return existfile.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void delteCacheFile(Context context, String fileName) {
        try {
            File file = File.createTempFile(fileName, null, context.getCacheDir());
            file.delete();
        } catch (Exception e) { e.printStackTrace();}
    }

    public static void deleteAllCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}
