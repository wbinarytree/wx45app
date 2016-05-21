package com.wx45.app;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * ȫ�ֱ�������
 * @author Administrator
 *
 */
public class MyApplication extends Application{
	private int flag;
	private List<Date> date;
	private int ignore_lvl;
	private Date last_ignore_date;
	private int flag_wel;
	
	public Date getLastIgnoreDate(){
		return this.last_ignore_date;
	}
	
	public void setLastIgnoreDate(Date date){
		this.last_ignore_date = date;
	}

	public List<Date> getDate() {
		return date;
	}

	public void addDate(Date date) {
		this.date.add(date);
	}
	
	

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
	 * ���¼���㷨��ÿ�ε��ȡ����ťflag+1��ÿ��flag�ӵ�3��lvl+1,�ɵȴ�ʱ��Ϊlvl^2-1��Сʱ
	 * @return
	 */
	public boolean checkUpdate(){

//		Log.w("IGNORE_LEVEL", ignore_lvl + "");
//		Log.w("LAST_IGNORE_DATE",last_ignore_date.toString());
//		Log.w("FLAG",flag + "");
		if(flag < 2){
			if((System.currentTimeMillis() - last_ignore_date.getTime()) >((Math.pow(ignore_lvl, 2) - 1)*60*60*1000)){
				//Log.w("Math",(Math.pow(ignore_lvl, 2) - 1)*60*60*1000 + "");
				return true;
			}
			else
				return false;
		}
		else{
			ignore_lvl++;
			if(ignore_lvl == 11)
				ignore_lvl =0;
			flag = 0;
			return false;
		}
	}

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ignore_lvl = 0;
		last_ignore_date = new Date(0);
		date = new ArrayList<Date>();
		flag = 0;
		flag_wel = 0;
		super.onCreate();
	}
	
	/**
	 * �����⹤��
	 * @return
	 */
    public boolean isNetConnected() {  
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo[] infos = cm.getAllNetworkInfo();  
            if (infos != null) {  
                for (NetworkInfo ni : infos) {  
                    if (ni.isConnected()) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }
    
    /**
     * �����ж�һ�������Ƿ����ֻ���
     * 
     * @author Administrator
     * 
     */

    	/*
    	 * �ƶ�: 2G�Ŷ�(GSM����)��139,138,137,136,135,134,159,158,152,151,150,
    	 * 3G�Ŷ�(TD-SCDMA����)��157,182,183,188,187 147���ƶ�TD������ר�úŶ�. ��ͨ:
    	 * 2G�Ŷ�(GSM����)��130,131,132,155,156 3G�Ŷ�(WCDMA����)��186,185 ����:
    	 * 2G�Ŷ�(CDMA����)��133,153 3G�Ŷ�(CDMA����)��189,180
    	 */
    	static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
    	static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
    	static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1}))[0-9]{8}$";


    	public int matchNum(String mobPhnNum) {
    		/**
    		 * flag = 1 YD 2 LT 3 DX 
    		 */
    		int flag;// �洢ƥ����
    		// �ж��ֻ������Ƿ���11λ
    		if (mobPhnNum.length() == 11) {
    			// �ж��ֻ������Ƿ�����й��ƶ��ĺ������
    			if (mobPhnNum.matches(YD)) {
    				flag = 1;
    			}
    			// �ж��ֻ������Ƿ�����й���ͨ�ĺ������
    			else if (mobPhnNum.matches(LT)) {
    				flag = 2;
    			}
    			// �ж��ֻ������Ƿ�����й����ŵĺ������
    			else if (mobPhnNum.matches(DX)) {
    				flag = 3;
    			}
    			// �������� δ֪
    			else {
    				flag = 4;
    			}
    		}
    		// ����11λ
    		else {
    			flag = 5;
    		}
    		Log.d("TelNumMatch", "flag"+flag);
    		return flag;
    	}


	public int getFlag_wel() {
		return flag_wel;
	}

	public void setFlag_wel(int flag_wel) {
		this.flag_wel = flag_wel;
	}


}
