package com.wx45.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.GoodDetailData;
import http.GoodsSearchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhenshi.util.ToastUtil;
import com.wx45.thread.HttpJsonThread;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * WX45APP �����б�ҳ��
 * @author Phoenix WANG
 * ��ҳ���������HTTP��Ʒ�����б����Ʒ����
 */
@SuppressLint("HandlerLeak")
public class SearchListActivity extends ListActivity {

	//HTTP message��ʶ
	public static final int HTTP_GET_MESSAGE_SEARCH_DATA = 0xF1;
	public static final int HTTP_GET_MESSAGE_DETAIL_DATA = 0xF2;
	public static final int HTTP_GET_MESSAGE_VERSION_DATA = 0xF3;
	//HTTP URL
	private static final String HTTP_URL_SEARCH = "http://www.wx45.com/json.php?mod=item&act=search&k=" ;
	private static final String HTTP_URL_DETAIL = "http://www.wx45.com/json.php?mod=product&act=query&id=" ;

	
	/**
	 * View��Ա
	 * tv_netchecker ��ʾ������Ϣ
	 * et_search ������
	 * btn_search ������ť
	 * list ��Ʒ�б�
	 * myapplication ȫ�ֱ�������汾��Ϣ 
	 * context Activity������
	 * mdialg����������
	 */
	private TextView tv_netchecker;
	private EditText et_search;
	private ImageButton btn_search;
	private ListView list;
	private MyApplication myapplication;
	private Context context = null;
	private ProgressDialog mdialog;
	private String goodsId;
	/**
	 * ListView����
	 * goodSearchList JsonDataArray
	 * goodDetailData ��Ʒϸ��JSON
	 * goodsTotal �������
	 * ArrayList �洢ListViewItem
	 */
	private  List<GoodsSearchData> goodSearchList;
	private GoodDetailData goodDetailData;
	private int goodsTotal;
	private ArrayList<Map<String,Object>> mData;
	

	
	/**
	 * �߳���
	 */
	private HttpJsonThread jthread_list;
	private HttpJsonThread jthread_detail;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_search);
				
		//��ʼ��View
		ViewConstruct();
		
		/**
		 * ���������ã�ȡֵ
		 */
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_search.clearFocus();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);	
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {
			
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				
				 if (actionId == EditorInfo.IME_ACTION_SEARCH)
				 {
					 btn_search.callOnClick();
				 }
				return false;
			}
		});
		
		/**
		 * ����������ť
		 */		
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//δ������,��ʾ��
				if(!myapplication.isNetConnected()){
					tv_netchecker.setText(R.string.net_tip);
					tv_netchecker.setVisibility(View.VISIBLE);
				}
				//����򿪣����������߳�
				else{
					//�ر����뷨����
					mdialog = ProgressDialog.show(SearchListActivity.this,"", "���ڲ�ѯ");
					mdialog.setOnKeyListener(new OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							if (keyCode == KeyEvent.KEYCODE_BACK  
			                        && event.getRepeatCount() == 0)
								handler.removeMessages(HTTP_GET_MESSAGE_SEARCH_DATA);
								
							
							finish();
							return false;
						}
					});
					tv_netchecker.setVisibility(View.GONE);
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);	
					imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
					//��ȡEditText���ݣ������ո�ת����%20
					String searchstr = et_search.getText().toString();
					searchstr =  searchstr.replace(" ", "%20");
					//�������������߳�
					jthread_list = new HttpJsonThread(handler, HTTP_GET_MESSAGE_SEARCH_DATA, HTTP_URL_SEARCH + searchstr);
					Thread thread = new Thread(new HttpJsonThread(handler, HTTP_GET_MESSAGE_SEARCH_DATA, HTTP_URL_SEARCH + searchstr));
					thread.start();

					
//					handler.post(new HttpJsonThread(handler, HTTP_GET_MESSAGE_SEARCH_DATA, HTTP_URL_SEARCH + searchstr));
				}
			}

		});
		
		/**
		 * ����ListViewItemListener
		 */
		list.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> adapterView, View view, int position,
			    long id) {
		    	//�������	
		    	if(!myapplication.isNetConnected()){
					
		    		tv_netchecker.setVisibility(View.VISIBLE);
		    		tv_netchecker.setText("û�м�⵽���磬�����������");
				}else{
					tv_netchecker.setVisibility(View.GONE);
			    	goodDetailData = new GoodDetailData();
									
					goodsId = (String) mData.get(position).get("id");
					mdialog  = ProgressDialog.show(SearchListActivity.this,"", "���Ժ�");
					mdialog.setOnKeyListener(new OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							if (keyCode == KeyEvent.KEYCODE_BACK  
			                        && event.getRepeatCount() == 0)
							handler.removeCallbacks(jthread_list);
							return false;
						}
					});
					jthread_detail = new HttpJsonThread(handler, HTTP_GET_MESSAGE_DETAIL_DATA, HTTP_URL_DETAIL + goodsId);
					Thread thread = new Thread(jthread_detail);
					thread.start();
					//handler.post(new HttpJsonThread(handler, HTTP_GET_MESSAGE_DETAIL_DATA, HTTP_URL_DETAIL + goodsId))
				}
		    }
		});
		

		
	}
	
	/**
	 * View�ĳ�ʼ��
	 */
	private void ViewConstruct(){
		tv_netchecker = (TextView) findViewById(R.id.tv_test_iconlist);
	    et_search = (EditText) findViewById(R.id.edSearchKey);
	    btn_search = (ImageButton) findViewById(R.id.btnSearch);
	    myapplication = (MyApplication) this.getApplication();
	    list = getListView();
	    context = SearchListActivity.this;
	    goodSearchList = new ArrayList<GoodsSearchData>();
	    mData = new ArrayList<Map<String,Object>>();
	}
	/**
	 * Handlerͨ��what��ʶ������һ������
	 */
	private Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			// search_data ���н���ListView
			case HTTP_GET_MESSAGE_SEARCH_DATA:
				parserJsonDataSearchMethod(msg.obj.toString());
				if(goodsTotal>29){
					ToastUtil.showToast(context, "�������������30�����������������ϸ����Ϣ", Toast.LENGTH_SHORT);
				}
				handler.removeMessages(HTTP_GET_MESSAGE_SEARCH_DATA);
