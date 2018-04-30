package cn.clazz;

import java.io.File;
import java.util.Date;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackMain
{
    private int count;
    private String zipName;
    private static String exportFolder = null;
    /**
     * 需要打包的目标工程CLASS运行目录， 末尾必须要有结束符
     */
    private static String targetHome = "D:\\TestCode\\lfgj\\target\\classes\\";

    public static void main(String[] args)
            throws Exception
    {
        // 打包后的zip文件在打包工程的target运行classpath根目录下
        // packFiles.properties 为需要打包的CLASS文件, 复制class的包完整路径即可, 打包自动包含内部类
        PackMain t = new PackMain();
        exportFolder = PackMain.class.getResource("/").getPath();
        t.doCopy();
        System.out.println();
        System.out.println("total file: " + t.count);
        System.out.println("created zip file: " + t.zipName);

    }

    private void doCopy() throws IOException {
        File f = new File(PackMain.class.getClassLoader().getResource("packFiles.properties").getPath());
        BufferedReader reader = new BufferedReader(new FileReader(f));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        Date curDate = new Date();

        this.zipName = (exportFolder + "/" + sdf.format(curDate) + "_"+curDate.getTime()+ ".zip");
        ZipOutputStream zipStream = new ZipOutputStream(new FileOutputStream(this.zipName));
        File temp = null;
        String line = null;
        while ((line = reader.readLine()) != null) {
            if(line.indexOf(".java")!= -1) {
                line = line.trim().replaceAll("\\.java$", "");
            }else {
                line = line.trim().replaceAll("\\.", "/");
            }
            if (line.length() != 0) {
                line = targetHome + "/" + line + ".class";
                temp = new File(line);
                copySinleFile(zipStream, temp);
                File[] files = temp.getParentFile().listFiles();
                for (File child : files) {
                    if (isRelatedClassFile(child, temp)) {
                        copySinleFile(zipStream, child);
                    }
                }

            }
        }
        reader.close();
        zipStream.close();
    }

    private void copySinleFile(ZipOutputStream zipStream, File src) throws IOException {
        String desc = src.getAbsolutePath().substring(targetHome.length());
        zipStream.putNextEntry(new ZipEntry(desc));
        FileInputStream fis = new FileInputStream(src);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) {
            zipStream.write(buf, 0, i);
        }
        zipStream.closeEntry();
        fis.close();
        this.count += 1;
        System.out.println("add File: " + desc);
    }

    private void copyChildFiles(ZipOutputStream zipStream, File folder) throws IOException {
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                copyChildFiles(zipStream, f);
            } else {
                copySinleFile(zipStream, f);
            }
        }
    }

    private boolean isRelatedClassFile(File child, File temp)
    {
        String childName = child.getName();
        boolean value = false;
        if (childName.endsWith(".class")) {
            childName = childName.substring(0, childName.length() - 6);
            String tempName = temp.getName().substring(0, temp.getName().length() - 6);
            value = childName.startsWith(tempName + "$");
        }
        return value;
    }
}