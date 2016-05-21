package http;


import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.util.JsonReader;
import android.util.Log;


/** 返回结果： 
* goods_name:产品名称
*  shop_price:店铺售价 
*  goods_number:产品库存
* goods_desc:产品描述，{JSONdata} 
* sday:发送时间，默认1天 
* st:产品成色，默认为10
*/

public class GoodDetailData implements Serializable{

	/**
	 * 
	 */
	
/*	public class descContainer{
		
		public String title;
		public String value;
	}*/
	private static final long serialVersionUID = 1L;
	private final static String TAGS="GoodDetailData";
	private HashMap<String,String>  goodSHashData;
	private LinkedHashMap<String,String>  goodDecpHashData;
//	private Vector<descContainer> goodDecpVector;
	
	public final static String KEY_GOODS_NAME = "goods_name";	
	public final static String KEY_GOODS_PRICE = "shop_price";
	public final static String KEY_GOODS_NUMS = "goods_number";	
	public final static String KEY_GOODS_SDAY = "sday";
	public final static String KEY_GOODS_ST = "st";	
	public final static String KEY_GOODS_DESC = "goods_desc";
	
	public GoodDetailData( )
	{
		goodSHashData = new HashMap<String,String>();
		goodDecpHashData = new 	LinkedHashMap<String,String>();	
//		goodDecpVector = new Vector<descContainer>();
	}
		
	public void setHashData( String key, String data)
	{
		if( key.equals(KEY_GOODS_DESC) )
		{
			//parser JSON data
			parseGoodsDescrition( data );
		}
		else
		{
			goodSHashData.put(key, data);	
		}

	}
	
	public String getHashData( String key )
	{
		 return goodSHashData.get( key );
	}
	
	public HashMap<String,String>  getDespHashTable()
	{
		return goodDecpHashData;
	}
	
/*	public Vector<descContainer> getDescpVector(){

		return goodDecpVector;
		
	}*/
	public HashMap<String,String>  getGoodsDetailTable()
	{
		return goodSHashData;
	}
	
	void parseGoodsDescrition(String jsonData) {
		try {
			Log.d( TAGS, "=======Starting parse json data : =======");
			JsonReader reader = new JsonReader(new StringReader(jsonData));
			while (reader.hasNext()) 
			{
				reader.beginObject();
				while (reader.hasNext()) 
				{
					String tagName = reader.nextName();	
					String dataValue = reader.nextString();
					Log.d(TAGS, ", tagName:" + tagName +", value:" + dataValue);
					goodDecpHashData.put(tagName, dataValue);
//					descContainer temp = new descContainer();
//					temp.title = tagName;
//					temp.value = dataValue;
//					goodDecpVector.addElement(temp);
				}
				reader.endObject();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String  toString()
	{
		StringBuilder  buffer = new StringBuilder();
		
		buffer.append("GoodName:");
		buffer.append(getHashData(KEY_GOODS_NAME));
		buffer.append("\nShopPrice:");
		buffer.append(getHashData(KEY_GOODS_PRICE));
		buffer.append("\nGoodNumber:");
		buffer.append(getHashData(KEY_GOODS_NUMS));
		buffer.append("\nGood SDay:");
		buffer.append(getHashData(KEY_GOODS_SDAY));
		buffer.append("\nGood ST:");
		buffer.append(getHashData(KEY_GOODS_ST));
		
		buffer.append("\n\nGood DESP BEGIN:{\n");
		for(Iterator<String> itr = goodDecpHashData.keySet().iterator(); itr.hasNext();){ 
			String key = (String) itr.next(); 
			String value = (String) goodDecpHashData.get(key); 
			buffer.append (key+"---->"+ value +"\n"); 
			} 		
		
		buffer.append(" \n}Good DESP END. ");
		return buffer.toString();
	}
	
	public void reset( )
	{
		goodSHashData.clear();
		goodDecpHashData.clear();
//		goodDecpVector.clear();
	}
		
}


