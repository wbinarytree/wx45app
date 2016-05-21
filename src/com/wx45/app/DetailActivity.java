package com.wx45.app;

import http.GoodDetailData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.wx45.thread.DetailImageThread;
import com.wx45.thread.HttpJsonThread;
import com.zhenshi.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * WX45APP ��Ʒ�������
 * @author Phoenix WANG
 * ��Ʒ������Ϣ��SearchListActivity�д�HTTP���ͨ��intent����
 *
 */
@SuppressLint("HandlerLeak")
public class DetailActivity extends ListActivity {
	
	/**
	 * View��Ա
	 * TextView:
	 * tv_label ��Ʒ����
	 * tv_prise ��Ʒ�۸�
	 * tv_express �ͻ�����
	 * tv_label_prise �۸����(�����������޿��ʱ��ǩ��ͬ)
	 * ImageView:
	 * iv_product ��ƷͼƬ
	 * iv_stock �������ͼƬ
	 * iv_new ȫ��/����
	 * Button:
	 * ibtm_dail ����绰ImageButton
	 * btm_dail ���ְ�ť
	 */	
	private TextView tv_label;
	private TextView tv_prise;
	private TextView tv_express;
	private TextView tv_label_prise;
	
	private ImageView iv_product;
	private ImageView iv_stock;
	private ImageView iv_new;

	private ListView mlistview;
	
	private Button btn_dail;
	private ImageButton ibtm_dail;
	
	private String imageUrl;
	private Bitmap btmp;
	
	private String goodId;
	
	private String url = "http://www.wx45.com/json.php?mod=product&act=qrycall&username=uname&telno=";
//	private final static String url_s = "http://www.wx45.com/json.php?mod=app&act=gettelno&telno=";
	private String url_less = "&proid=";
	ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();	
	//private Button button ;
	
	private MyApplication myApplication;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//�󶨲����ļ�
		setContentView(R.layout.activity_detail);
		
		ViewConstruct();
		
		//��ȡ����ֵ
		Intent intent = getIntent();
		
		
		GoodDetailData goodDetailData = (GoodDetailData) intent.getSerializableExtra("goodDetailData");
		goodId = (String) intent.getSerializableExtra("goodID");
		//��ȡ��Ʒ��ϸ��Ϣ
		String goods_name = goodDetailData.getHashData("goods_name");
		String shop_price = goodDetailData.getHashData("shop_price");
		String goods_number = goodDetailData.getHashData("goods_number");
		String goods_img = goodDetailData.getHashData("goods_img");
		String goods_st = goodDetailData.getHashData("st");
		String sday = goodDetailData.getHashData("sday");
		
		
		imageUrl = "http://www.wx45.com/"+goods_img;
		
		//��Ʒ����,�۸�
		tv_label.setText(goods_name);
		tv_prise.setText("��" + shop_price + " Ԫ");
		
		//��������
		if(Integer.valueOf(sday) == 1){
			if(Integer.valueOf(goods_number) != 0)
			tv_express.setText("���շ���");
			else 
			tv_express.setVisibility(View.INVISIBLE);			
		}
		else{
			if(Integer.valueOf(goods_number) != 0)
			tv_express.setText(sday + "���ڷ���");
			else 
			tv_express.setVisibility(View.INVISIBLE);
		}
		
		//��Ʒ���޻�
		if(Integer.valueOf(goods_st) == 10){
			iv_new.setImageResource(R.drawable.st);
		}
		else{
			iv_new.setImageResource(R.drawable.st_d);
		}
		
		//��ƷͼƬ
		if("".equals(goods_img)){
			iv_product.setImageResource(R.drawable.temp);
		}else{
			Thread thread = new Thread(new DetailImageThread(handler,0xf6,imageUrl));
			Log.d("IMAGE URL",imageUrl);
			thread.start();
		}
		
		//��Ʒ�޿�棬�����޿��ͼƬ��������ʾ���۸���ʾ
		if(Integer.valueOf(goods_number) != 0){
			tv_label_prise.setText("�۸�:");
			btn_dail.setText("����������߶���:400-800-0264");
			iv_stock.setImageResource(R.drawable.goods_have);
		}
		else{
			tv_label_prise.setText("�ο��۸�:");
			btn_dail.setText("����������߲�ѯ:400-800-0264");
			iv_stock.setImageResource(R.drawable.goods_lack);
		}
		
		HashMap<String, String> goodDecpHashData = goodDetailData
				.getDespHashTable();
		
