package com.wx45.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;




import java.util.Map;
import cn.m15.xys.R;
import http.GoodDetailData;
import http.GoodsSearchData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailActivity extends ListActivity implements OnClickListener{
	
	private TextView textView;
	private TextView textView_prise;
	private TextView textView_number;
	private TextView textview_st;
	private TextView tv_express;
	
	private TableLayout tableView ;
	private ListView mlistview;
	private ImageView imageView;
	private ImageView goods_stock;
	private ImageView im_st;
	private String imageUrl;


	
	ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();	
	//private Button button ;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//绑定布局文件
		setContentView(R.layout.activity_detail);
		
		mlistview = getListView();
		mlistview.setDivider(null);
		mlistview.setCacheColorHint(0);
		imageView = (ImageView) findViewById(R.id.im_goods);
		textView = (TextView) findViewById(R.id.tv_label);  
		textView_prise = (TextView) findViewById(R.id.tv_prise); 
		//textview_st = (TextView) findViewById(R.id.tv_st);
		goods_stock = (ImageView) findViewById(R.id.goods_st);
		im_st = (ImageView) findViewById(R.id.im_st);
		tv_express = (TextView) findViewById(R.id.tv_express);
		//获取AppActivity中传来的值
		
		Intent intent = getIntent();
		
		
		GoodDetailData goodDetailData = (GoodDetailData) intent.getSerializableExtra("goodDetailData");
		
		String goods_name = goodDetailData.getHashData("goods_name");
		String shop_price = goodDetailData.getHashData("shop_price");
		String goods_number = goodDetailData.getHashData("goods_number");
		String goods_img = goodDetailData.getHashData("goods_img");
		String goods_st = goodDetailData.getHashData("st");
		String sday = goodDetailData.getHashData("sday");
		
		imageUrl = "http://www.wx45.com/"+goods_img;
		textView.setText(goods_name);
		textView_prise.setText("￥" + shop_price + " 元");
		tv_express.setText(sday + "日内送达");
		if(Integer.valueOf(goods_st) == 10)
		{
			im_st.setImageResource(R.drawable.st);
		}
		else
		{
			im_st.setImageResource(R.drawable.st_d);
		}
		if("".equals(goods_img)){
			imageView.setImageResource(R.drawable.temp);
		}else{
			
			imageView.setImageBitmap(returnBitMap(imageUrl));  
		}
		
		
		if(Integer.valueOf(goods_number) != 0)
		{
			
			goods_stock.setImageResource(R.drawable.goods_have);
		}
		else
		{
			goods_stock.setImageResource(R.drawable.goods_lack);
		}
		
		HashMap<String, String> goodSHashData = goodDetailData
				.getGoodsDetailTable();
		HashMap<String, String> goodDecpHashData = goodDetailData
				.getDespHashTable();

		/**
		 * 1. reset the table view
		 */
		//tableView.removeAllViews();
		
		
		mData.clear();
		
		for (Iterator itr = goodDecpHashData.keySet().iterator(); itr.hasNext();)
		{
			String key = (String) itr.next();
			String value = (String) goodDecpHashData.get(key);
			
			
			//Log.d(TAGS, "DESP======key:"+key +"---->" + value);

			//add table item;				

	    	    Map<String,Object> item = new HashMap<String,Object>();
	    	    item.put("title", key);
	    	    item.put("text", value);
	    	    mData.add(item); 
	            

	            
	            
	            
		}
		SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.list_detail,
				new String[]{"title","text"},new int[]{R.id.title_detail,R.id.text_detail});
		setListAdapter(adapter); 
		
		
		
		
		

	}

	
	public Bitmap returnBitMap(String url) {   
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	

}
