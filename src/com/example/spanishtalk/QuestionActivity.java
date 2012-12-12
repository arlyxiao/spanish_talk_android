package com.example.spanishtalk;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.base.utils.BaseDialogs;
import com.example.tables.Question;
import com.example.tables.QuestionsHandler;






public class QuestionActivity extends Activity {
	
	protected Timer net_time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        
        new TestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }
    
    @Override
    protected void onPause() {
    	new TestTask().cancel(true);
    }

    
    public void post_question(View view) {
    	EditText title = (EditText)findViewById(R.id.question_title);
    	EditText content = (EditText)findViewById(R.id.question_content);
    	if(title.getText().length() == 0){
    		Context context = getApplicationContext();
            CharSequence text = "请填写标题";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
    	}
    	
    	if(content.getText().length() == 0){
    		Context context = getApplicationContext();
            CharSequence text = "请填写内容";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
    	}
    	
    	
    	QuestionsHandler db = new QuestionsHandler(this);
    	 
        /**
         * CRUD Operations
         * */
        // Inserting Questions
        Log.d("Insert: ", "Inserting ..");
        db.addQuestion(new Question("Ravi", "9100000000"));
        db.addQuestion(new Question("Srinivas", "9199999999"));
        db.addQuestion(new Question("Tommy", "9522222222"));
        db.addQuestion(new Question("Karthik", "9533333333"));
        
        if (hasConnected()) {
        	BaseDialogs.showSingleAlert("问题发送成功", this);
        	finish();
        } else {
        	BaseDialogs.showSingleAlert("网络没连接成功", this);
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
			net_time = new Timer();
			net_time.schedule(new TimerTask() {          
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
