package com.example.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;



public class BaseDialogs {
	public static void showSingleAlert(String message, Activity t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(t);
    	builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
	}
}
