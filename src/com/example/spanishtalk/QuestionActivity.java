package com.example.spanishtalk;

import java.io.IOException;
import java.util.ArrayList;
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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lib.BaseDialog;
import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.lib.SqliteLog;
import com.example.tables.Question;
import com.example.tables.QuestionsHandler;



public class QuestionActivity extends Activity {
	
	private EditText edit_text_title, edit_text_content;
	String title, content;
	Integer user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        loadUi();
        
        new PostQuestionTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }
    
    
    private void loadUi(){
		edit_text_title = (EditText)findViewById(R.id.question_title);
		edit_text_content = (EditText)findViewById(R.id.question_content);
		
		user_id = SessionManagement.getUserId(getApplicationContext());
	}
    
    public boolean validateQuestionForm(String title, String content) {
    	if (BaseUtils.is_str_blank(title) || BaseUtils.is_str_blank(content)) {
    		Context context = getApplicationContext();
            Toast.makeText(context, "请填写正确的标题或者内容", Toast.LENGTH_SHORT).show();
            
            return false;
    	}
    
    	return true;
    }
    
    
    public void postQuestion(View view) {
    	title = edit_text_title.getText().toString();
		content = edit_text_content.getText().toString();
		
    	if (validateQuestionForm(title, content)) {
			new QuestionsHandler(this).addQuestion(new Question(user_id, title, content));
			BaseDialog.showSingleAlert("发送成功", this);
		}
    }
    
    
    
    public class PostQuestionTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
		    
			new Timer().schedule(new TimerTask() {          
		        @Override
		        public void run() {
	    		    		    		    
	        		QuestionsHandler db = new QuestionsHandler(QuestionActivity.this);
	        		List<Question> questions = db.getAllQuestions();  
	        		
		        	if (HttpPack.hasConnected(QuestionActivity.this)) {
		        		
		        		HttpClient httpclient = new DefaultHttpClient();
		    		    HttpPost httppost = new HttpPost("http://192.168.1.17:3000/questions");

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
		        	
		        	// 输出目前本地数据库所有记录
		        	SqliteLog.showAllQuestions(QuestionActivity.this);
		        }

		    }, 0, 5000);
			return null;
	    }
		
		

	 }

    
}
