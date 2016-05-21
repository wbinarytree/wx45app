package com.wx45.app;

import java.io.File;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.wx45.thread.DownloadApkThread;
import com.wx45.thread.HttpJsonThread;
import com.zhenshi.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * WX45APP 加载更新欢迎页，用于延时检查更新
 * @author Phoenix WANG
 */
@SuppressLint("HandlerLeak") public class WelcomeActivity extends Activity{

	private static final String HTTP_URL_VERSION = "http://www.wx45.com/json.php?mod=app&act=checkver&lver=" ;
	public static final int HTTP_GET_MESSAGE_VERSION_DATA = 0xF3;
   
	/**
	 * 版本更新相关
	 * vercode 当前版本信息
	 * verName 版本名
	 * version 服务器版本号
	 * url 下载链接
	 * filename 下载名称
	 */
	private int verCode;
	private String verName;
	private String packageName;
	private Version version;
	private String url;
	private String fileName;
	
	private ProgressDialog mdialog;

	private Long logintime;
	private MyApplication myApplication;
	private Context mContext;
	private HttpJsonThread jthread_ver;
	private boolean needUpdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myApplication = (MyApplication) this.getApplicationContext();
		mContext = this;
		needUpdate = false;
		WelcomeActivity.this.setContentView(R.layout.activity_welcome);
		if(!myApplication.isNetConnected()){
			handler.postDelayed(r,2000);
		}
		else{
	    		logintime = System.currentTimeMillis();
	    		
	    		Log.d("Time_S", logintime.toString());
	    		checkUpdate();
		}

	}

	/**
	 * 检查更新方法
	 */
	private void checkUpdate(){
		packageName = this.getPackageName();
		getVerCode(WelcomeActivity.this);
	    jthread_ver = new HttpJsonThread(handler,HTTP_GET_MESSAGE_VERSION_DATA,HTTP_URL_VERSION);
		Thread thread = new Thread(jthread_ver);
		thread.start();
		
		
	}
	
    /** 
     * Role:取得程序的当前版本<BR> 
     * Date:2012-4-5<BR> 
     *  
     * @author ZHENSHI)peijiangping 
     */  
    private String getVerCode(Context context) {  
          
        try {  
            verCode = context.getPackageManager()  
                    .getPackageInfo(packageName, 0).versionCode;  
            verName = context.getPackageManager()  
                    .getPackageInfo(packageName, 0).versionName;  
        } catch (NameNotFoundException e) {  
            System.out.println("no");  
        }  
        System.out.println("verCode" + verCode + "===" + "verName" + verName);  
        return verName;  
    }  
    
    /**
     * 下载更新
     */
    private void doNewVersionUpdate(String nowVersion, String newVersion) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("当前版本: ");  
        sb.append(nowVersion);  
        sb.append(" , 发现新版本: ");  
        sb.append(newVersion);  
        sb.append(" , 是否更新?");  
        Dialog dialog = new AlertDialog.Builder(WelcomeActivity.this)  
        		.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						 if (keyCode == KeyEvent.KEYCODE_BACK  
			                        && event.getRepeatCount() == 0) {  
							 
                         	myApplication.setFlag(myApplication.getFlag() + 1);
                         	myApplication.setLastIgnoreDate(new Date(System.currentTimeMillis()));
                         	myApplication.addDate(new Date(System.currentTimeMillis()));
                            finish(); 
			                }  
			                return false;  
			            }  
					}
				)
                .setTitle("软件更新")  
                .setMessage(sb.toString())  
                // 设置内容  
                .setPositiveButton("更新",// 设置确定按钮  
                        new DialogInterface.OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog,  
                                    int which) {  
                            	mdialog = new ProgressDialog(  
                                		WelcomeActivity.this);  
                            	mdialog.setTitle("正在下载");  
                                mdialog.setMessage("请稍候...");  
                                mdialog.show();  
                                new Thread(new DownloadApkThread(mhandler,url,fileName)).start();  //进入下载页面
                            }  
                        })  
                .setNegativeButton("暂不更新",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog,  
                                    int whichButton) {  
                                // 点击"取消"按钮之后退出程序  
                            	myApplication.setFlag(myApplication.getFlag() + 1);
                             	myApplication.setLastIgnoreDate(new Date(System.currentTimeMillis()));
                             	myApplication.addDate(new Date(System.currentTimeMillis()));
                                dialog.cancel(); 
                                handler.post(r);
                            }  
                        }).create();// 创建  
        // 显示对话框  
        dialog.show();  
    }
    
    private Handler mhandler = new Handler() {  
        public void handleMessage(android.os.Message msg) {  
            super.handleMessage(msg);  
            if (msg.what == 1) {    
                ToastUtil.showToast(mContext, "下载成功",Toast.LENGTH_SHORT);
                Intent intent = new Intent(Intent.ACTION_VIEW);  
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(  
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/update/", "wx45.apk")),  
                        "application/vnd.android.package-archive");  
                startActivity(intent);  
            } else {    
                ToastUtil.showToast(mContext, "下载失败",Toast.LENGTH_SHORT);
            }  
            mdialog.cancel();  
        }  
    };
    
    
    private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		//更新标识，进行更新跳转
    		 switch (msg.what) {
				case HTTP_GET_MESSAGE_VERSION_DATA:
					if(msg.obj.toString().equals("")){
						
						Log.d("HTTP ERROR","网络数据获取失败");
					}else{
					parserJsonDataCheckVersion(msg.obj.toString());
					if ((Double.valueOf(version.getApp_ver()) > Double.valueOf(verName))&&myApplication.checkUpdate()) {  
						needUpdate = true;  
						doNewVersionUpdate(verName, version.getApp_ver());  
				          
						} 
					}
					handler.removeMessages(HTTP_GET_MESSAGE_VERSION_DATA);
					handler.removeCallbacks(jthread_ver);
					

					if(needUpdate == false){
						Long temp =System.currentTimeMillis();
						Log.d("time",temp.toString());
						if((temp -  logintime) > 1000 ){
							handler.post(r);
						}
						else{
							handler.postDelayed(r,1000-(temp -  logintime));
						}
    		 		}
    		 }
		}
    };
    /**
     * 启动器线程
     */
    private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext,IndexActivity.class); 
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 startActivity(intent);
		}
    };
	/**
	 * 将版本信息JSON准换成Version
	 * @param strContent
	 */
	private void parserJsonDataCheckVersion(String strContent) {
		JSONObject obj = null;	
		version = new Version();
		try {
			obj = new JSONObject(strContent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}

		//实际这个只有一层循环，
		
			JSONObject temp = null;
			try {
				temp = obj;
				
				
				version.setApp_desc(temp.getString("app_desc"));
				version.setApp_name(temp.getString("app_name"));
				version.setApp_size(temp.getString("app_size"));
				
				//这块需要修改
				version.setApp_url("http://www.wx45.com" +temp.getString( "app_url"));
				version.setApp_file(temp.getString("app_file"));
				version.setApp_ver(temp.getString("app_ver"));
				version.setMaijor_ver(temp.getString("maijor_ver"));
				version.setMinor_ver(temp.getString("minor_ver"));
				version.setUpdate_time(temp.getString("update_time"));
				
				url = version.getApp_url();
				fileName = version.getApp_file();
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(jthread_ver);
		super.onDestroy();
	}
}
