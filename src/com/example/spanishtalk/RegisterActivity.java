package com.example.spanishtalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.base.activity.SpanishTalkBaseActivity;
import com.example.base.utils.BaseUtils;




public class RegisterActivity extends SpanishTalkBaseActivity {
	private EditText et_email,et_username,et_password,et_confirm_password;
	private TextView tv_notice_email, tv_notice_username, tv_notice_password, tv_notice_confirm_password, tv_notice_network;
	private LinearLayout ll_show;
	String email, username, password, confirm_password;
	
	final String TAG_STRING = "TAG"; 

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
    	new TestTask().execute();
    }
    
    	 
    	
    	
//		//≈–∂œ Õ¯¬Á£¨ÃÓ–¥µƒ «∑Ò”–ø’÷µ
//		if(judgeRegister()){
//			clearTV();
//			
//			email = et_email.getText().toString();
//			username = et_username.getText().toString();
//		    password = et_password.getText().toString();
//		     
//		     
//			// postData(username, email, password);
//		    // postData();
//		}

	
    
    
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
		
//		if( !BaseUtils.is_wifi_active(RegisterActivity.this)){
//			tv_notice_network.setVisibility(View.VISIBLE);
//			judge = false;
//		}else{
			tv_notice_network.setVisibility(View.GONE);
//		}
		
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
	
	
	
	
	public class TestTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
	    	
	    	email = et_email.getText().toString();
			username = et_username.getText().toString();
		    password = et_password.getText().toString();
	    	
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/users");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("user[username]", username));
		        nameValuePairs.add(new BasicNameValuePair("user[email]", email));
		        nameValuePairs.add(new BasicNameValuePair("user[password]", password));
		        // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        
		        // httppost.setHeader("Accept", "application/json");
		        httppost.setEntity(new UrlEncodedFormEntity (nameValuePairs, HTTP.UTF_8));

		        // Execute HTTP Post Request
		        httpclient.execute(httppost);
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
	    	
			return null;
	    }
		
		

	 }
}
