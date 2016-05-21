package com.wx45.app;


import http.GoodsSearchData;
import http.GoodDetailData;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import cn.m15.xys.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IconList extends ListActivity {
	
	public static final int HTTP_GET_MESSAGE_SEARCH_DATA = 0xF1;
	public static final int HTTP_GET_MESSAGE_DETAIL_DATA = 0xF2;
	private EditText mSearchString;
	private ImageButton mBtnSearch;
	private int    GoodSelIndex = 0;
	private String goodsId;
	private String mResultData;
	private List<GoodsSearchData> goodSearchList;
    ListView mListView = null;
    private String msearchStringContext; 
    private int GoodsTotal;
    private http.GoodDetailData  goodDetailData;
    
    
    ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	this.setContentView(R.layout.iconlist);
    	mListView = getListView();
		mSearchString = (EditText) this.findViewById(R.id.edSearchKey);
		
		mBtnSearch = (ImageButton) this.findViewById(R.id.btnSearch);
		goodSearchList = new ArrayList<GoodsSearchData>();
		mBtnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				msearchStringContext = mSearchString.getText().toString();
				showToast("Start Searching...");
				

				//mHttpSearchThread.start();
				//(new HttpSearchThread()).start();
				httpGetRequest("http://www.wx45.com/json.php?mod=item&act=search&k="+msearchStringContext, HTTP_GET_MESSAGE_SEARCH_DATA);
				
				parserJsonDataSearchMethod(mResultData);
				
				if(GoodsTotal > 29){
					showToast("搜索结果大于30条，建议输入更多搜索条件");
					
				}
				
				
				
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> adapterView, View view, int position,
			    long id) {
		    	
		    	goodDetailData = new GoodDetailData();
								
				goodsId = (String) mData.get(position).get("id");
				//Toast.makeText(IconList.this,(CharSequence) mData.get(position).get("id"), Toast.LENGTH_SHORT).show();
				//(new HttpGoodsDetailThread()).start();
				httpGetRequest("http://www.wx45.com/json.php?mod=product&act=query&id="+ goodsId,HTTP_GET_MESSAGE_DETAIL_DATA);
				parserJsonDataProductDetail(mResultData);
				//System.out.println(goodDetailData);
				Intent intent = new Intent();
				intent.setClass(IconList.this, DetailActivity.class);
				intent.putExtra("goodDetailData", goodDetailData);
				startActivity(intent);
			
			
			
		    }
		});
			

	
	super.onCreate(savedInstanceState);
    }
    
    class HttpSearchThread extends Thread {
		public void run() {
			httpGetRequest("http://www.wx45.com/json.php?mod=item&act=search&k="+msearchStringContext, HTTP_GET_MESSAGE_SEARCH_DATA);
			// httpGetRequest("http://www.baidu.com" );
		}
	}
    
    
    class HttpGoodsDetailThread extends Thread {
		public void run() {
			httpGetRequest("http://www.wx45.com/json.php?mod=product&act=query&id="+ goodsId,HTTP_GET_MESSAGE_DETAIL_DATA);
			// httpGetRequest("http://www.baidu.com" );
		}
	}	
    
	private void httpGetRequest(String url, int cmd) {
			
			mResultData ="";
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.d("BaiKu2014-0504", "============= Http response OK!======");
					mResultData = EntityUtils.toString(httpResponse.getEntity());
					// mTextContent.setText(result);
					mHandler.sendEmptyMessage(cmd);
				}
			} catch (Exception e) {
	
				Log.d("BaiKu2014-0504",
						"============= Http response Failure!======");
				e.printStackTrace();
			}
		}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			// String strContent = bundle.getString("recv");
			switch (msg.what) {
			case HTTP_GET_MESSAGE_SEARCH_DATA:
				parserJsonDataSearchMethod(mResultData);
				break;
			
			//因为不明白这块的执行顺序和原理，暂时先注释掉这块；手工地去调用相应的模块
				
			//case  HTTP_GET_MESSAGE_DETAIL_DATA:
			//	parserJsonDataProductDetail(mResultData);
			//	break;	
			}
		}
	};
	
	
	
	
	private void parserJsonDataProductDetail(String strContent) {
		StringBuilder goodsItemDetail = new StringBuilder();

		JSONObject temp = null;
		JSONObject tempDesp = null;

		goodDetailData.reset();
		
		try {
			temp =  new JSONObject(strContent);
			
			goodDetailData.setHashData(GoodDetailData.KEY_GOODS_NAME, temp.getString(GoodDetailData.KEY_GOODS_NAME));
			goodDetailData.setHashData(GoodDetailData.KEY_GOODS_PRICE, temp.getString(GoodDetailData.KEY_GOODS_PRICE));
			goodDetailData.setHashData(GoodDetailData.KEY_GOODS_SDAY, temp.getString(GoodDetailData.KEY_GOODS_SDAY));
			goodDetailData.setHashData(GoodDetailData.KEY_GOODS_NUMS, temp.getString(GoodDetailData.KEY_GOODS_NUMS));
			goodDetailData.setHashData(GoodDetailData.KEY_GOODS_ST, temp.getString(GoodDetailData.KEY_GOODS_ST));
			goodDetailData.setHashData(GoodDetailData.KEY_GOODS_DESC, temp.getString(GoodDetailData.KEY_GOODS_DESC));
			
			goodDetailData.setHashData( "goods_img", temp.getString("goods_thumb"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	//	mTextContent.setText( goodDetailData.toString());
		
		
		//updateTableControl(goodDetailData );
		
		
	}
	
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
		GoodsTotal = arr.length();
		
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
		// mTextContent.setText( goodsInfo.toString() );
		
		
	}
	
	void filledListView()
	{
		int index = 0;
		mData.clear();
		
		for( index=0; index<goodSearchList.size(); index++)
		{
			Map<String,Object> item = new HashMap<String,Object>();
		    
			item.put("id", goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_ID));
		    item.put("title", goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_NAME));
		    if(Integer.valueOf(goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_LEFTNUMS)) != 0)
		    	item.put("image", R.drawable.good_h);
		    else
		    	item.put("image", R.drawable.good_l);
		    item.put("text","￥" + goodSearchList.get(index).getHashData(GoodsSearchData.KEY_GOODS_PRICE));
		    mData.add(item); 
			
		}
		SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.list,
				new String[]{"image","title","text"},new int[]{R.id.image,R.id.title,R.id.text});
		setListAdapter(adapter); 
		


	}
	
    private Toast mToast;
    public void showToast(String text) {  
        if(mToast == null) {  
            mToast = Toast.makeText(IconList.this, text, Toast.LENGTH_SHORT);  
        } else {  
            mToast.setText(text);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();  
    }  
     
    public void cancelToast() {  
            if (mToast != null) {  
                mToast.cancel();  
            }  
        }  
     
    public void onBackPressed() {  
            cancelToast();  
            super.onBackPressed();  
        }  
	
}
