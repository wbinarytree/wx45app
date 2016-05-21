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
 * WX45APP ���ظ��»�ӭҳ��������ʱ������
 * @author Phoenix WANG
 */
@SuppressLint("HandlerLeak") public class WelcomeActivity extends Activity{

	private static final String HTTP_URL_VERSION = "http://www.wx45.com/json.php?mod=app&act=checkver&lver=" ;
	public static final int HTTP_GET_MESSAGE_VERSION_DATA = 0xF3;
   
	/**
	 * �汾�������
	 * vercode ��ǰ�汾��Ϣ
	 * verName �汾��
	 * version �������汾��
	 * url ��������
	 * filename ��������
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
	 * �����·���
	 */
	private void checkUpdate(){
		packageName = this.getPackageName();
		getVerCode(WelcomeActivity.this);
	    jthread_ver = new HttpJsonThread(handler,HTTP_GET_MESSAGE_VERSION_DATA,HTTP_URL_VERSION);
		Thread thread = new Thread(jthread_ver);
		thread.start();
		
		
	}
	
    /** 
     * Role:ȡ�ó���ĵ�ǰ�汾<BR> 
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
     * ���ظ���
     */
    private void doNewVersionUpdate(String nowVersion, String newVersion) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("��ǰ�汾: ");  
        sb.append(nowVersion);  
        sb.append(" , �����°汾: ");  
        sb.append(newVersion);  
        sb.append(" , �Ƿ����?");  
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
                .setTitle("�������")  
                .setMessage(sb.toString())  
                // ��������  
                .setPositiveButton("����",// ����ȷ����ť  
                        new DialogInterface.OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog,  
                                    int which) {  
                            	mdialog = new ProgressDialog(  
                                		WelcomeActivity.this);  
                            	mdialog.setTitle("��������");  
                                mdialog.setMessage("���Ժ�...");  
                                mdialog.show();  
                                new Thread(new DownloadApkThread(mhandler,url,fileName)).start();  //��������ҳ��
                            }  
                        })  
                .setNegativeButton("�ݲ�����",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog,  
                                    int whichButton) {  
                                // ���"ȡ��"��ť֮���˳�����  
                            	myApplication.setFlag(myApplication.getFlag() + 1);
                             	myApplication.setLastIgnoreDate(new Date(System.currentTimeMillis()));
                             	myApplication.addDate(new Date(System.currentTimeMillis()));
                                dialog.cancel(); 
                                handler.post(r);
                            }  
                        }).create();// ����  
        // ��ʾ�Ի���  
        dialog.show();  
    }
    
    private Handler mhandler = new Handler() {  
        public void handleMessage(android.os.Message msg) {  
            super.handleMessage(msg);  
            if (msg.what == 1) {    
                ToastUtil.showToast(mContext, "���سɹ�",Toast.LENGTH_SHORT);
                Intent intent = new Intent(Intent.ACTION_VIEW);  
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(  
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/update/", "wx45.apk")),  
                        "application/vnd.android.package-archive");  
                startActivity(intent);  
            } else {    
                ToastUtil.showToast(mContext, "����ʧ��",Toast.LENGTH_SHORT);
            }  
            mdialog.cancel();  
        }  
    };
    
    
    private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		//���±�ʶ�����и�����ת
    		 switch (msg.what) {
				case HTTP_GET_MESSAGE_VERSION_DATA:
					if(msg.obj.toString().equals("")){
						
						Log.d("HTTP ERROR","�������ݻ�ȡʧ��");
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
     * �������߳�
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
	 * ���汾��ϢJSON׼����Version
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

		//ʵ�����ֻ��һ��ѭ����
		
			JSONObject temp = null;
			try {
				temp = obj;
				
				
				version.setApp_desc(temp.getString("app_desc"));
				version.setApp_name(temp.getString("app_name"));
				version.setApp_size(temp.getString("app_size"));
				
				//�����Ҫ�޸�
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
