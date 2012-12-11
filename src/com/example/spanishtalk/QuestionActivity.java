package com.example.spanishtalk;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    }
}
