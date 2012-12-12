package com.example.spanishtalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.base.activity.SpanishTalkBaseActivity;
import com.example.base.utils.BaseUtils;
import com.example.lib.SessionManagement;



public class RegisterActivity extends SpanishTalkBaseActivity {
	private EditText edit_text_email, edit_text_username, edit_text_password, edit_text_confirm_password;
	private TextView email_error, username_error, password_error, confirm_password_error, network_error;
	private LinearLayout error_list;
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
		edit_text_email = (EditText)findViewById(R.id.reg_email);
		edit_text_username = (EditText)findViewById(R.id.reg_username);
		edit_text_password = (EditText)findViewById(R.id.reg_password);
		edit_text_confirm_password = (EditText)findViewById(R.id.reg_confirm_password);
		
		error_list = (LinearLayout)findViewById(R.id.reg_error_show);
		email_error = (TextView)findViewById(R.id.reg_email_error);
		username_error = (TextView)findViewById(R.id.reg_username_error);
		password_error = (TextView)findViewById(R.id.reg_password_error);
		confirm_password_error = (TextView)findViewById(R.id.reg_confirm_password_error);
		network_error = (TextView)findViewById(R.id.reg_network_error);

	}
    
    
    
    public void do_register(View view){
    	if (validateRegister()) {

    		clearErrorList();
    		
	    	new HttpTask().execute();
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("注册成功")
	    	       .setCancelable(false)
	    	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {	    	    
	    	        	   Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
	    	        	   startActivity(intent);
	    	        	   // finish();
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
    	}
    }
    
 
	
    

    public void clearErrorList(){
    	error_list.setVisibility(View.GONE);
    	email_error.setVisibility(View.GONE);
    	username_error.setVisibility(View.GONE);
    	password_error.setVisibility(View.GONE);
    	network_error.setVisibility(View.GONE);
    }
    
	public boolean validateRegister(){
		 boolean checked = true;
		 email = edit_text_email.getText().toString();
		 username = edit_text_username.getText().toString();
	     password = edit_text_password.getText().toString();
	     confirm_password = edit_text_confirm_password.getText().toString();
		
//		if( !BaseUtils.is_wifi_active(RegisterActivity.this)){
//			tv_notice_network.setVisibility(View.VISIBLE);
//			checked = false;
//		}else{
			network_error.setVisibility(View.GONE);
//		}
		
		if(BaseUtils.is_str_blank(email)){
			email_error.setVisibility(View.VISIBLE);
			checked = false;
		}else{
			email_error.setVisibility(View.GONE);
		}
		if(BaseUtils.is_str_blank(username)){
			username_error.setVisibility(View.VISIBLE);
			checked = false;
		}else{
			username_error.setVisibility(View.GONE);
		}
		if(BaseUtils.is_str_blank(password)){
			password_error.setVisibility(View.VISIBLE);
			checked = false;
		}else{
			password_error.setVisibility(View.GONE);
		}
		if(BaseUtils.is_str_blank(confirm_password)){
			confirm_password_error.setVisibility(View.VISIBLE);
			checked = false;
		}else{
			confirm_password_error.setVisibility(View.GONE);
		}
		if(!checked){
			error_list.setVisibility(View.VISIBLE);
		}
		return checked;
	}
	
	
	
	
	
	
	public class HttpTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
	    	
	    	email = edit_text_email.getText().toString();
			username = edit_text_username.getText().toString();
		    password = edit_text_password.getText().toString();
	    	
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/users");
		    
		    StringBuilder builder = new StringBuilder();
		    SessionManagement session = new SessionManagement(getApplicationContext());

		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("user[username]", username));
		        nameValuePairs.add(new BasicNameValuePair("user[email]", email));
		        nameValuePairs.add(new BasicNameValuePair("user[password]", password));
		        
		        // httppost.setHeader("Accept", "application/json");
		        httppost.setEntity(new UrlEncodedFormEntity (nameValuePairs, HTTP.UTF_8));

		        HttpResponse response = httpclient.execute(httppost);
		        
		        if (response.getStatusLine().getStatusCode() == 200) {
			        BufferedReader reader = new BufferedReader(new InputStreamReader(
	        		response.getEntity().getContent()));
	        		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
	        			builder.append(s);
	        		}
	        		JSONObject jsonObject = new JSONObject(builder.toString());
	        		String username = jsonObject.getString("username");
	        		String user_id = jsonObject.getString("user_id");
	        		session.createLoginSession(user_id, username);
//	        		Log.d("url username", username);
//	        		Log.d("url user_id", Integer.toString(user_id));
		        }
		        

		        
		    } catch (ClientProtocolException e) {
		    } catch (IOException e) {
		    } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			return null;
	    }
		
		
		

	 }
}