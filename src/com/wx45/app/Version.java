package com.wx45.app;

public class Version {
	private String app_name;//软件名称

	private String app_ver;//软件版本号
	private String maijor_ver;//主版本号
	private String minor_ver;//次版本号
	private String update_time;//更新时间
	private String app_size;//软件大小
	
	private String app_desc;  //软件描述
	private String app_url;  //下载链接
	private String app_file;	
	
	
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_ver() {
		return app_ver;
	}
	public void setApp_ver(String app_ver) {
		this.app_ver = app_ver;
	}
	public String getMaijor_ver() {
		return maijor_ver;
	}
	public void setMaijor_ver(String maijor_ver) {
		this.maijor_ver = maijor_ver;
	}
	public String getMinor_ver() {
		return minor_ver;
	}
	public void setMinor_ver(String minor_ver) {
		this.minor_ver = minor_ver;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getApp_size() {
		return app_size;
	}
	public void setApp_size(String app_size) {
		this.app_size = app_size;
	}
	public String getApp_desc() {
		return app_desc;
	}
	public void setApp_desc(String app_desc) {
		this.app_desc = app_desc;
	}
	public String getApp_url() {
		return app_url;
	}
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	public String getApp_file() {
		return app_file;
	}
	public void setApp_file(String app_file) {
		this.app_file = app_file;
	}
	
}
