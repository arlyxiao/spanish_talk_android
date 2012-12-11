package com.example.spanishtalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
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






public class RegisteryActivity  extends Activity {
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
    		
	    	new TestTask().execute();
	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("×¢²á³É¹¦")
	    	       .setCancelable(false)
	    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   Intent intent = new Intent(RegisteryActivity.this, QuestionActivity.class);
	    	        	   startActivity(intent);
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
		
//		if(BaseUtils.is_str_blank(email)){
//			email_error.setVisibility(View.VISIBLE);
//			checked = false;
//		}else{
//			email_error.setVisibility(View.GONE);
//		}
//		if(BaseUtils.is_str_blank(username)){
//			username_error.setVisibility(View.VISIBLE);
//			checked = false;
//		}else{
//			username_error.setVisibility(View.GONE);
//		}
//		if(BaseUtils.is_str_blank(password)){
//			password_error.setVisibility(View.VISIBLE);
//			checked = false;
//		}else{
//			password_error.setVisibility(View.GONE);
//		}
//		if(BaseUtils.is_str_blank(confirm_password)){
//			confirm_password_error.setVisibility(View.VISIBLE);
//			checked = false;
//		}else{
//			confirm_password_error.setVisibility(View.GONE);
//		}
//		if(!checked){
//			error_list.setVisibility(View.VISIBLE);
//		}
		return checked;
	}
	
	
	
	
	public class TestTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
	    	
	    	email = edit_text_email.getText().toString();
			username = edit_text_username.getText().toString();
		    password = edit_text_password.getText().toString();
	    	
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/users");

		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("user[username]", username));
		        nameValuePairs.add(new BasicNameValuePair("user[email]", email));
		        nameValuePairs.add(new BasicNameValuePair("user[password]", password));
		        
		        // httppost.setHeader("Accept", "application/json");
		        httppost.setEntity(new UrlEncodedFormEntity (nameValuePairs, HTTP.UTF_8));

		        httpclient.execute(httppost);
		        
		    } catch (ClientProtocolException e) {
		    } catch (IOException e) {
		    }
	    	
			return null;
	    }
		
		

	 }
}
