package com.wx45.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wx45.thread.HttpJsonThread;
import com.zhenshi.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class IndexActivity extends ListActivity {
	
	private final static String url = "";
    Context mContext = null;
    private ListView mlist;
    private ImageButton imb;
    private TextView tv_test;
    private MyApplication myApplication;
    private int back;
    private Long time = (long) 0;
	private Long time_new = (long) 0;
	private String telnum;
	private EditText ed_temp;
	private Dialog dialog;
	private SharedPreferences sp;
	private Editor ed;
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	//获取本地数据
    	sp = this.getSharedPreferences("telnum", MODE_PRIVATE);
    	ed = sp.edit();
    	telnum = sp.getString("telnum", "0");
    	ed_temp = new EditText(this);
    	//用户未输入号码
    	if(telnum.equals("0")){
    		 dialog = new AlertDialog.Builder(IndexActivity.this)
    										.setMessage("系统检测到您还没有绑定手机号，是否现在输入？(输入手机号可帮助我们更好的服务您)")
    										.setView(ed_temp)
    										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface arg0, int arg1) {
													// TODO Auto-generated method stub
													String temp = ed_temp.getText().toString();
													if(myApplication.matchNum(temp) == 5 ||myApplication.matchNum(temp) == 4){
													ToastUtil.showToast(mContext, "您输入的电话号码有误，请重新输入",Toast.LENGTH_SHORT);
													ed_temp.setText("");
											        try {
									                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									                    field.setAccessible(true);
									                    field.set(dialog, false);
									                } catch (Exception e) {
									                    e.printStackTrace();
									                }
													
													}else{
														
													ed.putString("telnum", temp);
													ed.putBoolean("cancel", false);
													ed.putBoolean("is", true);
													ed.commit();
													
													Thread thread = new Thread(new HttpJsonThread(handler, 0xF7, url + temp + "&appver=1.5"));
													thread.start();
													
													try {
														Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
														} catch (Exception e) {
														e.printStackTrace();
														} 
													}
												}
											})
											.setNeutralButton("以后再说", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface arg0, int arg1) {
													// TODO Auto-generated method stub
													ed.putBoolean("cancel",true);
													ed.putBoolean("is", false);
													ed.commit();
													dialog.cancel();
													try {
														Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
														} catch (Exception e) {
														e.printStackTrace();
														} 
													
												}
											})
											.setNegativeButton("取消", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface arg0, int arg1) {
													// TODO Auto-generated method stub
													ed.putBoolean("cancel",false);
													ed.putBoolean("is", false);
													ed.commit();
													dialog.cancel();
													try {
														Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
														field.setAccessible(true);
														field.set(dialog, true);
														} catch (Exception e) {
														e.printStackTrace();
														} 
													
												}
											})
											.create();
    		 if(!sp.getBoolean("is",false)){
    			 dialog.show();
    		 }
    	}

        back = 0;
        setContentView(R.layout.activity_main);
        
        mContext = this;
        myApplication = (MyApplication) this.getApplication();
        Button edit = (Button) findViewById(R.id.editText1);
        imb = (ImageButton) findViewById(R.id.imb_dail_index);
        tv_test = (TextView)findViewById(R.id.tv_test);
        tv_test.setVisibility(View.GONE);
        edit.setOnClickListener(new OnClickListener() {
    	    
	    @Override
	    public void onClick(View arg0) {
	    	
	    	
	    	if(myApplication.isNetConnected()){	
	    		tv_test.setVisibility(View.GONE);
				 Intent intent = new Intent(mContext,SearchListActivity.class); 
				 startActivity(intent);
	    	}else{
	    		tv_test.setVisibility(View.VISIBLE);
	    		tv_test.setText("没有检测到网络，请打开网络连接");
	    	}
	    }
	}); 
        
        
        imb.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"4008000264")); 

    			 IndexActivity.this.startActivity(intent);
    			
    		}
    	});
        
        
        mlist = getListView();
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.list_public,
    			new String[]{"img"},
    			new int[]{R.id.im_public});
    	mlist.setAdapter(adapter);
    	
    	
    	
        super.onCreate(savedInstanceState);
    
    }
    


	
/***
 * 首页广告栏ListView
 * @return
 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img", R.drawable.public_1);
		list.add(map);
	
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.public_2);
		list.add(map);
	
		map = new HashMap<String, Object>();
		map.put("img",R.drawable.public_3);
		list.add(map);
		
		return list;
	}


    /**
     *
     * 重写BackPress方法
     * 设置按两次退出
     */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if(back == 0){
			back++;
			ToastUtil.showToast(mContext, "再按一次退出应用", Toast.LENGTH_SHORT);
			time = System.currentTimeMillis();
		}
		else{
			time_new = System.currentTimeMillis();
			if(time_new - time < 2000){
			back = 0;
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			}else{
				back = 0;
			}
		}

	}


	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			if(msg.what == 0xF7){
				handler.removeMessages(msg.what);
				Log.d("HTTP","HTTP SEND SUCCESS");
			}
		}
	};


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
/*		if(sp.getString("telnum", "0").equals("0")){
			ed.putBoolean("cancel", true);
			ed.commit();				
		}*/
		
		super.onDestroy();
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Boolean temp = sp.getBoolean("cancel", true);
		Boolean temp_1 = sp.getBoolean("is", true);
		if(temp == false&temp_1 == false)
			dialog.show();
		super.onResume();
	}

	
	
	
	
	
}