//				handler.removeCallbacks(jthread_list);
				break;
			
			// Detail��ʶ ����ҳ����ת����ֵ
			case HTTP_GET_MESSAGE_DETAIL_DATA:
				parserJsonDataProductDetail(msg.obj.toString());
				mdialog.dismiss();
				handler.removeMessages(HTTP_GET_MESSAGE_DETAIL_DATA);
//				handler.removeCallbacks(jthread_detail);
				//����Intent ��JSON�������DetailActivity
				Intent intent = new Intent();
				intent.setClass(SearchListActivity.this, DetailActivity.class);
				//��ת������ҳ
				intent.putExtra("goodDetailData", goodDetailData);
				intent.putExtra("goodID",goodsId);
				startActivity(intent);
				
				break;
			

			}
		}
		
	};
	
	/**
	 * String�ַ���ת��JSONObj
	 * @param strContent JSON�ַ���
	 */
	
	
	private void parserJsonDataSearchMethod(String strContent) {
		JSONArray arr = null;	
		
		try {
			arr = new JSONArray(strContent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		goodSearchList.clear();
		goodsTotal = arr.length();
		
		for (int i = 0; i < arr.length(); i++) {
			JSONObject temp = null;
			try {
				temp = (JSONObject) arr.get(i);
				GoodsSearchData searData = new GoodsSearchData();
				
				searData.setHashData( GoodsSearchData.KEY_GOODS_ID, temp.getString(GoodsSearchData.KEY_GOODS_ID));
				searData.setHashData( GoodsSearchData.KEY_GOODS_SN, temp.getString(GoodsSearchData.KEY_GOODS_SN));
				searData.setHashData( GoodsSearchData.KEY_GOODS_NAME, temp.getString(GoodsSearchData.KEY_GOODS_NAME));
				searData.setHashData( GoodsSearchData.KEY_GOODS_PRICE, temp.getString(GoodsSearchData.KEY_GOODS_PRICE));
				searData.setHashData( GoodsSearchData.KEY_GOODS_LEFTNUMS, temp.getString(GoodsSearchData.KEY_GOODS_LEFTNUMS));
				
				goodSearchList.add( searData );

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		filledListView();
		
	}
	/**
	 * ��Ʒ��ϸJSON�ַ���ת��JSONObject
	 * @param strContent
	 */
	private void parserJsonDataProductDetail(String strContent) {
			
			JSONObject temp = null;
			
			goodDetailData.reset();
			
			try {
	
				
				int a = strContent.indexOf("goods_desc");
				int b = strContent.indexOf("goods_thumb");
				
				String def = strContent.substring(a+12,b-2);

				goodDetailData.setHashData(GoodDetailData.KEY_GOODS_DESC, def);
				
				temp =  new JSONObject(strContent);

				goodDetailData.setHashData(GoodDetailData.KEY_GOODS_NAME, temp.getString(GoodDetailData.KEY_GOODS_NAME));
				goodDetailData.setHashData(GoodDetailData.KEY_GOODS_PRICE, temp.getString(GoodDetailData.KEY_GOODS_PRICE));
				goodDetailData.setHashData(GoodDetailData.KEY_GOODS_SDAY, temp.getString(GoodDetailData.KEY_GOODS_SDAY));
				goodDetailData.setHashData(GoodDetailData.KEY_GOODS_NUMS, temp.getString(GoodDetailData.KEY_GOODS_NUMS));
				goodDetailData.setHashData(GoodDetailData.KEY_GOODS_ST, temp.getString(GoodDetailData.KEY_GOODS_ST));
			//	goodDetailData.setHashData(GoodDetailData.KEY_GOODS_DESC, temp.getString(GoodDetailData.KEY_GOODS_DESC));
				
				goodDetailData.setHashData( "goods_img", temp.getString("goods_thumb"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	

	
	/**
	 * ����ListView
	 */
	private void filledListView(){
		
		int index = 0;
		mData.clear();
		
		for( index=0; index<goodSearchList.size(); index++)
		{
			Map<String,Object> item = new HashMap<String,Object>();
		    
			item.put("id", goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_ID));
		    item.put("title", goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_NAME));
		    if(Integer.valueOf(goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_LEFTNUMS)) != 0)
		    	item.put("image", R.drawable.good_h_new);
		    else
		    	item.put("image", R.drawable.good_l);
		    item.put("text","��" + goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_PRICE));
		    mData.add(item); 
			
		}
		SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.list,
				new String[]{"image","title","text"},new int[]{R.id.image,R.id.title,R.id.text});
		setListAdapter(adapter); 
		mdialog.dismiss();
		
	}


	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}  
	
    
    
	
}
