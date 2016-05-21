package com.wx45.app;


import org.json.JSONException;
import org.json.JSONObject;

import com.wx45.thread.HttpJsonThread;
import com.zhenshi.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak") public class LoginActivity extends Activity {

	private Button btn_login;
	private Button btn_register;
	private EditText ed_username;
	private EditText ed_password;
	private String username;
	private String password;
	
	private static String url = "http://www.wx45.com/json.php?mod=login&act=login&username=";
	private static String url_r = "&passwd=";
	private static String url_r2 = "&checkcode=cc";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Construct();
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				username = ed_username.getText().toString();
				password = ed_password.getText().toString();
				Thread thread = new Thread(new HttpJsonThread(handler, 0xFA, url + username + url_r + password + url_r2));
				thread.start();
			}
		});
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,IndexActivity.class);
				startActivity(intent);
				
			}
		});
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
	}
	
	private void Construct(){
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		ed_username = (EditText) findViewById(R.id.et_login);
		ed_username = (EditText) findViewById(R.id.et_password);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			if(msg.what == 0xFA){
				int temp = parserJsonDataResult(msg.obj.toString());
				switch (temp) {
				case 1:
					ToastUtil.showToast(LoginActivity.this, "µÇÂ½³É¹¦", Toast.LENGTH_SHORT);
					Log.d("HTTP","HTTP RESULT 1");
					break;
				case 2:
					ToastUtil.showToast(LoginActivity.this, "ÃÜÂë´íÎó", Toast.LENGTH_SHORT);
					Log.d("HTTP","HTTP RESULT 2");
					break;

				default:
					break;
				}
				handler.removeMessages(msg.what);
				Log.d("HTTP","HTTP SEND SUCCESS");
			}
		}
		
	};
	
	/**
	 * ÉÌÆ·ÏêÏ¸JSON×Ö·û´®×ª³ÉJSONObject
	 * @param strContent
	 */
	private int parserJsonDataResult(String strContent) {
			
			JSONObject temp = null;
			
			String result = null;
			
			try {

				
				temp =  new JSONObject(strContent);
				result = temp.getString("result");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return Integer.valueOf(result);
	}
	
	

}
