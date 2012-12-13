package com.example.spanishtalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.base.utils.BaseUtils;
import com.example.lib.SessionManagement;
import com.example.tables.Question;
import com.example.tables.QuestionsHandler;





public class QuestionActivity extends Activity {
	
	private EditText edit_text_title, edit_text_content;
	String user_id, title, content;
	static int local_question_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        loadUi();
        
        // new TestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }
    
    
    private void loadUi(){
		edit_text_title = (EditText)findViewById(R.id.question_title);
		edit_text_content = (EditText)findViewById(R.id.question_content);
	}
    
    public boolean validateQuestionForm() {
    	Boolean checked = true;
    	
    	if (BaseUtils.is_str_blank(title)) {
    		Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "请填写标题", Toast.LENGTH_SHORT);
            toast.show();
            
            checked = false;
    	}
    	
    	if (BaseUtils.is_str_blank(content)) {
    		Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "请填写内容", Toast.LENGTH_SHORT);
            toast.show();
            
            checked = false;
    	}
    	
    	return checked;
    }
    
    
    public void postQuestion(View view) {
    	title = edit_text_title.getText().toString();
		content = edit_text_content.getText().toString();
		
    	QuestionsHandler db = new QuestionsHandler(this);
    	
    	SessionManagement session = new SessionManagement(getApplicationContext());
    	HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(SessionManagement.KEY_USER_ID);
    	
		if (validateQuestionForm()) {
	        local_question_id = db.addQuestion(new Question(Integer.parseInt(user_id), title, content));
		}
        
        if (hasConnected()) {
        	
        	new HttpTask().execute();
        	
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("发送成功")
        	       .setCancelable(false)
        	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	           }
        	       });
        	builder.create().show();
        } else {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("网络连接有问题")
        	       .setCancelable(false)
        	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	           }
        	       });
        	builder.create().show();
        }
        
 
        // Reading all questions
        Log.d("Reading: ", "Reading all questions..");
        List<Question> questions = db.getAllQuestions();      
 
        for (Question cn : questions) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getTitle() + " ,Content: " + cn.getContent();
                // Writing Questions to log
            Log.d("Title: ", log);
        }
    }
    
    
    public boolean hasConnected(){

		final ConnectivityManager connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			return true;
		} else if (mobile.isAvailable()) {
			return true;
		} else {
			return false;
		}

    }
    
    
    public class HttpTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
//			title = edit_text_title.getText().toString();
//			content = edit_text_content.getText().toString();
//			
//			SessionManagement session = new SessionManagement(getApplicationContext());
//		    HashMap<String, String> user = session.getUserDetails();
//	        user_id = user.get(SessionManagement.KEY_USER_ID);
//			
//	    	HttpClient httpclient = new DefaultHttpClient();
//		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/questions");
//		    
//		    StringBuilder builder = new StringBuilder();


//		    try {
//		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//		        nameValuePairs.add(new BasicNameValuePair("question[creator_id]", user_id));
//		        nameValuePairs.add(new BasicNameValuePair("question[title]", title));
//		        nameValuePairs.add(new BasicNameValuePair("question[content]", content));
//		        
//		        httppost.setEntity(new UrlEncodedFormEntity (nameValuePairs, HTTP.UTF_8));
//
//		        HttpResponse response = httpclient.execute(httppost);
//		        
//		        if (response.getStatusLine().getStatusCode() == 200) {
//		        	BufferedReader reader = new BufferedReader(new InputStreamReader(
//	        		response.getEntity().getContent()));
//	        		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
//	        			builder.append(s);
//	        		}
//	        		JSONObject jsonObject = new JSONObject(builder.toString());
//	        		jsonObject.getString("question_id");
//	        		
//	        		Log.d("current question local ID: ", Integer.toString(local_question_id));
//			        		
//		        	QuestionsHandler db = new QuestionsHandler(QuestionActivity.this);
//		        	Question question = db.getQuestion(local_question_id);
//		        	db.deleteQuestion(question);
//		        }
//		        
//
//		        return null;
//		    } catch (ClientProtocolException e) {
//		    } catch (IOException e) {
//		    } catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    
		    
			new Timer().schedule(new TimerTask() {          
		        @Override
		        public void run() {
		        	HttpClient httpclient = new DefaultHttpClient();
	    		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/questions");
	    		    
	    		    		    		    
	        		QuestionsHandler db = new QuestionsHandler(QuestionActivity.this);
	        		List<Question> questions = db.getAllQuestions();  
	        		
		        	if (hasConnected()) {

		                for (Question cn : questions) {
		                	try {
		        		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		        		        nameValuePairs.add(new BasicNameValuePair("question[creator_id]", Integer.toString(cn.getCreatorId())));
		        		        nameValuePairs.add(new BasicNameValuePair("question[title]", cn.getTitle()));
		        		        nameValuePairs.add(new BasicNameValuePair("question[content]", cn.getContent()));
		        		        
		        		        httppost.setEntity(new UrlEncodedFormEntity (nameValuePairs, HTTP.UTF_8));

		        		        HttpResponse response = httpclient.execute(httppost);
		        		        
		        		        if (response.getStatusLine().getStatusCode() == 200) {
		        		        	
		        		        	Question question = db.getQuestion(cn.getID());
		        		        	db.deleteQuestion(question);
		        		        }
		        		        

		        		        
		        		    } catch (ClientProtocolException e) {
		        		    } catch (IOException e) {
		        		    } 
		                }
		              
		                
		        	}
		        	
		        	Log.d("Reading: ", "Reading all questions..");
	                questions = db.getAllQuestions();      
	         
	                for (Question cn : questions) {
	                    String log = "Id: "+cn.getID() + " , Title: " + cn.getTitle() + " , Content: " + cn.getContent();
	                    Log.d("Title: ", log);
	                }
		        }

		    }, 0, 5000);
			return null;
	    }
		

		

	 }

    
}
