package com.gzp.statisticssdk.utils.file;

import android.content.Context;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * author: Gzp
 * Create on 2018/7/4
 * Description:
 */
public class FileUtils {

    private final static String RESOURCE = "/filePath";
    public final static String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + RESOURCE;
    public final static String FILE_PATH2 = Environment.getDataDirectory().getAbsolutePath() + RESOURCE;
    public final static String FILE_PATH3 = Environment.getExternalStorageState() + RESOURCE;
//    private final static String SD_PATH = createSDCardResourceDir();

    public static File getFileDir(Context context) {
        return privateFilePath(context);
    }

    private static File privateFilePath(Context context) {
        File externalFilesDir = context.getExternalFilesDir(RESOURCE);
        return externalFilesDir == null ? new File(Environment.getExternalStorageDirectory().getAbsolutePath(), RESOURCE) : externalFilesDir;
    }

    /**
     * 创建一个文件
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {

        return false;
    }


    /**
     * 写入文件
     *
     * @param isAdd 是否追加内容
     * @throws FileNotFoundException
     */
    public static void outputStreamFile(String content,File filePath, boolean isAdd) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(filePath,isAdd)));
        dataOutputStream.writeBoolean(false);
        dataOutputStream.writeShort(content.getBytes().length);
        dataOutputStream.write(content.getBytes());
        dataOutputStream.writeInt(4);
        dataOutputStream.flush();
        dataOutputStream.close();

    }


    /**
     * 读写文件
     */
    public static void inputStreamFile(String filePath) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new GZIPInputStream(new FileInputStream(filePath)));
        System.out.println(dataInputStream.readBoolean());
        int len = dataInputStream.readUnsignedShort();
        System.out.println("byte length: " + len);
        byte[] b = new byte[len];
        dataInputStream.readFully(b);
        System.out.println(new String(b));
        System.out.println(dataInputStream.readInt());
    }


    /**
     * 创建SD卡目录
     *
     * @param context
     * @return
     */
    public static String createSDCardResourceDir(Context context) {
        String SDPath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
//            File sdcardDir = privateFilePath(context);
            File sdcardDir = new File(FILE_PATH);
            SDPath = sdcardDir.getPath();
            File path1 = new File(SDPath);
            if (!path1.exists()) {
                path1.mkdirs();
            }
            return SDPath;
        } else {
            return "sd卡不存在！";
        }
    }



//    public static void main(String[] args) {
//        System.out.println(FILE_PATH);
//    }
}
