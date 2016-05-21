package com.zhenshi.util;  
  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  
  
/** 
 * ������HttpDownload
 * ���ã������ļ�����
 */  
public class HttpDownload {  
    /** 
     * ���ܣ������ļ���ָ��Ŀ¼�£��ɹ�����1
     */  
    public static int downLoadFile(String httpUrl, String fileName, String path) {  
        FileOutputStream fos = null;  
        InputStream is = null;  
        HttpURLConnection conn = null;  
        // ������ļ����ļ�Ŀ¼�����ڵ�ʱ�򴴽��ļ�Ŀ¼  
        File tmpFile = new File(path);  
        if (!tmpFile.exists()) {  
            tmpFile.mkdir();  
        }  
        // ��ȡ�ļ�����  
        File file = new File(path + "/"+fileName);  
        try {  
            URL url = new URL(httpUrl);  
            try {  
                conn = (HttpURLConnection) url.openConnection();  
                is = conn.getInputStream();// ���http���󷵻ص�InputStream����  
                fos = new FileOutputStream(file);// ����ļ������������д�ļ��õ�  
                byte[] buf = new byte[256];  
                conn.connect();// http���������  
                double count = 0;  
                // http����ȡ����Ӧ��ʱ��  
                if (conn.getResponseCode() >= 400) {  
                    System.out.println("nono");  
                    return 0;  
                } else {  
                    while (count <= 100) {  
                        if (is != null) {  
                            int numRead = is.read(buf);  
                            if (numRead <= 0) {  
                                break;  
                            } else {  
                                fos.write(buf, 0, numRead);  
                            }  
  
                        } else {  
                            break;  
                        }  
                    }  
                }  
                conn.disconnect();  
                fos.close();  
                is.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
                return 0;  
            } finally {  
                if (conn != null) {  
                    conn.disconnect();  
                    conn = null;  
                }  
                if (fos != null) {  
                    try {  
                        fos.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                    fos = null;  
                }  
                if (is != null) {  
                    try {  
                        is.close();  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                    is = null;  
                }  
            }  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
            return 0;  
        }  
        return 1;  
    }  
}  