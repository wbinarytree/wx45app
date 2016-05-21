package com.wx45.app;

import cn.m15.xys.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class indexActivity extends Activity {
   
    Context mContext = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        /**简单的ListView列表**/
        Button edit = (Button) findViewById(R.id.editText1);
        edit.setOnClickListener(new OnClickListener() {
    	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,IconList.class); 
		 startActivity(intent);
	    }
	}); 
        
    
    }
}