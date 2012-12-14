package com.example.spanishtalk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class QuestionShowActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_show);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question_show, menu);
        return true;
    }
}
