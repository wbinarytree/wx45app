package http;

import java.util.HashMap;

/**
 * desciption: 1:�����б�Ľӿ�================== 
 * goods_id ��ƷID 
 * goods_sn ��Ʒ�ͺ�
 * goods_name ��Ʒ���� 
 * shop_price�� ��Ʒ�۸� 
 * goods_number: ��Ʒ�������
 * 
 * */
public class GoodsSearchData {

	private HashMap<String,String> goodSHashData;
	
	public final static String KEY_GOODS_ID = "goods_id";
	public final static String KEY_GOODS_SN = "goods_sn";	
	public final static String KEY_GOODS_NAME = "goods_name";	
	public final static String KEY_GOODS_PRICE = "shop_price";
	public final static String KEY_GOODS_LEFTNUMS = "goods_number";
	
	
	public GoodsSearchData( )
	{
		goodSHashData = new HashMap<String,String>();
		
	}
		
	public void setHashData( String key, String data)
	{
		goodSHashData.put(key, data);
	}
	
	public String getHashData( String key)
	{
		 return goodSHashData.get( key );
	}
	
	public void reset( )
	{
		goodSHashData.clear();
	}
	
	public String  toString()
	{
		StringBuilder  buffer = new StringBuilder();
		
		buffer.append("{\nGoodID:");
		buffer.append(getHashData(KEY_GOODS_ID));
		buffer.append("\nGoodPrice:");
		buffer.append(getHashData(KEY_GOODS_PRICE));
		buffer.append("\nGoodSN:");
		buffer.append(getHashData(KEY_GOODS_SN));
		buffer.append("\nGoodName:");
		buffer.append(getHashData(KEY_GOODS_NAME));
		buffer.append("\nGoodLeftNums:");
		buffer.append(getHashData(KEY_GOODS_LEFTNUMS));		
		buffer.append(" \n}. ");
		return buffer.toString();
	}
}