		//���ò���绰��ť����
		btn_dail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				

				Check();

			}
		});
		ibtm_dail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn_dail.callOnClick();
			}
		});
		
		//�����Ʒ����ListView
		mData.clear();
		
		for (Iterator<String> itr = goodDecpHashData.keySet().iterator(); itr.hasNext();)
		{
			String key = (String) itr.next();
			String value = (String) goodDecpHashData.get(key);
			
	    	    Map<String,Object> item = new HashMap<String,Object>();
	    	    item.put("title", key);
	    	    item.put("text", value);
	    	    mData.add(item); 
		}
		SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.list_detail,
				new String[]{"title","text"},new int[]{R.id.title_detail,R.id.text_detail});
		setListAdapter(adapter); 

		super.onCreate(savedInstanceState);
		
	}
	
	//������ƷͼƬ
	private Handler handler = new Handler(){
		
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 0xf6:
				btmp = (Bitmap) msg.obj;
				iv_product.setImageBitmap(btmp);  
				handler.removeMessages(0xf6);
				break;
			case 0xf5:
				String result = msg.obj.toString();
				Log.d("result",result);
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"4008000264")); 
				 DetailActivity.this.startActivity(intent);
				handler.removeMessages(0xf5);
				break;
			case 0xF7:
				Log.d("HTTP","HTTP SEND SUCCESS");
				handler.removeMessages(0xF7);
			default:
				break;
			}
			
		}
		
	};
	
	//��ʼ��View��Ա
	private void ViewConstruct(){
		
		mlistview = getListView();
		mlistview.setDivider(null);
		mlistview.setCacheColorHint(0);
		mlistview.setFocusable(false);
		mlistview.setPressed(false);
		
		iv_product = (ImageView) findViewById(R.id.im_goods);
		tv_label_prise = (TextView) findViewById(R.id.tv_prise_label);
		tv_label = (TextView) findViewById(R.id.tv_label);  
		tv_prise = (TextView) findViewById(R.id.tv_prise); 
		iv_stock = (ImageView) findViewById(R.id.goods_st);
		iv_new = (ImageView) findViewById(R.id.im_st);
		tv_express = (TextView) findViewById(R.id.tv_express);
		ibtm_dail = (ImageButton) findViewById(R.id.ib_dail);
		btn_dail = (Button) findViewById(R.id.dail);
		myApplication = (MyApplication) DetailActivity.this.getApplicationContext();
		
	}
	
	private void Check(){
		
		TelephonyManager mTelephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String temp = mTelephonyManager.getLine1Number();
		Log.d("Phone Number", temp);
		Dialog dialog = null;
		if(temp.equals(""))
		{
			final EditText edit = new EditText(DetailActivity.this);
			 dialog = new AlertDialog.Builder(DetailActivity.this)
			.setMessage("�����������ֻ��ŷ������Ǹ��õ�Ϊ������(�������ǻ��ע���û�����һ���̶ȵ��Ż�����)")
			.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK  
	                        && event.getRepeatCount() == 0)
					dialog.cancel();
					return false;
				}
			})
			.setView(edit)
			.setPositiveButton("ȷ��",// ����ȷ����ť  
                        new DialogInterface.OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog,  
                                    int which) {  
                            	String temp = edit.getText().toString();
                            	SharedPreferences sp = DetailActivity.this.getSharedPreferences("telnum", Context.MODE_APPEND);
                            	Editor ed = sp.edit();
                            	if(myApplication.matchNum(temp) == 5 ||myApplication.matchNum(temp) == 4){
                            		ToastUtil.showToast(DetailActivity.this, "������ĵ绰������������������",Toast.LENGTH_SHORT);
                            		edit.setText("");
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
            .setNegativeButton("ȡ��",  
                        new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog,  
                                    int whichButton) {  
                               dialog.cancel();
                               try {
									Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);
									} catch (Exception e) {
									e.printStackTrace();
									} 
                               Thread thread = new Thread(new HttpJsonThread(handler, 0xf5, url + url_less + goodId));
                   				thread.start();
                               
                            }  
                        })
			.create();
		}
		SharedPreferences sp = this.getSharedPreferences("telnum", Context.MODE_PRIVATE);
		String temp1 = sp.getString("telnum", "0");
		if(temp1.equals("0")){
			dialog.show();
		}
		else
		{
			Thread thread = new Thread(new HttpJsonThread(handler, 0xf5, url + temp1 + url_less + goodId));
			thread.start();
		}

	}
	
	
	

}
