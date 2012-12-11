package com.example.spanishtalk;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tables.Question;
import com.example.tables.QuestionsHandler;

public class QuestionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }
    
    public void post_question(View view) {
    	EditText title = (EditText)findViewById(R.id.question_title);
    	EditText content = (EditText)findViewById(R.id.question_content);
    	if(title.getText().length() == 0){
    		Context context = getApplicationContext();
            CharSequence text = "«ÎÃÓ–¥±ÍÃ‚";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
    	}
    	
    	if(content.getText().length() == 0){
    		Context context = getApplicationContext();
            CharSequence text = "«ÎÃÓ–¥ƒ⁄»›";
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
 
        // Reading all questions
        Log.d("Reading: ", "Reading all questions..");
        List<Question> questions = db.getAllQuestions();      
 
        for (Question cn : questions) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getTitle() + " ,Content: " + cn.getContent();
                // Writing Questions to log
            Log.d("Title: ", log);
        }
    }
}
