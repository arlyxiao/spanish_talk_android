package com.example.spanishtalk;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
}
