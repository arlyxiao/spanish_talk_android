package com.example.lib;

import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.example.tables.Question;
import com.example.tables.QuestionsHandler;

public class SqliteLog {
	public static void showAllQuestions(Activity t) {
		QuestionsHandler db = new QuestionsHandler(t);

		Log.d("Reading: ", "Reading all questions..");
		List<Question> questions = db.getAllQuestions();

		for (Question cn : questions) {
			String log = "Id: " + cn.id + " ,Name: " + cn.title
					+ " ,Content: " + cn.content;
			// Writing Questions to log
			Log.d("Title: ", log);
		}
	}

	public static void showSimple(Activity t, String message) {
		Log.d("Middle: ", message);
	}
}
