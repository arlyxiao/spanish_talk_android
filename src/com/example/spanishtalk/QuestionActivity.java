package com.example.spanishtalk;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import com.example.tables.Question;
import com.example.tables.QuestionsHandler;



public class QuestionActivity extends Activity {
	
	private EditText edit_text_title, edit_text_content;
	String title, content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        load_ui();
        
        new TestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }
    
    
    private void load_ui(){
		edit_text_title = (EditText)findViewById(R.id.question_title);
		edit_text_content = (EditText)findViewById(R.id.question_content);
		
		title = edit_text_title.getText().toString();
		content = edit_text_content.getText().toString();
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
    
    public void post_question(View view) {
    	QuestionsHandler db = new QuestionsHandler(this);
    	
		if (validateQuestionForm()) {
	        db.addQuestion(new Question(title, content));
		}
        
        if (hasConnected()) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("发送成功")
        	       .setCancelable(false)
        	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
        } else {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("网络连接有问题")
        	       .setCancelable(false)
        	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	           }
        	       });
        	AlertDialog alert = builder.create();
        	alert.show();
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
    
    
    public class TestTask extends AsyncTask<Void, Void, Void>{
		
		@Override
	    protected Void doInBackground(Void... arg0) {
			new Timer().schedule(new TimerTask() {          
		        @Override
		        public void run() {
		        	if (hasConnected()) {
		        		for(int i=1; i<11; i++){
		        			Log.d("成功: ", Integer.toString(i));
		        		}
		        	}
		        }

		    }, 0, 5000);
			return null;
	    }
		

		

	 }

    
}
