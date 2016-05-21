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
 * 全局变量容器
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
	 * 更新检查算法，每次点击取消按钮flag+1，每次flag加到3，lvl+1,可等待时间为lvl^2-1个小时
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
	 * 网络监测工具
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
     * 用于判断一串数字是否是手机号
     * 
     * @author Administrator
     * 
     */

    	/*
    	 * 移动: 2G号段(GSM网络)有139,138,137,136,135,134,159,158,152,151,150,
    	 * 3G号段(TD-SCDMA网络)有157,182,183,188,187 147是移动TD上网卡专用号段. 联通:
    	 * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185 电信:
    	 * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180
    	 */
    	static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
    	static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
    	static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1}))[0-9]{8}$";


    	public int matchNum(String mobPhnNum) {
    		/**
    		 * flag = 1 YD 2 LT 3 DX 
    		 */
    		int flag;// 存储匹配结果
    		// 判断手机号码是否是11位
    		if (mobPhnNum.length() == 11) {
    			// 判断手机号码是否符合中国移动的号码规则
    			if (mobPhnNum.matches(YD)) {
    				flag = 1;
    			}
    			// 判断手机号码是否符合中国联通的号码规则
    			else if (mobPhnNum.matches(LT)) {
    				flag = 2;
    			}
    			// 判断手机号码是否符合中国电信的号码规则
    			else if (mobPhnNum.matches(DX)) {
    				flag = 3;
    			}
    			// 都不合适 未知
    			else {
    				flag = 4;
    			}
    		}
    		// 不是11位
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
