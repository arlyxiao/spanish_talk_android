package com.example.spanishtalk;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.base.utils.BaseUtils;


public class RegisterActivity extends Activity {
	private EditText et_email,et_username,et_password,et_confirm_password;
	private TextView tv_notice_email, tv_notice_username, tv_notice_password, tv_notice_confirm_password, tv_notice_network;
	private LinearLayout ll_show;
	String email, username, password, confirm_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        load_ui();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register, menu);
        return true;
    }
    
    private void load_ui(){
		et_email = (EditText)findViewById(R.id.register_edittext_email);
		et_username = (EditText)findViewById(R.id.register_edittext_username);
		et_password = (EditText)findViewById(R.id.register_edittext_password);
		et_confirm_password = (EditText)findViewById(R.id.register_edittext_confirm_password);
		
		ll_show = (LinearLayout)findViewById(R.id.register_tv_error_show);
		tv_notice_email = (TextView)findViewById(R.id.register_email_notice);
		tv_notice_username = (TextView)findViewById(R.id.register_username_notice);
		tv_notice_password = (TextView)findViewById(R.id.register_password_notice);
		tv_notice_confirm_password = (TextView)findViewById(R.id.register_confirm_password_notice);
		tv_notice_network = (TextView)findViewById(R.id.register_network_notice);

	}
    
    
    
    public void click_register_button(View view){
		//≈–∂œ Õ¯¬Á£¨ÃÓ–¥µƒ «∑Ò”–ø’÷µ
		//if(judgeRegister()){
		//	clearTV();
		//	questVoid();	
		//}
    	ll_show.setVisibility(View.VISIBLE);
    	judgeRegister();
	}
	public void questVoid(){
		  
	}
    public void clearTV(){
    	ll_show.setVisibility(View.GONE);
    	tv_notice_email.setVisibility(View.GONE);
    	tv_notice_username.setVisibility(View.GONE);
    	tv_notice_password.setVisibility(View.GONE);
    	tv_notice_network.setVisibility(View.GONE);
    }
    
	public boolean judgeRegister(){
		 boolean judge = true;
		 email = et_email.getText().toString();
		 username = et_username.getText().toString();
	     password = et_password.getText().toString();
	     confirm_password = et_confirm_password.getText().toString();
		
		if( !BaseUtils.is_wifi_active(RegisterActivity.this)){
			tv_notice_network.setVisibility(View.VISIBLE);
			judge = false;
		}else{
			tv_notice_network.setVisibility(View.GONE);
		}
		
		if(BaseUtils.is_str_blank(email)){
			tv_notice_email.setVisibility(View.VISIBLE);
			judge = false;
		}else{
			tv_notice_email.setVisibility(View.GONE);
		}
		if(BaseUtils.is_str_blank(username)){
			tv_notice_username.setVisibility(View.VISIBLE);
			judge = false;
		}else{
			tv_notice_username.setVisibility(View.GONE);
		}
		if(BaseUtils.is_str_blank(password)){
			tv_notice_password.setVisibility(View.VISIBLE);
			judge = false;
		}else{
			tv_notice_password.setVisibility(View.GONE);
		}
		if(BaseUtils.is_str_blank(confirm_password)){
			tv_notice_confirm_password.setVisibility(View.VISIBLE);
			judge = false;
		}else{
			tv_notice_confirm_password.setVisibility(View.GONE);
		}
		if(!judge){
			ll_show.setVisibility(View.VISIBLE);
		}
		return judge;
	}
}
