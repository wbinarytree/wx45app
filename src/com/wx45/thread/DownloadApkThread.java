package com.wx45.thread;  
  
import com.zhenshi.util.HttpDownload;  
  

import android.os.Environment;
import android.os.Handler;  
import android.os.Message;  
  
/**
 * APK�����߳�
 * @author Phoenix WANG
 * 
 */
public class DownloadApkThread implements Runnable {  
    private Handler handler;  
    private static final String path = Environment.getExternalStorageDirectory().getPath() + "/update";  
    private String url;
    private String filename;
    public DownloadApkThread(Handler handler) {  
        this.handler = handler;  
    }  
  
    public DownloadApkThread(Handler handler,String url,String filename) {  
        this.handler = handler;  
        this.url = url;
        this.filename = filename;
        
    }  

    @Override  
    public void run() {  
        System.out.println("�����߳̿���");  
        
        Message message = new Message();  
        message.what = HttpDownload.downLoadFile(url, filename, path);  
        handler.sendMessage(message);  

    }
}  