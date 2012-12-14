package com.example.spanishtalk;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.SpanishTalkAction;




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
    	
    	if (SessionManagement.getUserId(getApplicationContext()) == null) {
    		login_error.setVisibility(View.VISIBLE);
        } else {
        	Intent i = new Intent(getApplicationContext(), QuestionActivity.class);
        	startActivity(i);
        	finish();
        }
    	
    }
    
    public class HttpTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {			
			Map<String, String> params = new HashMap<String, String>();
		    params.put("user[email]", edit_text_email.getText().toString());
		    params.put("user[password]", edit_text_password.getText().toString());

	        HttpResponse response = HttpPack.sendPost(SpanishTalkAction.login_url, params);
		    
	        if (response.getStatusLine().getStatusCode() == 200) {       	
	        	SpanishTalkAction.saveInSession(getApplicationContext(), HttpPack.getJsonByResponse(response) );
	        }
	    	
			return null;
	    }

	 }
}
