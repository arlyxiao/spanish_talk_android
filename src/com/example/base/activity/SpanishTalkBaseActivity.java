package com.example.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;



abstract public class SpanishTalkBaseActivity extends Activity {
	public class RequestCode{
	    public final static int NEW_TEXT = 9;
	    public final static int FROM_ALBUM = 1;
	    public final static int FROM_CAMERA = 2;
	}
	 

	// 绑定在顶栏 go_back 按钮上的事件处理
	final public void go_back(View view) {
		on_go_back();
		this.finish();
	}

	// 打开一个新的activity，此方法用来简化调用
	final public void open_activity(Class<?> cls) {
		startActivity(new Intent(getApplicationContext(), cls));
	}
 

	// 钩子，自行重载
	public void on_go_back() {
	};
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK){			
			return true;
		 }
		 return super.onKeyDown(keyCode, event);
	}
	
}
