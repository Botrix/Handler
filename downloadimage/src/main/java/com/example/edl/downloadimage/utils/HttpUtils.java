package com.example.edl.downloadimage.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * http工具类 http可以使用HttpURLConnection/HttpClient
 *
 * @author
 * @date
 * @version V1.0
 */
public class HttpUtils {
    /**
     * 获取网络图片
     * @param urlString
     * @return
     * @date
     */
    public static Bitmap getNetWorkBitmap(String urlString) {
        URL mImgUrl;
        Bitmap mBitmap = null;
        try {
            mImgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) mImgUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream(); //得到网络返回的IO流
            // 将InputStream转换成Bitmap
            mBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {

            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return mBitmap;
    }

    /*
    public static Bitmap getNetWorkImage(String stringURL) {
        Bitmap mBitmap = null;

        return mBitmap;
    }
    */

    /**
     * 从指定URL获取数据并返回一个字节流数据
     * 应在后台线程执行该方法
     * @param urlString URL
     * @return 字节流数组
     * @throws IOException
     */
    byte[] getURLBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        //HttpURLConnection对象提供了一个连接，但只有在调用getInputStream()方法时(如果是POST请求，则调用getOutputStream())，
        //它才会真正连接到指定的URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(); //创建一个字节数组输出流
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead;
            byte [] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead); //把buffer写到out流中
            }
            out.close();
            return out.toByteArray(); //得到一个新的byte[]
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 将getURLBytes()方法返回的字节数据转换为String
     * @param urlString URL
     * @return String
     * @throws IOException
     */
    public String getURL(String urlString) throws IOException {
        return new String(getURLBytes(urlString)); //将字节数组转换为String
    }


    /******************************************************************************
     *
     * @param urlStr
     * @return
     */
    public static String downloadWebpage(String urlStr) {
        String readStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            readStream = readStream(con.getInputStream());
            // Give output for the command line
            System.out.println(readStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readStream;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //******************************************************************************
}

