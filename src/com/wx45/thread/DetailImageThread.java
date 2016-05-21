package com.wx45.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * 图片加载线程
 * @author Phoenix WANG
 * 
 */
public class DetailImageThread implements Runnable {

	private Handler handler;
	private String url;
	private  int id;
	
	public DetailImageThread(Handler handler,int id,String url){
		this.handler = handler;
		this.url = url;
		this.id = id;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message msg = handler.obtainMessage();
		msg.obj = returnBitMap(url);
		msg.what = id;
		handler.sendMessage(msg);

	}
	
	//获取商品图片
	private Bitmap returnBitMap(String url) {   
		   URL myFileUrl = null;   
		   Bitmap bitmap = null;   
		   try {   
		    myFileUrl = new URL(url);   
		   } catch (MalformedURLException e) {   
		    e.printStackTrace();   
		   }   
		   try {   
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
		    conn.setDoInput(true);   
		    conn.connect();   
		    InputStream is = conn.getInputStream();   
		    bitmap = BitmapFactory.decodeStream(is);   
		    is.close();   
		   } catch (IOException e) {   
		    e.printStackTrace();   
		   }   
		   return bitmap;   
		} 

}
