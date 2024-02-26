package org.example.demo;

import java.io.*;

public class FileCtl {
    public static final String IS_PATH ="/Users/zhangweilong/Code/practice-parent/practice/src/main/java/org/example/demo/p26-串讲3.mp4";
    public static final String OS_PATH ="/Users/zhangweilong/Code/practice-parent/practice/src/main/java/org/example/demo/";
    public static void main(String[] args) throws Exception {

        copy1();
        copy2();
        copy3();
        copy4();

    }

    private static void copy4() {
        //       buffer字节数组
        long l4 = System.currentTimeMillis();

        try (
                InputStream is = new FileInputStream(IS_PATH);
                BufferedInputStream bis = new BufferedInputStream(is);
                OutputStream os = new FileOutputStream(OS_PATH+"buffer字节数组.mp4");
                BufferedOutputStream bos =new BufferedOutputStream(os);
        ) {
            byte [] bytes=new byte[1024];
            int len;
            while ((len=bis.read(bytes))!=-1){
                bos.write(bytes,0,len);
            }
            System.out.println((System.currentTimeMillis()-l4)/1000.0+"秒");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void copy3() {
        //       buffer字节
        long l3 = System.currentTimeMillis();

        try (
                InputStream is = new FileInputStream(IS_PATH);
                BufferedInputStream bis = new BufferedInputStream(is);
                OutputStream os = new FileOutputStream(OS_PATH+"buffer字节.mp4");
                BufferedOutputStream bos =new BufferedOutputStream(os);

        ) {
            int len;
            while ((len=bis.read())!=-1){
                bos.write(len);
            }
            System.out.println((System.currentTimeMillis()-l3)/1000.0+"秒");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void copy2() {
        //       字节数组
        long l2 = System.currentTimeMillis();

        try (
                InputStream is = new FileInputStream(IS_PATH);
                OutputStream os = new FileOutputStream(OS_PATH+"字节数组.mp4")
        ) {
            byte [] bytes=new byte[1024];
            int len;
            while ((len=is.read(bytes))!=-1){
                os.write(bytes,0,len);
            }
            System.out.println((System.currentTimeMillis()-l2)/1000.0+"秒");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void copy1() {
        //       单字节
        long l1 = System.currentTimeMillis();

        try (
                InputStream is = new FileInputStream(IS_PATH);
                OutputStream os = new FileOutputStream(OS_PATH+"单子节.mp4")
                ) {
            int len;
            while ((len=is.read())!=-1){
os.write(len);
            }
            System.out.println((System.currentTimeMillis()-l1)/1000.0+"秒");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
