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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lib.SessionManagement;





public class LoginActivity extends Activity {
	private EditText edit_text_email, edit_text_password;
	String user_id, email, username, password;
	private TextView login_error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        loadUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    
    private void loadUi(){
		edit_text_email = (EditText)findViewById(R.id.login_email);
		edit_text_password = (EditText)findViewById(R.id.login_password);
		
		login_error = (TextView)findViewById(R.id.login_error);
	}

    
    public void doLogin(View view) {
    	new HttpTask().execute();
    	login_error.setVisibility(View.VISIBLE);
    }
    
    public class HttpTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
			email = edit_text_email.getText().toString();
			password = edit_text_password.getText().toString();
			
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/users/do_login");
		    
		    StringBuilder builder = new StringBuilder();
		    SessionManagement session = new SessionManagement(getApplicationContext());

		    try {
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("email", email));
		        nameValuePairs.add(new BasicNameValuePair("password", password));
		        
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
	        		
	        		Intent i = new Intent(getApplicationContext(), QuestionActivity.class);
	                startActivity(i);
	                finish();
		        }
		        

		        return null;
		    } catch (ClientProtocolException e) {
		    } catch (IOException e) {
		    } catch (JSONException e) {
				e.printStackTrace();
			}
	    	
			return null;
	    }
		

	 }
}
