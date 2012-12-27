package com.example.spanishtalk;


import android.app.Application;
import android.content.Context;

public class SpanishTalkApplication extends Application {

	public static Context context;
	

	@Override
	public void onCreate() {
		context = getApplicationContext();
	}
	
}
